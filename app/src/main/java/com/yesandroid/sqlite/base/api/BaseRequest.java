package com.yesandroid.sqlite.base.api;

import static in.yesandroid.base_android.api.JSONConstants.JSON_ERROR;
import static in.yesandroid.base_android.api.JSONConstants.JSON_RESPONSE;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;

import in.yesandroid.base_android.api.network.ApiBuilder;
import in.yesandroid.base_android.api.network.AppNetworkExecutor;
import in.yesandroid.base_android.api.network.requests.NetworkThread;
import in.yesandroid.base_android.api.network.response.CallImplementation;

public class BaseRequest {

    private String rawResponse;

    public enum NetworkRequestType {GET, POST, HEAD, PUT}

    private NetworkThread networkThread;
    private NetworkRequestType requestMode;
    private HashMap<String, String> headers;
    private String url;
    private String body;
    private HashMap<String, String> queryParams;
    private ExecutorService networkExecutor;

    BaseRequest() {
        url = "";
        body = "";
        queryParams = new HashMap<>();
        headers = new HashMap<>();
        networkThread = new NetworkThread();
        networkExecutor = AppNetworkExecutor.getInstance();
    }


    void setUrl(String url) {
        this.url = url;
    }

    // Maybe have to remove this
    void setNetworkThread(NetworkThread networkThread) {
        this.networkThread = networkThread;
    }

    NetworkRequestType getRequestMode() {
        return requestMode;
    }

    void setRequestMode(NetworkRequestType requestMode) {
        this.requestMode = requestMode;
    }

    void addHeaders(String key, String value) {
        this.headers.put(key, value);
    }

    void addHeaders(HashMap<String, String> headers) {
        this.headers.putAll(headers);
    }

    void addQueryParams(String key, String value) {
        queryParams.put(key, value);
    }

    void setBody(String body) {
        this.body = body;
    }


    void needToAddTheseheaders() {
        // TODO: 13/05/20 Don't add these headers in Login and signup JSONPOST
        // con.setRequestProperty("App-Version", String.valueOf(BuildConfig.VERSION_CODE));
        // con.setRequestProperty("Application-name", AutoUpdateApk.Apps.DAKSH);

        // TODO: 13/05/20  dont add these URL_VERSION_API JSONGET

        // conn.setRequestProperty("App-Version", String.valueOf(BuildConfig.VERSION_CODE));
        // conn.setRequestProperty("Application-name", AutoUpdateApk.Apps.DAKSH);
    }

    /**
     * Execute method to perform network request
     *
     * @param responseHandler currently supports only CallImplementation<String, Exception> {@link CallImplementation}
     */
    public void execute(CallImplementation<String, Exception> responseHandler) {
        try {
            if (queryParams.size() != 0) {
                StringBuilder stringBuilder = new StringBuilder(url);
                stringBuilder.append("?");
                for (String key : queryParams.keySet()) {
                    stringBuilder.append(key).append("=").append(queryParams.get(key)).append("&");
                }
                url = stringBuilder.toString();

            }

            rawResponse = networkThread.execute(requestMode, url, headers, body);
            responseHandler.onResponse(rawResponse);
        } catch (IOException e) {
            responseHandler.onError(e);
        }


    }

    /**
     * Execute method to perform network request with standard response conversion
     *
     * @param responseHandler currently supports only CallImplementation<String, Exception> {@link CallImplementation}
     */
    public BaseRequest executeGeneric(AppGenericResponse<Exception> responseHandler) {
        try {
            if (queryParams.size() != 0) {
                StringBuilder stringBuilder = new StringBuilder(url);
                stringBuilder.append("?");
                for (String key : queryParams.keySet()) {
                    stringBuilder.append(key).append("=").append(queryParams.get(key)).append("&");
                }
                url = stringBuilder.toString();

            }
            // Calling the network thread to execute the request
            rawResponse = networkThread.execute(requestMode, url, headers, body);
            JSONObject apiResponseObject = new JSONObject(rawResponse);
            JSONObject responseObject;
            String errorObject;

            if (apiResponseObject.has(JSON_ERROR) && !apiResponseObject.getString(JSON_ERROR).equals("")) {
                errorObject = apiResponseObject.getString(JSON_ERROR);
                responseHandler.onError(errorObject);

                return this;
            }
            if (apiResponseObject.has(JSON_RESPONSE)) {
                responseObject = apiResponseObject.getJSONObject(JSON_RESPONSE);
                responseHandler.onResponse(responseObject);
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
            responseHandler.onRequestError(e);
        }

        return this;

    }

    public String get() {
        return rawResponse;
    }

    /**
     * Queues the request in the network thread
     *
     * @param responseHandler currently supports only CallImplementation<String, Exception> {@link CallImplementation}
     */
    public void enqueue(CallImplementation<String, Exception> responseHandler) {
        if (networkExecutor instanceof AppNetworkExecutor) {
            ((AppNetworkExecutor) networkExecutor).add(this, responseHandler);
        } else {
            networkExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    execute(responseHandler);
                }
            });
        }

    }

    /**
     * Queues the generic request in the network thread
     *
     * @param responseHandler
     */
    public void enqueueGeneric(AppGenericResponse<Exception> responseHandler) {
        if (networkExecutor instanceof AppNetworkExecutor) {
            ((AppNetworkExecutor) networkExecutor).addGeneric(this, responseHandler);
        } else {
            networkExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    executeGeneric(responseHandler);
                }
            });
        }
    }

    public BaseRequest runOn(ExecutorService executorService) {
        networkExecutor = executorService;
        return this;
    }


    public static class RequestBuilder {


        private BaseRequest baseRequest;
        private List<ApiBuilder> apiBuilders;

        public RequestBuilder() {

            baseRequest = new BaseRequest();
            apiBuilders = new ArrayList<>();
            addHeaders("Content-Type", "application/json");

        }

        public RequestBuilder setRequestUrl(String requestUrl) {
            baseRequest.setUrl(requestUrl);
            return this;
        }

        public RequestBuilder get() {
            baseRequest.setRequestMode(NetworkRequestType.GET);
            return this;
        }

        public RequestBuilder post() {
            baseRequest.setRequestMode(NetworkRequestType.POST);
            return this;
        }


        public RequestBuilder head() {
            baseRequest.setRequestMode(NetworkRequestType.HEAD);
            return this;
        }

        public RequestBuilder put() {
            baseRequest.setRequestMode(NetworkRequestType.PUT);
            return this;
        }

        public RequestBuilder addQueryParams(String key, String value) {
            baseRequest.addQueryParams(key, value);
            return this;
        }


        public RequestBuilder setHeaders(JSONObject jsonObject) throws JSONException {
            HashMap<String, String> header = new HashMap<>();
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                header.put(key, jsonObject.getString(key));
            }

            return setHeaders(header);
        }

        public RequestBuilder setHeaders(HashMap<String, String> headers) {
            baseRequest.addHeaders(headers);
            return this;
        }

        public RequestBuilder addHeaders(String key, String value) {
            baseRequest.addHeaders(key, value);
            return this;
        }

        public RequestBuilder addBody(String body) {
            baseRequest.setBody(body);
            return this;
        }

        public BaseRequest build() {

            for (ApiBuilder apiBuilder : apiBuilders) {
                apiBuilder.onRequestFormed(this);
            }
            return baseRequest;
        }


        public RequestBuilder addBuilder(ApiBuilder apiBuilder) {
            apiBuilders.add(apiBuilder);
            return this;
        }
    }
}
