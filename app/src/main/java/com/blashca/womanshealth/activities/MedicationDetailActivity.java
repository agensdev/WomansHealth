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
import com.blashca.womanshealth.models.Medication;

import java.text.DateFormat;

public class MedicationDetailActivity extends AppCompatActivity {
    private final static String DAILY_MEDICATION_NOTIFICATIONS = "com.blashca.womanshealth.DAILY_MEDICATION_NOTIFICATIONS";
    private final static String WEEKLY_MEDICATION_NOTIFICATIONS = "com.blashca.womanshealth.WEEKLY_MEDICATION_NOTIFICATIONS";
    private final static String MONTHLY_MEDICATION_NOTIFICATIONS = "com.blashca.womanshealth.MONTHLY_MEDICATION_NOTIFICATIONS";
    private final static String YEARLY_MEDICATION_NOTIFICATIONS = "com.blashca.womanshealth.YEARLY_MEDICATION_NOTIFICATIONS";

    private static String MEDICATION_ID = "medicationId";
    private WomansHealthDbHelper dbHelper;
    private DateFormat dateFormat;
    private Long medicationId;

    private TextView medicationNameTextView;
    private TextView dosageTextView;
    private TextView numberTextView;
    private TextView howTakenTextView;
    private TextView howOftenTextView;
    private TextView commencementTextView;
    private TextView[] medicationTimeTextViewsArray = new TextView[12];
    private TextView howLongTextView;
    private TextView medicationReminderTextView;

    private Medication medication;
    private AlarmHelper alarmHelper = new AlarmHelper();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medications_detail);

        dbHelper = new WomansHealthDbHelper(this);
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG);

        if (getIntent().getExtras() != null) {
            medicationId = getIntent().getExtras().getLong(MEDICATION_ID);
        }

        medication = dbHelper.loadMedication(medicationId);

        medicationNameTextView = (TextView) findViewById(R.id.medicine_name_textView);
        dosageTextView = (TextView) findViewById(R.id.dosage_textView);
        numberTextView = (TextView) findViewById(R.id.number_textView);
        howTakenTextView = (TextView) findViewById(R.id.how_taken_textView);
        howOftenTextView = (TextView) findViewById(R.id.how_often_textView);
        commencementTextView = (TextView) findViewById(R.id.commencement_textView);
        medicationTimeTextViewsArray[0] = (TextView) findViewById(R.id.medication_time1_textView);
        medicationTimeTextViewsArray[1] = (TextView) findViewById(R.id.medication_time2_textView);
        medicationTimeTextViewsArray[2] = (TextView) findViewById(R.id.medication_time3_textView);
        medicationTimeTextViewsArray[3] = (TextView) findViewById(R.id.medication_time4_textView);
        medicationTimeTextViewsArray[4] = (TextView) findViewById(R.id.medication_time5_textView);
        medicationTimeTextViewsArray[5] = (TextView) findViewById(R.id.medication_time6_textView);
        medicationTimeTextViewsArray[6] = (TextView) findViewById(R.id.medication_time7_textView);
        medicationTimeTextViewsArray[7] = (TextView) findViewById(R.id.medication_time8_textView);
        medicationTimeTextViewsArray[8] = (TextView) findViewById(R.id.medication_time9_textView);
        medicationTimeTextViewsArray[9] = (TextView) findViewById(R.id.medication_time10_textView);
        medicationTimeTextViewsArray[10] = (TextView) findViewById(R.id.medication_time11_textView);
        medicationTimeTextViewsArray[11] = (TextView) findViewById(R.id.medication_time12_textView);
        howLongTextView = (TextView) findViewById(R.id.how_long_textView);
        medicationReminderTextView = (TextView) findViewById(R.id.medication_reminder_textView);

        refreshUI();
    }

    public void onDeleteMedicationButtonClicked(final View v) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set dialog message
        alertDialogBuilder
                .setMessage(R.string.delete_medication_message)
                .setCancelable(false)
                .setPositiveButton(R.string.delete,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, do that

                        if (medication.howOftenNumber != null) {
                            for (int i = 0; i < medication.howOftenNumber; i++) {
                                alarmHelper.cancelAlarm(v.getContext(), DAILY_MEDICATION_NOTIFICATIONS, medication.getAlarmId(i));
                            }
                        }

                        alarmHelper.cancelAlarm(v.getContext(), WEEKLY_MEDICATION_NOTIFICATIONS, medication.id);
                        alarmHelper.cancelAlarm(v.getContext(), MONTHLY_MEDICATION_NOTIFICATIONS, medication.id);
                        alarmHelper.cancelAlarm(v.getContext(), YEARLY_MEDICATION_NOTIFICATIONS, medication.id);

                        dbHelper.deleteMedication(medicationId);

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

    public void onEditMedicationButtonClicked(View v) {
        Intent intent = new Intent(this, MedicationEditActivity.class);
        intent.putExtra(MEDICATION_ID, medicationId);
        startActivityForResult(intent, 2);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if(resultCode == MedicationEditActivity.RESULT_OK){
                medication = dbHelper.loadMedication(medicationId);
                refreshUI();
            }
        }
    }

    private void refreshUI() {
        String[] howTakenArray = getResources().getStringArray(R.array.how_taken_array);
        String[] howOftenArray = getResources().getStringArray(R.array.how_often_array);
        String[] howLongArray = getResources().getStringArray(R.array.length_array);

        medicationNameTextView.setText(medication.name);

        LinearLayout dosageLayout = (LinearLayout) findViewById(R.id.dosage_layout);

        if (medication.dosage != null) {
            dosageTextView.setText(medication.dosage);
            dosageLayout.setVisibility(View.VISIBLE);
        } else {
            dosageLayout.setVisibility(View.GONE);
        }

        LinearLayout numberLayout = (LinearLayout) findViewById(R.id.number_layout);

        if (medication.number != null) {
            numberTextView.setText("" + medication.number);
            numberLayout.setVisibility(View.VISIBLE);
        } else {
            numberLayout.setVisibility(View.GONE);
        }

        LinearLayout howTakenLayout = (LinearLayout) findViewById(R.id.how_taken_layout);

        if (medication.howTaken != null && medication.howTaken != howTakenArray.length - 1) {
            howTakenTextView.setText(howTakenArray[medication.howTaken]);
            howTakenLayout.setVisibility(View.VISIBLE);
        } else {
            howTakenLayout.setVisibility(View.GONE);
        }

        LinearLayout howOftenLayout = (LinearLayout) findViewById(R.id.how_often_layout);

        if (medication.howOftenNumber != null) {
            howOftenTextView.setText(medication.howOftenNumber + " " + howOftenArray[medication.howOftenPeriod]);
            howOftenLayout.setVisibility(View.VISIBLE);

        } else {
            howOftenLayout.setVisibility(View.GONE);
        }

        LinearLayout commencementLayout = (LinearLayout) findViewById(R.id.commencement_date_layout);

        if (medication.commencementDate != null) {
            commencementTextView.setText(dateFormat.format(medication.commencementDate));
            commencementLayout.setVisibility(View.VISIBLE);
        } else {
            commencementLayout.setVisibility(View.GONE);
        }

        LinearLayout timeLayout = (LinearLayout) findViewById(R.id.medication_time_layout);

        if (medication.medicationHourArray[0] != null) {
            timeLayout.setVisibility(View.VISIBLE);

            for (int i = 0; i < medication.medicationHourArray.length; i++) {
                if (medication.medicationHourArray[i] != null) {
                    medicationTimeTextViewsArray[i].setText(medication.getMedicationTime(i));
                    medicationTimeTextViewsArray[i].setVisibility(View.VISIBLE);
                } else {
                    medicationTimeTextViewsArray[i].setVisibility(View.GONE);
                }
            }
        } else {
            timeLayout.setVisibility(View.GONE);
        }

        LinearLayout howLongLayout = (LinearLayout) findViewById(R.id.how_long_layout);

        if (medication.howLongNumber != null) {
            howLongTextView.setText(medication.howLongNumber + " " + howLongArray[medication.howLongPeriod]);
            howLongLayout.setVisibility(View.VISIBLE);
        } else {
            howLongLayout.setVisibility(View.GONE);
        }

        if (medication.reminder == true) {
            medicationReminderTextView.setText(R.string.on);
        } else {
            medicationReminderTextView.setText(R.string.off);
        }
    }
}
