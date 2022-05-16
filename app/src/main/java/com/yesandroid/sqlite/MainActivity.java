package com.yesandroid.sqlite;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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



            Get_Interface apiService= Get_Retrofit_Client.getClient().create(Get_Interface.class);
            Call<JsonArray> call = apiService.getsocial();
            Log.d("pass 2" ,"passed form 2");
            call.enqueue(new Callback<JsonArray>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                   Log.d("finalres",response.body().toString());
                }
                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {

                    Log.d("failed","failed");
                    Log.d("reason",t.getMessage());
                    //  t.setText("Failed :"+t.getMessage());
                }
            });






       // Dialog dialog = new Dialog(new ContextThemeWrapper(this, R.style.DialogSlideAnim));

      //  getWindow().setBackgroundDrawableResource(android.R.color.background_dark);
    }


}