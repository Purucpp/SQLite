package com.yesandroid.sqlite.base.rx;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppSchedulerProvider implements SchedulerProvider {

    // TODO: 16/07/20 Maybe we should update to schedulers
    //       https://blog.gojekengineering.com/multi-threading-like-a-boss-in-android-with-rxjava-2-b8b7cf6eb5e2
    private MainThreadExecutor instance;
    private ExecutorService computationService;
    private ExecutorService ioService;


    @Override
    public Executor ui() {
        if (instance == null) {
            instance = new MainThreadExecutor();
        }
        return instance;
    }

    @Override
    public ExecutorService computation() {

        if (computationService == null) {
            computationService = Executors.newCachedThreadPool();
        }
        return computationService;
    }

    @Override
    public ExecutorService io() {
        if (ioService == null) {
            ioService = Executors.newSingleThreadExecutor();
        }
        return ioService;
    }

    public static ExecutorService provideSingleThread() {
        return Executors.newSingleThreadExecutor();
    }
}
