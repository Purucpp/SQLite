package com.yesandroid.sqlite.base.utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.Operation;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkContinuation;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.Worker;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class WorkerUtils {


    private static OneTimeWorkRequest getOneTimeRequest(Class<? extends Worker> clazz, String tag, Constraints constraints, Data data) {
        if (constraints == null) {
            if (data == null)
                return new OneTimeWorkRequest.Builder(clazz)
                        .addTag(tag)
                        .build();
            else {


                return new OneTimeWorkRequest.Builder(clazz)
                        .addTag(tag)
                        .setInputData(data)
                        .build();
            }
        } else {
            if (data == null)
                return new OneTimeWorkRequest.Builder(clazz)
                        .setConstraints(constraints)
                        .addTag(tag)
                        .build();
            else {
                return new OneTimeWorkRequest.Builder(clazz)
                        .setConstraints(constraints)
                        .addTag(tag)
                        .setInputData(data)
                        .build();
            }

        }

    }


    public static PeriodicWorkRequest.Builder triggerPeriodicWorker(Context context, Class<? extends Worker> workerClazz, long timeInMills, String tag) {
        return triggerPeriodicWorker(context, workerClazz, timeInMills, tag, null);

    }

    public static PeriodicWorkRequest.Builder triggerPeriodicWorker(Context context, Class<? extends Worker> workerClazz, long timeInMillis, String tag, Constraints constraints) {
        return triggerPeriodicWorker(context, workerClazz, timeInMillis, tag, constraints, null);

    }

    public static PeriodicWorkRequest.Builder triggerPeriodicWorker(Context context, Class<? extends Worker> workerClazz, long timeInMills, String tag, Constraints constraints, Data dataSentToWorker) {
        PeriodicWorkRequest.Builder periodicWork = new PeriodicWorkRequest.Builder(workerClazz, timeInMills, TimeUnit.MILLISECONDS);
        periodicWork.addTag(tag);


        if (constraints != null) {
            periodicWork.setConstraints(constraints);
        }
        if (dataSentToWorker != null) {
            periodicWork.setInputData(dataSentToWorker);

        }
        WorkManager workManager;

        if (context == null) {
            workManager = WorkManager.getInstance();
        } else {
            workManager = WorkManager.getInstance(context);
        }

        workManager.enqueueUniquePeriodicWork(tag, ExistingPeriodicWorkPolicy.KEEP, periodicWork.build());
        return periodicWork;
    }


    public static OneTimeWorkRequest triggerOneTimeWorker(Context context, Class<? extends Worker> workerClazz, String tag, Constraints constraints) {

        return triggerOneTimeWorker(context, workerClazz, tag, constraints, null);
    }

    public static OneTimeWorkRequest triggerOneTimeWorker(@NonNull Context context, Class<? extends Worker> workerClazz, String tag, Constraints constraints, Data dataSentToWorker) {
        OneTimeWorkRequest worker = getOneTimeRequest(workerClazz, tag, constraints, dataSentToWorker);
        WorkManager.getInstance(context).enqueueUniqueWork(tag, ExistingWorkPolicy.KEEP, worker);
        return worker;
    }


    public static void triggerChainedWorker(Context context, String tag, Constraints constraints, Data dataSentToWorker, Class<? extends Worker>... workers) {
        List<OneTimeWorkRequest> workRequests = new ArrayList<>();
        for (Class<? extends Worker> worker : workers) {
            workRequests.add(getOneTimeRequest(worker, tag, constraints, dataSentToWorker));
        }

        WorkContinuation work = WorkManager.getInstance(context).beginUniqueWork("unique_chained_" + tag, ExistingWorkPolicy.KEEP, workRequests.get(0));

        for (OneTimeWorkRequest worker : workRequests.subList(1, workRequests.size())) {
            work = work.then(worker);
        }

        work.enqueue();

    }


    public void dispatchMessage(Context context, String workerKey, Data data) throws ExecutionException, InterruptedException {
        for (WorkInfo workInfo : WorkManager.getInstance(context).getWorkInfosByTag(workerKey).get()) {
            dispatchMessage(context, workInfo.getId(), data);
        }
    }

    public void dispatchMessage(Context context, UUID workId, Data data) throws ExecutionException, InterruptedException {
        WorkInfo object = WorkManager.getInstance(context).getWorkInfoById(workId).get();

    }

    public static Operation stopWorker(Context context, String tag) {
        return WorkManager.getInstance(context).cancelAllWorkByTag(tag);
    }

}
