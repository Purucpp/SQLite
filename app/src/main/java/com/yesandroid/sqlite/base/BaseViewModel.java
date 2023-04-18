package com.yesandroid.sqlite.base;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

import com.yesandroid.sqlite.rx.AppSchedulerProvider;
import com.yesandroid.sqlite.rx.SchedulerProvider;


public abstract class BaseViewModel extends AndroidViewModel implements LifecycleOwner {


    private final SchedulerProvider mSchedulerProvider;
    private final LifecycleRegistry lifecycleRegistry;

    public BaseViewModel(Application application) {
        super(application);
        mSchedulerProvider = new AppSchedulerProvider();
        lifecycleRegistry = new LifecycleRegistry(this);
        lifecycleRegistry.setCurrentState(Lifecycle.State.CREATED);
        lifecycleRegistry.setCurrentState(Lifecycle.State.STARTED);
    }


    public abstract void init(Bundle savedInstanceState);


    public void addUiThread(Runnable runnable) {
        mSchedulerProvider.ui().execute(runnable);
    }

    public void addIoThread(Runnable runnable) {
        mSchedulerProvider.io().execute(runnable);
    }

    public void addComputationThread(Runnable runnable) {

        mSchedulerProvider.computation().execute(runnable);

    }

    public void onResume(){

    }


    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return lifecycleRegistry;
    }

    /**
     * Need to remove this later.
     * Need to think of alternative to this process.
     * This could be harmful says "gut feeling".
     * Need to know why.
     *
     * @return return type can be harmful. Runtime type declaration here
     */
    public <T extends Application> T getBaseApp() {
        return ((T) getApplication());

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mSchedulerProvider.io().shutdown();
        mSchedulerProvider.computation().shutdown();
        lifecycleRegistry.setCurrentState(Lifecycle.State.DESTROYED);
    }

    public String getString(int id) {
        return getApplication().getString(id);
    }


    public void onActivityStarted() {

    }

    protected final SchedulerProvider getSchedulerProvider() {
        return mSchedulerProvider;
    }
}

