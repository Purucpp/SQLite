package com.yesandroid.sqlite.db;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.yesandroid.sqlite.App;

/**
 * Created by Ali Asadi on 30/01/2019.
 */
@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {
    public abstract MovieDao movieDao();

    private static MovieDatabase sInstance;

    public static MovieDatabase getInstance() {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(App.getInstance(), MovieDatabase.class, "Movie.db").build();
        }
        return sInstance;
    }
}
