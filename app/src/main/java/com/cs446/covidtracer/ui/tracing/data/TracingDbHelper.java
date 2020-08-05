package com.cs446.covidtracer.ui.tracing.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;

import com.cs446.covidtracer.bluetooth.data.BluetoothDbHelper;
import com.cs446.covidtracer.ui.tracing.TracingItem;
import com.cs446.covidtracer.ui.tracing.data.TracingContract.TracingEntry;

import java.text.MessageFormat;
import java.util.ArrayList;

public class TracingDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = TracingDbHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "tracing.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    public TracingDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_DEVICE_TABLE =  "CREATE TABLE " + TracingEntry.TABLE_NAME + " ("
                + TracingEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TracingEntry.DEVICE_ADDRESS + " TEXT NOT NULL, "
                + TracingEntry.START_TIME + " INTEGER NOT NULL, "
                + TracingEntry.END_TIME + " INTEGER NOT NULL, "
                + TracingEntry.AVERAGE_RSSI + " REAL NOT NULL, "
                + TracingEntry.RISK_VALUE + " INTEGER NOT NULL)";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_DEVICE_TABLE);
        String item = MessageFormat.format("INSERT INTO {0}({1}, {2}, {3}, {4}, {5}) VALUES(\"abcdef\", 1596174793, 1596175393, -50, 1)", TracingEntry.TABLE_NAME, TracingEntry.DEVICE_ADDRESS, TracingEntry.START_TIME, TracingEntry.END_TIME, TracingEntry.AVERAGE_RSSI, TracingEntry.RISK_VALUE);
        db.execSQL(item);
        item = MessageFormat.format("INSERT INTO {0}({1}, {2}, {3}, {4}, {5}) VALUES(\"abcdef\", 1596174793, 1596175393, -90, 2)", TracingEntry.TABLE_NAME, TracingEntry.DEVICE_ADDRESS, TracingEntry.START_TIME, TracingEntry.END_TIME, TracingEntry.AVERAGE_RSSI, TracingEntry.RISK_VALUE);
        db.execSQL(item);
        item = MessageFormat.format("INSERT INTO {0}({1}, {2}, {3}, {4}, {5}) VALUES(\"abcdef\", 1596174793, 1596175393, -100, 3)", TracingEntry.TABLE_NAME, TracingEntry.DEVICE_ADDRESS, TracingEntry.START_TIME, TracingEntry.END_TIME, TracingEntry.AVERAGE_RSSI, TracingEntry.RISK_VALUE);
        db.execSQL(item);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<TracingItem> getAllTracingItems() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<TracingItem> tracingList = new ArrayList<>();
        String query = "SELECT " + TracingEntry._ID + ", "
                + TracingEntry.DEVICE_ADDRESS + ", "
                + TracingEntry.START_TIME + ", "
                + TracingEntry.END_TIME + ", "
                + TracingEntry.AVERAGE_RSSI + ", "
                + TracingEntry.RISK_VALUE
                + " FROM " + TracingEntry.TABLE_NAME
                + " ORDER BY " + TracingEntry.RISK_VALUE + ", " + TracingEntry.START_TIME;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            try {
                tracingList.add(new TracingItem(cursor.getString(cursor.getColumnIndex(TracingEntry.DEVICE_ADDRESS)),
                        cursor.getFloat(cursor.getColumnIndex(TracingEntry.AVERAGE_RSSI)),
                        cursor.getInt(cursor.getColumnIndex(TracingEntry.START_TIME)),
                        cursor.getInt(cursor.getColumnIndex(TracingEntry.END_TIME)),
                        cursor.getInt(cursor.getColumnIndex(TracingEntry.RISK_VALUE))));
            } catch (Exception e) {
                // nothing
            }
        }
        return tracingList;
    }

    public boolean alreadyExists(TracingItem item){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = MessageFormat.format("SELECT * FROM {0} WHERE {1} = \"{2}\" AND {3} = {4}", TracingEntry.TABLE_NAME,TracingEntry.DEVICE_ADDRESS, item.getBluetoothId(), TracingEntry.START_TIME, item.getStartTime());
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            return true;
        }
        return false;
    }
    public boolean checkAndAddTracingItems(ArrayList<Pair<String, Long>> positiveList, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        BluetoothDbHelper bluetoothDb = new BluetoothDbHelper(context);
        try {
            for (int i = 0; i < positiveList.size(); i++) {
                ArrayList<TracingItem> items = bluetoothDb.findPositiveId(positiveList.get(i).first);
                for (int j = 0; j < items.size(); j++) {
                    if (!alreadyExists(items.get(j))) {
                        String query = MessageFormat.format("INSERT INTO {0}({1}, {2}, {3}, {4}, {5}) VALUES(\"{6}\", {7}, {8}, {9}, {10})",
                                TracingEntry.TABLE_NAME,
                                TracingEntry.DEVICE_ADDRESS,
                                TracingEntry.START_TIME,
                                TracingEntry.END_TIME,
                                TracingEntry.AVERAGE_RSSI,
                                TracingEntry.RISK_VALUE,
                                positiveList.get(i).first,
                                items.get(j).getStartTime(),
                                items.get(j).getEndTime(),
                                items.get(j).getRssi(),
                                items.get(j).getRiskValue());
                        db.execSQL(query);
                    }
                }
            }
        } catch (Exception e){
            return false;
        }
        return true;
    }
}

