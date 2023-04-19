package com.yesandroid.sqlite.base.api;

import org.json.JSONObject;

import in.yesandroid.base_android.api.network.response.CallImplementation;

public abstract class AppGenericResponse<T extends Exception> implements CallImplementation<JSONObject, String> {


    public abstract void onRequestError(T exception);


}
