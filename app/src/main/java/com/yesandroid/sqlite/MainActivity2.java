package com.yesandroid.sqlite;

import android.os.Bundle;

import com.yesandroid.sqlite.databinding.ActivityMain2Binding;

public class MainActivity2 extends BaseActivity<FirstViewModel, ActivityMain2Binding> {

    @Override
    public int getLayout() {
        return R.layout.activity_main2;
    }

    @Override
    protected Class<FirstViewModel> getViewModel() {
        return FirstViewModel.class;
    }

//    @Override
//    protected ActivityMain2Binding getViewBinding() {
//        return null;
//    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}