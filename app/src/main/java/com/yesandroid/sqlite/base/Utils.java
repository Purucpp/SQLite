package com.yesandroid.sqlite.base;

import android.app.ProgressDialog;
import android.content.Context;

public class Utils {

    public static ProgressDialog showLoadingDialog(Context context, String title, String message, boolean determinate) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setIndeterminate(!determinate);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }
}
