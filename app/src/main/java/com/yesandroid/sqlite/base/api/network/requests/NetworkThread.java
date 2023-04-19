package com.yesandroid.sqlite.base.api.network.requests;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import in.yesandroid.base_android.api.BaseRequest;
import in.yesandroid.base_android.api.network.exceptions.AppVersionUpdateException;
import in.yesandroid.base_android.api.network.response.GenericResponse;

public class NetworkThread {


    private static final int CONNECTION_TIMEOUT_MILLISECONDS = 30000;
    private static final int READ_TIMEOUT_MILLISECONDS = 30000;


    public NetworkThread() {


    }

    // Dont not run threading on this function. This is the request calling function
    // Add threading annotaion lib for future functions
    // TODO: 24/05/20 Previous thought points: are  to connect network response together with database
    // Active feature thought : Zippy
    public String execute(BaseRequest.NetworkRequestType requestMode, String mUrl, HashMap<String, String> headers, String body) throws IOException, AppVersionUpdateException {


        URL urlConnection = new URL(mUrl);
        HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection.openConnection();
        httpURLConnection.setConnectTimeout(CONNECTION_TIMEOUT_MILLISECONDS);
        httpURLConnection.setReadTimeout(READ_TIMEOUT_MILLISECONDS);


        for (String header : headers.keySet()) {
            httpURLConnection.setRequestProperty(header, headers.get(header));
        }

        switch (requestMode) {
            case GET:
                httpURLConnection.setRequestMethod("GET");
                break;
            case PUT:
                httpURLConnection.setRequestMethod("PUT");
                break;
            case POST:
                httpURLConnection.setRequestMethod("POST");
                break;
            case HEAD:
                httpURLConnection.setRequestMethod("HEAD");
                break;
        }

        if (requestMode != BaseRequest.NetworkRequestType.GET) {
            /**
             * Writing out the jsonObject on to the connections outputstream.
             */
            OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
            wr.write(body);
            wr.flush();
        }

        /**
         * Running the request
         */
        int serverResponseCode = httpURLConnection.getResponseCode();
        String serverResponseMessage = httpURLConnection.getResponseMessage();


        InputStream inputStream = null;


        /**
         * If the request is successful, read the response from the body
         */
        try {
            inputStream = httpURLConnection.getInputStream();
        } catch (IOException ioe) {
            inputStream = httpURLConnection.getErrorStream();
        }

        /**
         * Building the response
         */
        BufferedReader br = new BufferedReader(new InputStreamReader((inputStream)));


        StringBuilder sb = new StringBuilder();
        String responseBody = "";

        String output;
        while ((output = br.readLine()) != null) {
            sb.append(output);
            responseBody = sb.toString();
        }


        if (serverResponseCode != 200) {
            httpURLConnection.disconnect();
            Log.e("Network", responseBody);
        }

        verifyResponse(responseBody);

        return responseBody;


    }


    private void verifyResponse(String response) throws AppVersionUpdateException, JsonSyntaxException {


        // Check and match the response if it matches the given structure
        GenericResponse responseEntity = new Gson().fromJson(response, GenericResponse.class);
        switch (responseEntity.getCode()) {
            case GenericResponse.RequestError.APP_UPDATE_MANDATORY:
                throw new AppVersionUpdateException("Application needs to be updateData to proceed ahead");
            default:

                break;
        }
        // This mean the api is properly supported with respect to application version and doesn't need a updateData


    }


}
