package com.yesandroid.sqlite.base.utils;


import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

public class PageSnapListener extends RecyclerView.OnScrollListener {
    private int snapPosition = RecyclerView.NO_POSITION;

    private SnapHelper snapHelper;
    OnSnapPositionChangeListener onSnapPositionChangeListener;
    Behavior behavior;

    enum Behavior {
        NOTIFY_ON_SCROLL,
        NOTIFY_ON_SCROLL_STATE_IDLE
    }

    public PageSnapListener(SnapHelper snapHelper, OnSnapPositionChangeListener onSnapPositionChangeListener) {
        this.snapHelper = snapHelper;
        this.onSnapPositionChangeListener = onSnapPositionChangeListener;
        this.behavior = Behavior.NOTIFY_ON_SCROLL;
    }


    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (behavior == Behavior.NOTIFY_ON_SCROLL_STATE_IDLE
                && newState == RecyclerView.SCROLL_STATE_IDLE) {
            maybeNotifySnapPositionChange(recyclerView);
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (behavior == Behavior.NOTIFY_ON_SCROLL) {
            maybeNotifySnapPositionChange(recyclerView);
        }
    }

    private void maybeNotifySnapPositionChange(RecyclerView recyclerView) {
        View centerView = snapHelper.findSnapView(recyclerView.getLayoutManager());
        int snapPosition = recyclerView.getLayoutManager().getPosition(centerView);
        boolean snapPositionChanged = this.snapPosition != snapPosition;
        if (snapPositionChanged) {
            onSnapPositionChangeListener.onSnapPositionChange(snapPosition);
            this.snapPosition = snapPosition;
        }
    }


    public interface OnSnapPositionChangeListener {

        void onSnapPositionChange(int position);
    }
}
