package com.yesandroid.sqlite.base.classes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class BaseAndroidViewModel extends AndroidViewModel {
    public BaseAndroidViewModel(@NonNull Application application) {
        super(application);
    }
}
