package com.yesandroid.sqlite;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


import com.yesandroid.sqlite.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // setContentView(R.layout.activity_main);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        user = new User();
        user.setName("Ravi Tamada");
        user.setEmail("ravi@androidhive.info");

        binding.setUser(user);

    }
}
