package com.yesandroid.sqlite;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;



public class FirstViewModel extends AndroidViewModel {
    Context context;
    public ObservableField<String> email = new ObservableField<>("");
    public FirstViewModel(@NonNull Application application) {

        super(application);
        this.context=application;
    }


    public void performLogin()
    {
        Log.d("tk","tk");
        Toast.makeText(context, "Testing"+email.get(), Toast.LENGTH_SHORT).show();

        Intent intent=new Intent(context,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }
}