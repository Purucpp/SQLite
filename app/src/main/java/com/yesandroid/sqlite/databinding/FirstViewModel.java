package com.yesandroid.sqlite.databinding;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

import com.yesandroid.sqlite.MainActivity;
import com.yesandroid.sqlite.base.BaseViewModel;


public class FirstViewModel extends BaseViewModel {
    Context context;
    public ObservableField<String> email = new ObservableField<>("");
    public FirstViewModel(@NonNull Application application) {

        super(application);
        this.context=application;
    }

    @Override
    public void init(Bundle savedInstanceState) {

    }


    public void performLogin()
    {
        Log.d("tk","tk");
        Toast.makeText(context, "Testing"+email.get(), Toast.LENGTH_SHORT).show();

        Intent intent=new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }
}