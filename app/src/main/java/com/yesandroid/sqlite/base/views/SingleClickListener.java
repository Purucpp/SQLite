package com.yesandroid.sqlite.base.views;

import android.view.View;

public abstract class SingleClickListener implements View.OnClickListener {


    long clickTime;
    long MIN_TIME = 500;

    @Override
    public void onClick(View v) {

        if ((System.currentTimeMillis()) - (clickTime) > MIN_TIME || (System.currentTimeMillis()) - (clickTime) < 0) {
            clickTime = System.currentTimeMillis();
            proceedClick(v);
            v.setEnabled(true);

        }
    }

    protected abstract void proceedClick(View v);
}
