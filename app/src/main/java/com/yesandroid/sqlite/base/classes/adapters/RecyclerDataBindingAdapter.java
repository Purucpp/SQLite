package com.yesandroid.sqlite.base.classes.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class RecyclerDataBindingAdapter<Layout extends ViewDataBinding, ViewHolder extends BaseViewDataHolder<Layout, Object>,
        Object> extends RecyclerView.Adapter<ViewHolder> {


    private List<Object> data;
    private OnItemClickListener<Object> onItemClickListener;
    private List<AdapterInteraction> adapterInteractions;
    protected boolean isBinding;

    public RecyclerDataBindingAdapter(List<Object> data) {
        this.data = data;
        this.adapterInteractions = new ArrayList<>();
    }


    public RecyclerDataBindingAdapter(List<Object> data, OnItemClickListener<Object> onItemClickListener, @Nullable comparator<Object> comparator) {
        this.data = data;
        this.onItemClickListener = onItemClickListener;

        if (comparator != null) {
            sort(comparator);
        }
        this.adapterInteractions = new ArrayList<>();
    }

    public RecyclerDataBindingAdapter(List<Object> data, OnItemClickListener<Object> onItemClickListener) {
        this(data, onItemClickListener, null);
    }


    public RecyclerDataBindingAdapter(List<Object> data, @Nullable comparator<Object> comparator) {
        this(data, null, comparator);
    }


    public void sort(comparator<Object> oldp) {
        Collections.sort(data, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return oldp.compare(o1, o2);
            }
        });

        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        isBinding = true;
        holder.bindingStarted();
        holder.bindObject(data.get(position));
        isBinding = false;
    }


    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.viewAttached();

    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.viewDestroyed();
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        holder.onViewPausing();
    }

    /**
     * Attaches a click listener to the items in the recycler view .
     * Always ensure to attach it before setting the adapter to the view
     *
     * @param onItemClickListener
     */
    public void attachItemClickListener(OnItemClickListener<Object> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void dispatchMessageToAdapter(int showKeyarDialog) {

    }

    public interface OnItemClickListener<P> {
        void onItemSelected(P object, ViewDataBinding itemView);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    /**
     * Updates the data set in the existing list and calls it's respective
     * changes such as {@link #notifyDataSetChanged()} or {@link #notifyItemChanged(int)} ()}
     *
     * @param newData new data set
     */
    public void updateData(@NonNull List<Object> newData) {
        DiffUtil.DiffResult callback = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return data.size();
            }

            @Override
            public int getNewListSize() {
                return newData.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return data.get(oldItemPosition).equals(newData.get(newItemPosition));
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return data.get(oldItemPosition).hashCode() == newData.get(newItemPosition).hashCode();
            }
        });

        data.clear();
        notifyDataSetChanged();
        data.addAll(newData);
        callback.dispatchUpdatesTo(this);

    }

    /**
     * Insert a new data set at the beginning of the list
     *
     * @param newData list of new data set
     */
    public void rollUp(List<Object> newData) {
        if (newData.size() != 0) {
            data.addAll(0, newData);
            notifyDataSetChanged();
        }

    }

    /**
     * Insert a new data set
     *
     * @param newData list of new data set
     */
    public void addData(List<Object> newData) {
        if (newData.size() != 0) {
            int START = data.size();
            data.addAll(newData);
            notifyItemRangeInserted(START, newData.size());
        }

    }

    /**
     * Update an existing object.  Item will not inserted in case object doesn't exist.
     *
     * @param updatedObject the updated object
     */
    public void updateData(Object updatedObject) {
        updateData(updatedObject, data.indexOf(updatedObject));
    }

    public void updateData(Object updatedObject, int position) {
        if (position >= 0 && position < data.size()) {
            data.set(position, updatedObject);
            notifyItemChanged(position);
        }
    }

    /**
     * Insert a new object into the list
     *
     * @param newObject the new object
     */
    public void insetData(Object newObject) {
        insetData(newObject, data.size());
    }

    /**
     * Insert a new object a specific postion
     *
     * @param newObject the new object to insert
     * @param position  the position to insert to
     */
    public void insetData(Object newObject, int position) {
        data.add(position, newObject);
        notifyItemInserted(position);
    }


    /**
     * Used to inform click without by passing the itemClickListener.
     *
     * @param object
     * @param itemView
     */
    public void informClick(Object object, ViewDataBinding itemView) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemSelected(object, itemView);
        }
    }

    /**
     * Get the data memebers used in the adapter
     *
     * @return
     */
    public List<Object> getData() {
        return data;
    }

    public void removeObject(Object object) {
        int removedPosition = data.indexOf(object);
        data.remove(object);
        notifyItemRemoved(removedPosition);
    }


    public void removeObjectAtLast(int count) {

        if (count < 0) {
            return;
        }
        if (count == data.size()) {
            data.clear();
            notifyDataSetChanged();
            return;
        }
        List<Object> needToRemoveObjects = new ArrayList<>(data.subList(data.size() - count, data.size()));
        int positionStart = data.size() - needToRemoveObjects.size();
        data.removeAll(needToRemoveObjects);
        notifyItemRangeRemoved(positionStart, needToRemoveObjects.size());

    }


    /**
     * check if the holder is currently binding the data member with the view
     *
     * @return True if the currently binding else false
     */
    public boolean isBinding() {
        return isBinding;
    }

    /**
     * get the type of view holder
     *
     * @return <T>
     */
    @SuppressWarnings("unchecked")
    public Class<ViewHolder> getTypeParameterClass() {
        Type type = getClass().getGenericSuperclass();
        ParameterizedType paramType = (ParameterizedType) type;
        return (Class<ViewHolder>) paramType.getActualTypeArguments()[0];
    }

    /**
     * get the type of Data member
     *
     * @return <T>
     */
    @SuppressWarnings("unchecked")
    public Class<Object> getTypeDataClass() {
        Type type = getClass().getGenericSuperclass();
        ParameterizedType paramType = (ParameterizedType) type;
        return (Class<Object>) paramType.getActualTypeArguments()[1];
    }

    protected void informInteraction(ViewHolder inProgressPatientHolder, int message) {
        for (AdapterInteraction adapterInteraction : adapterInteractions) {
            adapterInteraction.onMessageReceviced(inProgressPatientHolder, message);
        }
    }

    public RecyclerDataBindingAdapter<Layout, ViewHolder, Object> attachInteraction(AdapterInteraction adapterInteraction) {
        adapterInteractions.add(adapterInteraction);
        return this;

    }

    public void detachInteraction(AdapterInteraction adapterInteraction) {
        adapterInteractions.remove(adapterInteraction);

    }


    public interface comparator<P> {
        int compare(P oldone, P newone);
    }


    public interface AdapterInteraction<ViewHolder> {

        void onMessageReceviced(ViewHolder viewHolder, int message);

    }
}
