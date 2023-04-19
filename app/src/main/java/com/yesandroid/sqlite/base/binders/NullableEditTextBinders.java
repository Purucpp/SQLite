package com.yesandroid.sqlite.base.binders;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;
import androidx.databinding.InverseBindingMethod;
import androidx.databinding.InverseBindingMethods;

import in.yesandroid.base_android.views.NullableEditText;

@InverseBindingMethods({
        @InverseBindingMethod(type = NullableEditText.class, attribute = "selected_text"),
})
public class NullableEditTextBinders {

    @BindingAdapter(value = "selected_textAttrChanged")
    public static void setListener(NullableEditText nullableEditText, final InverseBindingListener textAttrChanged) {
        if (textAttrChanged != null) {
            nullableEditText.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    textAttrChanged.onChange();
                }
            });
        }
    }


    @BindingAdapter("selected_text")
    public static void bindNullable(NullableEditText view, String text) {
        if (view != null) {
            if (!view.getText().equals(text))
                view.setText(text);
        }
    }

    @InverseBindingAdapter(attribute = "selected_text")
    public static String getNullableText(NullableEditText view) {
        if (view != null) {
            return ((NullableEditText) view).getText();
        } else {
            return "";

        }
    }
}
