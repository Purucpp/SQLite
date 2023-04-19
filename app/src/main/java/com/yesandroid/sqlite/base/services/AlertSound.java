package com.yesandroid.sqlite.base.services;

import static android.media.AudioManager.STREAM_MUSIC;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.work.Data;

import in.yesandroid.base_android.R;
import in.yesandroid.base_android.YourPreference;
import in.yesandroid.base_android.utils.os.ServiceUtils;

/**
 * When triggered, plays the audible sound
 */

@SuppressWarnings("HardCodedStringLiteral")
public class AlertSound extends Service {

    public int live=0;

    private static final Object LOCK = new Object();

    public static Object getLock() {
        return LOCK;
    }

    public static final String ALERT_TYPE_MONITORING = "ALERT_TYPE_MONITORING";
    public static final String ALERT_TYPE_COMPLICATION = "ALERT_TYPE_COMPLICATION";
    public static final String ALERT_TYPE_INCOMING = "ALERT_TYPE_INCOMING";
    public static final String ALERT_TYPE_DOPPLER="ALERT_TYPE_DOPPLER";

    private static final String LOG_TAG = AlertSound.class.getName();
    private final String CHANNEL_ID = "daksh_alert";
    private static final String ALERT_TYPE = "ALERT_TYPE";
    private final IBinder mBinder = new LocalBinder();
    private static MediaPlayer alertMediaPlayer;

    public volatile static boolean isRunning = false;

    public static final int ALERT_NOTIFICATION_ID = 3;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */

    public static void trigger(String alert_type, Context context) {
        ServiceUtils.triggerOneTimeService(AlertSound.class, true, "alert_sound_" + alert_type, null, new Data.Builder().putString(ALERT_TYPE, alert_type).build(), context);
    }

    public class LocalBinder extends Binder {
        public AlertSound getService() {
            // Return this instance of LocalService so clients can call public methods
            return AlertSound.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopAlertSound();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        synchronized (getLock()) {



            try{

                YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                if(yourPrefrence.getData("email").equals("live"))
                {
                    live=1;
                }

            }
            catch(Exception e)
            {

            }

            if (intent == null || intent.getExtras() == null) {
                return START_REDELIVER_INTENT;
            }

            String alertValue = intent.getExtras().getString(ALERT_TYPE);
            if (alertValue == null) {
                return START_REDELIVER_INTENT;
            }

            if (isRunning && alertValue.equals(ALERT_TYPE_INCOMING)) {
                if (alertMediaPlayer != null) {
                    alertMediaPlayer.stop();
                    alertMediaPlayer.release();
                }
                alertMediaPlayer = null;
                isRunning = false;
            }

            if (isRunning && alertValue.equals(ALERT_TYPE_MONITORING)) {
                return START_REDELIVER_INTENT;
            }

            Intent notificationIntent = new Intent();
           // PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

            final Notification.Builder builder;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createChannel();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder = new Notification.Builder
                        (getApplicationContext(), CHANNEL_ID);
            } else {
                builder = new Notification.Builder(getApplicationContext());
            }

          /*  if(!alertValue.equals(ALERT_TYPE_POSITIVE_BEEP) || !alertValue.equals(ALERT_TYPE_NEGATIVE_BEEP)) {
                Notification notification = builder
                        .setContentTitle("Audible alert")
                        .setContentText("Audible alert")
                        .setSmallIcon(R.drawable.notification_logo)
                        .setContentIntent(pendingIntent)
                        .setTicker("Audible sound")
                        .build();

                startForeground(ALERT_NOTIFICATION_ID, notification);
            } */

            if (alertValue.equals(ALERT_TYPE_MONITORING)) {
                alertMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.enter_labor_data);
                alertMediaPlayer.setLooping(false);
            } else if (alertValue.equals(ALERT_TYPE_COMPLICATION)) {
                alertMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.new_complication);
                alertMediaPlayer.setLooping(false);
            } else if (alertValue.equals(ALERT_TYPE_INCOMING)) {
                alertMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.referral);
                alertMediaPlayer.setLooping(false);
            }

            alertMediaPlayer.setVolume(100, 100);


            AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(AUDIO_SERVICE);
            if (audioManager != null && audioManager.getStreamVolume(STREAM_MUSIC) != audioManager.getStreamMaxVolume(STREAM_MUSIC)) {
                audioManager.setStreamVolume(STREAM_MUSIC, audioManager.getStreamMaxVolume(STREAM_MUSIC), 1);
            }

            if(live==0)
            {
                alertMediaPlayer.start();
            }
//            alertMediaPlayer.start();


            if (alertMediaPlayer.isPlaying()) {
                isRunning = true;
            }
            alertMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    if (alertMediaPlayer != null) {
                        alertMediaPlayer.stop();
                        alertMediaPlayer.release();
                    }
                    alertMediaPlayer = null;
                    isRunning = false;
                    stopForeground(true);
                    stopSelf();
                }
            });
            alertMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    if (alertMediaPlayer != null) {
                        alertMediaPlayer.stop();
                        alertMediaPlayer.release();
                    }
                    alertMediaPlayer = null;
                    isRunning = false;
                    stopForeground(true);
                    stopSelf();
                    return false;
                }
            });

            return START_REDELIVER_INTENT;
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createChannel() {

        NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            if (notificationManager.getNotificationChannel(CHANNEL_ID) != null) {
                return;
            }
        } else
            return;
        String name = "yesandroid sound alerts";
        String description = "This channel is used to create notifications for sounds of the system";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        @SuppressLint("WrongConstant") NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);

        notificationManager.createNotificationChannel(channel);


    }


    public void stopAlertSound() {
        if (alertMediaPlayer != null && alertMediaPlayer.isPlaying()) {
            synchronized (getLock()) {
                alertMediaPlayer.stop();
                alertMediaPlayer.release();
                alertMediaPlayer = null;
                isRunning = false;
                stopForeground(true);
            }
        }
    }

}