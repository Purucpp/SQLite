package com.yesandroid.sqlite.base.utils;

import static android.Manifest.permission.SYSTEM_ALERT_WINDOW;
import static android.Manifest.permission.WRITE_SETTINGS;
import static android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION;
import static android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionUtil {
    public static final int PERMISSION_CODE = 770;

    private PermissionUtil() {
        throw new AssertionError();
    }

    public static <T extends AppCompatActivity> boolean checkIfLocationPermissionIsGiven(
            final T activityRef) {
        return ContextCompat.checkSelfPermission(activityRef, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean checkIfReadPhoneStatePermissionIsGiven(final Context context) {

        return ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_NUMBERS) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;

    }

    public static <T extends AppCompatActivity> void requestForReadPhoneStatePermission(final T activityRef,
                                                                                        final int permissionRequestCode) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ActivityCompat.requestPermissions(activityRef,
                    new String[]{Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_NUMBERS,
                            Manifest.permission.READ_PHONE_STATE}, permissionRequestCode);
        } else {
            ActivityCompat.requestPermissions(activityRef,
                    new String[]{Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.READ_SMS,
                            Manifest.permission.READ_PHONE_STATE}, permissionRequestCode);
        }

    }

    public static boolean checkIfStoragePermissionIsGiven(
            final Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean checkIfCameraPermissionIsGiven(
            final Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    public static <T extends AppCompatActivity> void requestForLocationPermission(final T activityRef,
                                                                                  final int permissionRequestCode) {
        ActivityCompat.requestPermissions(activityRef,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, permissionRequestCode);
    }

    public static <T extends AppCompatActivity> boolean shouldShowPermissionRationaleForLocation(T activity) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION);
    }


    public static <T extends AppCompatActivity> void requestForStoragePermission(final T activityRef,
                                                                                 final int permissionRequestCode) {
        ActivityCompat.requestPermissions(activityRef,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, permissionRequestCode);
    }

    public static <T extends AppCompatActivity> void requestForCameraPermission(final T activityRef,
                                                                                final int permissionRequestCode) {
        ActivityCompat.requestPermissions(activityRef,
                new String[]{Manifest.permission.CAMERA}, permissionRequestCode);
    }


    public static void requestForSettingsPermission(final Activity context) {

        boolean permission;

        String[] permissions = {SYSTEM_ALERT_WINDOW, WRITE_SETTINGS};
        String[] managePermissions = {ACTION_MANAGE_OVERLAY_PERMISSION, ACTION_MANAGE_WRITE_SETTINGS};

        for (int i = 0; i < permissions.length; i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (permissions[i].equals(WRITE_SETTINGS))
                    permission = Settings.System.canWrite(context);
                else
                    permission = ContextCompat.checkSelfPermission(context, permissions[i]) == PackageManager.PERMISSION_GRANTED;
            } else {
                permission = ContextCompat.checkSelfPermission(context, permissions[i]) == PackageManager.PERMISSION_GRANTED;
            }
            if (!permission) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Intent intent = new Intent(managePermissions[i]);
                    intent.setData(Uri.parse("package:" + context.getPackageName()));
                    context.startActivity(intent);
                } else {
                    ActivityCompat.requestPermissions(context, new String[]{permissions[i]}, 2000);
                }
            }
        }
    }


    public static boolean checkForSettingsPermission(final Activity context) {

        boolean permission = false;

        String[] permissions = {SYSTEM_ALERT_WINDOW, WRITE_SETTINGS};

        for (String permission1 : permissions) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                switch (permission1) {
                    case WRITE_SETTINGS:
                        permission = Settings.System.canWrite(context);
                        break;
                    case SYSTEM_ALERT_WINDOW:
                        permission = Settings.canDrawOverlays(context);
                        break;
                    default:
                        permission = ContextCompat.checkSelfPermission(context, permission1) == PackageManager.PERMISSION_GRANTED;
                        break;
                }
            } else {
                permission = ContextCompat.checkSelfPermission(context, permission1) == PackageManager.PERMISSION_GRANTED;
            }

            if (!permission)
                break;

        }
        return permission;
    }


    public static void requestSettingPermission(AppCompatActivity context) {
        if (!PermissionUtil.checkForSettingsPermission(context))
            PermissionUtil.requestForSettingsPermission(context);
    }

    public static void requestApplicationPermission(AppCompatActivity context) {

        if (!PermissionUtil.checkIfReadPhoneStatePermissionIsGiven(context))
            PermissionUtil.requestForReadPhoneStatePermission(context, PERMISSION_CODE);

        if (!PermissionUtil.checkIfLocationPermissionIsGiven(context))
            PermissionUtil.requestForLocationPermission(context, PERMISSION_CODE);

        if (!PermissionUtil.checkIfStoragePermissionIsGiven(context))
            PermissionUtil.requestForStoragePermission(context, PERMISSION_CODE);

        if (!PermissionUtil.checkIfCameraPermissionIsGiven(context))
            PermissionUtil.requestForCameraPermission(context, PERMISSION_CODE);

    }


}
