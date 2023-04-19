package com.yesandroid.sqlite.base.classes;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import in.yesandroid.base_android.R;
import in.yesandroid.base_android.views.SingleClickListener;

/**
 * This view is basically used to replace back and next button through the entire making it constant at every place
 * Please ensure to always call attachView
 */
public class BackNextLayout {

    private Button back, next;
    private OnBackNextListener onBackNextListener;
    private LinearLayout view;


    /**
     * Ensure that the functionality is attached
     *
     * @param viewGroup parent view or main view of the back_next_layout
     */
    void attachView(ViewGroup viewGroup) {

        if ((view = viewGroup.findViewById(R.id.back_next_layout)) == null) {
            throw new NullPointerException("View does not have back and next buttons");
        }

        back = view.findViewById(R.id.back);
        next = view.findViewById(R.id.next);

        back.setOnClickListener(new SingleClickListener() {

            @Override
            protected void proceedClick(View v) {
                if (onBackNextListener != null) {
                    onBackNextListener.onBackClick(v);
                }
            }
        });

        next.setOnClickListener(new SingleClickListener() {
            @Override
            protected void proceedClick(View v) {
                if (onBackNextListener != null) {
                    onBackNextListener.onNextClick(v);

                }
            }
        });
    }

    /**
     * Attach a listener to the layout
     *
     * @param onBackNextListener the listener that call back the data
     */
    void attachListener(OnBackNextListener onBackNextListener) {
        this.onBackNextListener = onBackNextListener;
    }


    public BackNextLayout(ViewGroup view, OnBackNextListener onBackNextListener) {
        attachView(view);
        attachListener(onBackNextListener);
    }

    public Button getBack() {
        return back;
    }

    public Button getNext() {
        return next;
    }

    public OnBackNextListener getOnBackNextListener() {
        return onBackNextListener;
    }

    public LinearLayout getView() {
        return view;
    }

    public interface OnBackNextListener {
        boolean onBackClick(View view);

        void onNextClick(View view);

    }
}
