package com.example.calendarv2;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public TextView tv;

    public HolidayViewAdapter mAdapter;
    public RecyclerView recyclerView;
    public SharedPreferences prefs;

    public List<Holiday> holidays = new ArrayList<>();
    public ArrayList<String> mNames = new ArrayList<>();
    public ArrayList<String> mIds = new ArrayList<>();
    public ArrayList<String> mDayOfWeek = new ArrayList<>();
    public ArrayList<String> mDays = new ArrayList<>();
    public ArrayList<String> mMonths = new ArrayList<>();
    public ArrayList<String> mYears = new ArrayList<>();
    public ArrayList<String> mHolidayRed = new ArrayList<>();
    public ArrayList<String> mHolidayImage = new ArrayList<>();
    public ArrayList<String> mFastingDays = new ArrayList<>();
    public ArrayList<String> mIsStepYear = new ArrayList<>();
    public ArrayList<Integer> mMonthChosen = new ArrayList<>();
    public ArrayList<String> mYearChosen = new ArrayList<>();


    public ArrayList<String> mNamesv = new ArrayList<>();
    public ArrayList<String> mIdsv = new ArrayList<>();
    public ArrayList<String> mDayOfWeekv = new ArrayList<>();
    public ArrayList<String> mDaysv = new ArrayList<>();
    public ArrayList<String> mMonthsv = new ArrayList<>();
    public ArrayList<String> mYearsv = new ArrayList<>();
    public ArrayList<String> mHolidayRedv = new ArrayList<>();
    public ArrayList<String> mHolidayImagev = new ArrayList<>();
    public ArrayList<String> mFastingDaysv = new ArrayList<>();

    public String selectedDate;
    public int month;
    public String year;
    public DatabaseHelper myDb;

    public Context context;
    public Cursor mCursor;
    public Spinner monthSpinner;
    public Spinner yearSpinner;
    ActionBar ab;
    public JobInfo info;
    private NotificationManagerCompat notificationManagerCompat;
    private String holidayName;
    boolean tomorrowIsHoliday;
    TextView tvCurrentDate;
    public static final String appPreferences = "appPreferences";
    public SharedPreferences sharedPreferences;
    public int monthLoad;
    public String yearLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(appPreferences, MODE_PRIVATE);

        initViews();

        int year = 1970;
        List<String> years = new ArrayList<>();
        while (years.size() < 68) {
            years.add(String.valueOf(year));
            year++;
        }
        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(this, R.array.months, android.R.layout.simple_spinner_item);
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, years);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);
        monthSpinner.setOnItemSelectedListener(this);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);
        yearSpinner.setOnItemSelectedListener(this);

        setSelectionDate();

        mMonthChosen.add(monthSpinner.getSelectedItemPosition() + 1);
        mYearChosen.add(yearSpinner.getSelectedItem().toString());


        myDb = new DatabaseHelper(this);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {

            databaseSetup();

            SharedPreferences.Editor editor1 = prefs.edit();
            editor1.putBoolean("firstTime", true);
            editor1.apply();
        }

        mCursor = myDb.getAllData();
        while (mCursor.moveToNext()) {
            String id;
            String dayOfWeek;
            String name;
            String day;
            String month;
            String yearH;
            String isRed;
            String isImage;
            String isFasting;
            id = mCursor.getString(0);
            name = mCursor.getString(6);
            dayOfWeek = mCursor.getString(4);
            day = mCursor.getString(1);
            month = mCursor.getString(2);
            yearH = mCursor.getString(3);
            isRed = mCursor.getString(5);
            isImage = mCursor.getString(7);
            isFasting = mCursor.getString(8);
            mNames.add(name);
            mDayOfWeek.add(dayOfWeek);
            mIds.add(id);
            mDays.add(day);
            mMonths.add(month);
            mYears.add(yearH);
            mHolidayRed.add(isRed);
            mHolidayImage.add(isImage);
            mFastingDays.add(isFasting);
        }


        mAdapter = new HolidayViewAdapter(mIds, mNames, mDays, mMonths, mYears, mHolidayRed, mHolidayImage, mFastingDays, mDayOfWeek, mIsStepYear, mMonthChosen, mYearChosen, context);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

        Calendar calendar;
        String currentTime;
        currentTime = Calendar.getInstance().getTime().toString();
        tvCurrentDate.setText(currentTime);
        boolean useNotifications = prefs.getBoolean("key_notifications", false);

        if (useNotifications) {
            notificationManagerCompat = NotificationManagerCompat.from(this);
            scheduleJob();
        }
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerviewMain);
        tvCurrentDate = findViewById(R.id.tvCurrentDate);
        monthSpinner = findViewById(R.id.monthSpinner);
        yearSpinner = findViewById(R.id.yearSpinner);
        tv = findViewById(R.id.tv);
    }

    public void cancelJob() {
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(123);
        Log.d("Cancelled job", "cancelJob: " + 123);
    }

    @Override
    protected void onResume() {
        scheduleJob();

        setSelectionDate();
        super.onResume();
    }

    public void scheduleJob() {

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean useNotifications = prefs.getBoolean("key_notifications", false);

        Log.d("Notifications Job", String.valueOf(useNotifications));
        if (useNotifications) {
            ComponentName componentName = new ComponentName(this, JobServiceApp.class);
            JobInfo info = new JobInfo.Builder(123, componentName)
                    .setRequiresCharging(true)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                    .setPersisted(true)
                    .setPeriodic(60 * 1000)
                    .build();

            JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            int resultCode = scheduler.schedule(info);
            if (resultCode == JobScheduler.RESULT_SUCCESS) {
                Log.d("JOB", "Scheduled in " + (60 * 1000) + "milliseconds");
            }
        }
    }

    private void databaseSetup() {
        readCsvDataReal();
        addData();
    }

    public void addData() {
        int id = 0;

        while (id != holidays.size()) {

            boolean isInserted = myDb.insertData(holidays.get(id).getHolidayName(),
                    holidays.get(id).getDay(), holidays.get(id).getMonth(), holidays.get(id).getYear(),
                    holidays.get(id).getIsHolidayRed(), holidays.get(id).getIsHolidayImage(),
                    holidays.get(id).getIsFasting(), holidays.get(id).getDayOfWeek());
            if (isInserted = true) {
                Log.d("MSG", holidays.get(id).getHolidayName());

            } else {

                Log.d("MSG", "False");
            }
            id++;
        }
    }

    public void readCsvDataReal() {

        InputStream is = getResources().openRawResource(R.raw.data);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

        String line;
        try {

            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(":");
                Holiday holiday = new Holiday();

                if (tokens[0].length() > 0) {
                    holiday.setDay(tokens[0]);
                } else {
                    holiday.setDay("");
                }

                if (tokens[1].length() > 0) {
                    holiday.setMonth(tokens[1]);
                } else {
                    holiday.setMonth("");
                }

                if (tokens[2].length() > 0) {
                    holiday.setYear(tokens[2]);
                } else {
                    holiday.setYear("");
                }

                if (tokens[3].length() > 0) {
                    holiday.setDayOfWeek(tokens[3]);
                } else {
                    holiday.setDayOfWeek("");
                }

                if (tokens[4].length() > 0) {
                    holiday.setIsHolidayRed(tokens[4]);
                } else {
                    holiday.setIsHolidayRed("");
                }

                if (tokens[5].length() > 0) {
                    holiday.setHolidayName(tokens[5]);
                } else {
                    holiday.setHolidayName("");
                }

                if (tokens[6].length() > 0) {
                    holiday.setIsHolidayImage(tokens[6]);
                } else {
                    holiday.setIsHolidayImage("");
                }

                if (tokens[7].length() > 0) {
                    holiday.setIsFasting(tokens[7]);
                } else {
                    holiday.setIsFasting("");
                }
                holidays.add(holiday);
            }
        } catch (IOException e) {
            Log.wtf("My", "ERROR");
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.calendar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.options:
                Intent settings = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settings);
                return true;
            case R.id.holidayBase:
                Intent holidays = new Intent(MainActivity.this, HolidayBase.class);
                startActivity(holidays);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setSelectionDate() {
        monthLoad = sharedPreferences.getInt("monthSpinnerSelection", 0);
        yearLoad = sharedPreferences.getString("yearSpinnerSelection", "1970");
        monthSpinner.setSelection(monthLoad - 1);
        yearSpinner.setSelection(Integer.parseInt(yearLoad) - 1970);
        Log.d("monthSpinnerLoad", String.valueOf(monthLoad));
        Log.d("yearSpinnerLoad", yearLoad);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        SharedPreferences.Editor editor = sharedPreferences.edit();

        Spinner monthSpinner = (Spinner) adapterView;
        Spinner yearSpinner = (Spinner) adapterView;
        month = monthLoad;
        year = yearLoad;
        if (monthSpinner.getId() == R.id.monthSpinner) {
            month = (adapterView.getSelectedItemPosition() + 1);
            editor.putInt("monthSpinnerSelection", month);
            editor.apply();

            Log.d("monthspinnerSave", String.valueOf(month));
        }

        if (yearSpinner.getId() == R.id.yearSpinner) {
            year = adapterView.getSelectedItem().toString();
            editor.putString("yearSpinnerSelection", year);
            editor.apply();
            Log.d("yearspinnerSave", year);

        }
        mMonthChosen.clear();
        mMonthChosen.add(sharedPreferences.getInt("monthSpinnerSelection", 0));
        mYearChosen.clear();
        mYearChosen.add(sharedPreferences.getString("yearSpinnerSelection", "1970"));
        selectedDate = mMonthChosen.get(0) + "." + mYearChosen.get(0);
        setDate();

        mCursor = myDb.getSelectionData(String.valueOf(mMonthChosen.get(0)), mYearChosen.get(0));
        mIds.clear();
        mNames.clear();
        mDays.clear();
        mMonths.clear();
        mYears.clear();
        mHolidayRed.clear();
        mHolidayImage.clear();
        mFastingDays.clear();
        mDayOfWeek.clear();
        while (mCursor.moveToNext()) {
            String id;
            String dayOfWeek;
            String name;
            String day;
            String month;
            String yearH;
            String isRed;
            String isImage;
            String isFasting;
            id = mCursor.getString(0);
            name = mCursor.getString(6);
            dayOfWeek = mCursor.getString(4);
            day = mCursor.getString(1);
            month = mCursor.getString(2);
            yearH = mCursor.getString(3);
            isRed = mCursor.getString(5);
            isImage = mCursor.getString(7);
            isFasting = mCursor.getString(8);
            mNames.add(name);
            mDayOfWeek.add(dayOfWeek);
            mIds.add(id);
            mDays.add(day);
            mMonths.add(month);
            mYears.add(yearH);
            mHolidayRed.add(isRed);
            mHolidayImage.add(isImage);
            mFastingDays.add(isFasting);
        }
        mAdapter.notifyDataSetChanged();

    }

    private void setDate() {
        tv.setText(selectedDate);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void showItem(View view) {
        mIdsv.clear();
        mNamesv.clear();
        mDaysv.clear();
        mMonthsv.clear();
        mYearsv.clear();
        mHolidayRedv.clear();
        mHolidayImagev.clear();
        mFastingDaysv.clear();
        mDayOfWeekv.clear();
        int itemPosition = recyclerView.getChildLayoutPosition(view);

        Cursor mCursorSd;
        mCursorSd = myDb.getSelectedDate(String.valueOf(itemPosition + 1), mMonthChosen.get(0).toString(), mYearChosen.get(0));
        while (mCursorSd.moveToNext()) {
            String id;
            String dayOfWeek;
            String name;
            String day;
            String month;
            String yearH;
            String isRed;
            String isImage;
            String isFasting;
            id = mCursorSd.getString(0);
            name = mCursorSd.getString(6);
            dayOfWeek = mCursorSd.getString(4);
            day = mCursorSd.getString(1);
            month = mCursorSd.getString(2);
            yearH = mCursorSd.getString(3);
            isRed = mCursorSd.getString(5);
            isImage = mCursorSd.getString(7);
            isFasting = mCursorSd.getString(8);

            mNamesv.add(name);
            mDayOfWeekv.add(dayOfWeek);
            mIdsv.add(id);
            mDaysv.add(day);
            mMonthsv.add(month);
            mYearsv.add(yearH);
            mHolidayRedv.add(isRed);
            mHolidayImagev.add(isImage);
            mFastingDaysv.add(isFasting);
        }

        String dialogMessage = "Date: " + mDaysv.get(0) + "." + mMonthsv.get(0) + "." + mYearsv.get(0) + "\n"
                + "Day: " + mNamesv.get(0) + "\n"
                + "Fasting: " + mFastingDaysv.get(0) + "\n";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Day summay").setCancelable(true)
                .setMessage(dialogMessage);
        AlertDialog dialog = builder.create();

        dialog.show();

    }

}



