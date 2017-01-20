package com.blashca.womanshealth.activities;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blashca.womanshealth.R;
import com.blashca.womanshealth.data.WomansHealthDbHelper;
import com.blashca.womanshealth.models.Medication;

import java.text.DateFormat;

public class MedicationDetailActivity extends AppCompatActivity {
    private static String MEDICATION_ID = "medicationId";
    private WomansHealthDbHelper dbHelper;
    private DateFormat dateFormat;
    private long medicationId;

    private TextView medicationNameTextView;
    private TextView dosageTextView;
    private TextView numberTextView;
    private TextView howTakenTextView;
    private TextView howOftenTextView;
    private TextView commencementTextView;
    private TextView timeTextView;
    private TextView howLongTextView;
    private TextView medicationReminderTextView;

    private Medication medication;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medications_detail);

        dbHelper = new WomansHealthDbHelper(this);
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG);

        if (getIntent().getExtras() != null) {
            medicationId = getIntent().getExtras().getLong(MEDICATION_ID);
        }
        medication = dbHelper.loadMedicationDataFromDb(medicationId);

        medicationNameTextView = (TextView) findViewById(R.id.medicine_name_textView);
        dosageTextView = (TextView) findViewById(R.id.dosage_textView);
        numberTextView = (TextView) findViewById(R.id.number_textView);
        howTakenTextView = (TextView) findViewById(R.id.how_taken_textView);
        howOftenTextView = (TextView) findViewById(R.id.how_often_textView);
        commencementTextView = (TextView) findViewById(R.id.commencement_textView);
        timeTextView = (TextView) findViewById(R.id.medication_time_textView);
        howLongTextView = (TextView) findViewById(R.id.how_long_textView);
        medicationReminderTextView = (TextView) findViewById(R.id.medication_reminder_textView);

        refreshUI();
    }



    public void onDeleteMedicationButtonClicked(View v) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set dialog message
        alertDialogBuilder
                .setMessage(R.string.delete_medication_message)
                .setCancelable(false)
                .setPositiveButton(R.string.delete,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, do that
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
                medication = dbHelper.loadMedicationDataFromDb(medicationId);
                refreshUI();
            }
        }
    }


    private void refreshUI() {
        String[] howTakenArray = getResources().getStringArray(R.array.how_taken_array);
        String[] howOftenArray = getResources().getStringArray(R.array.how_often_array);
        String[] howLongArray = getResources().getStringArray(R.array.length_array);

        medicationNameTextView.setText(medication.name);

        if (!medication.dosage.equals("")) {
            dosageTextView.setText(medication.dosage);

            LinearLayout dosageLayout = (LinearLayout) findViewById(R.id.dosage_layout);
            dosageLayout.setVisibility(View.VISIBLE);
        } else {
            LinearLayout dosageLayout = (LinearLayout) findViewById(R.id.dosage_layout);
            dosageLayout.setVisibility(View.GONE);
        }

        if (medication.number != 0) {
            numberTextView.setText("" + medication.number);

            LinearLayout numberLayout = (LinearLayout) findViewById(R.id.number_layout);
            numberLayout.setVisibility(View.VISIBLE);
        } else {
            LinearLayout numberLayout = (LinearLayout) findViewById(R.id.number_layout);
            numberLayout.setVisibility(View.GONE);
        }

        if (medication.howTaken != howTakenArray.length - 1) {
            howTakenTextView.setText(howTakenArray[medication.howTaken]);

            LinearLayout howTakenLayout = (LinearLayout) findViewById(R.id.how_taken_layout);
            howTakenLayout.setVisibility(View.VISIBLE);
        } else {
            LinearLayout howTakenLayout = (LinearLayout) findViewById(R.id.how_taken_layout);
            howTakenLayout.setVisibility(View.GONE);
        }

        if (medication.howOftenNumber != 0) {
            howOftenTextView.setText(medication.howOftenNumber + " " + howOftenArray[medication.howOftenPeriod]);

            LinearLayout howOftenLayout = (LinearLayout) findViewById(R.id.how_often_layout);
            howOftenLayout.setVisibility(View.VISIBLE);
        } else {
            LinearLayout howOftenLayout = (LinearLayout) findViewById(R.id.how_often_layout);
            howOftenLayout.setVisibility(View.GONE);
        }

        if (medication.commencementDate != null) {
            commencementTextView.setText(dateFormat.format(medication.commencementDate));

            LinearLayout commencementLayout = (LinearLayout) findViewById(R.id.commencement_date_layout);
            commencementLayout.setVisibility(View.VISIBLE);
        } else {
            LinearLayout commencementLayout = (LinearLayout) findViewById(R.id.commencement_date_layout);
            commencementLayout.setVisibility(View.GONE);
        }

        if (!medication.getMedicationTime().equals("")) {
            timeTextView.setText(medication.getMedicationTime());

            LinearLayout timeLayout = (LinearLayout) findViewById(R.id.medication_time_layout);
            timeLayout.setVisibility(View.VISIBLE);
        } else {
            LinearLayout timeLayout = (LinearLayout) findViewById(R.id.medication_time_layout);
            timeLayout.setVisibility(View.GONE);
        }

        if (medication.howLongNumber != 0) {
            howLongTextView.setText(medication.howLongNumber + " " + howLongArray[medication.howLongPeriod]);

            LinearLayout howLongLayout = (LinearLayout) findViewById(R.id.how_long_layout);
            howLongLayout.setVisibility(View.VISIBLE);
        } else {
            LinearLayout howLongLayout = (LinearLayout) findViewById(R.id.how_long_layout);
            howLongLayout.setVisibility(View.GONE);
        }

        if (medication.reminder == true) {
            medicationReminderTextView.setText(R.string.on);
        }
    }
}
