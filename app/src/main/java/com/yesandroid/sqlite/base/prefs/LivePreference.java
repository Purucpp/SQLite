package com.yesandroid.sqlite.base.prefs;


import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Credits to
 * <a href="https://android.jlelse.eu/android-observe-shared-preferences-as-livedata-27e25e7d3172#:~:text=Android%20Shared%20Preferences%20is%20best,changes%20in%20the%20preference%20values."> Live preference example</>
 */
public class LivePreference<T> extends MutableLiveData<T> {

    private Observable<String> updates;
    private SharedPreferences sharedPreferences;
    private String key;
    private T defaultValue;

    private Disposable disposable;
    private Scheduler schedulers;

    public LivePreference(Observable<String> updates, SharedPreferences sharedPreferences, String key, T defaultValue) {
        this.updates = updates;
        this.sharedPreferences = sharedPreferences;
        this.key = key;
        this.defaultValue = defaultValue;
        schedulers = Schedulers.io();
    }

    @Override
    protected void onActive() {
        super.onActive();
        disposable = updates.filter(t -> t.equals(key))
                .subscribeOn(schedulers)
                //.observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        Map<String, ?> alls = sharedPreferences.getAll();
                        for (Map.Entry<String, ?> keyinShareprefs : alls.entrySet()) {
                            if (keyinShareprefs.getKey().equals(key)) {
                                if (keyinShareprefs.getValue() == null) {
                                    postValue(defaultValue);
                                } else
                                    postValue((T) keyinShareprefs.getValue());

                            }
                        }


                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        Map<String, ?> alls = sharedPreferences.getAll();
        for (Map.Entry<String, ?> keyinShareprefs : alls.entrySet()) {
            if (keyinShareprefs.getKey().equals(key)) {

                if (keyinShareprefs.getValue() == null) {
                    postValue(defaultValue);
                } else
                    postValue((T) keyinShareprefs.getValue());


            }
        }

    }

    @Override
    protected void onInactive() {
        super.onInactive();
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
