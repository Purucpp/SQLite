package com.yesandroid.sqlite.base.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.appcompat.widget.AppCompatRadioButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.yesandroid.base_android.R;


public class RadioSelection extends FrameLayout implements View.OnClickListener {


    private static final String ADDITION_TEXT = "+";
    private String[][] values;
    private String[][] labels;

    private RadioButton selectedRadioButton;
    private Drawable boxDrawable;
    private Drawable background;
    private int leftMargin;
    private int rightMargin;
    private int topMargin;
    private float textSize;
    private int bottomMargin;
    private boolean adjustWidth;
    private List<OnRadioSelected> radioSelectors;
    private Typeface textStyle;
    private String selectText;
    private boolean hasDropDown;
    private Spinner dropDown;
    private TableLayout tableLayout;
    private RadioButton additionButton;
    private List<String> disabledText;


    public RadioSelection(Context context) {
        this(context, null);
    }


    public RadioSelection(Context context, AttributeSet attrs) {
        super(context, attrs);
        leftMargin = 8;
        rightMargin = 4;
        topMargin = 8;
        bottomMargin = 4;
        adjustWidth = true;
        textSize = getContext().getResources().getDimension(R.dimen.small_text);
        hasDropDown = false;
        selectText = "";
        radioSelectors = new ArrayList<>();
        disabledText = new ArrayList<>();


        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.RadioSelection);


        selectText = a.getString(R.styleable.RadioSelection_radio_text);
        if (selectText == null) {
            selectText = "";
        }

        a.recycle();
        if (isInEditMode()) {
            setData(new String[][]{{"Option", "Option"}});
        }
    }

    private void reset() {
        removeAllViews();
        tableLayout = new TableLayout(getContext());
        tableLayout.setStretchAllColumns(true);

        if (labels != null) {
            for (int i = 0; i < labels.length; i++) {
                TableRow tableRow = new TableRow(getContext());
                String[] innerData = labels[i];
                tableRow.setWeightSum(innerData.length);
                for (int j = 0; j < innerData.length; j++) {

                    RadioButton radioButton = getCellWithText(innerData[j]);
                    radioButton.setFocusable(true);

                    if (background != null)
                        radioButton.setBackground(background);
                    if (selectText != null && selectText.equalsIgnoreCase(values[i][j])) {
                        selectedRadioButton = radioButton;
                    }
                    if (disabledText.contains(innerData[j])) {
                        disable(radioButton);
                    } else {
                        enable(radioButton);
                    }
                    tableRow.addView(radioButton);


                }
                if (hasDropDown) {

                    tableRow.addView(additionButton = getCellWithText(ADDITION_TEXT));
                    tableLayout.addView(tableRow);
                    setChildrenOnClickListener(tableRow);
                    break;
                }
                setChildrenOnClickListener(tableRow);
                tableLayout.addView(tableRow);

            }
        }
        addView(tableLayout);


        if (!selectText.equals("") && selectedRadioButton == null) {


            // check if the select text is within other position of the values;
            int postion = Arrays.asList(convertData(values)).indexOf(selectText);

            if (postion != -1) {
                // The selected text is not part of the visible table row .
                // To remove the hassle of programattically handling views we used perform click so that on click will be called.
                // This is will ensure that there is one point of code to be changed in case code change.
                additionButton.performClick();
                dropDown.setSelection(postion);
            }


        }

        if (selectedRadioButton != null) {
            selectedRadioButton.performClick();
        }
    }


    private RadioButton getCellWithText(String text) {
        RadioButton radioButton = new AppCompatRadioButton(getContext());

        TableRow.LayoutParams layoutparams;
        if (adjustWidth)
            layoutparams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1);
        else
            layoutparams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
        layoutparams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
        radioButton.setLayoutParams(layoutparams);
        radioButton.setTypeface(getTextStyle());
        radioButton.setBackground(getResources().getDrawable(R.drawable.background_border_rounded_white));
        radioButton.setText(text.toUpperCase());
        radioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize());
        radioButton.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        radioButton.setButtonDrawable(boxDrawable);
        radioButton.setElevation(5);
        radioButton.setPadding(0, 12, 0, 12);
        return radioButton;
    }

    private void addSpinner() {
        dropDown = new Spinner(getContext());
        dropDown.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, convertData(labels)));
        dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                for (OnRadioSelected radioSelected : radioSelectors) {
                    radioSelected.onRadioButtonSelected(null, convertData(values)[position]);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        addView(dropDown);
        dropDown.setVisibility(GONE);

    }

    String[] convertData(String[][] data) {
        List<String> dataList = new ArrayList<>();
        for (String[] stringArray : data) {
            for (String innerValue : stringArray) {
                dataList.add(innerValue);
            }
        }
        return dataList.toArray(new String[dataList.size()]);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof RadioButton) {
            hideKeyboard(getContext(), v);
            final RadioButton rb = (RadioButton) v;

            if (rb.getText().equals(ADDITION_TEXT) && hasDropDown) {
                addSpinner();
                dropDown.setVisibility(VISIBLE);
                tableLayout.setVisibility(GONE);
                return;
            }
            if (selectedRadioButton != null) {
                deselect(selectedRadioButton);
            }
            select(rb);

        }
    }

    private void deselect(RadioButton radioButton) {
        radioButton.setChecked(false);
        radioButton.setTextColor(getResources().getColor(android.R.color.black));
        radioButton.setBackground(getResources().getDrawable(R.drawable.background_border_rounded_white));
        radioButton.setTypeface(Typeface.DEFAULT);
    }

    private void select(RadioButton radioButton) {
        selectedRadioButton = radioButton;
        if (disabledText.contains(radioButton.getText().toString())) {
            radioButton.setEnabled(false);
        }
        radioButton.setTypeface(Typeface.DEFAULT_BOLD);
        radioButton.setBackground(getResources().getDrawable(R.drawable.background_rounded_color));
        radioButton.setTextColor(getResources().getColor(android.R.color.white));
        radioButton.setChecked(true);
        selectedRadioButton.requestFocus();
        for (OnRadioSelected radioSelected : radioSelectors) {
            radioSelected.onRadioButtonSelected(selectedRadioButton, find(selectedRadioButton.getText().toString()));
        }

    }

    private void disable(RadioButton radioButton) {

        radioButton.setEnabled(false);
        radioButton.setBackgroundResource(R.drawable.border_grey_rounded);
        radioButton.setTextColor(getResources().getColor(android.R.color.white));
        radioButton.setTypeface(Typeface.DEFAULT);
    }

    private void enable(RadioButton radioButton) {
        radioButton.setEnabled(true);
        radioButton.setTextColor(getResources().getColor(android.R.color.black));
        radioButton.setBackground(getResources().getDrawable(R.drawable.background_border_rounded_white));
        radioButton.setTypeface(Typeface.DEFAULT);
    }

    String find(String text) {
        for (int i = 0; i < labels.length; i++) {
            for (int j = 0; j < labels[i].length; j++) {
                if (labels[i][j].equalsIgnoreCase(text))
                    return values[i][j];
            }
        }
        return "";
    }

    public int[] findIndexForValue(String value) {
        int[] indexedData = new int[2];
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                if (values[i][j].equals(value)) {
                    indexedData[0] = i;
                    indexedData[1] = j;
                    return indexedData;

                }
            }
        }
        indexedData[0] = -1;
        indexedData[1] = -1;


        return indexedData;
    }

    public void setHasDropDown(boolean hasDropDown) {
        this.hasDropDown = hasDropDown;
    }


    private void setChildrenOnClickListener(TableRow tr) {
        final int c = tr.getChildCount();
        for (int i = 0; i < c; i++) {
            final View v = tr.getChildAt(i);
            if (v instanceof RadioButton) {
                v.setOnClickListener(this);
            }
        }
    }

    public void setDisabled(List<String> disabledText) {
        this.disabledText.clear();
        this.disabledText.addAll(disabledText);
        reset();
    }

    public String getSelectedButtonText() {
        String text = "";

        if (dropDown != null && dropDown.getVisibility() == VISIBLE) {
            text = ((String) dropDown.getItemAtPosition(dropDown.getSelectedItemPosition()));
        } else if (selectedRadioButton != null) {
            text = selectedRadioButton.getText().toString();
        }

        if (!text.equals("")) {
            for (int i = 0; i < labels.length; i++) {
                String[] innerData = labels[i];
                for (int j = 0; j < innerData.length; j++) {
                    if (innerData[j].equalsIgnoreCase(text)) {
                        return values[i][j];
                    }
                }
            }
        }

        return "";
    }

    public String[][] getValues() {
        return values;
    }

    public String[][] getLabels() {
        return labels;
    }

    public void setData(String[][] data) {
        setData(data, data);
    }

    public void setData(String[][] labels, String[][] values) {
        this.labels = labels;
        this.values = values;
        selectText = "";
        if (selectedRadioButton != null) {
            deselect(selectedRadioButton);
            this.selectedRadioButton = null;
        }

        reset();
        requestLayout();
        invalidate();
    }

    public void setMargin(int margin) {
        setMargin(margin, margin, margin, margin);

    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public void setMargin(int leftMargin, int rightMargin, int topMargin, int bottomMargin) {
        this.leftMargin = leftMargin;
        this.rightMargin = rightMargin;
        this.topMargin = topMargin;
        this.bottomMargin = bottomMargin;
    }

    public void setSelectedValue(String selectedValue) {
        if (selectedValue == null) {
            return;
        }
        if (selectedValue.trim().equals("") && this.selectedRadioButton != null) {
            deselect(this.selectedRadioButton);
            this.selectedRadioButton = null;
        }
        this.selectText = selectedValue;
        if (values == null) {
            return;
        }
        reset();
        requestLayout();
        invalidate();
    }

    public Drawable getBoxDrawable() {
        return boxDrawable;
    }

    public void setBoxDrawable(Drawable boxDrawable) {
        this.boxDrawable = boxDrawable;
    }


    public boolean isAdjustWidth() {
        return adjustWidth;
    }

    public void setAdjustWidth(boolean adjustWidth) {
        this.adjustWidth = adjustWidth;
        removeAllViews();
        reset();
        requestLayout();
        invalidate();
    }

    @Override

    public Drawable getBackground() {
        return background;
    }

    @Override
    public void setBackground(Drawable background) {
        this.background = background;
    }

    public Typeface getTextStyle() {
        return textStyle;
    }

    public void setTextStyle(Typeface textStyle) {
        this.textStyle = textStyle;
    }

    public void setOnRadioSelected(OnRadioSelected onRadioSelected) {
        if (onRadioSelected == null) {
            return;
        }
        this.radioSelectors.add(onRadioSelected);
    }


    public interface OnRadioSelected {

        void onRadioButtonSelected(RadioButton selectedRadioButton, String value);
    }

    public void hideKeyboard(Context ctx, View view) {
        if (ctx != null) {
            InputMethodManager inputManager = (InputMethodManager) ctx
                    .getSystemService(Context.INPUT_METHOD_SERVICE);

            if (view == null || inputManager == null)
                return;

            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
