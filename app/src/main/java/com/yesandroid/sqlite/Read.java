package com.yesandroid.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.database.Cursor;



import android.os.Bundle;

import android.view.View;

import android.widget.Button;

import android.widget.EditText;

import android.widget.TextView;

import android.widget.Toast;


public class Read extends AppCompatActivity {


    DataBaseHelper myDb;

    TextView txtResult;

    Button btnClick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);


        myDb = new DataBaseHelper(this);

        txtResult =  findViewById(R.id.idResult);

        btnClick = findViewById(R.id.idBtn);

        btnClick.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                ClickMe();

            }

        });


    }

    private void ClickMe() {

        Cursor res = myDb.getAllData();

        StringBuffer stringBuffer = new StringBuffer();

        if(res!=null && res.getCount()>0){

            while (res.moveToNext()){

                stringBuffer.append("Id: "+res.getString(0)+"\n");

                stringBuffer.append("Name: "+res.getString(1)+"\n");

                stringBuffer.append("Surname: "+res.getString(2)+"\n");

                stringBuffer.append("Marks: "+res.getString(3)+"\n"+"\n");

            }

            txtResult.setText(stringBuffer.toString());

            Toast.makeText(this,"Data Retrieved Successfully",Toast.LENGTH_SHORT).show();

        }else{

            Toast.makeText(this,"No Data to Retrieve",Toast.LENGTH_SHORT).show();

        }



    }
}




















