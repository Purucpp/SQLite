package com.yesandroid.sqlite.base.services;

import android.os.Binder;

public class ServiceBinder<T extends LongRunningService> extends Binder implements BinderImplementation<T> {

    private LongRunningService service;

    public ServiceBinder(LongRunningService service) {
        this.service = service;
    }

    @Override
    public T getService() {
        return (T) service;
    }
}
