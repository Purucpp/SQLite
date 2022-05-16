package com.yesandroid.sqlite;

import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Get_Interface {

    @GET("social/a.json")
    Call<JsonArray> getsocial();
}
