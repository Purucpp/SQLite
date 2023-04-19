package com.yesandroid.sqlite.base;

public interface BaseUrls {

    String URL_SERVER = BuildConfig.DEBUG ? "https://beta.yesandroid.in" : "https://beta.yesandroid.in";
    String URL_PATIENTS = URL_SERVER + "/nurse/patients/";
}
