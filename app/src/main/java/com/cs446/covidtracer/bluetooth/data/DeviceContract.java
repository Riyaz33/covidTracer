package com.cs446.covidtracer.bluetooth.data;

import android.provider.BaseColumns;

public final class DeviceContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private DeviceContract() {}

    public static final class DeviceEntry implements BaseColumns {
        // Name of database table that will store bluetooth device contacts.
        public final static String TABLE_NAME = "devices";

        public final static String _ID = BaseColumns._ID;

        public final static String DEVICE_ADDRESS ="address";

        public final static String START_TIME ="startTime";

        public final static String END_TIME ="endTime";

        public final static String AVERAGE_RSSI ="averageRssi";
    }
}
