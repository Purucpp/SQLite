package com.yesandroid.sqlite.databinding;

import android.os.Bundle;

import com.yesandroid.sqlite.BR;
import com.yesandroid.sqlite.base.BaseActivity;
import com.yesandroid.sqlite.R;

public class MainActivity2 extends BaseActivity<FirstViewModel, ActivityMain2Binding> {

    @Override
    public int getLayout() {
        return R.layout.activity_main2;
    }
    @Override
    public int getViewModelId() {
        return BR.firstViewModel;
    }
//    @Override
//    protected Class<FirstViewModel> getViewModel() {
//        return FirstViewModel.class;
//    }

    @Override
    protected void init(Bundle savedInstanceState) {

     /*   getViewDataBinding().button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(MainActivity2.this, ""+getViewDataBinding().editText.getText(), Toast.LENGTH_SHORT).show();
                showToast(getViewDataBinding().editText.getText().toString());
            }
        }); */



    }


}