package com.cs446.covidtracer.ui.updates.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cs446.covidtracer.ui.updates.data.SummaryContract.SummaryEntry;

public class SummaryDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = SummaryDbHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "summary.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    public SummaryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_PETS_TABLE =  "CREATE TABLE " + SummaryEntry.TABLE_NAME + " ("
                + SummaryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SummaryEntry.COLUMN_PROVINCE_NAME + " TEXT NOT NULL);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_PETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
