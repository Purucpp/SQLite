package com.yesandroid.sqlite.base.prefs;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.LinkedHashSet;
import java.util.Set;

public abstract class BaseSharedPreferences {
    private SharedPreferences sharedPreferences;

    public BaseSharedPreferences(Application application) {
        init(application);
    }

    protected void init(Application context) {
        sharedPreferences = context.getSharedPreferences(getName(), Context.MODE_PRIVATE);
    }


    protected abstract String getName();


    protected final void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value).apply();
    }






    protected final void putLong(String key, long value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value).apply();
    }

    protected final void putInteger(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value).apply();
    }

    protected final void putFloat(String key, float value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value).apply();
    }

    protected final void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value).apply();
    }

    protected final void putStringSet(String key, Set<String> value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(key, value).apply();
    }


    protected final String getString(String key) {
        return sharedPreferences.getString(key, "");
    }



    protected final long getLong(String key) {
        return sharedPreferences.getLong(key, -1);
    }

    protected final int getInteger(String key) {
        return sharedPreferences.getInt(key, -1);
    }

    protected final float getFloat(String key) {
        return sharedPreferences.getFloat(key, -1);
    }

    protected final boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    protected final Set<String> getStringSet(String key) {
        return sharedPreferences.getStringSet(key, new LinkedHashSet<>());
    }

    public final void clearAll() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }


    protected SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
}
