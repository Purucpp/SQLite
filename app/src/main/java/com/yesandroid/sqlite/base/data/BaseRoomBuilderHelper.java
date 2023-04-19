package com.yesandroid.sqlite.base.data;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import in.yesandroid.base_android.BuildConfig;
import in.yesandroid.base_android.init.ModuleReInitializationError;

public abstract class BaseRoomBuilderHelper {

    private Context context;

    protected RoomDatabase roomDatabase;

    public BaseRoomBuilderHelper(Context context) {
        this.context = context;
        build();
    }

    public void build() {
        if (roomDatabase == null) {
            if (BuildConfig.DEBUG)
                roomDatabase = Room.databaseBuilder(getContext(), getDataBaseClass(), getDBName())
                        .fallbackToDestructiveMigration()
                        .build();
            else
                roomDatabase = Room.databaseBuilder(getContext(), getDataBaseClass(), getDBName())
                        .build();
        } else
            throw new ModuleReInitializationError();

    }

    protected abstract Class<? extends RoomDatabase> getDataBaseClass();

    protected abstract String getDBName();

    public Context getContext() {
        return context;
    }


    public RoomDatabase getRoomDatabase() {
        return roomDatabase;
    }
}
