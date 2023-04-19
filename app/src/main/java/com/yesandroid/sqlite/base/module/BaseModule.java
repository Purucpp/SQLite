package com.yesandroid.sqlite.base.module;

import androidx.room.RoomDatabase;

import in.yesandroid.base_android.BaseApp;
import in.yesandroid.base_android.init.ModuleInitializationException;

public abstract class BaseModule {

    private RoomDatabase roomDatabase;


    protected BaseModule() {

    }

    public abstract void init(BaseApp application) throws ModuleInitializationException;

    public final RoomDatabase getRoomDatabase() {
        return roomDatabase;
    }

    public final void setRoomDatabase(RoomDatabase roomDatabase) {
        this.roomDatabase = roomDatabase;
    }
}
