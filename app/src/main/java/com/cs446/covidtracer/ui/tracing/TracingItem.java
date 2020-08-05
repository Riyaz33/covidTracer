package com.cs446.covidtracer.ui.tracing;

import android.graphics.Color;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class TracingItem {
    private String mBluetoothId;
    private float mRssi;
    private Date mStartTime;
    private Date mEndTime;
    private int mRiskValue;

    public TracingItem(String bluetoothId, float rssi, long startTime, long endTime, int riskValue) throws ParseException {
        mBluetoothId = bluetoothId;
        mRssi = rssi;
        mStartTime = new Date(startTime * 1000);
        mEndTime = new Date(endTime * 1000);
        mRiskValue = riskValue;
    }

    public TracingItem(String bluetoothId, float rssi, long startTime, long endTime) throws ParseException {
        mBluetoothId = bluetoothId;
        mRssi = rssi;
        mStartTime = new Date(startTime * 1000);
        mEndTime = new Date(endTime * 1000);
        String risk = calcRisk();
        if(risk == "High") {
            mRiskValue = 1;
        } else if(risk == "Moderate") {
            mRiskValue = 2;
        } else {
            mRiskValue = 3;
        }
    }

    private int getDistance() { // in meters
        // txPower can be changed
        int txPower = -50;
        float diff = (txPower - mRssi) > 0 ? (txPower - mRssi) : 0;

        return (int)Math.pow(10d, ((double)diff) / (10 * 2));
    }

    private int getDuration() { // in seconds
        long difference = mEndTime.getTime() - mStartTime.getTime();
        int timeElapsed = (int) TimeUnit.MILLISECONDS.toMinutes(difference);
        return timeElapsed;
    }

    public float getRssi(){
        return mRssi;
    }

    public long getStartTime(){
        return mStartTime.getTime();
    }

    public long getEndTime(){
        return mEndTime.getTime();
    }

    public int getRiskValue() {
        return mRiskValue;
    }

    public String getBluetoothId() {
        return mBluetoothId;
    }

    public String getDurationString() {
        int duration = getDuration();
        Random rand = new Random();
        int upperBound = 5;
        int lower_rand = duration - rand.nextInt(upperBound);
        lower_rand = (lower_rand <= 0 ? 0 : lower_rand);
        return Integer.toString(lower_rand) + " - " + Integer.toString(duration + rand.nextInt(upperBound)) + " Mins";
    }

    public String getDaysAgo() {
        Random rand = new Random();
        int upperBound = 5;
        long difference = System.currentTimeMillis() - mEndTime.getTime();
        int timeElapsed = (int) TimeUnit.MILLISECONDS.toDays(difference);
        if (timeElapsed == 0) {
            return "0 - " + (rand.nextInt(upperBound) + timeElapsed) + " Days";
        } else {
            int bottom_rand = rand.nextInt(upperBound);
            bottom_rand = (bottom_rand-timeElapsed <= 0 ? 0 : bottom_rand);
            return "" + Integer.toString(timeElapsed-bottom_rand) + " - " + Integer.toString(timeElapsed + rand.nextInt(upperBound)) + " Days";
        }
    }

    public String getRisk() {
        int risk = mRiskValue;
        if(risk == 1) {
            return "High";
        } else if(risk == 2) {
            return "Moderate";
        } else {
            return "Low";
        }
    }
    public String calcRisk() {
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
            return Color.parseColor("#FF6961"); // color for High Risk from colors.xml
        } else if(risk == "Moderate") {
            return Color.parseColor("#FFB346"); // color for Moderate Risk from colors.xml
        } else {
            return Color.parseColor("#77DD77"); // color for Low Risk from colors.xml
        }
    }
}
