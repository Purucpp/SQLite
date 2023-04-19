package com.yesandroid.sqlite.base.classes.adapters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseViewDataHolder<K extends ViewDataBinding, P> extends RecyclerView.ViewHolder implements LifecycleOwner {


    private Context context;
    private K itemBinding;
    private LifecycleRegistry lifecycle;
    private P dataObject;

    public BaseViewDataHolder(View itemView) {
        super(itemView);
        this.context = itemView.getContext();

        itemBinding = DataBindingUtil.bind(itemView);
        lifecycle = new LifecycleRegistry(this);
        lifecycle.setCurrentState(Lifecycle.State.INITIALIZED);
        lifecycle.setCurrentState(Lifecycle.State.CREATED);


    }

    public void bindingStarted() {
        if (lifecycle != null)
            lifecycle.setCurrentState(Lifecycle.State.STARTED);
    }

    /**
     * Bind method to connect view and data member
     *
     * @param object
     */
    protected void bindObject(P object) {
        this.dataObject = object;
    }

    /**
     * Returns the context of the binding view  of item
     *
     * @return {@link Context}
     */

    protected Context getContext() {
        return context;
    }

    protected String getString(int id) {
        return context.getString(id);
    }

    protected String getString(int id, Object... args) {
        return context.getString(id, args);
    }


    public K getItemBinding() {
        return itemBinding;
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return lifecycle;
    }


    protected void viewDestroyed() {
        if (lifecycle != null)
            lifecycle.setCurrentState(Lifecycle.State.DESTROYED);
    }

    public P getDataObject() {
        return dataObject;
    }

    public void onViewPausing() {

    }

    public void viewAttached() {

    }

    public void dispatchMessageToHolder(int showKeyarDialog) {

    }
}
