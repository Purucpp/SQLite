package com.yesandroid.sqlite.base.init;

import android.os.Build;

import androidx.annotation.RequiresApi;

public class ModuleInitializationException extends RuntimeException {


    public ModuleInitializationException() {
        this("Error initialization please check  initialization once again in BaseModule");
    }

    public ModuleInitializationException(String message) {
        super(message);
    }


    public ModuleInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModuleInitializationException(Throwable cause) {
        super(cause);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ModuleInitializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
