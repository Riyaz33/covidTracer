package com.cs446.covidtracer.ui.tracing;

import android.graphics.Color;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class TracingItem {
    private String mBluetoothId;
    private int mRssi;
    private Date mStartTime;
    private Date mEndTime;

    public TracingItem(String bluetoothId, int rssi, String startTime, String endTime) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a");
        mBluetoothId = bluetoothId;
        mRssi = rssi;
        mStartTime = formatter.parse(startTime);
        mEndTime = formatter.parse(endTime);
    }

    private int getDistance() { // in meters
        // txPower can be changed
        int txPower = -50;
        int diff = (txPower - mRssi) > 0 ? (txPower - mRssi) : 0;

        return (int)Math.pow(10d, ((double)diff) / (10 * 2));
    }

    private int getDuration() { // in seconds
        long difference = mEndTime.getTime() - mStartTime.getTime();
        int timeElapsed = (int) TimeUnit.MILLISECONDS.toMinutes(difference);
        return timeElapsed;
    }

    public String getDurationString() {
        float duration = getDuration();
        float distance = getDistance();
        float factor = duration*duration/distance;
        return "T-" + String.format("%.0f", duration) + " D-" + String.format("%.0f", distance) + " F-" + String.format("%.3f",factor);
    }

    public String getDaysAgo() {
        long difference = mEndTime.getTime() - System.currentTimeMillis();
        int timeElapsed = (int) TimeUnit.MICROSECONDS.toDays(difference);
        return timeElapsed + " Days Ago";
    }

    public String getRisk() {
        float timeElapsed = getDuration();
        float distance = getDistance();
        float factor = timeElapsed*timeElapsed/distance;
        if (factor >= 30) {
            return "High";
        } else if (factor >= 5) {
            return "Moderate";
        } else {
            return "Low";
        }
    }

    public int getBackgroundColor() {
        String risk = getRisk();
        if(risk == "High") {
            return Color.parseColor("#F4511E"); // color for High Risk from colors.xml
        } else if(risk == "Moderate") {
            return Color.parseColor("#FB8C00"); // color for Moderate Risk from colors.xml
        } else {
            return Color.parseColor("#7CB342"); // color for Low Risk from colors.xml
        }
    }
}
