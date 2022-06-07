package com.yesandroid.sqlite;

import android.app.Application;
import android.util.Log;


/**
 * Created by Ali Asadi on 10/03/2018.
 * https://stackoverflow.com/questions/2002288/static-way-to-get-context-in-android
 */
public class App extends Application {

    private static App sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        init(this);
    }

    private static void init(App app) {
        sInstance = app;
    }

    public static App getInstance() {
        return sInstance;
    }

}
