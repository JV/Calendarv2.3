package com.example.calendarv2;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

import static com.example.calendarv2.Notifications.CHANNEL_1_ID;

public class Alarm extends BroadcastReceiver {
    NotificationManagerCompat notificationManagerCompat;



    @Override
    public void onReceive(Context context, Intent intent) {

        Intent serviceIntent = new Intent(context, AlarmService.class);
        context.startService(serviceIntent);

    }

    public void setAlarm(Context context) {
        notificationManagerCompat = NotificationManagerCompat.from(context);

        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.SECOND, 30);
        Intent intent = new Intent(context, AlarmService.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 1000 * 60, sender);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_adjust_black_24dp)
                .setContentTitle("Notification Alarm")
                .setContentText("Alarm notification")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManagerCompat.notify(1, notification);
    }


}
