package com.yesandroid.sqlite;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //final Dialog dialog = new Dialog(this);

        /*
        Dialog dialog = new Dialog(this);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        lp.windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setAttributes(lp);

        */


       Context context= MyApplication.getAppContext();


       // Dialog dialog = new Dialog(new ContextThemeWrapper(this, R.style.DialogSlideAnim));

      //  getWindow().setBackgroundDrawableResource(android.R.color.background_dark);
    }

    public void openb(View v)
    {
//        ButtomSheet bt=new ButtomSheet();
//        bt.show(getSupportFragmentManager(),"example");

    }
}