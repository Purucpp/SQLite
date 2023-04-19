package com.yesandroid.sqlite.base.init;

import android.os.Build;

import androidx.annotation.RequiresApi;

public class ModuleReInitializationError extends RuntimeException {


    public ModuleReInitializationError() {
        this("Module Reinitialized. Please ensure to call init method of module only once ");
    }

    public ModuleReInitializationError(String message) {
        super("Module Reinitialized. Please ensure to call init method of module only once ");
    }

    public ModuleReInitializationError(String message, Throwable cause) {
        super("Module Reinitialized. Please ensure to call init method of module only once ", cause);
    }

    public ModuleReInitializationError(Throwable cause) {
        super(cause);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ModuleReInitializationError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super("Module Reinitialized. Please ensure to call init method of module only once ", cause, enableSuppression, writableStackTrace);
    }
}
