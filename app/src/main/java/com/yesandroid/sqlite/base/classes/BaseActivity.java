package com.yesandroid.sqlite.base.classes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.lang.reflect.ParameterizedType;
import java.util.Objects;

import in.yesandroid.base_android.BaseApp;
import in.yesandroid.base_android.R;
import in.yesandroid.base_android.module.BaseModule;
import in.yesandroid.base_android.utils.Utils;

public abstract class BaseActivity<
        DataBinding extends ViewDataBinding,
        ActivityNavigator extends BaseNavigator,
        ActivityModel extends BaseViewModel<ActivityNavigator>
        >
        extends AppCompatActivity implements BaseNavigator {

    public final String LOG_TAG = this.getClass().getName();
    protected final int PERMISSION_REQUEST = 101;
    protected boolean enableBackButton = true;
    ActivityModel mViewModel;
    ActivityNavigator navigator;
    private ProgressDialog mProgressDialog;
    private DataBinding mViewDataBinding;
    public static final String KEY_BLUETOOTH = "KEY_PRESSED";

    public <T extends Fragment> T findFrag(@IdRes int id) {
        return ((T) getSupportFragmentManager().findFragmentById(id));
    }

    /**
     * @return layout resource id
     */
    public abstract
    @LayoutRes
    int getLayoutId();

    public abstract int getViewModelId();

    protected abstract void init(Bundle savedInstanceState);

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        //boolean result = super.dispatchKeyEvent(event);

        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN || event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                Log.d("activity", "dispatchKeyEvent: Key pressed ");
                LocalBroadcastManager localBroadcast = LocalBroadcastManager.getInstance(this);
                Intent localIntent = new Intent(KEY_BLUETOOTH);
                localBroadcast.sendBroadcast(localIntent);
            }
            return true;
        }

        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            return true;
        }

        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            mViewModel = new ViewModelProvider(BaseViewStoreOwner.getInstance((BaseApp) getApplication())).
                    get((Class<ActivityModel>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[2]);
        } catch (Exception e) {
            mViewModel = new ViewModelProvider(BaseViewStoreOwner.getInstance((BaseApp) getApplication())).
                    get((Class<ActivityModel>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[3]);
        }
        performDataBinding();

        getViewModel().setNavigator(navigator = (ActivityNavigator) this);
        getViewModel().init(savedInstanceState);
        if (getSupportActionBar() != null) {
            if (enableBackButton) {
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            } else {
                getSupportActionBar().setHomeButtonEnabled(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }
        }
        init(savedInstanceState);
        getViewModel().onActivityStarted();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getViewModel().onCleared();
    }


    protected void disableBackButton() {
        enableBackButton = false;
    }


    public ActivityModel getViewModel() {
        return mViewModel;
    }

    private void performDataBinding() {

        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        if (getViewModelId() != -1)
            mViewDataBinding.setVariable(getViewModelId(), mViewModel);
        mViewDataBinding.executePendingBindings();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionGranted();
                } else {
                    permissionNotGranted();
                }
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getViewModel().onResume();
    }

    protected void injectModuleScope(Class<? extends BaseModule> clazz) {

        BaseModule module = ((BaseApp) getApplication()).queryModule(clazz);

        if (getViewModel() instanceof BaseDataViewModel) {
            ((BaseDataViewModel) getViewModel()).setDataBase(module.getRoomDatabase());
        } else {
            throw new RuntimeException("No point in using inject method with BaseViewModel. Please use BaseDataViewModel for better management");
        }
    }

    public void permissionGranted() {

    }

    public void permissionNotGranted() {

    }

    public DataBinding getViewDataBinding() {
        return mViewDataBinding;
    }


    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


    @Override
    public void showLoading(String title, String message) {
        showLoading(title, message, false);

    }


    @Override
    public void showLoading(String title, String message, boolean determinate) {
        hideLoading();
        mProgressDialog = Utils.showLoadingDialog(this, title, message, determinate);
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }


    @Override
    public void updateProgress(int number) {
        if (mProgressDialog != null && !mProgressDialog.isIndeterminate()) {
            mProgressDialog.setProgress(number);
        }
    }

    @Override
    public void showToast(int message) {
        showToast(getString(message));

    }

    @Override
    public void updateProgress(String message) {
        displayMessage(false, message);

    }

    @Override
    public void showAlertDialog(@NonNull String title, @NonNull String message) {
        showAlertDialog(title, message, getString(R.string.ok), (dialog, which) -> dialog.dismiss(), "", null);
    }

    @Override
    public void showAlertDialog(@NonNull String title, @NonNull String message, @NonNull String positiveText, DialogInterface.OnClickListener pClickListener) {
        showAlertDialog(title, message, positiveText, pClickListener, "", null);
    }

    @Override
    public void showAlertDialog(@NonNull String title, @NonNull String message, @NonNull String positiveText, DialogInterface.OnClickListener pClickListener, @NonNull String negative, DialogInterface.OnClickListener nClickListener) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialogOverlay);
        builder.setTitle(title);
        builder.setMessage(message);
        if (pClickListener != null)
            builder.setPositiveButton(positiveText, pClickListener);

        if (nClickListener != null) {
            builder.setNegativeButton(negative, nClickListener);
        }
        builder.create().show();

    }

    @Override
    public void displayMessage(boolean success, String message) {
        if (mProgressDialog != null && mProgressDialog.isShowing() && success)
            mProgressDialog.dismiss();

        if (!success && mProgressDialog.isShowing()) {
            mProgressDialog.setMessage(message);
        }

    }

    @Override
    public <T extends BaseActivity> void pushToActivity(Class<?> tClass) {
        startActivity(new Intent(this, tClass));
    }

    @Override
    public <T extends BaseActivity> void pushToActivity(Class<?> tClass, Bundle bundle) {
        Intent intent = new Intent(this, tClass);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showLoading(int title, int message) {
        showLoading(getString(title), getString(message));
    }

    @Override
    public void showLoading(int title, int message, boolean cancelable) {
        showLoading(getString(title), getString(message), cancelable);
    }


    @Override
    public Fragment replaceFrag(int id, String className) {
        Fragment frag = getSupportFragmentManager().getFragmentFactory().instantiate(ClassLoader.getSystemClassLoader(), className);
        getSupportFragmentManager().beginTransaction()
                .replace(id, frag)
                .commit();

        return frag;
    }

    @Override
    public Fragment replaceFrag(int id, String className, Bundle bundle) {
        Fragment frag = getSupportFragmentManager().getFragmentFactory().instantiate(ClassLoader.getSystemClassLoader(), className);
        frag.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(id, frag)
                .commit();

        return frag;
    }

    @Override
    public Fragment createFragment(String className) {
        return getSupportFragmentManager().getFragmentFactory().instantiate(ClassLoader.getSystemClassLoader(), className);
    }


    @Override
    public void removeFrag(@IdRes int id) {

        if (getSupportFragmentManager().findFragmentById(id) != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(Objects.requireNonNull(getSupportFragmentManager().findFragmentById(id)));
            transaction.commit();
        }
    }

    @Override
    public void dispatchMessageToActivity(int number) {
        throw new RuntimeException("Activity cannot dispatch to itself");
    }

    @Override
    public void dispatchObjectToActivity(Bundle message) {
        throw new RuntimeException("Activity cannot dispatch to itself");
    }

    @Override
    public void showAlertDialog(@StringRes int title, @StringRes int message) {
        if (title == -1)
            showAlertDialog("", getString(message));
        else if (message == -1)
            showAlertDialog(getString(title), "");
        else
            showAlertDialog(getString(title), getString(message));
    }
}
