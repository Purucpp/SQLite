package com.yesandroid.sqlite.base.classes.navigators;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import in.yesandroid.base_android.classes.BaseActivity;

public interface ActivityAwareNavigator extends ContextAwareNavigator {


    /**
     * Used to navigate from screen to screen
     *
     * @param tClass Activity class to be pushed to
     * @param <T>    Type class of Activity
     */
    <T extends BaseActivity> void pushToActivity(Class<?> tClass);

    /**
     * Used to navigate from screen to screen with bundle
     *
     * @param tClass Activity class to be pushed to
     * @param <T>    Type class of Activity
     */
    <T extends BaseActivity> void pushToActivity(Class<?> tClass, Bundle bundle);


    /**
     * Exposed {@link AppCompatActivity#finish()} of activity. Useless in terms of fragment
     */
    void finish();

    /**
     * Exposed context of activity and {@link Fragment#getContext()}
     */
    Context getContext();

    Fragment replaceFrag(int id, String className);

    Fragment createFragment(String className);

    Fragment replaceFrag(int id, String className, Bundle bundle);

    void removeFrag(@IdRes int id);
}
