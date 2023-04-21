package com.yesandroid.sqlite.base;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.yesandroid.sqlite.R;

import java.lang.reflect.ParameterizedType;
import java.util.Objects;

public abstract class BaseActivity<V extends BaseViewModel, B extends ViewDataBinding> extends AppCompatActivity {
    protected V mViewModel;
    protected B mBinding;

    private ProgressDialog mProgressDialog;
    protected abstract @LayoutRes
    int getLayout();

    public abstract int getViewModelId();
  //  protected abstract Class<V> getViewModel();

   // protected abstract B getViewBinding();

    protected abstract void init(Bundle savedInstanceState);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // init();
        mBinding = DataBindingUtil.setContentView(this, getLayout());

      //  mViewModel = ViewModelProviders.of(this).get(getViewModel());

        try {
            mViewModel = new ViewModelProvider(this).
                    get((Class<V>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        } catch (Exception e) {
            mViewModel = new ViewModelProvider(this).
                    get((Class<V>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1]);
        }

        mBinding.setVariable(getViewModelId(),mViewModel);
        mBinding.executePendingBindings();
        getmViewModel().init(savedInstanceState);
        init(savedInstanceState);


    }



    public B getViewDataBinding() {
        return mBinding;
    }

    public V getmViewModel()
    {
        return mViewModel;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        getmViewModel().onCleared();

    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }



    public void showLoading(String title, String message) {
        showLoading(title, message, false);

    }



    public void showLoading(String title, String message, boolean determinate) {
        hideLoading();
        mProgressDialog = Utils.showLoadingDialog(this, title, message, determinate);
    }


    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }



    public void updateProgress(int number) {
        if (mProgressDialog != null && !mProgressDialog.isIndeterminate()) {
            mProgressDialog.setProgress(number);
        }
    }


    public void showToast(int message) {
        showToast(getString(message));

    }


    public void updateProgress(String message) {
        displayMessage(false, message);

    }


    public void showAlertDialog(@NonNull String title, @NonNull String message) {
        showAlertDialog(title, message, "Ok", (dialog, which) -> dialog.dismiss(), "", null);
    }


    public void showAlertDialog(@NonNull String title, @NonNull String message, @NonNull String positiveText, DialogInterface.OnClickListener pClickListener) {
        showAlertDialog(title, message, positiveText, pClickListener, "", null);
    }


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


    public void displayMessage(boolean success, String message) {
        if (mProgressDialog != null && mProgressDialog.isShowing() && success)
            mProgressDialog.dismiss();

        if (!success && mProgressDialog.isShowing()) {
            mProgressDialog.setMessage(message);
        }

    }


    public <T extends BaseActivity> void pushToActivity(Class<?> tClass) {
        startActivity(new Intent(this, tClass));
    }


    public <T extends BaseActivity> void pushToActivity(Class<?> tClass, Bundle bundle) {
        Intent intent = new Intent(this, tClass);
        intent.putExtras(bundle);
        startActivity(intent);
    }



    public Context getContext() {
        return this;
    }


    public void showLoading(int title, int message) {
        showLoading(getString(title), getString(message));
    }


    public void showLoading(int title, int message, boolean cancelable) {
        showLoading(getString(title), getString(message), cancelable);
    }


    public Fragment replaceFrag(int id, String className) {
        Fragment frag = getSupportFragmentManager().getFragmentFactory().instantiate(ClassLoader.getSystemClassLoader(), className);
        getSupportFragmentManager().beginTransaction()
                .replace(id, frag)
                .commit();

        return frag;
    }


    public Fragment replaceFrag(int id, String className, Bundle bundle) {
        Fragment frag = getSupportFragmentManager().getFragmentFactory().instantiate(ClassLoader.getSystemClassLoader(), className);
        frag.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(id, frag)
                .commit();

        return frag;
    }


    public Fragment createFragment(String className) {
        return getSupportFragmentManager().getFragmentFactory().instantiate(ClassLoader.getSystemClassLoader(), className);
    }



    public void removeFrag(@IdRes int id) {

        if (getSupportFragmentManager().findFragmentById(id) != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(Objects.requireNonNull(getSupportFragmentManager().findFragmentById(id)));
            transaction.commit();
        }
    }


    public void dispatchMessageToActivity(int number) {
        throw new RuntimeException("Activity cannot dispatch to itself");
    }


    public void dispatchObjectToActivity(Bundle message) {
        throw new RuntimeException("Activity cannot dispatch to itself");
    }


    public void showAlertDialog(@StringRes int title, @StringRes int message) {
        if (title == -1)
            showAlertDialog("", getString(message));
        else if (message == -1)
            showAlertDialog(getString(title), "");
        else
            showAlertDialog(getString(title), getString(message));
    }

}