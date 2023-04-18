package com.yesandroid.sqlite.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import java.lang.reflect.ParameterizedType;

public abstract class BaseFragment<V extends BaseViewModel, B extends ViewDataBinding> extends Fragment {

    protected V mViewModel;
    protected B mBinding;

    protected abstract @LayoutRes
    int getLayout();

    public abstract int getViewModelId();
  //  protected abstract Class<V> getViewModel();

    // protected abstract B getViewBinding();

    protected abstract void init(View view, Bundle savedInstanceState);
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


      //  mViewModel = ViewModelProviders.of(this).get(getViewModel());

      //  mViewModel = new ViewModelProvider(this).get((Class<V>) types[types.length - 1]);
        mViewModel =  new ViewModelProvider(this).
                get((Class<V>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getViewModelId() != -1)
            mBinding.setVariable(getViewModelId(), mViewModel);
        mBinding.executePendingBindings();
        getmViewModel().init(savedInstanceState);

        init(view, savedInstanceState);

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
     //   Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getmViewModel().onCleared();
    }
}
