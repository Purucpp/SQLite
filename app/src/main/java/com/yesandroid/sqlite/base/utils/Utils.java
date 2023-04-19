package com.yesandroid.sqlite.base.utils;


import static android.content.ContentValues.TAG;
import static in.yesandroid.base_android.constants.Constants.DEFAULT_DATE_TIME_FORMAT;
import static in.yesandroid.base_android.constants.Constants.SAFE_THREAD_FORCE_CLEAN_THRESHOLD;
import static in.yesandroid.base_android.constants.Constants.SAFE_THREAD_MIN_MEM;
import static in.yesandroid.base_android.constants.Constants.SAFE_THREAD_SLEEP_MILL;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Debug;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import in.yesandroid.base_android.R;


public class Utils {

    @SuppressWarnings("HardCodedStringLiteral")
    public static final String YES = "yes";
    @SuppressWarnings("HardCodedStringLiteral")
    public static final String NO = "no";
    public static final String[][] YES_NO_VALUES = {{YES, NO}};


    public final static String[][] FOETUS_VALUE = {{"Single", "Twins", "Others"}};
    public final static String[][] PRESENTATION_VALUE = {{"Cephalic", "Breech", "Transverse"}};
    @SuppressWarnings("HardCodedStringLiteral")
    public static final String STATUS_UNKNOWN = "Status unknown";
    public static final String[][] YES_NO_UNKNOWN_VALUES = {{YES, NO, STATUS_UNKNOWN}};
    @SuppressWarnings("HardCodedStringLiteral")
    public static final String OTHER = "other";
    @SuppressWarnings("HardCodedStringLiteral")
    public static final String NA = "NA";
    public static final long HOUR_INTERVAL = AlarmManager.INTERVAL_HOUR;
    @SuppressWarnings("HardCodedStringLiteral")
    public static final String FLAVOR_PRO = "pro";
    @SuppressWarnings("HardCodedStringLiteral")
    public static final String FLAVOR_GOVERNMENT = "government";
    @SuppressWarnings("HardCodedStringLiteral")
    public static final String FLAVOR_STANDLONE = "standlone";
    public static final String LOCALE_EN_US = "en_US";
    public static final String KEYAR_DEVICE = "Keyar_";
    public static final String HIV_REACTIVE = "Reactive";
    public static final String HIV_NON_REACTIVE = "Non-Reactive";

    private static int gc = 0;

    public static ProgressDialog showLoadingDialog(Context context, String title, String message, boolean determinate) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setIndeterminate(!determinate);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }

    public static boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }


    public static void showTimePicker(Context context, final OnSelectedTime onSelectedTime) {


        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context, (timePicker, selectedHour, selectedMinute) -> onSelectedTime.onSelected(selectedHour + ":" + selectedMinute), hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }


    public static void showDatePicker(Context context, final OnSelectedTime onSelectedTime) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(context, (view, year1, month1, dayOfMonth) -> onSelectedTime.onSelected(year1 + "/" + month1 + "/" + dayOfMonth), year, month, day).show();

    }


    public static void showDateAndTime(final Context context, final OnSelectedTime onSelectedTime) {

        showDatePicker(context, date -> {
            final String[] data = {date};
            showTimePicker(context, query -> {
                data[0] += ":" + query;
                onSelectedTime.onSelected(data[0]);

            });

        });
    }

    public static String epochSeconds2DateTimeString(long epoch) {
        return epochSeconds2DateTimeString(epoch, DEFAULT_DATE_TIME_FORMAT);
    }



    public static String epochSeconds2DateTimeString(long epoch, String strFormat) {
        if (epoch == 0) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(strFormat);
        return format.format(new Date(epoch));
    }

    public static void freeMemory() {
        if (Debug.getNativeHeapAllocatedSize() >= 0.75 * Runtime.getRuntime().maxMemory()) {
            System.runFinalization();
            Runtime.getRuntime().gc();
            System.gc();
            Log.d(TAG, "freeMemory: cleared " + gc++);
        }
    }

    public static boolean isUnsafeSafeThread() {
        long maxMem = Runtime.getRuntime().maxMemory();
        long alocMem = Debug.getNativeHeapAllocatedSize();
        boolean unsafe = maxMem - alocMem < SAFE_THREAD_MIN_MEM;
        if (unsafe) {
            if (maxMem - alocMem < SAFE_THREAD_FORCE_CLEAN_THRESHOLD) {
                Log.d(TAG, "isUnsafeSafeThread: force clear memory");
                freeMemory();
            }
            try {
                Thread.sleep(SAFE_THREAD_SLEEP_MILL);
            } catch (InterruptedException e) {
                Log.d(TAG, "isUnsafeSafeThread: thread sleep failed");
                e.printStackTrace();
            }
        }
        return unsafe;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean isIntegerParsable(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

    public static boolean isFloatParsable(String s) {
        try {
            Float.parseFloat(s);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

    public static String capitalize(String str) {
        if (str == null) {
            return "";
        }
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }

    public static String bitmapToBase64(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static void hideKeyboard(Context ctx, View view) {
        if (ctx != null) {
            InputMethodManager inputManager = (InputMethodManager) ctx
                    .getSystemService(Context.INPUT_METHOD_SERVICE);

            if (view == null || inputManager == null)
                return;

            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static MaterialAlertDialogBuilder showChoiceDialog(Context context, String title, String message, String positiveText, Dialog.OnClickListener positiveButtonClick, String negativeText, Dialog.OnClickListener negativeButtonClick, DialogInterface.OnDismissListener onDismissListener) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context, R.style.MaterialAlertDialogOverlay);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, positiveButtonClick);
        if (onDismissListener != null) {
            builder.setOnDismissListener(onDismissListener);
        }
        if (negativeButtonClick != null)
            builder.setNegativeButton(negativeText, negativeButtonClick);
        builder.show();

        return builder;
    }

    public static MaterialAlertDialogBuilder showChoiceDialogReject(Context context, String title, String message, String positiveText, Dialog.OnClickListener positiveButtonClick, String negativeText, Dialog.OnClickListener negativeButtonClick, DialogInterface.OnDismissListener onDismissListener) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context, R.style.MaterialAlertDialogOverlay);
        builder.setTitle(title);
        builder.setMessage(message);


        builder.setPositiveButton(positiveText, positiveButtonClick);
        if (onDismissListener != null) {
            builder.setOnDismissListener(onDismissListener);
        }
        if (negativeButtonClick != null)
            builder.setNegativeButton(negativeText, negativeButtonClick);
        builder.show();

        return builder;
    }

    public static String getStringResourceByName(Context context, String aString) {
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(aString, "string", packageName);
        return context.getString(resId);
    }

    public static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    public static AlertDialog showAlert(Context activityContext, String title, String message) {
        return showAlert(activityContext, title, message, (dialog, which) -> dialog.dismiss());
    }

    public static AlertDialog showAlert(Context activityContext, String title, String message, DialogInterface.OnClickListener onClickListener) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activityContext, R.style.MaterialAlertDialogOverlay);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(activityContext.getString(R.string.ok_caps), onClickListener);
        return builder.show();
    }

    public static String matchValuesWithLabel(String[][] status_labels, String[][] status_values, String child_status) {
        for (int row = 0; row < status_values.length; row++) {
            for (int col = 0; col < status_values[row].length; col++) {
                if (child_status.equals(status_values[row][col])) {
                    return status_labels[row][col];
                }
            }
        }
        return null;
    }

    public interface OnSelectedTime {
        void onSelected(String query);
    }
}
