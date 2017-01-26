package com.blashca.womanshealth.data;

import android.provider.BaseColumns;


public class WomansHealthContract {

    public WomansHealthContract() {}

    public static final class WomansHealthAppointment implements BaseColumns {

        public static final String TABLE_APPOINTMENT = "appointments";
        public static final String COLUMN_APPOINTMENT_NAME = "appointment_name";
        public static final String COLUMN_DOCTOR_NAME = "doctor_name";
        public static final String COLUMN_ADDRESS = "address";
        public static final String COLUMN_TELEPHONE = "telephone";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_LAST_APPOINTMENT_DATE = "last_appointment_date";
        public static final String COLUMN_NEXT_APPOINTMENT_SPINNER_POSITION = "next_appointment_spinner_position";
        public static final String COLUMN_NEXT_APPOINTMENT_DATE = "next_appointment_date";
        public static final String COLUMN_NEXT_APPOINTMENT_HOUR = "next_appointment_hour";
        public static final String COLUMN_NEXT_APPOINTMENT_MINUTE = "next_appointment_minute";
        public static final String COLUMN_APPOINTMENT_REMINDER = "appointment_reminder";
    }

    public static final class WomansHealthMedication implements BaseColumns {

        public static final String TABLE_MEDICATION = "medications";
        public static final String COLUMN_MEDICATION_NAME = "medication_name";
        public static final String COLUMN_DOSAGE = "dosage";
        public static final String COLUMN_NUMBER = "number";
        public static final String COLUMN_HOW_TAKEN = "how_taken";
        public static final String COLUMN_HOW_OFTEN_NUMBER = "how_often_number";
        public static final String COLUMN_HOW_OFTEN_PERIOD = "how_often_period";
        public static final String COLUMN_COMMENCEMENT_DATE = "commencement_date";
        public static final String[] COLUMN_MEDICATION_HOURS = {"medication_hour_1", "medication_hour_2", "medication_hour_3",
                "medication_hour_4", "medication_hour_5", "medication_hour_6", "medication_hour_7", "medication_hour_8",
                "medication_hour_9", "medication_hour_10", "medication_hour_11", "medication_hour_12"};
        public static final String[] COLUMN_MEDICATION_MINUTES = {"medication_minute_1", "medication_minute_2", "medication_minute_3",
                "medication_minute_4", "medication_minute_5", "medication_minute_6", "medication_minute_7", "medication_minute_8",
                "medication_minute_9","medication_minute_10","medication_minute_11","medication_minute_12"};
        public static final String COLUMN_HOW_LONG_NUMBER = "how_long_number";
        public static final String COLUMN_HOW_LONG_PERIOD = "how_long_period";
        public static final String COLUMN_MEDICATION_REMINDER = "medication_reminder";
        public static final String COLUMN_IS_ALLERGEN = "is_alergen";
        public static final String COLUMN_ALLERGIES_EFFECTS = "allergies_effects";
    }

    public static final class WomansHealthPeriod implements BaseColumns {

        public static final String TABLE_PERIOD = "periods";
        public static final String COLUMN_PERIOD_DATE = "last_period";
        public static final String COLUMN_DURATION = "duration";
    }

    public static final class WomansHealthWeight implements BaseColumns {

        public static final String TABLE_WEIGHT = "weights";
        public static final String COLUMN_WEIGHT_DATE = "weight_date";
        public static final String COLUMN_HEIGHT = "height";
        public static final String COLUMN_WEIGHT = "weight";
    }
}
