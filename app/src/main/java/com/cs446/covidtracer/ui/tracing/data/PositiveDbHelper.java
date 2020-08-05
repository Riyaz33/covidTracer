package com.cs446.covidtracer.ui.tracing.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;

import com.cs446.covidtracer.ui.tracing.TracingItem;
import com.cs446.covidtracer.ui.tracing.data.PositiveContract.PositiveEntry;

import java.text.MessageFormat;
import java.util.ArrayList;

public class PositiveDbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = TracingDbHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "positive.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    public PositiveDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_DEVICE_TABLE =  "CREATE TABLE " + PositiveEntry.TABLE_NAME + " ("
                + PositiveEntry.DEVICE_ADDRESS + " TEXT PRIMARY KEY, "
                + PositiveEntry.TIME_UPLOADED + " INTEGER NOT NULL)";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_DEVICE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addDevice(ArrayList<Pair<String, Integer>> positiveList) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (positiveList.size() > 0) {
            String query = MessageFormat.format("INSERT INTO {0}({1}, {2}) VALUES ", PositiveEntry.TABLE_NAME, PositiveEntry.DEVICE_ADDRESS, PositiveEntry.TIME_UPLOADED);
            for (int i = 0; i < positiveList.size(); i++) {
                if (i != 0) {
                    query += ", ";
                }
                query += "(" + positiveList.get(i).first + ", " + positiveList.get(i).second + ")";
            }
            try {
                db.execSQL(query);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
}
