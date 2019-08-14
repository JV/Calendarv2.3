package com.example.calendarv2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 3;
    private static final String DB_NAME = "holidays3.db";
    public static final String TABLE_NAME = "holidays";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_HOLIDAY_RED = "holidayRed";
    public static final String COLUMN_HOLIDAY_IMAGE = "holidayImage";
    public static final String COLUMN_DAY_OF_WEEK = "dayOfWeek";
    public static final String COLUMN_DAY = "day";
    public static final String COLUMN_MONTH = "month";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_NAME = "holidayName";
    public static final String COLUMN_FASTING = "fasting";

    private static final String CREATE_SQL = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID +
            " integer primary key autoincrement, " + COLUMN_DAY + " text, " + COLUMN_MONTH
            + " text, " + COLUMN_YEAR + " text, " + COLUMN_DAY_OF_WEEK + " text, " + COLUMN_HOLIDAY_RED + " text, " + COLUMN_NAME + " text, " +
            COLUMN_HOLIDAY_IMAGE + " text, " + COLUMN_FASTING + " text );";
    private static final String DROP_SQL = "DROP TABLE " + TABLE_NAME + ";";


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_SQL);
    }

    public boolean insertData(String name, String day, String month, String year, String holidayRed, String holidayImage, String fasting, String dayOfWeek) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DAY, day);
        contentValues.put(COLUMN_MONTH, month);
        contentValues.put(COLUMN_YEAR, year);
        contentValues.put(COLUMN_DAY_OF_WEEK, dayOfWeek);
        contentValues.put(COLUMN_HOLIDAY_RED, holidayRed);
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_HOLIDAY_IMAGE, holidayImage);
        contentValues.put(COLUMN_FASTING, fasting);
        long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getAllData() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        return cursor;
    }

    public Cursor getSelectionData(String month, String year) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE month = ? AND year = ?;", new String[]{month,year});
        return cursor;
    }

    public Cursor getToday(String day, String month, String year) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE day = ? AND month = ? AND year = ?;", new String[]{day,month,year});
        return cursor;
    }

    public Cursor getTomorrow(String day, String month, String year) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE day = ? AND month = ? AND year = ?;", new String[]{day,month,year});
        return cursor;

    }

    public Cursor getSelectedDate(String day, String month, String year) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE day = ? AND month = ? AND year = ?;", new String[]{day,month,year});
        return cursor;
    }
}
