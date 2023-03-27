package com.yesandroid.sqlite;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

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




       // Dialog dialog = new Dialog(new ContextThemeWrapper(this, R.style.DialogSlideAnim));

      //  getWindow().setBackgroundDrawableResource(android.R.color.background_dark);
    }

    public void openb(View v)
    {

        WorkManager mWorkManager = WorkManager.getInstance();
        OneTimeWorkRequest mRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class).build();
        mWorkManager.enqueue(mRequest);
//        ButtomSheet bt=new ButtomSheet();
//        bt.show(getSupportFragmentManager(),"example");

    }
}