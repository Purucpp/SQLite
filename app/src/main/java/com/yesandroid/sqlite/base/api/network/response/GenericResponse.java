package com.yesandroid.sqlite.base.api.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Generic response for api server. Do not parse response here as the usage of this object is to
 * only verify response but not the response result.
 */
public class GenericResponse {


    @Expose
    @SerializedName("error")
    private String error;
    @Expose
    @SerializedName("code")
    private String code;

    public GenericResponse() {
        code = "";
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public interface RequestError {
        String APP_UPDATE_MANDATORY = "APP_UPDATE_REQUIRED";
    }
}
