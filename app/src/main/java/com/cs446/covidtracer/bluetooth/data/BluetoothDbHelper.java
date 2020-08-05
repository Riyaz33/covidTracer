package com.cs446.covidtracer.bluetooth.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cs446.covidtracer.bluetooth.data.DeviceContract.DeviceEntry;
import com.cs446.covidtracer.ui.tracing.TracingItem;
import com.cs446.covidtracer.ui.tracing.data.TracingContract;

import java.text.MessageFormat;
import java.util.ArrayList;

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

    public ArrayList<TracingItem> findPositiveId(String bluetoothID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = MessageFormat.format("SELECT * FROM {0} WHERE {1} = \"{2}\"", DeviceEntry.TABLE_NAME, DeviceEntry.DEVICE_ADDRESS, bluetoothID);
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        ArrayList<TracingItem> tracingList = new ArrayList<>();
        while (cursor.moveToNext()) {
            try {
                tracingList.add(new TracingItem(cursor.getString(cursor.getColumnIndex(DeviceEntry.DEVICE_ADDRESS)),
                        cursor.getFloat(cursor.getColumnIndex(DeviceEntry.AVERAGE_RSSI)),
                        cursor.getInt(cursor.getColumnIndex(DeviceEntry.START_TIME)),
                        cursor.getInt(cursor.getColumnIndex(DeviceEntry.END_TIME))));
            } catch (Exception e) {
                // nothing
            }
        }
        return tracingList;
    }
}
