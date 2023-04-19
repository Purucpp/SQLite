package com.yesandroid.sqlite.base.workers;

import androidx.work.Data;

public interface WorkMessageDispatcher {

    void onDataReceived(Data data);
}
