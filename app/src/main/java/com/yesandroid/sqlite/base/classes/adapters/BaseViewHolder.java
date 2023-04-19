package com.yesandroid.sqlite.base.classes.adapters;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Base class of view holder  for BaseRecyclerViewAdapter
 * @param <P>
 */
@Deprecated
public abstract class BaseViewHolder<P> extends RecyclerView.ViewHolder {

    Context context;

    public BaseViewHolder(View itemView) {
        super(itemView);
        this.context = itemView.getContext();
    }

    /**
     * Bind method to connect view and data member
     *
     * @param object
     */
    protected abstract void bindObject(P object);

    /**
     * Returns the context of the binding view  of item
     *
     * @return {@link Context}
     *
     */

    protected Context getContext() {
        return context;
    }

    protected String getString(int id) {
        return context.getString(id);
    }


}
