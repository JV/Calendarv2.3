package com.example.calendarv2;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class Notifications extends Application {

    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";
    public static final String CHANNEL_3_ID = "channel3";
    public static final String CHANNEL_4_ID = "channel4";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }

    private void createNotificationChannels() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_1_ID, "Channel1", NotificationManager.IMPORTANCE_DEFAULT);
            channel1.setDescription("This is channel 1");

            NotificationChannel channel2 = new NotificationChannel(CHANNEL_2_ID, "Channel2", NotificationManager.IMPORTANCE_DEFAULT);
            channel2.setDescription("This is channel 2");


            NotificationChannel channel3 = new NotificationChannel(CHANNEL_3_ID, "Channel3", NotificationManager.IMPORTANCE_DEFAULT);
            channel3.setDescription("This is channel 3");

            NotificationChannel channel4 = new NotificationChannel(CHANNEL_4_ID, "Channel4", NotificationManager.IMPORTANCE_DEFAULT);
            channel4.setDescription("This is channel 4");

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel1);
                manager.createNotificationChannel(channel2);
                manager.createNotificationChannel(channel3);
                manager.createNotificationChannel(channel4);
            }
        }
    }
}
