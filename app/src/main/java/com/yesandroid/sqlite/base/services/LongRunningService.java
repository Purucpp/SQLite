package com.yesandroid.sqlite.base.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

public abstract class LongRunningService extends Service implements LifecycleOwner {

    private final ServiceBinder serviceBinder;
    private int notificationId;
    private String channelID;

  // private PendingIntent snoozePendingIntent;

    private String notificationTag;
    private static final String INTENT_FILTER_STRING = "in.yesandroid.base_app.service";
    public static final String notification_id_intent = "notification_id";
    public static final String notification_tag_intent = "notification_tag";
    // Service things
    private NotificationManager notificationManager;
    private CloseBoardcast closeBoardcast;


    //Life cycle
    private LifecycleRegistry lifecycleRegistry;


    public LongRunningService(int notificationId, String channelID, String notificationTag) {
        super();
        serviceBinder = new ServiceBinder(this);
        this.notificationId = notificationId;
        this.channelID = channelID;

        this.notificationTag = notificationTag;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return serviceBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        closeBoardcast = new CloseBoardcast();
        IntentFilter intentFilter = new IntentFilter(INTENT_FILTER_STRING);
        getApplicationContext().registerReceiver(closeBoardcast, intentFilter);
        lifecycleRegistry = new LifecycleRegistry(this);
        lifecycleRegistry.setCurrentState(Lifecycle.State.CREATED);
        lifecycleRegistry.setCurrentState(Lifecycle.State.STARTED);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lifecycleRegistry.setCurrentState(Lifecycle.State.DESTROYED);
        NotificationManagerCompat.from(getApplicationContext()).cancel(notificationTag, notificationId);
        stopForeground(true);
        if (closeBoardcast != null)
            getApplicationContext().unregisterReceiver(closeBoardcast);
    }

    protected abstract String getChannelName();

    protected abstract String getChannelDescription();

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return lifecycleRegistry;
    }

    @NonNull
    protected void updateForegroundInfo(String title, String message, @DrawableRes int drawableRes) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
        Notification notification = createNotificationWith(title, message, drawableRes);
        startForeground(notificationId, notification);
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private void createChannel() {

        NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            if (notificationManager.getNotificationChannel(channelID) != null) {
                return;
            }
        } else
            return;
        String name = getChannelName();
        String description = getChannelDescription();
        int importance = NotificationManager.IMPORTANCE_HIGH;
        @SuppressLint("WrongConstant") NotificationChannel channel = new NotificationChannel(channelID, name, importance);
        channel.setDescription(description);

        notificationManager.createNotificationChannel(channel);


    }

    protected void updateNotification(String title, String message, @DrawableRes int drawableRes) {
        Notification notification = createNotificationWith(title, message, drawableRes);

        notificationManager.notify(notificationId, notification);
    }


    @SuppressLint("WrongConstant")
    private Notification createNotificationWith(String title, String message, @DrawableRes int smallIcon) {

        Intent actionIntent = new Intent(getApplicationContext(), CloseBoardcast.class);
        actionIntent.putExtra(notification_id_intent, notificationId);
        actionIntent.putExtra(notification_tag_intent, notificationTag);
        actionIntent.putExtra("calling_class",this.getClass().getName());
        actionIntent.setAction(INTENT_FILTER_STRING);
        PendingIntent  snoozePendingIntent =null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            snoozePendingIntent=  PendingIntent.getBroadcast(getApplicationContext(), 0, actionIntent, PendingIntent.FLAG_MUTABLE);

        }
        else
        {
            snoozePendingIntent=  PendingIntent.getBroadcast(getApplicationContext(), 0, actionIntent, 0);
        }


        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle(title)
                .setContentText(message)
                .setOnlyAlertOnce(true)
                .setSmallIcon(smallIcon)
                .setOngoing(true)
                .addAction(android.R.drawable.ic_delete, "Cancel", snoozePendingIntent)
                .build();

    }


    public static class CloseBoardcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                context.stopService(new Intent(context, Class.forName(intent.getStringExtra("calling_class"))));

                NotificationManagerCompat
                        .from(context.getApplicationContext())
                        .cancel(intent.getStringExtra(notification_tag_intent),
                                intent.getIntExtra(notification_id_intent, -1));


              //  Log.d("close->","called");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        }

    }



}
