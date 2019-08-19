package com.example.calendarv2;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelperPremade extends SQLiteOpenHelper {

    String DB_PATH = null;
    private static String DATABASE_NAME = "holidays3.db";
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

    private SQLiteDatabase myDb;

    private final Context myContext;

    public DatabaseHelperPremade(Context context) {

        super(context, DATABASE_NAME, null, 3);
        this.myContext = context;
        this.DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";
        Log.e("path1", DB_PATH);
    }


    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();

        if (dbExist) {

        } else {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying base");

            }
        }
    }


    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLException e) {

        }

        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB !=null ? true : false;
    }

    private void copyDataBase() throws IOException {
        InputStream myInput = myContext.getAssets().open(DATABASE_NAME);
        String outFileName = DB_PATH + DATABASE_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DATABASE_NAME;
        myDb = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }


    @Override
    public synchronized void close() {
        if(myDb != null) {
            myDb.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        if (i > i1) {
            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public Cursor getAllData(String table, String [] columns, String selection, String [] selectionArgs, String groupBy, String having, String orderBy) {

        return myDb.query("holidays", columns, selection, selectionArgs, null, null, null);

    }

    public Cursor getAllData1() {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        return cursor;

    }


}
