package com.blashca.womanshealth;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.blashca.womanshealth.data.WomansHealthContract;
import com.blashca.womanshealth.data.WomansHealthDbHelper;

public class AppointmentDetailActivity extends AppCompatActivity {
    private static String APPOINTMENT_ID = "appointmentId";
    private long appointmentId;
    private WomansHealthDbHelper dbHelper;
    private TextView appointmentName;
    private TextView doctorsName;
    private TextView address;
    private TextView telephone1;
    private TextView telephone2;
    private TextView email;
    private TextView lastDate;
    private TextView nextDate;
    private TextView appointmentReminder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_detail);

        appointmentId = getIntent().getExtras().getLong(APPOINTMENT_ID);

        dbHelper = new WomansHealthDbHelper(this);

        appointmentName = (TextView) findViewById(R.id.appointment_name_textView);
        doctorsName = (TextView) findViewById(R.id.doctors_name_textView);
        address = (TextView) findViewById(R.id.address_textView);
        telephone1 = (TextView) findViewById(R.id.telephone_number1_textView);
        telephone2 = (TextView) findViewById(R.id.telephone_number2_textView);
        email = (TextView) findViewById(R.id.email_textView);
        lastDate = (TextView) findViewById(R.id.last_date_textView);
        nextDate = (TextView) findViewById(R.id.next_date_textView);
        appointmentReminder = (TextView) findViewById(R.id.appointment_reminder_textView);

        setAppointmentDataFromDb();
    }

    public void onDeleteAppointmentButtonClicked(View v) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set dialog message
        alertDialogBuilder
                .setMessage(R.string.delete_appointment_message)
                .setCancelable(false)
                .setPositiveButton(R.string.delete_dialog_positive,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, do that
                        dbHelper.deleteAppointment(appointmentId);

                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                })
                .setNegativeButton(R.string.delete_dialog_negative,new DialogInterface.OnClickListener() {
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
                setAppointmentDataFromDb();
            }
        }
    }

    private void setAppointmentDataFromDb() {
        Cursor appointmentCursor = dbHelper.getAppointmentIdCursor(appointmentId);

        String appointmentText = appointmentCursor.getString(appointmentCursor.getColumnIndex(WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_NAME));
        appointmentName.setText(appointmentText);
        String doctorText = appointmentCursor.getString(appointmentCursor.getColumnIndex(WomansHealthContract.WomansHealthAppointment.COLUMN_DOCTOR_NAME));
        doctorsName.setText(doctorText);
        String addressText = appointmentCursor.getString(appointmentCursor.getColumnIndex(WomansHealthContract.WomansHealthAppointment.COLUMN_ADDRESS));
        address.setText(addressText);
        String tel1Text = appointmentCursor.getString(appointmentCursor.getColumnIndex(WomansHealthContract.WomansHealthAppointment.COLUMN_TELEPHONE1));
        telephone1.setText(tel1Text);
        String tel2Text = appointmentCursor.getString(appointmentCursor.getColumnIndex(WomansHealthContract.WomansHealthAppointment.COLUMN_TELEPHONE2));
        telephone2.setText(tel2Text);
        String emailText = appointmentCursor.getString(appointmentCursor.getColumnIndex(WomansHealthContract.WomansHealthAppointment.COLUMN_EMAIL));
        email.setText(emailText);
        String lastDateText = appointmentCursor.getString(appointmentCursor.getColumnIndex(WomansHealthContract.WomansHealthAppointment.COLUMN_LAST_APPOINTMENT));
        lastDate.setText(lastDateText);
        String nextDateText = appointmentCursor.getString(appointmentCursor.getColumnIndex(WomansHealthContract.WomansHealthAppointment.COLUMN_NEXT_APPOINTMENT));
        String timeText = appointmentCursor.getString(appointmentCursor.getColumnIndex(WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_TIME));
        nextDate.setText(nextDateText + " " + timeText);
        int appointmentReminderData = appointmentCursor.getInt(appointmentCursor.getColumnIndex(WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_REMINDER));
        appointmentReminder.setText("" + appointmentReminderData);
    }
}
