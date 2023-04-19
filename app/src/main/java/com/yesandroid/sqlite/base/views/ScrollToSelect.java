package com.yesandroid.sqlite.base.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.InverseBindingListener;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.yesandroid.base_android.R;
import in.yesandroid.base_android.classes.adapters.BaseViewDataHolder;
import in.yesandroid.base_android.classes.adapters.RecyclerDataBindingAdapter;
import in.yesandroid.base_android.databinding.ItemScrollDataBinding;
import in.yesandroid.base_android.utils.PageSnapListener;


public class ScrollToSelect extends FrameLayout {
    ArrayList<String> data;
    ArrayList<String> label;

    private RecyclerView recyclerView;
    private ScrollAdapter adapter;
    private String selectedtext;

    private static final String NA = "NA";

    private boolean defaultScroll;
    private List<InverseBindingListener> inverseBindingListeners;

    public ScrollToSelect(@NonNull Context context) {
        this(context, null);
    }

    public ScrollToSelect(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ScrollToSelect(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, -1);
    }

    public ScrollToSelect(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    void init() {

        data = new ArrayList<>();
        inverseBindingListeners = new ArrayList<>();
        label = new ArrayList<>();
        LayoutParams frame;
        setLayoutParams(frame = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        frame.gravity = Gravity.CENTER;
        recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        addView(recyclerView);
        ImageView imageView = new ImageView(getContext());
        imageView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER));

        imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.square_wndow));
        addView(imageView);


        recyclerView.setClipToPadding(false);

        recyclerView.addOnScrollListener(new PageSnapListener(snapHelper, position -> {
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    if (!defaultScroll) {
                        selectedtext = data.get(position);

                        resetView(false);
                        informChange();
                    }

                }
            });

        }));


        recyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                recyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                if (recyclerView.getPaddingStart() == 0) {
                    Log.d("ScrollToSelect", "Predraw called");
                    recyclerView.setPadding(getMeasuredWidth() / 2, 0, getMeasuredWidth() / 2, 0);
                    if (selectedtext != null && !selectedtext.trim().equals("")) {
                        int selectedTextPosition = findPositionForvalue(selectedtext);
                        if (selectedTextPosition != -1) {
                            defaultScroll = true;
                            resetView(true);

                        }
                    } else {
                        recyclerView.smoothScrollToPosition(0);
                    }
                }
                return true;
            }
        });


    }


    public void setDefaultValue(String value) {
        try {
            double cvalue = Double.parseDouble(value);
            if (cvalue % 1 == 0) {
                selectedtext = ((int) cvalue) + "";
            } else {
                selectedtext = value;
            }
        } catch (Exception e) {
            selectedtext = value;
        }
        resetView(true);
        informChange();

    }

    private int findPositionForvalue(String value) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) == null) {
                throw new RuntimeException("Data null for " + data.size());
            }
            if (data.get(i).equalsIgnoreCase(value)) {
                if (i == 0) {
                    return i;
                }
                return i;

            }
        }
        return -1;

    }


    public void setRange(long min, long max, float increament) {
        List<String> data = new ArrayList<>();
        double startValue = min;
        while (startValue <= max) {
            if (startValue % 1 == 0) {
                data.add(Integer.toString((int) startValue));
            } else {
                data.add(Double.toString(startValue));
            }


            startValue = startValue + increament;
            startValue = Math.round(startValue * 10.0) / 10.0;


        }
        setData(data.toArray(new String[data.size()]));
    }

    public void setRange(long min, long max, float increament, double naPosition) {
        List<String> data = new ArrayList<>();
        double startValue = min;
        while (startValue <= max) {
            if (startValue == naPosition) {
                selectedtext = NA;
                data.add(NA);
            } else {
                if (startValue % 1 == 0) {
                    data.add(Integer.toString((int) startValue));
                } else {
                    data.add(Double.toString(startValue));
                }

            }

            startValue = startValue + increament;
            startValue = Math.round(startValue * 10.0) / 10.0;
        }
        setData(data.toArray(new String[data.size()]));
    }

    public void setData(String[] data) {
        recyclerView.setHasFixedSize(false);
        setData(data, data);
        recyclerView.setHasFixedSize(true);

    }

    public void setData(String[] data, String[] label) {
        this.data.clear();
        this.label.clear();
        this.data.addAll(Arrays.asList(data));
        this.label.addAll(Arrays.asList(label));

        if (adapter != null) {
            adapter.getData().clear();
            adapter.getData().addAll(Arrays.asList(label));
            adapter.notifyDataSetChanged();
        } else {
            recyclerView.setAdapter(adapter = new ScrollAdapter(new ArrayList<>(Arrays.asList(label)), new RecyclerDataBindingAdapter.OnItemClickListener<String>() {
                @Override
                public void onItemSelected(String object, ViewDataBinding itemView) {
                    selectedtext = object;
                    resetView(true);
                    informChange();
                }
            }));

        }
    }

    public String getSelectedText() {
        return selectedtext == null ? "" : selectedtext;
    }

    public void setSelectedText(String value) {
        Log.d("Scroller", "Called");
        try {
            double cvalue = Double.parseDouble(value);
            if (cvalue % 1 == 0) {
                selectedtext = ((int) cvalue) + "";
            } else {
                selectedtext = value;
            }
        } catch (Exception e) {
            selectedtext = value;
        }
        resetView(true);

    }

    protected void resetView(boolean updateScroll) {
        int selectedTextPosition = findPositionForvalue(selectedtext);
        if (selectedTextPosition != -1) {
            if (updateScroll) {
                if (!defaultScroll) {
                    recyclerView.smoothScrollToPosition(selectedTextPosition);
                } else {
                    defaultScroll = false;
                    recyclerView.scrollToPosition(selectedTextPosition);
                }
            }

            ((ScrollAdapter) recyclerView.getAdapter()).setSelected(selectedTextPosition);
        }
    }

    public void informChange() {
        for (InverseBindingListener inverseBindingListener : inverseBindingListeners) {
            inverseBindingListener.onChange();
        }
    }

    public void addInverserListener(InverseBindingListener inverseBindingListener) {
        if (!inverseBindingListeners.contains(inverseBindingListener))
            inverseBindingListeners.add(inverseBindingListener);
    }


    class ScrollAdapter extends RecyclerDataBindingAdapter<ItemScrollDataBinding, ScrollAdapter.StringHolder, String> {


        int selectedPosition;

        public ScrollAdapter(List<String> data) {
            super(data);
        }

        public ScrollAdapter(List<String> data, OnItemClickListener<String> onItemClickListener) {
            super(data, onItemClickListener);
        }

        @NonNull
        @Override
        public StringHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new StringHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_scroll_data, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull StringHolder holder, int position) {
            super.onBindViewHolder(holder, position);

        }

        public int getSelectedPosition() {
            return selectedPosition;
        }


        public void setSelected(int selectedPosition) {
            int previousPos = this.selectedPosition;
            this.selectedPosition = selectedPosition;
            notifyItemChanged(previousPos);
            notifyItemChanged(selectedPosition);

        }


        class StringHolder extends BaseViewDataHolder<ItemScrollDataBinding, String> {


            public StringHolder(View itemView) {
                super(itemView);
            }

            @Override
            protected void bindObject(String data) {

                getItemBinding().scrollValue.setText(data);
                itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        informClick(data, getItemBinding());
                    }
                });

                if (getAdapterPosition() == selectedPosition)
                    color();
                else
                    discolor();
            }


            public void color() {
                getItemBinding().scrollValue.setBackgroundColor(getResources().getColor(R.color.blue));
                getItemBinding().scrollValue.setTextColor(getResources().getColor(R.color.white));
            }

            public void discolor() {
                getItemBinding().scrollValue.setBackground(getResources().getDrawable(R.drawable.round_button));
                getItemBinding().scrollValue.setTextColor(getResources().getColor(android.R.color.black));

            }
        }
    }


}
