package com.blashca.womanshealth.activities;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blashca.womanshealth.AlarmHelper;
import com.blashca.womanshealth.R;
import com.blashca.womanshealth.data.WomansHealthDbHelper;
import com.blashca.womanshealth.models.Appointment;

import java.text.DateFormat;

public class AppointmentDetailActivity extends AppCompatActivity {
    private final static String FIRST_APPOINTMENT_NOTIFICATION = "com.blashca.womanshealth.FIRST_APPOINTMENT_NOTIFICATION";
    private final static String SECOND_APPOINTMENT_NOTIFICATION = "com.blashca.womanshealth.SECOND_APPOINTMENT_NOTIFICATION";
    private final static int NOTIFICATION_OFFSET = 1000000;

    private static String APPOINTMENT_ID = "appointmentId";
    private long appointmentId;
    private WomansHealthDbHelper dbHelper;
    private DateFormat dateFormat;

    private Appointment appointment;
    private AlarmHelper alarmHelper = new AlarmHelper();

    private TextView appointmentNameTextView;
    private TextView doctorsNameTextView;
    private TextView addressTextView;
    private TextView telephoneTextView;
    private TextView emailTextView;
    private TextView lastDateTextView;
    private TextView nextDateTextView;
    private TextView appointmentReminderTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_detail);

        dbHelper = new WomansHealthDbHelper(this);
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG);

        if (getIntent().getExtras() != null) {
            appointmentId = getIntent().getExtras().getLong(APPOINTMENT_ID);
        }
        appointment = dbHelper.loadAppointmentDataFromDb(appointmentId);

        appointmentNameTextView = (TextView) findViewById(R.id.appointment_name_textView);
        doctorsNameTextView = (TextView) findViewById(R.id.doctors_name_textView);
        addressTextView = (TextView) findViewById(R.id.address_textView);
        telephoneTextView = (TextView) findViewById(R.id.telephone_number_textView);
        emailTextView = (TextView) findViewById(R.id.email_textView);
        lastDateTextView = (TextView) findViewById(R.id.last_date_textView);
        nextDateTextView = (TextView) findViewById(R.id.next_date_textView);
        appointmentReminderTextView = (TextView) findViewById(R.id.appointment_reminder_textView);

        refreshUI();
    }

    public void onDeleteAppointmentButtonClicked(final View v) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set dialog message
        alertDialogBuilder
                .setMessage(R.string.delete_appointment_message)
                .setCancelable(false)
                .setPositiveButton(R.string.delete,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, do that
                        alarmHelper.cancelAlarm(v.getContext(), FIRST_APPOINTMENT_NOTIFICATION, appointment.id);
                        alarmHelper.cancelAlarm(v.getContext(), SECOND_APPOINTMENT_NOTIFICATION, appointment.id + NOTIFICATION_OFFSET);

                        dbHelper.deleteAppointment(appointmentId);

                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                })
                .setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    public void onEditAppointmentButtonClicked(View v) {
        Intent intent = new Intent(this, AppointmentEditActivity.class);
        intent.putExtra(APPOINTMENT_ID, appointmentId);
        startActivityForResult(intent, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == AppointmentEditActivity.RESULT_OK){
                appointment = dbHelper.loadAppointmentDataFromDb(appointmentId);
                refreshUI();
            }
        }
    }

    private void refreshUI() {
        appointmentNameTextView.setText(appointment.name);

        LinearLayout doctorsNameLayout = (LinearLayout) findViewById(R.id.doctors_name_linearLayout);

        if (appointment.doctorsName != null) {
            doctorsNameTextView.setText(appointment.doctorsName);
            doctorsNameLayout.setVisibility(View.VISIBLE);
        } else {
            doctorsNameLayout.setVisibility(View.GONE);
        }

        LinearLayout addressLayout = (LinearLayout) findViewById(R.id.address_linearLayout);

        if (appointment.address != null) {
            addressTextView.setText(appointment.address);
            addressLayout.setVisibility(View.VISIBLE);
        } else {
            addressLayout.setVisibility(View.GONE);
        }

        LinearLayout telephoneLayout = (LinearLayout) findViewById(R.id.telephone_number_linearLayout);

        if (appointment.telephone != null) {
            telephoneTextView.setText(appointment.telephone);
            telephoneLayout.setVisibility(View.VISIBLE);
        } else {
            telephoneLayout.setVisibility(View.GONE);
        }

        LinearLayout emailLayout = (LinearLayout) findViewById(R.id.email_linearLayout);

        if (appointment.email != null) {
            emailTextView.setText(appointment.email);
            emailLayout.setVisibility(View.VISIBLE);
        } else {
            emailLayout.setVisibility(View.GONE);
        }

        LinearLayout lastDateLayout = (LinearLayout) findViewById(R.id.last_date_linearLayout);

        if (appointment.lastDate != null) {
            lastDateTextView.setText(dateFormat.format(appointment.lastDate));
            lastDateLayout.setVisibility(View.VISIBLE);
        } else {
            lastDateLayout.setVisibility(View.GONE);
        }

        LinearLayout nextDateLayout = (LinearLayout) findViewById(R.id.next_date_linearLayout);

        if (appointment.nextDate != null) {
            nextDateTextView.setText(dateFormat.format(appointment.nextDate) + " " + appointment.getAppointmentTime());
            nextDateLayout.setVisibility(View.VISIBLE);
        } else {
            nextDateLayout.setVisibility(View.GONE);
        }

        if (appointment.reminder) {
            appointmentReminderTextView.setText(R.string.on);
        } else {
            appointmentReminderTextView.setText(R.string.off);
        }
    }
}
