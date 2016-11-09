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
        public static final String COLUMN_LAST_APPOINTMENT = "last_appointment";
        public static final String COLUMN_NEXT_APPOINTMENT = "next_appointment";
        public static final String COLUMN_APPOINTMENT_TIME = "appointment_time";
        public static final String COLUMN_APPOINTMENT_REMINDER = "appointment_reminder";
    }

    public static final class WomansHealthMedication implements BaseColumns {

        public static final String TABLE_MEDICATION = "medications";
        public static final String COLUMN_MEDICATION_NAME = "medication_name";
        public static final String COLUMN_DOSAGE = "dosage";
        public static final String COLUMN_NUMBER = "number";
        public static final String COLUMN_HOW_TAKEN = "how_taken";
        public static final String COLUMN_HOW_OFTEN = "how_often";
        public static final String COLUMN_COMMENCEMENT = "commencement";
        public static final String COLUMN_MEDICATION_TIME = "medication_time";
        public static final String COLUMN_HOW_LONG = "how_long";
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
