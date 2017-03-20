package com.blashca.womanshealth;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.blashca.womanshealth.data.WomansHealthDbHelper;
import com.blashca.womanshealth.models.Appointment;
import com.blashca.womanshealth.models.Medication;

import java.util.ArrayList;
import java.util.Calendar;


public class AlarmBroadcastReceiver extends BroadcastReceiver {
    private final static String FIRST_APPOINTMENT_NOTIFICATION = "com.blashca.womanshealth.FIRST_APPOINTMENT_NOTIFICATION";
    private final static String SECOND_APPOINTMENT_NOTIFICATION = "com.blashca.womanshealth.SECOND_APPOINTMENT_NOTIFICATION";
    private final static String DAILY_MEDICATION_NOTIFICATIONS = "com.blashca.womanshealth.DAILY_MEDICATION_NOTIFICATIONS";
    private final static String WEEKLY_MEDICATION_NOTIFICATIONS = "com.blashca.womanshealth.WEEKLY_MEDICATION_NOTIFICATIONS";
    private final static String MONTHLY_MEDICATION_NOTIFICATIONS = "com.blashca.womanshealth.MONTHLY_MEDICATION_NOTIFICATIONS";
    private final static String YEARLY_MEDICATION_NOTIFICATIONS = "com.blashca.womanshealth.YEARLY_MEDICATION_NOTIFICATIONS";
    private final static String ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";

    private static String APPOINTMENT_ID = "appointmentId";
    private static String MEDICATION_ID = "medicationId";
    private static String ALARM_ID = "alarmId";

    private long medicationId;
    private AlarmHelper alarmHelper = new AlarmHelper();


    @Override
    public void onReceive(Context context, Intent intent) {
        medicationId = intent.getLongExtra(MEDICATION_ID, -1);
        WomansHealthDbHelper dbHelper = new WomansHealthDbHelper(context);

        try {
            if (intent.getAction().equals(FIRST_APPOINTMENT_NOTIFICATION)) {
                long appointmentId = intent.getLongExtra(APPOINTMENT_ID, -1);

                String appointmentName = dbHelper.loadAppointment(appointmentId).name;
                String messageFormat = context.getResources().getString(R.string.appointment_tomorrow);
                String message = String.format(messageFormat, appointmentName);
                int icon = R.drawable.ic_heart;
                showNotification(context.getResources().getString(R.string.app_name), message, context, appointmentId, icon);

            } else if (intent.getAction().equals(SECOND_APPOINTMENT_NOTIFICATION)) {
                long appointmentId = intent.getLongExtra(APPOINTMENT_ID, -1);

                String appointmentName = dbHelper.loadAppointment(appointmentId).name;
                String messageFormat = context.getResources().getString(R.string.appointment_today);
                String message = String.format(messageFormat, appointmentName);
                int icon = R.drawable.ic_heart;
                showNotification(context.getResources().getString(R.string.app_name), message, context, appointmentId, icon);

            } else if (intent.getAction().equals(DAILY_MEDICATION_NOTIFICATIONS)) {
                long alarmId = intent.getLongExtra(ALARM_ID, -1);
                Medication medication = dbHelper.loadMedication(medicationId);
                int icon = R.drawable.ic_pill;

                showNotification(context.getResources().getString(R.string.app_name),
                        context.getResources().getString(R.string.take_medicine) + " " + medication.name, context, medicationId, icon);
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                alarmHelper.setDailyMedicationAlarm(context, medication, calendar.getTimeInMillis(), alarmId);

            } else if (intent.getAction().equals(WEEKLY_MEDICATION_NOTIFICATIONS)) {
                Medication medication = dbHelper.loadMedication(medicationId);
                int icon = R.drawable.ic_pill;

                showNotification(context.getResources().getString(R.string.app_name),
                        context.getResources().getString(R.string.take_medicine) + " " + medication.name, context, medicationId, icon);
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, 7);
                alarmHelper.setWeeklyMedicationAlarm(context, medication, calendar.getTimeInMillis(), medicationId);

            } else if (intent.getAction().equals(MONTHLY_MEDICATION_NOTIFICATIONS)) {
                Medication medication = dbHelper.loadMedication(medicationId);
                int icon = R.drawable.ic_pill;

                showNotification(context.getResources().getString(R.string.app_name),
                        context.getResources().getString(R.string.take_medicine) + " " + medication.name, context, medicationId, icon);
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MONTH, 1);
                alarmHelper.setMonthlyMedicationAlarm(context, medication, calendar.getTimeInMillis(), medicationId);

            } else if (intent.getAction().equals(YEARLY_MEDICATION_NOTIFICATIONS)) {
                Medication medication = dbHelper.loadMedication(medicationId);
                int icon = R.drawable.ic_pill;

                showNotification(context.getResources().getString(R.string.app_name),
                        context.getResources().getString(R.string.take_medicine) + " " + medication.name, context, medicationId, icon);
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.YEAR, 1);
                alarmHelper.setYearlyMedicationAlarm(context, medication, calendar.getTimeInMillis(), medicationId);

            } else if (intent.getAction().equals(ACTION_BOOT_COMPLETED)) {
                ArrayList<Appointment> appointmentArrayList = dbHelper.getAppointmentsWithReminders();

                for (int i = 0; i < appointmentArrayList.size(); i++) {
                    Appointment appointment = appointmentArrayList.get(i);
                    alarmHelper.setAppointmentAlarms(context, appointment);
                }

                ArrayList<Medication> medicationArrayList = dbHelper.getMedicationsWithReminders();

                for (int i = 0; i < medicationArrayList.size(); i++) {
                    Medication medication = medicationArrayList.get(i);
                    alarmHelper.setMedicationAlarms(context, medication);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void showNotification(String title, String string, Context context, long notificationId, int iconId) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setSmallIcon(iconId);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        notificationBuilder.setWhen(0);
        notificationBuilder.setContentText(string);
        notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
        getNotificationManager(context).notify((int) notificationId, notificationBuilder.build());
    }

    private NotificationManager getNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }
}