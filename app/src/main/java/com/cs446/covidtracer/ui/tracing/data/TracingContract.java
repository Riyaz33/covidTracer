package com.cs446.covidtracer.ui.tracing.data;
import android.provider.BaseColumns;

public final class TracingContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private TracingContract() {}

    public static final class TracingEntry implements BaseColumns {
        // Name of database table that will store bluetooth device contacts.
        public final static String TABLE_NAME = "tracing";

        public final static String _ID = BaseColumns._ID;

        public final static String DEVICE_ADDRESS ="address";

        public final static String START_TIME ="startTime";

        public final static String END_TIME ="endTime";

        public final static String AVERAGE_RSSI ="averageRssi";

        public final static String RISK_VALUE = "riskValue"; // 1- High, 2- Moderate, 3- Low
    }
}


