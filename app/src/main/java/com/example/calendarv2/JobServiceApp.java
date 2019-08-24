package com.example.calendarv2;

import android.app.Notification;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.calendarv2.Notifications.CHANNEL_1_ID;
import static com.example.calendarv2.Notifications.CHANNEL_2_ID;
import static com.example.calendarv2.Notifications.CHANNEL_3_ID;
import static com.example.calendarv2.Notifications.CHANNEL_4_ID;

public class JobServiceApp extends JobService {

    private boolean isCancelled = false;
    NotificationManagerCompat notificationManagerCompat;
    public List<Holiday> holidays = new ArrayList<>();
    public ArrayList<String> mNamess = new ArrayList<>();
    public ArrayList<String> mIdss = new ArrayList<>();
    public ArrayList<String> mDayOfWeeks = new ArrayList<>();
    public ArrayList<String> mDayss = new ArrayList<>();
    public ArrayList<String> mMonthss = new ArrayList<>();
    public ArrayList<String> mYearss = new ArrayList<>();
    public ArrayList<String> mHolidayReds = new ArrayList<>();
    public ArrayList<String> mHolidayImages = new ArrayList<>();
    public ArrayList<String> mFastingDayss = new ArrayList<>();
    public ArrayList<String> mIsStepYears = new ArrayList<>();
    public ArrayList<Integer> mMonthChosens = new ArrayList<>();
    public ArrayList<String> mYearChosens = new ArrayList<>();
    public ArrayList<String> tomorrowDates = new ArrayList<>();

    public ArrayList<String> mNamest = new ArrayList<>();
    public ArrayList<String> mIdst = new ArrayList<>();
    public ArrayList<String> mDayOfWeekt = new ArrayList<>();
    public ArrayList<String> mDayst = new ArrayList<>();
    public ArrayList<String> mMonthst = new ArrayList<>();
    public ArrayList<String> mYearst = new ArrayList<>();
    public ArrayList<String> mHolidayRedt = new ArrayList<>();
    public ArrayList<String> mHolidayImaget = new ArrayList<>();
    public ArrayList<String> mFastingDayst = new ArrayList<>();

    public String selectedDates;
    public int months;
    public String years;
    DatabaseHelper myDbs;
    public Context context;
    Cursor mCursorToday;
    Cursor mCursorTomorrow;
    SharedPreferences prefs;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        boolean useNotifications = prefs.getBoolean("key_notifications", false);

        Log.d("NotificationSer", String.valueOf(useNotifications));
        if (useNotifications) {

            mIdst.clear();
            mNamest.clear();
            mDayOfWeekt.clear();
            mDayst.clear();
            mMonthst.clear();
            mYearst.clear();
            mHolidayRedt.clear();
            mHolidayImaget.clear();
            mFastingDayst.clear();

            notificationManagerCompat = NotificationManagerCompat.from(this);
            Calendar calendar = Calendar.getInstance();
            int todayDay = calendar.get(Calendar.DATE);
            int todayMonth = calendar.get(Calendar.MONTH) + 1;
            int todayYear = calendar.get(Calendar.YEAR);
            int tomorrowDay = calendar.get(Calendar.DATE) + 1;
            int tomorrowMonth = todayMonth + 1;
            int tomorrowYear = todayYear + 1;


            if (tomorrowDay < todayDay) {
                tomorrowMonth = todayMonth + 1;
            }
            if (tomorrowMonth < todayMonth) {
                tomorrowYear = todayYear + 1;
            }

            myDbs = new DatabaseHelper(this);

            mCursorToday = myDbs.getToday(String.valueOf(todayDay), String.valueOf(todayMonth), String.valueOf(todayYear));
            while (mCursorToday.moveToNext()) {
                String id;
                String dayOfWeek;
                String name;
                String day;
                String month;
                String yearH;
                String isRed;
                String isImage;
                String isFasting;
                id = mCursorToday.getString(0);
                name = mCursorToday.getString(6);
                dayOfWeek = mCursorToday.getString(4);
                day = mCursorToday.getString(1);
                month = mCursorToday.getString(2);
                yearH = mCursorToday.getString(3);
                isRed = mCursorToday.getString(5);
                isImage = mCursorToday.getString(7);
                isFasting = mCursorToday.getString(8);

                mNamest.add(name);
                mDayOfWeekt.add(dayOfWeek);
                mIdst.add(id);
                mDayst.add(day);
                mMonthst.add(month);
                mYearst.add(yearH);
                mHolidayRedt.add(isRed);
                mHolidayImaget.add(isImage);
                mFastingDayst.add(isFasting);
            }
            mCursorTomorrow = myDbs.getTomorrow(String.valueOf(tomorrowDay), String.valueOf(tomorrowMonth), String.valueOf(tomorrowYear));

            while (mCursorTomorrow.moveToNext()) {
                String id;
                String dayOfWeek;
                String name;
                String day;
                String month;
                String yearH;
                String isRed;
                String isImage;
                String isFasting;
                id = mCursorTomorrow.getString(0);
                name = mCursorTomorrow.getString(6);
                dayOfWeek = mCursorTomorrow.getString(4);
                day = mCursorTomorrow.getString(1);
                month = mCursorTomorrow.getString(2);
                yearH = mCursorTomorrow.getString(3);
                isRed = mCursorTomorrow.getString(5);
                isImage = mCursorTomorrow.getString(7);
                isFasting = mCursorTomorrow.getString(8);

                mNamest.add(name);
                mDayOfWeekt.add(dayOfWeek);
                mIdst.add(id);
                mDayst.add(day);
                mMonthst.add(month);
                mYearst.add(yearH);
                mHolidayRedt.add(isRed);
                mHolidayImaget.add(isImage);
                mFastingDayst.add(isFasting);
            }

            doBackgroundWork(jobParameters);
        }
        return true;
    }

    private void doBackgroundWork(final JobParameters jobParameters) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isCancelled) {
                    return;
                }
                checkIfTodayIsFasting();
                checkIfTodayIsHoliday();
                checkIfTomorrowIsHoliday();
                checkIfTomorrowIsFasting();
                try {
                    Thread.sleep(60 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                jobFinished(jobParameters, true);
            }
        }).start();
    }

    private void checkIfTomorrowIsFasting() {
        if (mFastingDayst.get(1).equals("true")) {
            String holidayName = mNamest.get(1);
            sendOnChannel4(holidayName);
        }
    }

    private void sendOnChannel4(String holidayName) {
        Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_4_ID)
                .setSmallIcon(R.drawable.ic_adjust_black_24dp)
                .setContentTitle("Fasting tomorrow")
                .setContentText(holidayName)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManagerCompat.notify(4, notification);
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        isCancelled = true;
        return true;
    }

    public void checkIfTodayIsFasting() {

        if (mFastingDayst.get(0).equals("Post")) {
            String holidayName = mNamest.get(0);
            sendOnChannel1(holidayName);
        }
    }

    public void checkIfTodayIsHoliday() {

        if (mHolidayRedt.get(0).equals("true")) {
            String holidayName = mNamest.get(0);
            sendOnChannel2(holidayName);
        }
    }

    public void checkIfTomorrowIsHoliday() {
        if (mHolidayRedt.get(1).equals("true")) {
            String holidayName = mNamest.get(1);
            sendOnChannel3(holidayName);
        }
    }

    private void sendOnChannel3(String holidayName) {

        Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_3_ID)
                .setSmallIcon(R.drawable.ic_adjust_black_24dp)
                .setContentTitle("Holiday Red tomorrow")
                .setContentText(holidayName)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManagerCompat.notify(3, notification);
    }

    public void sendOnChannel1(String holidayName) {

        Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_adjust_black_24dp)
                .setContentTitle("Fasting today")
                .setContentText(holidayName)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManagerCompat.notify(1, notification);
    }

    private void sendOnChannel2(String holidayName) {

        Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_add_black_24dp)
                .setContentTitle("Holiday red")
                .setContentText(holidayName)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        Log.d("Holiday", holidayName);
        notificationManagerCompat.notify(2, notification);
    }
}
