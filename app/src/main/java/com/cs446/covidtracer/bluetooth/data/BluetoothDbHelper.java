package com.cs446.covidtracer.bluetooth.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cs446.covidtracer.bluetooth.data.DeviceContract.DeviceEntry;

public class BluetoothDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = BluetoothDbHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "bluetooth.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    public BluetoothDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_DEVICE_TABLE =  "CREATE TABLE " + DeviceEntry.TABLE_NAME + " ("
                + DeviceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DeviceEntry.DEVICE_ADDRESS + " TEXT NOT NULL, "
                + DeviceEntry.START_TIME + " INTEGER NOT NULL, "
                + DeviceEntry.END_TIME + " INTEGER NOT NULL, "
                + DeviceEntry.AVERAGE_RSSI + " REAL NOT NULL);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_DEVICE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
