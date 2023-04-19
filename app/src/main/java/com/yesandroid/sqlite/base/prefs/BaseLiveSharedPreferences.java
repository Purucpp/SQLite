package com.yesandroid.sqlite.base.prefs;

import android.app.Application;
import android.content.SharedPreferences;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;


public abstract class BaseLiveSharedPreferences extends BaseSharedPreferences {

    private PublishSubject<String> publisher;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;
    private Observable<String> updates;

    public BaseLiveSharedPreferences(Application application) {
        super(application);
        publisher = PublishSubject.create();
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key != null)
                    publisher.onNext(key);
            }
        };
        updates = publisher
                .doOnSubscribe(
                        disposable -> getSharedPreferences().registerOnSharedPreferenceChangeListener(listener))
                .doOnDispose(() -> {
                    if (!publisher.hasObservers())
                        getSharedPreferences().unregisterOnSharedPreferenceChangeListener(listener);

                });


    }

    public final <T> LivePreference<T> getLive(String key, T defaultVal) {
        return new LivePreference<T>(updates, getSharedPreferences(), key, defaultVal);
    }
}
