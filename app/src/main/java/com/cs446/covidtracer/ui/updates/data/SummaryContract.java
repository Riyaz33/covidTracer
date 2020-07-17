package com.cs446.covidtracer.ui.updates.data;

import android.provider.BaseColumns;

public final class SummaryContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private SummaryContract() {}

    public static final class SummaryEntry implements BaseColumns {
        // Name of database table that will store provinces of interest
        public final static String TABLE_NAME = "provinces";

        /**
         * Unique ID number for the province (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the pet.
         *
         * Type: TEXT
         */
        public final static String COLUMN_PROVINCE_NAME ="name";
    }
}
