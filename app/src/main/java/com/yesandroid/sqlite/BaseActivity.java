package com.yesandroid.sqlite;


import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

public abstract class BaseActivity<V extends ViewModel, B extends ViewDataBinding> extends AppCompatActivity {
    protected V mViewModel;
    protected B mBinding;

    public abstract @LayoutRes
    int getLayout();

    public abstract int  getViewModel();

 //   protected abstract B getViewBinding();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    protected void init() {
        mBinding = DataBindingUtil.setContentView(this, getLayout());
       // mViewModel = ViewModelProviders.of(this).get(getViewModel());
    }

}