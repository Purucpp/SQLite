package com.yesandroid.sqlite.base.classes;

import android.os.Bundle;

import in.yesandroid.base_android.classes.navigators.ActivityAwareNavigator;

public interface BaseNavigator extends ActivityAwareNavigator {


    void dispatchMessageToActivity(int number);

    void dispatchObjectToActivity(Bundle message);
}
