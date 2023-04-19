package com.yesandroid.sqlite.base.classes;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import in.yesandroid.base_android.BaseApp;
import in.yesandroid.base_android.R;
import in.yesandroid.base_android.module.BaseModule;
import in.yesandroid.base_android.utils.Utils;

public abstract class BaseFragment<DataBinding extends ViewDataBinding, ActivityNavigator extends BaseNavigator, ActivityModel extends BaseViewModel<ActivityNavigator>> extends Fragment implements BaseNavigator {


    public static final int DISMISS = 1;
    public static final int BACK = 2;
    public static final int SUCCESS = 3;
    public static final int MANDATORY_FORM_DISMISS = 4;
    public static final String EXTRA_OBJECT = "extra_object";

    public final String LOG_TAG = this.getClass().getName();
    protected final int PERMISSION_REQUEST = 101;
    protected boolean enableBackButton = true;
    ActivityModel mViewModel;
    ActivityNavigator navigator;
    DataBinding mViewBinding;
    private ProgressDialog mProgressDialog;
    private List<FragmentInteraction> fragmentInteractions = new ArrayList<>();
    private boolean portraitOnly = false;

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    public abstract int getBindingVariable();

    /**
     * @return layout resource id
     */
    public abstract @LayoutRes
    int getLayoutId();


    public abstract void init(View view, Bundle savedInstances);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getActivity() == null) {
            throw new RuntimeException("Activity context lost. Please recreate the fragment");
        }

        Type[] types = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();
        mViewModel = new ViewModelProvider(BaseViewStoreOwner.getInstance((BaseApp) getActivity().getApplication())).get((Class<ActivityModel>) types[types.length - 1]);

        getViewModel().setNavigator(navigator = (ActivityNavigator) this);

        if (getResources().getBoolean(in.yesandroid.base_android.R.bool.portrait_only)) {
            portraitOnly = true;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        return mViewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getBindingVariable() != -1)
            mViewBinding.setVariable(getBindingVariable(), mViewModel);
        mViewBinding.executePendingBindings();
        getViewModel().init(savedInstanceState);
        init(view, savedInstanceState);
        if (fragmentInteractions != null) {
            for (FragmentInteraction fragmentInteraction : fragmentInteractions) {
                fragmentInteraction.onFragmentStarted(this);
            }

        }
        getViewModel().onResume();
    }

    public ActivityModel getViewModel() {
        return mViewModel;
    }

    public DataBinding getViewDataBinding() {
        return mViewBinding;
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
    public void onDestroy() {
        super.onDestroy();
        getViewModel().onCleared();
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
    public void showAlertDialog(String title, String message, String positiveText, DialogInterface.OnClickListener pClickListener, String negative, DialogInterface.OnClickListener nClickListener) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity(), R.style.MaterialAlertDialogOverlay);
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
    public void finish() {
        getActivity().finish();
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return (ActivityCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void showLoading(String title, String message, boolean determinate) {
        hideLoading();
        mProgressDialog = Utils.showLoadingDialog(getContext(), title, message, false);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    @Override
    public void showLoading(String title, String message) {
        showLoading(title, message, false);

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
    public void updateProgress(int number) {
        if (mProgressDialog != null && !mProgressDialog.isIndeterminate()) {
            mProgressDialog.setProgress(number);
        }
    }


    @Override
    public <T extends BaseActivity> void pushToActivity(Class<?> tClass) {
        if (getActivity() != null)
            getActivity().startActivity(new Intent(getContext(), tClass));
        else
            showToast("Lost activity scope");
    }

    public interface Callback {

        void onFragmentAttached();

        void onFragmentDetached(String tag);

    }


    @Override
    public void showLoading(int title, int message) {
        showLoading(getString(title), getString(message));
    }

    @Override
    public void showLoading(int title, int message, boolean cancelable) {
        showLoading(getString(title), getString(message), cancelable);
    }

    public void injectModuleScope(Class<? extends BaseModule> clazz) {
        if (getActivity() == null) {
            return;

        }
        BaseModule module = ((BaseApp) getActivity().getApplication()).queryModule(clazz);

        if (getViewModel() instanceof BaseDataViewModel) {
            ((BaseDataViewModel) getViewModel()).setDataBase(module.getRoomDatabase());
        } else {
            throw new RuntimeException("No point in using inject method with BaseViewModel. Please use BaseDataViewModel for better management");
        }
    }


    @Override
    public <T extends BaseActivity> void pushToActivity(Class<?> tClass, Bundle bundle) {
        if (getActivity() != null) {
            if (getActivity() instanceof BaseActivity) {
                ((BaseActivity) getActivity()).pushToActivity(tClass, bundle);
            } else {
                throw new RuntimeException("Activity not supporeted please use BaseActivity as the calling activity");
            }
        }

    }

    @SuppressWarnings("rawtypes")
    @Override
    public void removeFrag(int id) {
        if (getActivity() != null) {
            if (getActivity() instanceof BaseActivity)
                ((BaseActivity) getActivity()).removeFrag(id);
        }
    }

    @Override
    public Fragment replaceFrag(int id, String className) {
        return replaceFrag(id, className, null);
    }


    @Override
    public Fragment createFragment(String className) {
        return getChildFragmentManager().getFragmentFactory().instantiate(ClassLoader.getSystemClassLoader(), className);
    }

    @Override
    public Fragment replaceFrag(int id, String className, Bundle bundle) {
        Fragment frag = getChildFragmentManager().getFragmentFactory().instantiate(ClassLoader.getSystemClassLoader(), className);
        if (bundle != null)
            frag.setArguments(bundle);
        getChildFragmentManager().beginTransaction()
                .add(id, frag)
                .commit();

        return frag;
    }

    @Override
    public final void dispatchMessageToActivity(int number) {
        if (fragmentInteractions != null) {
            for (FragmentInteraction fragmentInteraction : fragmentInteractions) {
                fragmentInteraction.onMessageReceived(number);
            }

        }
    }

    @Override
    public void dispatchObjectToActivity(Bundle message) {
        if (fragmentInteractions != null) {
            for (FragmentInteraction fragmentInteraction : fragmentInteractions) {
                //fragmentInteraction.onObjectReceviced(message);
            }

        }
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

    public void attachInteraction(@NonNull FragmentInteraction fragmentInteraction) {
        this.fragmentInteractions.add(fragmentInteraction);
    }

    public void dettachInteraction(@NonNull FragmentInteraction fragmentInteraction) {
        if (fragmentInteraction != null) {
            fragmentInteractions.remove(fragmentInteraction);
        }
    }

    protected boolean isPortraitOnly() {
        return portraitOnly;
    }


    public interface FragmentInteraction {
        void onFragmentStarted(BaseFragment baseFragment);

        void onMessageReceived(int value);

    }


}
