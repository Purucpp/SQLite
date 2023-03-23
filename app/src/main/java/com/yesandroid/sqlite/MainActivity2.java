package com.yesandroid.sqlite;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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

    @Override
    protected void init(Bundle savedInstanceState) {

        getViewDataBinding().button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(MainActivity2.this, ""+getViewDataBinding().editText.getText(), Toast.LENGTH_SHORT).show();
                showToast(getViewDataBinding().editText.getText().toString());
            }
        });

    }

//    @Override
//    protected ActivityMain2Binding getViewBinding() {
//        return null;
//    }


//    @Override
//    public void onPointerCaptureChanged(boolean hasCapture) {
//        super.onPointerCaptureChanged(hasCapture);
//    }
}