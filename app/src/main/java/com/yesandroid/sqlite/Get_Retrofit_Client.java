package com.yesandroid.sqlite;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Get_Retrofit_Client {


    public static final String Base_url="https://yesandroid.com/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}