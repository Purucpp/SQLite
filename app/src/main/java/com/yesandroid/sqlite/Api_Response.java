package com.yesandroid.sqlite;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Api_Response {


    @SerializedName("name")
    @Expose
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }



    @SerializedName("imgurl")
    @Expose
    private String imgurl;
    public String getImgurl() {
        return imgurl;
    }
    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }


    @SerializedName("url")
    @Expose
    private String url;
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }



}
