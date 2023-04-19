package com.yesandroid.sqlite.base.classes;

import android.os.Bundle;

import androidx.room.RoomDatabase;

import in.yesandroid.base_android.BaseApp;
import in.yesandroid.base_android.module.BaseModule;

/**
 * Experimentation class for simple access of room data base
 */
public abstract class BaseDataViewModel<T extends RoomDatabase, L extends BaseNavigator> extends BaseViewModel<L> {
    private T dataBase;

    public BaseDataViewModel(BaseApp application) {
        super(application);
    }

    @Override
    public void init(Bundle savedInstanceState) {

    }


    public void injectModuleScope(Class<? extends BaseModule> clazz) {
        BaseModule module = getBaseApp().queryModule(clazz);
        setDataBase((T) module.getRoomDatabase());
    }

    public T getDataBase() {
        return dataBase;
    }

    public final void setDataBase(T dataBase) {
        this.dataBase = dataBase;
    }
}
