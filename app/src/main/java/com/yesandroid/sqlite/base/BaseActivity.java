package com.yesandroid.sqlite.base;


import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import java.lang.reflect.ParameterizedType;

public abstract class BaseActivity<V extends BaseViewModel, B extends ViewDataBinding> extends AppCompatActivity {
    protected V mViewModel;
    protected B mBinding;

    protected abstract @LayoutRes
    int getLayout();

    public abstract int getViewModelId();
  //  protected abstract Class<V> getViewModel();

   // protected abstract B getViewBinding();

    protected abstract void init(Bundle savedInstanceState);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // init();
        mBinding = DataBindingUtil.setContentView(this, getLayout());

      //  mViewModel = ViewModelProviders.of(this).get(getViewModel());

        try {
            mViewModel = new ViewModelProvider(this).
                    get((Class<V>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        } catch (Exception e) {
            mViewModel = new ViewModelProvider(this).
                    get((Class<V>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1]);
        }

        mBinding.setVariable(getViewModelId(),mViewModel);
        mBinding.executePendingBindings();
        getmViewModel().init(savedInstanceState);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getmViewModel().onCleared();

    }

}