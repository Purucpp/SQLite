package com.yesandroid.sqlite.base.binders;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import java.util.regex.Pattern;

import in.yesandroid.base_android.views.ScrollToSelect;

public class ViewBinders {



    @BindingAdapter("selected_text")
    public static void bindScrollToSelect(ScrollToSelect view, String text) {
        if (view != null) {
            if (!view.getSelectedText().equals(text))
                ((ScrollToSelect) view).setSelectedText(text);
        }
    }

    @InverseBindingAdapter(attribute = "selected_text")
    public static String getScrolledText(ScrollToSelect view) {
        if (view != null) {
            return ((ScrollToSelect) view).getSelectedText();
        } else {
            return "";
        }
    }



    @BindingAdapter("box_checked")
    public static void bindCheckBox(CheckBox view, String text) {
        if (view.getTag().equals(text)) {
            view.setChecked(true);
        } else {
            view.setChecked(false);
        }
    }





    @BindingAdapter("selected_textAttrChanged")
    public static void setListeners(ScrollToSelect view,
                                    InverseBindingListener inverseBindingListener) {
        // Set a listener for click, focus, touch, etc.
        Log.e("Scroller", "onchange called");

        view.addInverserListener(inverseBindingListener);
    }




    public static void onCheckChanged(View checkbox, String appendingText) {
        CheckBox view = null;
        if (checkbox instanceof CheckBox) {

            view = ((CheckBox) checkbox);

        }

        if (view == null) {
            return;
        }
        String textToAppend = view.getText().toString();
        StringBuilder builder = new StringBuilder(appendingText);
        if (view.isChecked()) {
            // Append to text
            if (appendingText.equals("")) {
                builder.append(textToAppend);
            } else {
                builder.append(",").append(textToAppend);
            }
        } else {
            if (appendingText.contains(textToAppend)) {
                if (appendingText.contains(Pattern.quote(","))) {
                    if (appendingText.startsWith(textToAppend)) {
                        builder.replace(appendingText.indexOf(textToAppend), textToAppend.length() + 1, "");
                    } else {
                        builder.replace(appendingText.indexOf(textToAppend) - 1, textToAppend.length() + 1, "");
                    }

                } else {
                    builder.replace(appendingText.indexOf(textToAppend), textToAppend.length(), "");
                }

            }
        }
    }


}
