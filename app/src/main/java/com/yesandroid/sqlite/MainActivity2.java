package com.yesandroid.sqlite;

import android.os.Bundle;

import com.yesandroid.sqlite.databinding.ActivityMain2Binding;

public class MainActivity2 extends BaseActivity<ViewModel, ActivityMain2Binding> {

    @Override
    public int getLayout() {
        return R.layout.activity_main2;
    }

    @Override
    protected Class<ViewModel> getViewModel() {
        return null;
    }

    @Override
    protected ActivityMain2Binding getViewBinding() {
        return null;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}