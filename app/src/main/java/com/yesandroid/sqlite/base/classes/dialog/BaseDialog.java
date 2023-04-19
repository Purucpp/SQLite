package com.yesandroid.sqlite.base.classes.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import in.yesandroid.base_android.R;
import in.yesandroid.base_android.classes.navigators.ContextAwareNavigator;
import in.yesandroid.base_android.utils.Utils;

public abstract class BaseDialog<L extends ViewDataBinding> extends Dialog implements ContextAwareNavigator {

    private L viewDataBinding;
    private ProgressDialog mProgressDialog;
    private boolean portraitOnly;

    public BaseDialog(@NonNull Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getContext().getResources().getBoolean(R.bool.portrait_only)) {
            portraitOnly = true;
        }

        viewDataBinding = DataBindingUtil.inflate(getLayoutInflater(), getLayoutId(), null, false);
        setContentView(viewDataBinding.getRoot());
    }

    @LayoutRes
    protected abstract int getLayoutId();

    public L getViewDataBinding() {
        return viewDataBinding;
    }

    public boolean isPortraitOnly() {
        return portraitOnly;
    }

    @Override
    public void showLoading(int title, int message) {
        showLoading(getContext().getString(title), getContext().getString(message));
    }

    @Override
    public void showLoading(int title, int message, boolean cancelable) {
        showLoading(getContext().getString(title), getContext().getString(message), cancelable);
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
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }


    @Override
    public void showLoading(String title, String message) {
        showLoading(title, message, false);

    }


    @Override
    public void showLoading(String title, String message, boolean determinate) {
        hideLoading();
        mProgressDialog = Utils.showLoadingDialog(getContext(), title, message, determinate);
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
        showToast(getContext().getResources().getString(message));

    }

    @Override
    public void updateProgress(String message) {
        displayMessage(false, message);

    }

    @Override
    public void showAlertDialog(@NonNull String title, @NonNull String message) {
        showAlertDialog(title, message, getContext().getResources().getString(R.string.ok), (dialog, which) -> dialog.dismiss(), "", null);
    }

    @Override
    public void showAlertDialog(@NonNull String title, @NonNull String message, @NonNull String positiveText, OnClickListener pClickListener) {
        showAlertDialog(title, message, positiveText, pClickListener, "", null);
    }

    @Override
    public void showAlertDialog(@NonNull String title, @NonNull String message, @NonNull String positiveText, OnClickListener pClickListener, @NonNull String negative, OnClickListener nClickListener) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext(), R.style.MaterialAlertDialogOverlay);
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
    public void showAlertDialog(@StringRes int title, @StringRes int message) {
        if (title == -1)
            showAlertDialog("", getContext().getString(message));
        else if (message == -1)
            showAlertDialog(getContext().getString(title), "");
        else
            showAlertDialog(getContext().getString(title), getContext().getString(message));
    }
}
