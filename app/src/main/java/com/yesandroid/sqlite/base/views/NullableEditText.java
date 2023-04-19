package com.yesandroid.sqlite.base.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import in.yesandroid.base_android.R;


public class NullableEditText extends LinearLayout {

    private TextView editText;

    private TextView labelTextView;
    private ToggleButton naButton;
    private boolean singleLine;
    private float buttonWeight;
    private static String NA = "NA";

    @DrawableRes
    private int background_button;
    private String buttonText;

    private String labelText;
    private String editTextData;
    private float textSize;
    private float editTextSize;
    private boolean isTextMode;
    private boolean highlightRequired;


    int textColor;

    @ColorRes
    int highlightColor;


    public NullableEditText(Context context) {
        this(context, null);
    }

    public NullableEditText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NullableEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceType")
    public NullableEditText(final Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);

    }

    private void init(AttributeSet attrs) {
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        setOrientation(HORIZONTAL);
        labelTextView = new TextView(getContext());


        buttonWeight = 0.2f;

        if (isInEditMode()) {
            labelText = "Labeled data";
            buttonText = NA;
            isTextMode = false;
            singleLine = true;
            highlightRequired = false;
        }
        try {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.NullableEditText);
            labelText = typedArray.getString(R.styleable.NullableEditText_label_text);
            buttonText = typedArray.getString(R.styleable.NullableEditText_button_text);
            isTextMode = typedArray.getBoolean(R.styleable.NullableEditText_text_mode, false);
            singleLine = typedArray.getBoolean(R.styleable.NullableEditText_singleLine, true);
            highlightRequired = typedArray.getBoolean(R.styleable.NullableEditText_highlight, false);
            typedArray.recycle();

        } catch (Exception e) {

        }


        naButton = new ToggleButton(getContext(), attrs);


        if (buttonText == null || buttonText.trim().equals("")) {
            buttonText = NA;
        }

        if (isTextMode) {
            editText = new AppCompatTextView(getContext(), attrs);
        } else {
            editText = new AppCompatEditText(getContext(), attrs);
        }
        editText.setId(generateViewId());
        naButton.setId(generateViewId());
        naButton.setTextOff(getButtonText());
        naButton.setTextOn(getButtonText());

        editText.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.border_rounded_left));


        naButton.setBackground(getResources().getDrawable(R.drawable.background_nullable_toggle));
        naButton.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, buttonWeight));

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(VERTICAL);
        layout.setFocusable(true);
        layout.setFocusableInTouchMode(true);
        layout.setDescendantFocusability(FOCUS_BEFORE_DESCENDANTS);
        layout.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1 - buttonWeight));

        LayoutParams editTextLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        editText.setLayoutParams(editTextLayoutParams);
        editText.setSingleLine(singleLine);
        editText.setInputType(editText.getInputType() | EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        if (labelText != null && !labelText.equals("")) {
            layout.addView(labelTextView);
        }
        layout.addView(editText);
        editText.setTextColor(getResources().getColor(android.R.color.black));
        labelTextView.setTextColor(getResources().getColor(android.R.color.black));

        naButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                hideKeyboard(getContext(), compoundButton);
                if (isChecked) {
                    naButton.setTextColor(getResources().getColor(android.R.color.white));
                    disableEditText();
                } else {
                    naButton.setTextColor(getResources().getColor(android.R.color.black));
                    enableEditText();
                }
            }
        });
        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (naButton.isChecked()) {
                        editTextData = "";
                        naButton.toggle();
                    }
                }
            }
        });


        labelTextView.setText(getLabelText());
        naButton.setText(getButtonText());

        labelTextView.setPadding(5, 0, 0, 0);
        editText.setPadding((int) convertToPx(12), 0, (int) convertToPx(12), 0);

        addView(layout);
        addView(naButton);


        textColor = labelTextView.getCurrentTextColor();

    }

    private float convertToPx(float dp) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }

    void enableEditText() {
        getEditText().setText(editTextData);
        editText.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.border_rounded_left));
    }

    public void disableEditText() {
        editTextData = getEditText().getText().toString();
        getEditText().setText("");
        getEditText().setBackgroundResource(R.drawable.border_grey_rounded_left);
        // highlight(false);
        getEditText().clearFocus();

    }


    public boolean validate() {
        return true;

    }


    void addTextChangedListener(TextWatcher editextListener) {
        getEditText().addTextChangedListener(editextListener);
    }

    void removeTextChangedListener(TextWatcher editextListener) {
        getEditText().removeTextChangedListener(editextListener);
    }

    public String getText() {
        if (naButton.isChecked()) {
            return NA;
        }
        return getEditText().getText().toString();
    }

    public void setText(String text) {
        if (text == null)
            return;
        if (text.equalsIgnoreCase(NA)) {
            if (!naButton.isChecked()) {
                naButton.toggle();
                return;
            }
        } else {
            if (naButton.isChecked()) {
                naButton.toggle();
            }
        }
        getEditText().setText(text);
    }

    public String getLabelText() {
        return labelText;
    }

    public void setLabelText(String labelText) {
        this.labelText = labelText;
    }

    public String getButtonText() {
        return buttonText;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public TextView getEditText() {
        return editText;
    }

    public TextView getLabelTextView() {
        return labelTextView;
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
