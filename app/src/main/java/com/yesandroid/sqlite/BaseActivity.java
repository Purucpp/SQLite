package com.yesandroid.sqlite;


import android.os.Bundle;
import android.widget.Toast;

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

    protected abstract @LayoutRes
    int getLayout();

    protected abstract Class<V> getViewModel();

   // protected abstract B getViewBinding();

    protected abstract void init(Bundle savedInstanceState);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // init();
        mBinding = DataBindingUtil.setContentView(this, getLayout());
        mViewModel = ViewModelProviders.of(this).get(getViewModel());
        mBinding.executePendingBindings();
        init(savedInstanceState);
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
        Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
    }

}