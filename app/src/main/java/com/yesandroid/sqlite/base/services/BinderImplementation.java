package com.yesandroid.sqlite.base.services;

public interface BinderImplementation<T extends LongRunningService> {

    T getService();

}
