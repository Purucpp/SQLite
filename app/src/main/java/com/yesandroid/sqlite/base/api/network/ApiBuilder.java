package com.yesandroid.sqlite.base.api.network;

import in.yesandroid.base_android.api.BaseRequest;

public abstract class ApiBuilder {


    public ApiBuilder() {
    }

    public abstract void onRequestFormed(BaseRequest.RequestBuilder baseRequest);
}
