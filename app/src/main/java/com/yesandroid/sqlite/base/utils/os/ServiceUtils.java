package com.yesandroid.sqlite.base.utils.os;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.work.Constraints;
import androidx.work.Data;

import java.util.List;
import java.util.Map;

public class ServiceUtils {


    public static void triggerOneTimeService(Class<? extends Service> clazz, boolean cancelPrevious, String tag, Constraints constraints, Context context) {
        triggerOneTimeService(clazz, cancelPrevious, tag, constraints, null, context);
    }

    public static void triggerOneTimeService(Class<? extends Service> clazz, boolean cancelPrevious, String tag, Constraints constraints, @Nullable Data dataSentToWorker, Context context) {
        if (isAppIsInBackground(context) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return;
        }

        Intent intent = new Intent(context, clazz);
        if (dataSentToWorker != null) {
            Map<String, Object> dataObjects = dataSentToWorker.getKeyValueMap();
            for (String key : dataObjects.keySet()) {
                if (dataObjects.get(key) instanceof String) {
                    intent.putExtra(key, ((String) dataObjects.get(key)));
                } else if (dataObjects.get(key) instanceof Boolean) {
                    intent.putExtra(key, ((Boolean) dataObjects.get(key)));
                } else if (dataObjects.get(key) instanceof Long) {
                    intent.putExtra(key, ((Long) dataObjects.get(key)));
                } else {
                    throw new Error("Data type not supported yet");
                }
            }
        }
        if (cancelPrevious) {
            context.stopService(intent);
            context.startService(intent);
        } else {
            if (!isMyServiceRunning(context, clazz)) {
                context.startService(intent);
            }
        }
   /*     if (cancelPerviouse)
            WorkManager.getInstance().cancelAllWorkByTag(tag);

        WorkManager.getInstance().enqueue(getOneTimeRequest(clazz, tag, constraints, dataSentToWorker));
   */
    }

    public static boolean isMyServiceRunning(Context activity, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null)
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        return false;
    }


    public static void stopService(Context context, Class<? extends Service> serverServiceClass) {
        context.stopService(new Intent(context, serverServiceClass));

    }

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
        if (runningProcesses == null) {
            return isInBackground;
        } else {
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }

            return isInBackground;
        }
    }

    public static void bindService(Class<?> clazz, Context context, ServiceConnection serviceConnection) {

        if (context != null && serviceConnection != null) {
            Intent intent = new Intent(context, clazz);
            context.bindService(intent, serviceConnection, Context.BIND_IMPORTANT);
        }
    }
}
