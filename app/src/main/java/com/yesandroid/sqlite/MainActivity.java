package com.yesandroid.sqlite;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.yesandroid.sqlite.db.Movie;
import com.yesandroid.sqlite.db.MovieDao;
import com.yesandroid.sqlite.db.MovieDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {


    ExecutorService executorService=Executors.newSingleThreadExecutor();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //final Dialog dialog = new Dialog(this);

        MovieDatabase movieDatabase=MovieDatabase.getInstance();

        Movie movie=new Movie();
        movie.setTitle("pk");
        movie.setDescription("test");
        movie.setImage("ik");


        executorService.execute(new Runnable() {
            @Override
            public void run() {
                movieDatabase.movieDao().singleMovie(movie);
            }
        });



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
        ButtomSheet bt=new ButtomSheet();
        bt.show(getSupportFragmentManager(),"example");

    }
}