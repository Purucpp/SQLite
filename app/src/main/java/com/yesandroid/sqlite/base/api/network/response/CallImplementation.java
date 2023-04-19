package com.yesandroid.sqlite.base.api.network.response;

public interface CallImplementation<ResponseObject, ErrorResponse> {

    void onResponse(ResponseObject response);

    void onError(ErrorResponse error);


}
