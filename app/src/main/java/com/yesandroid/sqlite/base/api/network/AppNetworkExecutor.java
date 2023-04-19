package com.yesandroid.sqlite.base.api.network;

import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import in.yesandroid.base_android.api.AppGenericResponse;
import in.yesandroid.base_android.api.BaseRequest;
import in.yesandroid.base_android.api.network.response.CallImplementation;

public class AppNetworkExecutor extends ThreadPoolExecutor {


    private static AppNetworkExecutor appNetworkExecutor;

    private ExecutorService networkExecutorService;
    private static int THREAD_POOL_COUNT = 5;

    public static AppNetworkExecutor getInstance() {
        if (appNetworkExecutor == null) {
            synchronized (AppNetworkExecutor.class) {
                if (appNetworkExecutor == null) {
                    appNetworkExecutor = new AppNetworkExecutor();
                }
            }
        }

        return appNetworkExecutor;
    }

    AppNetworkExecutor() {
        super(THREAD_POOL_COUNT, THREAD_POOL_COUNT, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    }


    public void add(BaseRequest baseRequest, CallImplementation<String, Exception> responseHandler) {
        submit(new Runnable() {
            @Override
            public void run() {
                baseRequest.execute(responseHandler);
            }
        });

    }

    public <T extends JSONObject, P extends JSONObject> void addGeneric(BaseRequest baseRequest, AppGenericResponse<Exception> responseHandler) {
        networkExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                baseRequest.executeGeneric(responseHandler);
            }
        });

    }


}
