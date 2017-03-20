package com.blashca.womanshealth;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.blashca.womanshealth.models.Appointment;
import com.blashca.womanshealth.models.Medication;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class AlarmHelper {
    private final static String FIRST_APPOINTMENT_NOTIFICATION = "com.blashca.womanshealth.FIRST_APPOINTMENT_NOTIFICATION";
    private final static String SECOND_APPOINTMENT_NOTIFICATION = "com.blashca.womanshealth.SECOND_APPOINTMENT_NOTIFICATION";
    private final static int NOTIFICATION_24_HOURS = 24 * 60 * 60 * 1000;
    private final static int NOTIFICATION_1_HOUR = 60 * 60 * 1000;
    private final static int NOTIFICATION_OFFSET = 1000000;
    private final static String DAILY_MEDICATION_NOTIFICATIONS = "com.blashca.womanshealth.DAILY_MEDICATION_NOTIFICATIONS";
    private final static String WEEKLY_MEDICATION_NOTIFICATIONS = "com.blashca.womanshealth.WEEKLY_MEDICATION_NOTIFICATIONS";
    private final static String MONTHLY_MEDICATION_NOTIFICATIONS = "com.blashca.womanshealth.MONTHLY_MEDICATION_NOTIFICATIONS";
    private final static String YEARLY_MEDICATION_NOTIFICATIONS = "com.blashca.womanshealth.YEARLY_MEDICATION_NOTIFICATIONS";

    private static String APPOINTMENT_ID = "appointmentId";
    private static String MEDICATION_ID = "medicationId";
    private static String ALARM_ID = "alarmId";


    private void setAlarm(Context context, Intent intent, long timeInMillis, long alarmId) {
        Calendar calendar = Calendar.getInstance();

        if (timeInMillis >= calendar.getTimeInMillis()) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) alarmId, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
        }
    }

    private void setAlarm(Context context, Intent intent, long timeInMillis, long alarmId, long endTime) {
        Calendar calendar = Calendar.getInstance();

        if (timeInMillis >= calendar.getTimeInMillis() && timeInMillis <= endTime) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) alarmId, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
        }
    }

    public void setAppointmentAlarms(Context context, Appointment appointment) {
        long secondAlarm;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(appointment.nextDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = 12;
        int minute = 0;

        calendar.clear();
        calendar.set(year, month, day, hour, minute, 0);

        long firstAlarm = calendar.getTimeInMillis() - NOTIFICATION_24_HOURS; // first alarm is shown at noon 24 hours before the event
        calendar.clear();

        if (appointment.nextAppointmentHour != null) {
            hour = appointment.nextAppointmentHour;
            minute = appointment.nextAppointmentMinute;

            calendar.set(year, month, day, hour, minute, 0);
            secondAlarm = calendar.getTimeInMillis() - NOTIFICATION_1_HOUR; // second alarm is shown an hour before the event

        } else {
            hour = 8;
            minute = 0;

            calendar.set(year, month, day, hour, minute, 0);
            secondAlarm = calendar.getTimeInMillis() - NOTIFICATION_24_HOURS; // second alarm is shown at 8.00 on the date of the event
        }

        setFirstAppointmentAlarm(context, appointment, firstAlarm);
        setSecondAppointmentAlarm(context, appointment, secondAlarm);
    }

    private void setFirstAppointmentAlarm(Context context, Appointment appointment, long alarmTime) {
        Intent intent = new Intent(FIRST_APPOINTMENT_NOTIFICATION);
        intent.putExtra(APPOINTMENT_ID, appointment.id);
        setAlarm(context, intent, alarmTime, appointment.getAlarmId());
    }

    private void setSecondAppointmentAlarm(Context context, Appointment appointment, long alarmTime) {
        Intent intent = new Intent(SECOND_APPOINTMENT_NOTIFICATION);
        intent.putExtra(APPOINTMENT_ID, appointment.id);
        setAlarm(context, intent, alarmTime, appointment.getAlarmId() + NOTIFICATION_OFFSET);
    }

    public void setMedicationAlarms(Context context, Medication medication) {
        Calendar calendar = getCalendarWithMedicationCommencementDate(medication, 12);

        String[] frequencyArray = context.getResources().getStringArray(R.array.frequency_array);
        if (frequencyArray[medication.howOftenPeriod] == frequencyArray[0]){
            for (int i = 0; i < medication.howOftenNumber; i++) {
                if (medication.medicationHourArray[i] != null) {
                    int hour = medication.medicationHourArray[i];
                    int minute = medication.medicationMinuteArray[i];

                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minute);

                    setDailyMedicationAlarm(context, medication, calendar.getTimeInMillis(), medication.getAlarmId(i));
                }
            }
        } else if (frequencyArray[medication.howOftenPeriod] == frequencyArray[1] && medication.howOftenNumber == 1) {
            setWeeklyMedicationAlarm(context, medication, getMedicationAlarmStartTime(medication, Calendar.DAY_OF_YEAR, 7), medication.id);

        } else if (frequencyArray[medication.howOftenPeriod] == frequencyArray[2] && medication.howOftenNumber == 1) {
            setMonthlyMedicationAlarm(context, medication, getMedicationAlarmStartTime(medication, Calendar.MONTH, 1), medication.id);

        } else if (frequencyArray[medication.howOftenPeriod] == frequencyArray[3] && medication.howOftenNumber == 1) {
            setYearlyMedicationAlarm(context, medication, getMedicationAlarmStartTime(medication, Calendar.YEAR, 1), medication.id);
        }
    }

    public void setDailyMedicationAlarm(Context context, Medication medication, long alarmTime, long alarmId) {
        Intent intent = new Intent(DAILY_MEDICATION_NOTIFICATIONS);
        intent.putExtra(MEDICATION_ID, medication.id);
        intent.putExtra(ALARM_ID, alarmId);
        setAlarm(context, intent, alarmTime, alarmId, getMedicationAlarmEndTime(medication));
    }

    public void setWeeklyMedicationAlarm(Context context, Medication medication, long alarmTime, long alarmId) {
        Intent intent = new Intent(WEEKLY_MEDICATION_NOTIFICATIONS);
        intent.putExtra(MEDICATION_ID, medication.id);
        intent.putExtra(ALARM_ID, alarmId);
        setAlarm(context, intent, alarmTime, alarmId, getMedicationAlarmEndTime(medication));
    }

    public void setMonthlyMedicationAlarm(Context context, Medication medication, long alarmTime, long alarmId) {
        Intent intent = new Intent(MONTHLY_MEDICATION_NOTIFICATIONS);
        intent.putExtra(MEDICATION_ID, medication.id);
        intent.putExtra(ALARM_ID, alarmId);
        setAlarm(context, intent, alarmTime, alarmId, getMedicationAlarmEndTime(medication));
    }

    public void setYearlyMedicationAlarm(Context context, Medication medication, long alarmTime, long alarmId) {
        Intent intent = new Intent(YEARLY_MEDICATION_NOTIFICATIONS);
        intent.putExtra(MEDICATION_ID, medication.id);
        intent.putExtra(ALARM_ID, alarmId);
        setAlarm(context, intent, alarmTime, alarmId, getMedicationAlarmEndTime(medication));
    }

    private void cancelAlarm(Context context, String intentFilter, long alarmId) {
        Intent intent = new Intent(intentFilter);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) alarmId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public void cancelFirstAppointmentAlarm(Context context, Appointment appointment) {
        cancelAlarm(context, FIRST_APPOINTMENT_NOTIFICATION, appointment.getAlarmId());
    }

    public void cancelSecondAppointmentAlarm(Context context, Appointment appointment) {
        cancelAlarm(context, SECOND_APPOINTMENT_NOTIFICATION, appointment.getAlarmId() + NOTIFICATION_OFFSET);
    }

    public void cancelDailyMedicationAlarms(Context context, Medication medication) {
        if (medication.howOftenNumber != null) {
            for (int i = 0; i < medication.howOftenNumber; i++) {
                cancelAlarm(context, DAILY_MEDICATION_NOTIFICATIONS, medication.getAlarmId(i));
            }
        }
    }

    public void cancelWeeklyMedicationAlarm(Context context, Medication medication) {
        cancelAlarm(context, WEEKLY_MEDICATION_NOTIFICATIONS, medication.id);
    }

    public void cancelMonthlyMedicationAlarm(Context context, Medication medication) {
        cancelAlarm(context, MONTHLY_MEDICATION_NOTIFICATIONS, medication.id);
    }

    public void cancelYearlyMedicationAlarm(Context context, Medication medication) {
        cancelAlarm(context, YEARLY_MEDICATION_NOTIFICATIONS, medication.id);
    }


    private static Calendar getCalendarWithMedicationCommencementDate(Medication medication, int hourOfDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(medication.commencementDate);
        calendar.set(Calendar.SECOND, 0);
        Integer hour = medication.medicationHourArray[0];

        if (hour != null) {
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, medication.medicationMinuteArray[0]);
        } else {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, 0);
        }
        return calendar;
    }


    private long getMedicationAlarmStartTime(Medication medication, int field, int value) {
        Calendar calendar = Calendar.getInstance();
        Calendar calendarWithCommencementDate = getCalendarWithMedicationCommencementDate(medication, 12);

        long alarmStartTimeInMillis = 0;

        while (alarmStartTimeInMillis < calendar.getTimeInMillis()) {
            calendarWithCommencementDate.add(field, value);
            alarmStartTimeInMillis = calendarWithCommencementDate.getTimeInMillis();
        }

        return alarmStartTimeInMillis;
    }

    private long getMedicationAlarmEndTime(Medication medication) {
        Calendar calendar = getCalendarWithMedicationCommencementDate(medication, 0);

        if (medication.howLongNumber != null) {
            if (medication.howLongPeriod == 0) {
                calendar.add(Calendar.DAY_OF_YEAR, medication.howLongNumber);
            } else if (medication.howLongPeriod == 1) {
                calendar.add(Calendar.DAY_OF_YEAR, medication.howLongNumber * 7);
            } else if (medication.howLongPeriod == 2) {
                calendar.add(Calendar.MONTH, medication.howLongNumber);
            } else if (medication.howLongPeriod == 3) {
                calendar.add(Calendar.YEAR, medication.howLongNumber);
            }

            return calendar.getTimeInMillis();
        } else {
            return Long.MAX_VALUE;
        }
    }
}
