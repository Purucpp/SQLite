package com.yesandroid.sqlite.rx;


import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * Should not used for network request.
 * Created by kautilya on 01/02/18.
 */

public interface SchedulerProvider {

    Executor ui();

    ExecutorService computation();

    ExecutorService io();
}
