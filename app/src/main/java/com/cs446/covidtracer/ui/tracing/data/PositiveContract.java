package com.cs446.covidtracer.ui.tracing.data;
import android.provider.BaseColumns;

public class PositiveContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private PositiveContract() {}

    public static final class PositiveEntry implements BaseColumns {
        // Name of database table that will store bluetooth device contacts.
        public final static String TABLE_NAME = "positive";

        public final static String DEVICE_ADDRESS ="address";

        public final static String TIME_UPLOADED ="timeUploaded";
    }
}
