package com.yesandroid.sqlite.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yesandroid.sqlite.BR;
import com.yesandroid.sqlite.R;
import com.yesandroid.sqlite.base.BaseActivity;
import com.yesandroid.sqlite.base.BaseFragment;
import com.yesandroid.sqlite.databinding.FragmentBlankBinding;


public class BlankFragment extends BaseFragment<BlankFragmentViewModel, FragmentBlankBinding> {


    @Override
    protected int getLayout() {
        return R.layout.fragment_blank;
    }

    @Override
    public int getViewModelId() {
        return BR.fragmentblankviewmodel;
    }

//    @Override
//    protected Class<BlankFragmentViewModel> getViewModel() {
//        return BlankFragmentViewModel.class;
//    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {

        getViewDataBinding().button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              // Log.e("clicked","clicked");
                getmViewModel().performLogin();
                Toast.makeText(getContext(), "clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}