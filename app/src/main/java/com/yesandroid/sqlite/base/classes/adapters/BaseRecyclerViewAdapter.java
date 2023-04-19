package com.yesandroid.sqlite.base.classes.adapters;


import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A Base recycler view class adapter that has some functionaries of a adapter class such as adding new data, modifying the existing data
 *
 * @param <T> The view class holder of adapter used by super class
 * @param <P> The object type of data members used inside the super class
 */
@Deprecated
public abstract class BaseRecyclerViewAdapter<T extends BaseViewHolder, P> extends RecyclerView.Adapter<T> {

    private boolean shouldsort;

    private List<P> data;
    // TODO: 19/06/20 Remove this
    private List<T> holders;
    private OnItemClickListener<P> onItemClickListener;

    protected boolean isBinding;

    public BaseRecyclerViewAdapter(List<P> data, @Nullable comparator<P> comparator) {
        this.data = data;
        holders = new ArrayList<>();
        if (comparator != null) {
            sort(comparator);
        }
    }

    public BaseRecyclerViewAdapter(List<P> data) {
        this.data = data;
        holders = new ArrayList<>();
    }

    //
    public BaseRecyclerViewAdapter(List<P> data, OnItemClickListener<P> onItemClickListener, @Nullable comparator<P> comparator) {
        this(data, null);
        this.onItemClickListener = onItemClickListener;
    }


    public void sort(comparator<P> oldp) {
        Collections.sort(data, new Comparator<P>() {
            @Override
            public int compare(P o1, P o2) {
                return oldp.compare(o1, o2);
            }
        });

        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull T holder, int position) {
        if ((holders.size()) <= position) {
            holders.add(holder);
        } else {
            holders.set(position, holder);
        }

        isBinding = true;
        holder.bindObject(data.get(position));
        isBinding = false;
    }


    /**
     * Attaches a click listener to the items in the recylcer view . Always ensure to attach it before setting the adapter to the view
     *
     * @param onItemClickListener
     */
    public void attachItemClickListerner(OnItemClickListener<P> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener<P> {
        void onItemSelected(P object, View itemView);
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
     * Updates the data set in the existing list and calls it's respective changes such as notifyitemchanged or notifyDataSetChanges
     *
     * @param newData the new array to updateData to
     */
    public void updateData(@NonNull List<P> newData) {
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
                return data.get(oldItemPosition).equals(newData.size());
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return data.get(oldItemPosition).hashCode() == data.get(newItemPosition).hashCode();
            }
        });

        callback.dispatchUpdatesTo(this);

    }

    /**
     * Insert a new data set
     *
     * @param newData list of new data set
     */
    public void addData(List<P> newData) {
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
    public void updateData(P updatedObject) {
        updateData(updatedObject, data.indexOf(updatedObject));
    }

    public void updateData(P updatedObject, int position) {
        if (position > 0 && position < data.size()) {
            data.set(position, updatedObject);
            notifyItemChanged(position);
        }
    }

    /**
     * Insert a new object into the list
     *
     * @param newObject the new object
     */
    public void insetData(P newObject) {
        insetData(newObject, data.size());
    }

    /**
     * Insert a new object a specific postion
     *
     * @param newObject the new object to insert
     * @param position  the position to insert to
     */
    public void insetData(P newObject, int position) {
        data.add(position, newObject);
        notifyItemInserted(position);
    }

    /**
     * Get the holder at a specific position
     *
     * @param position the position of the holder
     * @return View holder typed casted to {T}.
     */
    public T getHolderAtPosition(int position) {
        if (position >= holders.size() || position < 0)
            return null;
        return holders.get(position);
    }

    /**
     * Used to inform click without by passing the itemClickListener.
     *
     * @param object
     * @param itemView
     */
    public void informClick(P object, View itemView) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemSelected(object, itemView);
        }
    }

    /**
     * Get the data memebers used in the adapter
     *
     * @return
     */
    public List<P> getData() {
        return data;
    }

    public void removeObject(P object) {
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
        List<P> needToRemoveObjects = new ArrayList<>(data.subList(data.size() - count, data.size()));
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
    public Class<T> getTypeParameterClass() {
        Type type = getClass().getGenericSuperclass();
        ParameterizedType paramType = (ParameterizedType) type;
        return (Class<T>) paramType.getActualTypeArguments()[0];
    }

    /**
     * get the type of Data member
     *
     * @return <T>
     */
    @SuppressWarnings("unchecked")
    public Class<P> getTypeDataClass() {
        Type type = getClass().getGenericSuperclass();
        ParameterizedType paramType = (ParameterizedType) type;
        return (Class<P>) paramType.getActualTypeArguments()[1];
    }


    @Deprecated
    public interface comparator<P> {
        int compare(P oldone, P newone);

    }

}
