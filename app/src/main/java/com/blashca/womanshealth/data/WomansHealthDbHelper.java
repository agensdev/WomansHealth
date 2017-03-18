package com.blashca.womanshealth.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.blashca.womanshealth.models.Period;
import com.blashca.womanshealth.R;
import com.blashca.womanshealth.models.Appointment;
import com.blashca.womanshealth.models.Medication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


public class WomansHealthDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "womanshealth.db";
    Context context;

    private static final String[] APPOINTMENT_COLUMNS_TO_BE_BOUND_WITH_ID = new String[]{
            WomansHealthContract.WomansHealthAppointment._ID,
            WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_NAME,
            WomansHealthContract.WomansHealthAppointment.COLUMN_DOCTOR_NAME,
            WomansHealthContract.WomansHealthAppointment.COLUMN_ADDRESS,
            WomansHealthContract.WomansHealthAppointment.COLUMN_TELEPHONE,
            WomansHealthContract.WomansHealthAppointment.COLUMN_EMAIL,
            WomansHealthContract.WomansHealthAppointment.COLUMN_LAST_APPOINTMENT_DATE,
            WomansHealthContract.WomansHealthAppointment.COLUMN_NEXT_APPOINTMENT_SPINNER_POSITION,
            WomansHealthContract.WomansHealthAppointment.COLUMN_NEXT_APPOINTMENT_DATE,
            WomansHealthContract.WomansHealthAppointment.COLUMN_NEXT_APPOINTMENT_HOUR,
            WomansHealthContract.WomansHealthAppointment.COLUMN_NEXT_APPOINTMENT_MINUTE,
            WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_REMINDER
    };

    private static final String[] MEDICATION_COLUMNS_TO_BE_BOUND_WITH_ID = new String[]{
            WomansHealthContract.WomansHealthMedication._ID,
            WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_NAME,
            WomansHealthContract.WomansHealthMedication.COLUMN_DOSAGE,
            WomansHealthContract.WomansHealthMedication.COLUMN_NUMBER,
            WomansHealthContract.WomansHealthMedication.COLUMN_HOW_TAKEN,
            WomansHealthContract.WomansHealthMedication.COLUMN_HOW_OFTEN_NUMBER,
            WomansHealthContract.WomansHealthMedication.COLUMN_HOW_OFTEN_PERIOD,
            WomansHealthContract.WomansHealthMedication.COLUMN_COMMENCEMENT_DATE,
            WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[0],
            WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[1],
            WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[2],
            WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[3],
            WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[4],
            WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[5],
            WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[6],
            WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[7],
            WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[8],
            WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[9],
            WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[10],
            WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[11],
            WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[0],
            WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[1],
            WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[2],
            WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[3],
            WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[4],
            WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[5],
            WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[6],
            WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[7],
            WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[8],
            WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[9],
            WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[10],
            WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[11],
            WomansHealthContract.WomansHealthMedication.COLUMN_HOW_LONG_NUMBER,
            WomansHealthContract.WomansHealthMedication.COLUMN_HOW_LONG_PERIOD,
            WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_REMINDER,
            WomansHealthContract.WomansHealthMedication.COLUMN_IS_ALLERGEN,
            WomansHealthContract.WomansHealthMedication.COLUMN_ALLERGY_EFFECTS
    };

    private static final String[] WEIGHT_COLUMNS_TO_BE_BOUND_WITH_ID = new String[] {
            WomansHealthContract.WomansHealthWeight._ID,
            WomansHealthContract.WomansHealthWeight.COLUMN_WEIGHT_DATE,
            WomansHealthContract.WomansHealthWeight.COLUMN_HEIGHT,
            WomansHealthContract.WomansHealthWeight.COLUMN_WEIGHT
    };

    private static final String[] PERIOD_COLUMNS_TO_BE_BOUND_WITH_ID = new String[] {
            WomansHealthContract.WomansHealthPeriod._ID,
            WomansHealthContract.WomansHealthPeriod.COLUMN_PERIOD_DATE,
            WomansHealthContract.WomansHealthPeriod.COLUMN_DURATION
    };

    public WomansHealthDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_TABLE_APPOINTMENT = "CREATE TABLE " + WomansHealthContract.WomansHealthAppointment.TABLE_APPOINTMENT + " (" +
                WomansHealthContract.WomansHealthAppointment._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_NAME + " TEXT NOT NULL, " +
                WomansHealthContract.WomansHealthAppointment.COLUMN_DOCTOR_NAME + " TEXT, " +
                WomansHealthContract.WomansHealthAppointment.COLUMN_ADDRESS + " TEXT, " +
                WomansHealthContract.WomansHealthAppointment.COLUMN_TELEPHONE + " TEXT, " +
                WomansHealthContract.WomansHealthAppointment.COLUMN_EMAIL + " TEXT, " +
                WomansHealthContract.WomansHealthAppointment.COLUMN_LAST_APPOINTMENT_DATE + " INTEGER, " +
                WomansHealthContract.WomansHealthAppointment.COLUMN_NEXT_APPOINTMENT_SPINNER_POSITION + " INTEGER DEFAULT 0 NOT NULL, " +
                WomansHealthContract.WomansHealthAppointment.COLUMN_NEXT_APPOINTMENT_DATE + " INTEGER, " +
                WomansHealthContract.WomansHealthAppointment.COLUMN_NEXT_APPOINTMENT_HOUR + " INTEGER, " +
                WomansHealthContract.WomansHealthAppointment.COLUMN_NEXT_APPOINTMENT_MINUTE + " INTEGER, " +
                WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_REMINDER + " INTEGER DEFAULT 0 NOT NULL " +
                " );";

        db.execSQL(SQL_CREATE_TABLE_APPOINTMENT);

        ContentValues appointmentValues = new ContentValues();
        appointmentValues.put(WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_NAME, context.getResources().getString(R.string.skin_check));
        db.insert(WomansHealthContract.WomansHealthAppointment.TABLE_APPOINTMENT, null, appointmentValues);
        appointmentValues.put(WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_NAME, context.getResources().getString(R.string.hearing_test));
        db.insert(WomansHealthContract.WomansHealthAppointment.TABLE_APPOINTMENT, null, appointmentValues);
        appointmentValues.put(WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_NAME, context.getResources().getString(R.string.eye_exam));
        db.insert(WomansHealthContract.WomansHealthAppointment.TABLE_APPOINTMENT, null, appointmentValues);
        appointmentValues.put(WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_NAME, context.getResources().getString(R.string.dental_exam));
        db.insert(WomansHealthContract.WomansHealthAppointment.TABLE_APPOINTMENT, null, appointmentValues);
        appointmentValues.put(WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_NAME, context.getResources().getString(R.string.bone_density_test));
        db.insert(WomansHealthContract.WomansHealthAppointment.TABLE_APPOINTMENT, null, appointmentValues);
        appointmentValues.put(WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_NAME, context.getResources().getString(R.string.colonoscopy));
        db.insert(WomansHealthContract.WomansHealthAppointment.TABLE_APPOINTMENT, null, appointmentValues);
        appointmentValues.put(WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_NAME, context.getResources().getString(R.string.gastroscopy));
        db.insert(WomansHealthContract.WomansHealthAppointment.TABLE_APPOINTMENT, null, appointmentValues);
        appointmentValues.put(WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_NAME, context.getResources().getString(R.string.abdominal_ultrasound));
        db.insert(WomansHealthContract.WomansHealthAppointment.TABLE_APPOINTMENT, null, appointmentValues);
        appointmentValues.put(WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_NAME, context.getResources().getString(R.string.blood_pressure_test));
        db.insert(WomansHealthContract.WomansHealthAppointment.TABLE_APPOINTMENT, null, appointmentValues);
        appointmentValues.put(WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_NAME, context.getResources().getString(R.string.doppler_ultrasound));
        db.insert(WomansHealthContract.WomansHealthAppointment.TABLE_APPOINTMENT, null, appointmentValues);
        appointmentValues.put(WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_NAME, context.getResources().getString(R.string.ekg));
        db.insert(WomansHealthContract.WomansHealthAppointment.TABLE_APPOINTMENT, null, appointmentValues);
        appointmentValues.put(WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_NAME, context.getResources().getString(R.string.spirometry));
        db.insert(WomansHealthContract.WomansHealthAppointment.TABLE_APPOINTMENT, null, appointmentValues);
        appointmentValues.put(WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_NAME, context.getResources().getString(R.string.chest_Xray));
        db.insert(WomansHealthContract.WomansHealthAppointment.TABLE_APPOINTMENT, null, appointmentValues);
        appointmentValues.put(WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_NAME, context.getResources().getString(R.string.mammogram));
        db.insert(WomansHealthContract.WomansHealthAppointment.TABLE_APPOINTMENT, null, appointmentValues);
        appointmentValues.put(WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_NAME, context.getResources().getString(R.string.breast_ultrasound));
        db.insert(WomansHealthContract.WomansHealthAppointment.TABLE_APPOINTMENT, null, appointmentValues);
        appointmentValues.put(WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_NAME, context.getResources().getString(R.string.transvaginal_ultrasound));
        db.insert(WomansHealthContract.WomansHealthAppointment.TABLE_APPOINTMENT, null, appointmentValues);
        appointmentValues.put(WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_NAME, context.getResources().getString(R.string.pelvic_and_pap));
        db.insert(WomansHealthContract.WomansHealthAppointment.TABLE_APPOINTMENT, null, appointmentValues);
        appointmentValues.put(WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_NAME, context.getResources().getString(R.string.fecal_blood_test));
        db.insert(WomansHealthContract.WomansHealthAppointment.TABLE_APPOINTMENT, null, appointmentValues);
        appointmentValues.put(WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_NAME, context.getResources().getString(R.string.urine_test));
        db.insert(WomansHealthContract.WomansHealthAppointment.TABLE_APPOINTMENT, null, appointmentValues);
        appointmentValues.put(WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_NAME, context.getResources().getString(R.string.thyroid_test));
        db.insert(WomansHealthContract.WomansHealthAppointment.TABLE_APPOINTMENT, null, appointmentValues);
        appointmentValues.put(WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_NAME, context.getResources().getString(R.string.blood_tests));
        db.insert(WomansHealthContract.WomansHealthAppointment.TABLE_APPOINTMENT, null, appointmentValues);


        final String SQL_CREATE_TABLE_MEDICATION = "CREATE TABLE " + WomansHealthContract.WomansHealthMedication.TABLE_MEDICATION + " (" +
                WomansHealthContract.WomansHealthMedication._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_NAME + " TEXT NOT NULL, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_DOSAGE + " TEXT, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_NUMBER + " INTEGER, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_HOW_TAKEN + " INTEGER, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_HOW_OFTEN_NUMBER + " INTEGER, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_HOW_OFTEN_PERIOD + " INTEGER, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_COMMENCEMENT_DATE + " INTEGER, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[0] + " INTEGER, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[1] + " INTEGER, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[2] + " INTEGER, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[3] + " INTEGER, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[4] + " INTEGER, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[5] + " INTEGER, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[6] + " INTEGER, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[7] + " INTEGER, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[8] + " INTEGER, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[9] + " INTEGER, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[10] + " INTEGER, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[11] + " INTEGER, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[0] + " INTEGER, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[1] + " INTEGER, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[2] + " INTEGER, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[3] + " INTEGER, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[4] + " INTEGER, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[5] + " INTEGER, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[6] + " INTEGER, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[7] + " INTEGER, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[8] + " INTEGER, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[9] + " INTEGER, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[10] + " INTEGER, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[11] + " INTEGER, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_HOW_LONG_NUMBER + " INTEGER, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_HOW_LONG_PERIOD + " INTEGER, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_REMINDER + " INTEGER DEFAULT 0 NOT NULL, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_IS_ALLERGEN + " INTEGER DEFAULT 0 NOT NULL, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_ALLERGY_EFFECTS + " TEXT " +
                " );";

        db.execSQL(SQL_CREATE_TABLE_MEDICATION);

        ContentValues medicationValues = new ContentValues();
        medicationValues.put(WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_NAME, "AAAAA");
        medicationValues.put(WomansHealthContract.WomansHealthMedication.COLUMN_HOW_OFTEN_NUMBER, 1);
        medicationValues.put(WomansHealthContract.WomansHealthMedication.COLUMN_HOW_OFTEN_PERIOD, 1);
        db.insert(WomansHealthContract.WomansHealthMedication.TABLE_MEDICATION, null, medicationValues);
        medicationValues.put(WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_NAME, "BBBBB");
        medicationValues.put(WomansHealthContract.WomansHealthMedication.COLUMN_HOW_OFTEN_NUMBER, 2);
        medicationValues.put(WomansHealthContract.WomansHealthMedication.COLUMN_HOW_OFTEN_PERIOD, 2);
        db.insert(WomansHealthContract.WomansHealthMedication.TABLE_MEDICATION, null, medicationValues);
        medicationValues.put(WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_NAME, "CCCCC");
        medicationValues.put(WomansHealthContract.WomansHealthMedication.COLUMN_HOW_OFTEN_NUMBER, 3);
        medicationValues.put(WomansHealthContract.WomansHealthMedication.COLUMN_HOW_OFTEN_PERIOD, 3);
        db.insert(WomansHealthContract.WomansHealthMedication.TABLE_MEDICATION, null, medicationValues);
        medicationValues.put(WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_NAME, "ALLERGEN");
        medicationValues.put(WomansHealthContract.WomansHealthMedication.COLUMN_IS_ALLERGEN, 1);
        medicationValues.put(WomansHealthContract.WomansHealthMedication.COLUMN_ALLERGY_EFFECTS, "redness");
        db.insert(WomansHealthContract.WomansHealthMedication.TABLE_MEDICATION, null, medicationValues);


        final String SQL_CREATE_TABLE_PERIOD = "CREATE TABLE " + WomansHealthContract.WomansHealthPeriod.TABLE_PERIOD + " (" +
                WomansHealthContract.WomansHealthPeriod._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WomansHealthContract.WomansHealthPeriod.COLUMN_PERIOD_DATE + " INTEGER NOT NULL, " +
                WomansHealthContract.WomansHealthPeriod.COLUMN_DURATION + " INTEGER NOT NULL " +
                " );";

        db.execSQL(SQL_CREATE_TABLE_PERIOD);


        final String SQL_CREATE_TABLE_WEIGHT = "CREATE TABLE " + WomansHealthContract.WomansHealthWeight.TABLE_WEIGHT + " (" +
                WomansHealthContract.WomansHealthWeight._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WomansHealthContract.WomansHealthWeight.COLUMN_WEIGHT_DATE + " INTEGER NOT NULL, " +
                WomansHealthContract.WomansHealthWeight.COLUMN_HEIGHT + " INTEGER NOT NULL, " +
                WomansHealthContract.WomansHealthWeight.COLUMN_WEIGHT + " REAL NOT NULL " +
                " );";

        db.execSQL(SQL_CREATE_TABLE_WEIGHT);
    }

    // Enable foreign key verification - add to every dbHelper in every app!!!
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + WomansHealthContract.WomansHealthAppointment.TABLE_APPOINTMENT);
        db.execSQL("DROP TABLE IF EXISTS " + WomansHealthContract.WomansHealthMedication.TABLE_MEDICATION);
        db.execSQL("DROP TABLE IF EXISTS " + WomansHealthContract.WomansHealthPeriod.TABLE_PERIOD);
        db.execSQL("DROP TABLE IF EXISTS " + WomansHealthContract.WomansHealthWeight.TABLE_WEIGHT);
        onCreate(db);
    }


    //Appointment

    public Cursor getAppointmentsCursor() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor appointmentsCursor = db.query(
                WomansHealthContract.WomansHealthAppointment.TABLE_APPOINTMENT,
                APPOINTMENT_COLUMNS_TO_BE_BOUND_WITH_ID,
                null,
                null,
                null,
                null,
                null);

        return appointmentsCursor;
    }

    public Cursor getAppointmentIdCursor(long appointmentId) {
        String[] selectionArgs = {String.valueOf(appointmentId)};
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                WomansHealthContract.WomansHealthAppointment.TABLE_APPOINTMENT,
                APPOINTMENT_COLUMNS_TO_BE_BOUND_WITH_ID,
                WomansHealthContract.WomansHealthAppointment._ID + " = ?",
                selectionArgs,
                null,
                null,
                null);
        cursor.moveToNext();

        return cursor;
    }

    public long insertAppointment(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();

        long id = db.insert(
                WomansHealthContract.WomansHealthAppointment.TABLE_APPOINTMENT,
                null,
                values);
        db.close();
        return id;
    }

    public void updateAppointment(ContentValues values, long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = WomansHealthContract.WomansHealthAppointment._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        db.update(
                WomansHealthContract.WomansHealthAppointment.TABLE_APPOINTMENT,
                values,
                selection,
                selectionArgs);
        db.close();
    }

    public void deleteAppointment(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = WomansHealthContract.WomansHealthAppointment._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        db.delete(
                WomansHealthContract.WomansHealthAppointment.TABLE_APPOINTMENT,
                selection,
                selectionArgs);
        db.close();
    }

    public Appointment loadAppointmentDataFromDb(long id) {
        Cursor appointmentCursor = getAppointmentIdCursor(id);
        Appointment appointment = new Appointment();

        appointment.id = id;
        appointment.name = getStringFromCursor(appointmentCursor, WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_NAME);
        appointment.doctorsName = getStringFromCursor(appointmentCursor, WomansHealthContract.WomansHealthAppointment.COLUMN_DOCTOR_NAME);
        appointment.address = getStringFromCursor(appointmentCursor, WomansHealthContract.WomansHealthAppointment.COLUMN_ADDRESS);
        appointment.telephone = getStringFromCursor(appointmentCursor, WomansHealthContract.WomansHealthAppointment.COLUMN_TELEPHONE);
        appointment.email = getStringFromCursor(appointmentCursor, WomansHealthContract.WomansHealthAppointment.COLUMN_EMAIL);
        appointment.lastDate = getDateFromCursor(appointmentCursor, WomansHealthContract.WomansHealthAppointment.COLUMN_LAST_APPOINTMENT_DATE);
        appointment.nextDateSpinnerPosition = getIntegerFromCursor(appointmentCursor, WomansHealthContract.WomansHealthAppointment.COLUMN_NEXT_APPOINTMENT_SPINNER_POSITION);
        appointment.nextDate = getDateFromCursor(appointmentCursor, WomansHealthContract.WomansHealthAppointment.COLUMN_NEXT_APPOINTMENT_DATE);
        appointment.nextAppointmentHour = getIntegerFromCursor(appointmentCursor, WomansHealthContract.WomansHealthAppointment.COLUMN_NEXT_APPOINTMENT_HOUR);
        appointment.nextAppointmentMinute = getIntegerFromCursor(appointmentCursor, WomansHealthContract.WomansHealthAppointment.COLUMN_NEXT_APPOINTMENT_MINUTE);
        appointment.reminder = getBooleanFromCursor(appointmentCursor, WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_REMINDER);

        return appointment;
    }

    public ArrayList<Appointment> getAppointmentsWithReminders() {
        String[] selectionArgs = {String.valueOf(1)};
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                WomansHealthContract.WomansHealthAppointment.TABLE_APPOINTMENT,
                APPOINTMENT_COLUMNS_TO_BE_BOUND_WITH_ID,
                WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_REMINDER + " = ?",
                selectionArgs,
                null,
                null,
                null);

        ArrayList<Appointment> appointmentArrayList = new ArrayList<>();
        Appointment appointment = new Appointment();

        while (cursor.moveToNext()) {
            appointment.id = cursor.getLong(cursor.getColumnIndex(WomansHealthContract.WomansHealthAppointment._ID));
            appointment.name = getStringFromCursor(cursor, WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_NAME);
            appointment.doctorsName = getStringFromCursor(cursor, WomansHealthContract.WomansHealthAppointment.COLUMN_DOCTOR_NAME);
            appointment.address = getStringFromCursor(cursor, WomansHealthContract.WomansHealthAppointment.COLUMN_ADDRESS);
            appointment.telephone = getStringFromCursor(cursor, WomansHealthContract.WomansHealthAppointment.COLUMN_TELEPHONE);
            appointment.email = getStringFromCursor(cursor, WomansHealthContract.WomansHealthAppointment.COLUMN_EMAIL);
            appointment.lastDate = getDateFromCursor(cursor, WomansHealthContract.WomansHealthAppointment.COLUMN_LAST_APPOINTMENT_DATE);
            appointment.nextDateSpinnerPosition = getIntegerFromCursor(cursor, WomansHealthContract.WomansHealthAppointment.COLUMN_NEXT_APPOINTMENT_SPINNER_POSITION);
            appointment.nextDate = getDateFromCursor(cursor, WomansHealthContract.WomansHealthAppointment.COLUMN_NEXT_APPOINTMENT_DATE);
            appointment.nextAppointmentHour = getIntegerFromCursor(cursor, WomansHealthContract.WomansHealthAppointment.COLUMN_NEXT_APPOINTMENT_HOUR);
            appointment.nextAppointmentMinute = getIntegerFromCursor(cursor, WomansHealthContract.WomansHealthAppointment.COLUMN_NEXT_APPOINTMENT_MINUTE);
            appointment.reminder = getBooleanFromCursor(cursor, WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_REMINDER);

            appointmentArrayList.add(appointment);
        }

        return appointmentArrayList;
    }


    //Medication

    public Cursor getMedicationsCursor() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor medicationsCursor = db.query(
                WomansHealthContract.WomansHealthMedication.TABLE_MEDICATION,
                MEDICATION_COLUMNS_TO_BE_BOUND_WITH_ID,
                null,
                null,
                null,
                null,
                WomansHealthContract.WomansHealthMedication.COLUMN_IS_ALLERGEN + " ASC");

        return medicationsCursor;
    }

    public Cursor getMedicationIdCursor(long medicationId) {
        String[] selectionArgs = {String.valueOf(medicationId)};
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                WomansHealthContract.WomansHealthMedication.TABLE_MEDICATION,
                MEDICATION_COLUMNS_TO_BE_BOUND_WITH_ID,
                WomansHealthContract.WomansHealthMedication._ID + " = ?",
                selectionArgs,
                null,
                null,
                null);
        cursor.moveToNext();

        return cursor;
    }

    public long insertMedication(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();

        long id = db.insert(
                WomansHealthContract.WomansHealthMedication.TABLE_MEDICATION,
                null,
                values);
        db.close();

        return id;
    }

    public void updateMedication(ContentValues values, long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = WomansHealthContract.WomansHealthMedication._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        db.update(
                WomansHealthContract.WomansHealthMedication.TABLE_MEDICATION,
                values,
                selection,
                selectionArgs);
        db.close();
    }

    public void deleteMedication(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = WomansHealthContract.WomansHealthMedication._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        db.delete(
                WomansHealthContract.WomansHealthMedication.TABLE_MEDICATION,
                selection,
                selectionArgs);
        db.close();
    }

    public Medication loadMedicationDataFromDb(long id) {
        Cursor medicationCursor = getMedicationIdCursor(id);
        Medication medication = new Medication();

        medication.id = id;
        medication.name = getStringFromCursor(medicationCursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_NAME);
        medication.dosage = getStringFromCursor(medicationCursor, WomansHealthContract.WomansHealthMedication.COLUMN_DOSAGE);
        medication.number = getIntegerFromCursor(medicationCursor, WomansHealthContract.WomansHealthMedication.COLUMN_NUMBER);
        medication.howTaken = getIntegerFromCursor(medicationCursor, WomansHealthContract.WomansHealthMedication.COLUMN_HOW_TAKEN);
        medication.howOftenNumber = getIntegerFromCursor(medicationCursor, WomansHealthContract.WomansHealthMedication.COLUMN_HOW_OFTEN_NUMBER);
        medication.howOftenPeriod = getIntegerFromCursor(medicationCursor, WomansHealthContract.WomansHealthMedication.COLUMN_HOW_OFTEN_PERIOD);
        medication.commencementDate = getDateFromCursor(medicationCursor, WomansHealthContract.WomansHealthMedication.COLUMN_COMMENCEMENT_DATE);
        medication.medicationHourArray[0] = getIntegerFromCursor(medicationCursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[0]);
        medication.medicationHourArray[1] = getIntegerFromCursor(medicationCursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[1]);
        medication.medicationHourArray[2] = getIntegerFromCursor(medicationCursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[2]);
        medication.medicationHourArray[3] = getIntegerFromCursor(medicationCursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[3]);
        medication.medicationHourArray[4] = getIntegerFromCursor(medicationCursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[4]);
        medication.medicationHourArray[5] = getIntegerFromCursor(medicationCursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[5]);
        medication.medicationHourArray[6] = getIntegerFromCursor(medicationCursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[6]);
        medication.medicationHourArray[7] = getIntegerFromCursor(medicationCursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[7]);
        medication.medicationHourArray[8] = getIntegerFromCursor(medicationCursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[8]);
        medication.medicationHourArray[9] = getIntegerFromCursor(medicationCursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[9]);
        medication.medicationHourArray[10] = getIntegerFromCursor(medicationCursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[10]);
        medication.medicationHourArray[11] = getIntegerFromCursor(medicationCursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[11]);
        medication.medicationMinuteArray[0] = getIntegerFromCursor(medicationCursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[0]);
        medication.medicationMinuteArray[1] = getIntegerFromCursor(medicationCursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[1]);
        medication.medicationMinuteArray[2] = getIntegerFromCursor(medicationCursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[2]);
        medication.medicationMinuteArray[3] = getIntegerFromCursor(medicationCursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[3]);
        medication.medicationMinuteArray[4] = getIntegerFromCursor(medicationCursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[4]);
        medication.medicationMinuteArray[5] = getIntegerFromCursor(medicationCursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[5]);
        medication.medicationMinuteArray[6] = getIntegerFromCursor(medicationCursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[6]);
        medication.medicationMinuteArray[7] = getIntegerFromCursor(medicationCursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[7]);
        medication.medicationMinuteArray[8] = getIntegerFromCursor(medicationCursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[8]);
        medication.medicationMinuteArray[9] = getIntegerFromCursor(medicationCursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[9]);
        medication.medicationMinuteArray[10] = getIntegerFromCursor(medicationCursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[10]);
        medication.medicationMinuteArray[11] = getIntegerFromCursor(medicationCursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[11]);
        medication.howLongNumber = getIntegerFromCursor(medicationCursor, WomansHealthContract.WomansHealthMedication.COLUMN_HOW_LONG_NUMBER);
        medication.howLongPeriod = getIntegerFromCursor(medicationCursor, WomansHealthContract.WomansHealthMedication.COLUMN_HOW_LONG_PERIOD);
        medication.reminder = getBooleanFromCursor(medicationCursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_REMINDER);
        medication.isAllergen = getBooleanFromCursor(medicationCursor, WomansHealthContract.WomansHealthMedication.COLUMN_IS_ALLERGEN);
        medication.allergyEffects = getStringFromCursor(medicationCursor, WomansHealthContract.WomansHealthMedication.COLUMN_ALLERGY_EFFECTS);

        return medication;
    }


    public ArrayList<Medication> getMedicationsWithReminders() {
        String[] selectionArgs = {String.valueOf(1)};
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                WomansHealthContract.WomansHealthMedication.TABLE_MEDICATION,
                MEDICATION_COLUMNS_TO_BE_BOUND_WITH_ID,
                WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_REMINDER + " = ?",
                selectionArgs,
                null,
                null,
                null);

        ArrayList<Medication> medicationArrayList = new ArrayList<>();
        Medication medication = new Medication();

        while (cursor.moveToNext()) {
            medication.id = cursor.getLong(cursor.getColumnIndex(WomansHealthContract.WomansHealthMedication._ID));
            medication.name = getStringFromCursor(cursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_NAME);
            medication.dosage = getStringFromCursor(cursor, WomansHealthContract.WomansHealthMedication.COLUMN_DOSAGE);
            medication.number = getIntegerFromCursor(cursor, WomansHealthContract.WomansHealthMedication.COLUMN_NUMBER);
            medication.howTaken = getIntegerFromCursor(cursor, WomansHealthContract.WomansHealthMedication.COLUMN_HOW_TAKEN);
            medication.howOftenNumber = getIntegerFromCursor(cursor, WomansHealthContract.WomansHealthMedication.COLUMN_HOW_OFTEN_NUMBER);
            medication.howOftenPeriod = getIntegerFromCursor(cursor, WomansHealthContract.WomansHealthMedication.COLUMN_HOW_OFTEN_PERIOD);
            medication.commencementDate = getDateFromCursor(cursor, WomansHealthContract.WomansHealthMedication.COLUMN_COMMENCEMENT_DATE);
            medication.medicationHourArray[0] = getIntegerFromCursor(cursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[0]);
            medication.medicationHourArray[1] = getIntegerFromCursor(cursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[1]);
            medication.medicationHourArray[2] = getIntegerFromCursor(cursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[2]);
            medication.medicationHourArray[3] = getIntegerFromCursor(cursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[3]);
            medication.medicationHourArray[4] = getIntegerFromCursor(cursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[4]);
            medication.medicationHourArray[5] = getIntegerFromCursor(cursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[5]);
            medication.medicationHourArray[6] = getIntegerFromCursor(cursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[6]);
            medication.medicationHourArray[7] = getIntegerFromCursor(cursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[7]);
            medication.medicationHourArray[8] = getIntegerFromCursor(cursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[8]);
            medication.medicationHourArray[9] = getIntegerFromCursor(cursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[9]);
            medication.medicationHourArray[10] = getIntegerFromCursor(cursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[10]);
            medication.medicationHourArray[11] = getIntegerFromCursor(cursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[11]);
            medication.medicationMinuteArray[0] = getIntegerFromCursor(cursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[0]);
            medication.medicationMinuteArray[1] = getIntegerFromCursor(cursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[1]);
            medication.medicationMinuteArray[2] = getIntegerFromCursor(cursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[2]);
            medication.medicationMinuteArray[3] = getIntegerFromCursor(cursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[3]);
            medication.medicationMinuteArray[4] = getIntegerFromCursor(cursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[4]);
            medication.medicationMinuteArray[5] = getIntegerFromCursor(cursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[5]);
            medication.medicationMinuteArray[6] = getIntegerFromCursor(cursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[6]);
            medication.medicationMinuteArray[7] = getIntegerFromCursor(cursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[7]);
            medication.medicationMinuteArray[8] = getIntegerFromCursor(cursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[8]);
            medication.medicationMinuteArray[9] = getIntegerFromCursor(cursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[9]);
            medication.medicationMinuteArray[10] = getIntegerFromCursor(cursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[10]);
            medication.medicationMinuteArray[11] = getIntegerFromCursor(cursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[11]);
            medication.howLongNumber = getIntegerFromCursor(cursor, WomansHealthContract.WomansHealthMedication.COLUMN_HOW_LONG_NUMBER);
            medication.howLongPeriod = getIntegerFromCursor(cursor, WomansHealthContract.WomansHealthMedication.COLUMN_HOW_LONG_PERIOD);
            medication.reminder = getBooleanFromCursor(cursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_REMINDER);
            medication.isAllergen = getBooleanFromCursor(cursor, WomansHealthContract.WomansHealthMedication.COLUMN_IS_ALLERGEN);
            medication.allergyEffects = getStringFromCursor(cursor, WomansHealthContract.WomansHealthMedication.COLUMN_ALLERGY_EFFECTS);

            medicationArrayList.add(medication);
        }

        return  medicationArrayList;
    }



    //Weight

    public int getWeightsCount(Date date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Long timestamp = date.getTime(); // We store dates as timestamps (int as a number of seconds since 1970-01-01) in db column
        String[] selectionArgs = {String.valueOf(timestamp)};

        Cursor weightCursor = db.query(
                WomansHealthContract.WomansHealthWeight.TABLE_WEIGHT,
                WEIGHT_COLUMNS_TO_BE_BOUND_WITH_ID,
                WomansHealthContract.WomansHealthWeight.COLUMN_WEIGHT_DATE + " = ?",
                selectionArgs,
                null,
                null,
                null);

        return weightCursor.getCount();
    }

    public int getAllWeightsCount() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor weightCursor = db.query(
                WomansHealthContract.WomansHealthWeight.TABLE_WEIGHT,
                WEIGHT_COLUMNS_TO_BE_BOUND_WITH_ID,
                null,
                null,
                null,
                null,
                null);

        return weightCursor.getCount();
    }

    public void insertWeight(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(
                WomansHealthContract.WomansHealthWeight.TABLE_WEIGHT,
                null,
                values);
        db.close();
    }

    public void updateWeight(ContentValues values, Date date) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = WomansHealthContract.WomansHealthWeight.COLUMN_WEIGHT_DATE + " = ?";
        String[] selectionArgs = {String.valueOf(date.getTime())};

        db.update(
                WomansHealthContract.WomansHealthWeight.TABLE_WEIGHT,
                values,
                selection,
                selectionArgs);
        db.close();
    }

    public Cursor getWeightsCursor() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor weightsCursor = db.query(
                WomansHealthContract.WomansHealthWeight.TABLE_WEIGHT,
                WEIGHT_COLUMNS_TO_BE_BOUND_WITH_ID,
                null,
                null,
                null,
                null,
                WomansHealthContract.WomansHealthWeight.COLUMN_WEIGHT_DATE + " DESC");

        return weightsCursor;
    }

    public void deleteWeight(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = WomansHealthContract.WomansHealthWeight._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        db.delete(
                WomansHealthContract.WomansHealthWeight.TABLE_WEIGHT,
                selection,
                selectionArgs);
        db.close();
    }


    //Period

    public int getPeriodsCount(Date date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Long timestamp = date.getTime(); // We store dates as timestamps (int as a number of seconds since 1970-01-01) in db column
        String[] selectionArgs = {String.valueOf(timestamp)};

        Cursor periodCursor = db.query(
                WomansHealthContract.WomansHealthPeriod.TABLE_PERIOD,
                PERIOD_COLUMNS_TO_BE_BOUND_WITH_ID,
                WomansHealthContract.WomansHealthPeriod.COLUMN_PERIOD_DATE + " = ?",
                selectionArgs,
                null,
                null,
                null);

        return periodCursor.getCount();
    }

    public int getAllPeriodsCount() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor periodCursor = db.query(
                WomansHealthContract.WomansHealthPeriod.TABLE_PERIOD,
                PERIOD_COLUMNS_TO_BE_BOUND_WITH_ID,
                null,
                null,
                null,
                null,
                null);

        return periodCursor.getCount();
    }

    public void insertPeriod(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(
                WomansHealthContract.WomansHealthPeriod.TABLE_PERIOD,
                null,
                values);
        db.close();
    }

    public void updatePeriod(ContentValues values, Date date) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = WomansHealthContract.WomansHealthPeriod.COLUMN_PERIOD_DATE + " = ?";
        String[] selectionArgs = {String.valueOf(date.getTime())};

        db.update(
                WomansHealthContract.WomansHealthPeriod.TABLE_PERIOD,
                values,
                selection,
                selectionArgs);
        db.close();
    }

    public Cursor getPeriodsCursor() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor periodsCursor = db.query(
                WomansHealthContract.WomansHealthPeriod.TABLE_PERIOD,
                PERIOD_COLUMNS_TO_BE_BOUND_WITH_ID,
                null,
                null,
                null,
                null,
                WomansHealthContract.WomansHealthPeriod.COLUMN_PERIOD_DATE);

        return periodsCursor;
    }

    public void deletePeriod(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = WomansHealthContract.WomansHealthPeriod._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        db.delete(
                WomansHealthContract.WomansHealthPeriod.TABLE_PERIOD,
                selection,
                selectionArgs);
        db.close();
    }

    public ArrayList<Period> getPeriods(Cursor periodCursor) {
        ArrayList<Period> periods = new ArrayList<>();
        Long oldTimestamp = null;

        while (periodCursor.moveToNext()) {
            Long id = getLongFromCursor(periodCursor, WomansHealthContract.WomansHealthPeriod._ID);
            Long timestamp = getLongFromCursor(periodCursor, WomansHealthContract.WomansHealthPeriod.COLUMN_PERIOD_DATE);
            Integer duration = getIntegerFromCursor(periodCursor, WomansHealthContract.WomansHealthPeriod.COLUMN_DURATION);
            Integer intervalInDays = null;

            if (oldTimestamp != null) {
                intervalInDays = (int) Math.abs((timestamp - oldTimestamp) / (24 * 60 * 60 * 1000));
            }

            periods.add(new Period(id, timestamp, duration, intervalInDays));

            oldTimestamp = timestamp;
        }

        Collections.reverse(periods);

        return periods;
    }

    public String getStringFromCursor(Cursor cursor, String columnName) {
        String value;
        if (!cursor.isNull(cursor.getColumnIndex(columnName))) {
            value = cursor.getString(cursor.getColumnIndex(columnName));
        } else {
            value = null;
        }
        return value;
    }


    public Integer getIntegerFromCursor(Cursor cursor, String columnName) {
        Integer value;

        if (!cursor.isNull(cursor.getColumnIndex(columnName))) {
            value = cursor.getInt(cursor.getColumnIndex(columnName));
        } else {
            value = null;
        }
        return value;
    }

    public Long getLongFromCursor(Cursor cursor, String columnName) {
        Long value;

        if (!cursor.isNull(cursor.getColumnIndex(columnName))) {
            value = cursor.getLong(cursor.getColumnIndex(columnName));
        } else {
            value = null;
        }
        return value;
    }

    private Boolean getBooleanFromCursor(Cursor cursor, String columnName) {
        Boolean value;

        if (!cursor.isNull(cursor.getColumnIndex(columnName))) {
            int intValue = cursor.getInt(cursor.getColumnIndex(columnName));

            if (intValue == 1) {
                value = true;
            } else {
                value = false;
            }

        } else {
            value = null;
        }
        return value;
    }

    private Date getDateFromCursor(Cursor cursor, String columnName) {
        Date value;

        if (!cursor.isNull(cursor.getColumnIndex(columnName))) {
            Long longValue = cursor.getLong(cursor.getColumnIndex(columnName));

            if (longValue != null) {
                value = new Date(longValue);
            } else {
                value = null;
            }

        } else {
            value = null;
        }
        return value;
    }
}