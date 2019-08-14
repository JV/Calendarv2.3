package com.example.calendarv2;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class HolidayBase extends AppCompatActivity {

    DatabaseHelper myDb;

    private HolidayViewAdapter mAdapter;
    RecyclerView recyclerView;


    private List<Holiday> holidays = new ArrayList<>();
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mIds = new ArrayList<>();
    private ArrayList<String> mDayOfWeek = new ArrayList<>();
    private ArrayList<String> mDays = new ArrayList<>();
    private ArrayList<String> mMonths = new ArrayList<>();
    private ArrayList<String> mYears = new ArrayList<>();
    private ArrayList<String> mHolidayRed = new ArrayList<>();
    private ArrayList<String> mHolidayImage = new ArrayList<>();
    private ArrayList<String> mFastingDays = new ArrayList<>();
    private Context context;
    private ArrayList<String> mIsStepYear = new ArrayList<>();
    private ArrayList<Integer> mMonthChosen = new ArrayList<>();
    private ArrayList<String> mYearChosen = new ArrayList<>();

    Cursor mCursor;
    int daysInSelectedMonth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday_base);

        myDb = new DatabaseHelper(this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {

            databaseSetup();

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.apply();
        }


      //  String chosenMonth = String.valueOf(monthSpinner.getSelectedItemPosition());
      //  String chosenYear = String.valueOf(yearSpinner.getSelectedItemPosition());

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


        ActionBar ab = getActionBar();


        recyclerView = findViewById(R.id.recyclerview);

        mAdapter = new HolidayViewAdapter(mIds, mNames, mDays, mMonths, mYears, mHolidayRed, mHolidayImage, mFastingDays, mDayOfWeek, mIsStepYear, mMonthChosen, mYearChosen, context);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

    }
    private void databaseSetup() {

        readCsvDataReal();
        addData();
    }

    public void addData() {

        boolean isInserted = myDb.insertData(holidays.get(0).getHolidayName(),
                holidays.get(0).getDay(), holidays.get(0).getMonth(), holidays.get(0).getYear(),
                holidays.get(0).getIsHolidayRed(), holidays.get(0).getIsHolidayImage(), holidays.get(0).getIsFasting(), holidays.get(0).getDayOfWeek());
        if (isInserted = true) {
            Toast.makeText(this, "Data inserted", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Data NOT inserted", Toast.LENGTH_SHORT).show();

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

                Log.d("Base", "Just created: " + holiday);
                Log.d("Data", "Just created: " + holidays.size());
            }
        } catch (IOException e) {
            Log.wtf("My", "ERROR");
            e.printStackTrace();
        }

    }

}
