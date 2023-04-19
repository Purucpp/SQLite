package com.yesandroid.sqlite.base.classes.navigators;

import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

public interface ContextAwareNavigator {

    /**
     * Show loading with title and a message
     * Auto hides the previously display laoding view
     * Uses the overloading method {@link #showLoading(String, String, boolean)} and defaults the cancelable to false
     *
     * @param title   title of loading view
     * @param message message to be display inside the loading view
     */
    void showLoading(String title, String message);

    /**
     * Show the loading with tile , message and option to choose whether is loading is cancelable on pressing back button or outside the view
     * Auto hides the previously display loading view
     *
     * @param title      title of the loading view
     * @param message    message to be displayed inside the loading view
     * @param cancelable true to enable cancellation or false other wise
     */
    void showLoading(String title, String message, boolean cancelable);

    /**
     * Supporting ints {@link #showLoading(String, String)}
     *
     * @param title   title of loading view
     * @param message message to be display inside the loading view
     */
    void showLoading( @StringRes int title,@StringRes int message);

    /**
     * Supporting ints {@link #showLoading(String, String, boolean)}
     *
     * @param title      title of the loading view
     * @param message    message to be displayed inside the loading view
     * @param cancelable true to enable cancellation or false other wise
     */
    void showLoading(@StringRes int title, @StringRes int message,  boolean cancelable);


    /**
     * Updates the progress
     *
     * @param number
     */
    void updateProgress(int number);


    /**
     * Hide the currently displayed loading view
     */
    void hideLoading();


    /**
     * Show toast message with duration short
     *
     * @param message Message in string format
     */
    void showToast(String message);

    /**
     * Show toast message with duration short
     * Uses the overloading method {@link #showToast(String)} with {@link Context#getString(int)}
     *
     * @param message Message in the resource file
     */
    void showToast( @StringRes int message);

    /**
     * Shows the standard alert message
     *
     * @param title   Title of the alert
     * @param message Message to be displayed inside the alert dialog box
     */
    void showAlertDialog(@StringRes int title, @StringRes int message);

    /**
     * /**
     * Shows the standard alert message
     *
     * @param title   Title of the alert
     * @param message Message to be displayed inside the alert dialog box
     */
    void showAlertDialog(@NonNull String title, @NonNull String message);

    /**
     * Shows the standard alert message
     *
     * @param title          Title of the alert
     * @param message        Message to be displayed inside the alert dialog box
     * @param positiveText   Position text to be displayed.
     * @param pClickListener Positive click listener of the dialog.
     */
    void showAlertDialog(@NonNull String title, @NonNull String message, @NonNull String positiveText, @Nullable DialogInterface.OnClickListener pClickListener);


    /**
     * Shows the standard alert message
     *
     * @param title          Title of the alert
     * @param message        Message to be displayed inside the alert dialog box
     * @param positiveText   Position text to be displayed.
     * @param pClickListener Positive click listener of the dialog.
     * @param negative       Negative text to be displayed.
     * @param nClickListener Negative click listener of the dialog
     */
    // TODO: 25/05/20 Convert to utils
    void showAlertDialog(@NonNull String title, @NonNull String message, @NonNull String positiveText, @Nullable DialogInterface.OnClickListener pClickListener, @NonNull String negative, @Nullable DialogInterface.OnClickListener nClickListener);


    /**
     * Updates the currently shown progress dialog
     *
     * @param message Message to be displayed
     */
    void updateProgress(String message);


    /**
     * Displays a message to the user
     *
     * @param success Passing true will dismiss the progress dialog or passing false will updateData the progress dialog message
     * @param message Message to be updated
     */
    void displayMessage(boolean success, String message);

}
