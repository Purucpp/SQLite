package com.yesandroid.sqlite;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AbstractBaseActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_main);


        showToast("Success");

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    public void openb(View v)
    {
//        ButtomSheet bt=new ButtomSheet();
//        bt.show(getSupportFragmentManager(),"example");

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}