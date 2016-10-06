package com.blashca.womanshealth.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

import static com.blashca.womanshealth.R.string.date;


public class WomansHealthDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "womanshealth.db";
    Context context;

    private static final String[] APPOINTMENT_COLUMNS_TO_BE_BOUND_WITH_ID = new String[]{
            WomansHealthContract.WomansHealthAppointment._ID,
            WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_NAME,
            WomansHealthContract.WomansHealthAppointment.COLUMN_DOCTOR_NAME,
            WomansHealthContract.WomansHealthAppointment.COLUMN_ADDRESS,
            WomansHealthContract.WomansHealthAppointment.COLUMN_TELEPHONE1,
            WomansHealthContract.WomansHealthAppointment.COLUMN_TELEPHONE2,
            WomansHealthContract.WomansHealthAppointment.COLUMN_EMAIL,
            WomansHealthContract.WomansHealthAppointment.COLUMN_LAST_APPOINTMENT,
            WomansHealthContract.WomansHealthAppointment.COLUMN_NEXT_APPOINTMENT,
            WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_TIME,
            WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_REMINDER
    };

    private static final String[] MEDICATION_COLUMNS_TO_BE_BOUND_WITH_ID = new String[]{
            WomansHealthContract.WomansHealthMedication._ID,
            WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_NAME,
            WomansHealthContract.WomansHealthMedication.COLUMN_DOSAGE,
            WomansHealthContract.WomansHealthMedication.COLUMN_NUMBER,
            WomansHealthContract.WomansHealthMedication.COLUMN_HOW_TAKEN,
            WomansHealthContract.WomansHealthMedication.COLUMN_HOW_OFTEN,
            WomansHealthContract.WomansHealthMedication.COLUMN_COMMENCEMENT,
            WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_TIME,
            WomansHealthContract.WomansHealthMedication.COLUMN_HOW_LONG,
            WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_REMINDER,
            WomansHealthContract.WomansHealthMedication.COLUMN_IS_ALLERGEN,
            WomansHealthContract.WomansHealthMedication.COLUMN_ALLERGIES_EFFECTS
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
                WomansHealthContract.WomansHealthAppointment.COLUMN_TELEPHONE1 + " TEXT, " +
                WomansHealthContract.WomansHealthAppointment.COLUMN_TELEPHONE2 + " TEXT, " +
                WomansHealthContract.WomansHealthAppointment.COLUMN_EMAIL + " TEXT, " +
                WomansHealthContract.WomansHealthAppointment.COLUMN_LAST_APPOINTMENT + " TEXT, " +
                WomansHealthContract.WomansHealthAppointment.COLUMN_NEXT_APPOINTMENT + " TEXT, " +
                WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_TIME + " TEXT, " +
                WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_REMINDER + " INTEGER DEFAULT 0 NOT NULL " +
                " );";

        db.execSQL(SQL_CREATE_TABLE_APPOINTMENT);

        ContentValues appointmentValues = new ContentValues();
        appointmentValues.put(WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_NAME, "Appointment1");
        appointmentValues.put(WomansHealthContract.WomansHealthAppointment.COLUMN_NEXT_APPOINTMENT, "01.01.2016");
        appointmentValues.put(WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_TIME, "10:00");
        db.insert(WomansHealthContract.WomansHealthAppointment.TABLE_APPOINTMENT, null, appointmentValues);
        appointmentValues.put(WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_NAME, "Appointment2");
        appointmentValues.put(WomansHealthContract.WomansHealthAppointment.COLUMN_NEXT_APPOINTMENT, "02.01.2016");
        appointmentValues.put(WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_TIME, "11:00");
        db.insert(WomansHealthContract.WomansHealthAppointment.TABLE_APPOINTMENT, null, appointmentValues);
        appointmentValues.put(WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_NAME, "Appointment3");
        appointmentValues.put(WomansHealthContract.WomansHealthAppointment.COLUMN_NEXT_APPOINTMENT, "03.01.2016");
        appointmentValues.put(WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_TIME, "12:00");
        db.insert(WomansHealthContract.WomansHealthAppointment.TABLE_APPOINTMENT, null, appointmentValues);


        final String SQL_CREATE_TABLE_MEDICATION = "CREATE TABLE " + WomansHealthContract.WomansHealthMedication.TABLE_MEDICATION + " (" +
                WomansHealthContract.WomansHealthMedication._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_NAME + " TEXT NOT NULL, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_DOSAGE + " TEXT, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_NUMBER + " INTEGER, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_HOW_TAKEN + " INTEGER, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_HOW_OFTEN + " TEXT, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_COMMENCEMENT + " TEXT, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_TIME + " TEXT, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_HOW_LONG + " TEXT, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_REMINDER + " INTEGER DEFAULT 0 NOT NULL, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_IS_ALLERGEN + " INTEGER DEFAULT 0 NOT NULL, " +
                WomansHealthContract.WomansHealthMedication.COLUMN_ALLERGIES_EFFECTS + " TEXT " +
                " );";

        db.execSQL(SQL_CREATE_TABLE_MEDICATION);

        ContentValues medicationValues = new ContentValues();
        medicationValues.put(WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_NAME, "AAAAA");
        medicationValues.put(WomansHealthContract.WomansHealthMedication.COLUMN_HOW_OFTEN, "0");
        db.insert(WomansHealthContract.WomansHealthMedication.TABLE_MEDICATION, null, medicationValues);
        medicationValues.put(WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_NAME, "BBBBB");
        medicationValues.put(WomansHealthContract.WomansHealthMedication.COLUMN_HOW_OFTEN, "1");
        db.insert(WomansHealthContract.WomansHealthMedication.TABLE_MEDICATION, null, medicationValues);
        medicationValues.put(WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_NAME, "CCCCC");
        medicationValues.put(WomansHealthContract.WomansHealthMedication.COLUMN_HOW_OFTEN, "2");
        db.insert(WomansHealthContract.WomansHealthMedication.TABLE_MEDICATION, null, medicationValues);
        medicationValues.put(WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_NAME, "ALLERGEN");
        medicationValues.put(WomansHealthContract.WomansHealthMedication.COLUMN_IS_ALLERGEN, 1);
        medicationValues.put(WomansHealthContract.WomansHealthMedication.COLUMN_ALLERGIES_EFFECTS, "redness");
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


    public void insertAppointment(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(
                WomansHealthContract.WomansHealthAppointment.TABLE_APPOINTMENT,
                null,
                values);
        db.close();

    }

    public void insertMedication(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(
                WomansHealthContract.WomansHealthMedication.TABLE_MEDICATION,
                null,
                values);
        db.close();
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
}
