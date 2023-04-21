package com.yesandroid.sqlite.base;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.yesandroid.sqlite.R;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseFragment<V extends BaseViewModel, B extends ViewDataBinding> extends Fragment {

    protected V mViewModel;
    protected B mBinding;

    private ProgressDialog mProgressDialog;

    protected abstract @LayoutRes
    int getLayout();

    public abstract int getViewModelId();
  //  protected abstract Class<V> getViewModel();

    // protected abstract B getViewBinding();

    protected abstract void init(View view, Bundle savedInstanceState);
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


      //  mViewModel = ViewModelProviders.of(this).get(getViewModel());

      //  mViewModel = new ViewModelProvider(this).get((Class<V>) types[types.length - 1]);
        mViewModel =  new ViewModelProvider(this).
                get((Class<V>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getViewModelId() != -1)
            mBinding.setVariable(getViewModelId(), mViewModel);
        mBinding.executePendingBindings();
        getmViewModel().init(savedInstanceState);

        init(view, savedInstanceState);

    }



    public B getViewDataBinding() {
        return mBinding;
    }

    public V getmViewModel()
    {
        return mViewModel;
    }

    public void showToast(String message)
    {
     //   Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getmViewModel().onCleared();
    }



    public Fragment replaceFrag(int id, String className) {
        return replaceFrag(id, className, null);
    }



    public Fragment createFragment(String className) {
        return getChildFragmentManager().getFragmentFactory().instantiate(ClassLoader.getSystemClassLoader(), className);
    }


    public Fragment replaceFrag(int id, String className, Bundle bundle) {
        Fragment frag = getChildFragmentManager().getFragmentFactory().instantiate(ClassLoader.getSystemClassLoader(), className);
        if (bundle != null)
            frag.setArguments(bundle);
        getChildFragmentManager().beginTransaction()
                .add(id, frag)
                .commit();

        return frag;
    }


    public void showAlertDialog(@NonNull String title, @NonNull String message) {
        showAlertDialog(title, message, "ok", (dialog, which) -> dialog.dismiss(), "", null);
    }


    public void showAlertDialog(@NonNull String title, @NonNull String message, @NonNull String positiveText, DialogInterface.OnClickListener pClickListener) {
        showAlertDialog(title, message, positiveText, pClickListener, "", null);
    }


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


    public void finish() {
        getActivity().finish();
    }


    public void showLoading(String title, String message, boolean determinate) {
        hideLoading();
        mProgressDialog = Utils.showLoadingDialog(getContext(), title, message, false);
    }




    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }


    public void showLoading(String title, String message) {
        showLoading(title, message, false);

    }


    public void showToast(int message) {
        showToast(getString(message));

    }

    public void displayMessage(boolean success, String message) {
        if (mProgressDialog != null && mProgressDialog.isShowing() && success)
            mProgressDialog.dismiss();

        if (!success && mProgressDialog.isShowing()) {
            mProgressDialog.setMessage(message);
        }

    }

    public void updateProgress(String message) {
        displayMessage(false, message);

    }


    public void updateProgress(int number) {
        if (mProgressDialog != null && !mProgressDialog.isIndeterminate()) {
            mProgressDialog.setProgress(number);
        }
    }



    public void showLoading(int title, int message) {
        showLoading(getString(title), getString(message));
    }


    public void showLoading(int title, int message, boolean cancelable) {
        showLoading(getString(title), getString(message), cancelable);
    }

    public void showAlertDialog(@StringRes int title, @StringRes int message) {
        if (title == -1)
            showAlertDialog("", getString(message));
        else if (message == -1)
            showAlertDialog(getString(title), "");
        else
            showAlertDialog(getString(title), getString(message));
    }

    /*
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




    public void attachInteraction(@NonNull FragmentInteraction fragmentInteraction) {
        this.fragmentInteractions.add(fragmentInteraction);
    }

    public void dettachInteraction(@NonNull FragmentInteraction fragmentInteraction) {
        if (fragmentInteraction != null) {
            fragmentInteractions.remove(fragmentInteraction);
        }
    }

    */




}
