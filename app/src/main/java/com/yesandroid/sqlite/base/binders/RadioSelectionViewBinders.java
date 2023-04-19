package com.yesandroid.sqlite.base.binders;


import android.widget.RadioButton;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;
import androidx.databinding.InverseBindingMethod;
import androidx.databinding.InverseBindingMethods;

import in.yesandroid.base_android.views.RadioSelection;

@InverseBindingMethods({
        @InverseBindingMethod(type = RadioSelection.class, attribute = "selected_text"),
})
public class RadioSelectionViewBinders {

    @BindingAdapter(value = "radio_textAttrChanged")
    public static void setListener(RadioSelection radioSelection, final InverseBindingListener textAttrChanged) {
        if (textAttrChanged != null) {
            radioSelection.setOnRadioSelected(new RadioSelection.OnRadioSelected() {
                @Override
                public void onRadioButtonSelected(RadioButton selectedRadioButton, String value) {
                    textAttrChanged.onChange();
                }
            });

        }
    }

    @BindingAdapter("radio_text")
    public static void bindRadioSelectionText(RadioSelection view, String text) {
        if (view != null) {
            if (!view.getSelectedButtonText().equals(text))
                view.setSelectedValue(text);
        }
    }

    @InverseBindingAdapter(attribute = "radio_text")
    public static String getRadioSelectedText(RadioSelection view) {
        if (view != null) {
            return ((RadioSelection) view).getSelectedButtonText();
        } else {
            return "";
        }
    }

}
