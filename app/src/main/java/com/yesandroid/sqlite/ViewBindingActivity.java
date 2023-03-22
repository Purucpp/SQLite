package com.yesandroid.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.yesandroid.sqlite.databinding.ActivityMainBinding;
import com.yesandroid.sqlite.databinding.ActivityViewBindingBinding;

public class ViewBindingActivity extends AppCompatActivity {

    // calling binding class for activity_main.xml
    // which is generated automatically.
    ActivityViewBindingBinding activityViewBindingBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // inflating our xml layout in our activity main binding
        activityViewBindingBinding = ActivityViewBindingBinding.inflate(getLayoutInflater());

        // getting our root layout in our view.
        View view = activityViewBindingBinding.getRoot();

        // below line is to set
        // Content view for our layout.
        setContentView(view);

        // calling button and setting on click listener for our button.
        // we have called our button with its id and set on click listener on it.
        activityViewBindingBinding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = activityViewBindingBinding.editText.getText().toString();
                if(str.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter something", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "You entered " + activityViewBindingBinding.editText.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}