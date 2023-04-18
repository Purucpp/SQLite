package com.yesandroid.sqlite.fragment;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.yesandroid.sqlite.base.BaseViewModel;

public class BlankFragmentViewModel extends BaseViewModel {
    public BlankFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void init(Bundle savedInstanceState) {

    }
}
