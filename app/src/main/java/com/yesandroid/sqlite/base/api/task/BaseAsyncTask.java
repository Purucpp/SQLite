package com.yesandroid.sqlite.base.api.task;


import android.os.AsyncTask;

import androidx.annotation.NonNull;


/**
 * Wraper class around the AsyncTask
 *
 * @param <Params>   Param type sent to async task
 * @param <Progress> Progress type of the async task
 * @param <Result>   Result type returned by the async task
 */
public abstract class BaseAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    private OnCompleteListener<Result> resultOnCompleteListener;
    private OnErrorListener onErrorListener;
    private OnProgressListener<Progress> onProgressListener;
    private Exception exception;
    protected int successLogger;

    public BaseAsyncTask(OnCompleteListener<Result> resultOnCompleteListener, OnErrorListener onErrorListener, OnProgressListener<Progress> onProgressListener) {
        successLogger = -1;
        this.resultOnCompleteListener = resultOnCompleteListener;
        this.onErrorListener = onErrorListener;
        this.onProgressListener = onProgressListener;
    }

    public BaseAsyncTask(OnCompleteListener<Result> resultOnCompleteListener, OnErrorListener onErrorListener) {
        this(resultOnCompleteListener, onErrorListener, null);
    }

    public BaseAsyncTask(OnCompleteListener<Result> resultOnCompleteListener) {
        this(resultOnCompleteListener, null, null);
    }

    public BaseAsyncTask() {
        this(null);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    final protected Result doInBackground(Params... params) {
        try {
            return doWork(params);
        } catch (Exception e) {
            exception = e;
        }
        return null;
    }


    @Override
    protected void onProgressUpdate(Progress... values) {
        super.onProgressUpdate(values);
        if (onProgressListener != null)
            onProgressListener.onProgressUpdated(values);
    }

    @Override
    final protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        if (result != null) {
            if (resultOnCompleteListener != null)
                resultOnCompleteListener.onComplete(result);
        } else {
            if (onErrorListener != null)
                onErrorListener.onError(exception);
        }
    }

    protected abstract Result doWork(Params... params) throws Exception;

    public OnCompleteListener<Result> getResultOnCompleteListener() {
        return resultOnCompleteListener;
    }

    public void setResultOnCompleteListener(OnCompleteListener<Result> resultOnCompleteListener) {
        this.resultOnCompleteListener = resultOnCompleteListener;
    }

    public OnErrorListener getOnErrorListener() {
        return onErrorListener;
    }

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    /**
     * Interface to detect completion of the AsyncTask. Exposed at the constructor
     *
     * @param <Result> The result type returned by AsyncTask
     */
    public interface OnCompleteListener<Result> {
        void onComplete(Result result);
    }

    /**
     * Interface to detect Progress of the AsyncTask. Exposed at the constructor
     *
     * @param <Progress> The result type returned by progres
     */
    public interface OnProgressListener<Progress> {
        void onProgressUpdated(Progress... result);
    }

    /**
     * Interface to detect error of the AsyncTask. Exposed at the constructor
     */
    public interface OnErrorListener {
        void onError(@NonNull Exception e);
    }


}
