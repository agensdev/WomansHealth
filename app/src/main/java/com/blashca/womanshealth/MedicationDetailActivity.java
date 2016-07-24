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

public class MedicationDetailActivity extends AppCompatActivity {
    private static String MEDICATION_ID = "medicationId";
    private long medicationId;
    private WomansHealthDbHelper dbHelper;
    private TextView medicationName;
    private TextView dosage;
    private TextView number;
    private TextView howTaken;
    private TextView howOften;
    private TextView commencement;
    private TextView time;
    private TextView howLong;
    private TextView medicationReminder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medications_detail);

        medicationId = getIntent().getExtras().getLong(MEDICATION_ID);

        dbHelper = new WomansHealthDbHelper(this);

        medicationName = (TextView) findViewById(R.id.medicine_name_textView);
        dosage = (TextView) findViewById(R.id.dosage_textView);
        number = (TextView) findViewById(R.id.number_textView);
        howTaken = (TextView) findViewById(R.id.how_taken_textView);
        howOften = (TextView) findViewById(R.id.how_often_textView);
        commencement = (TextView) findViewById(R.id.commencement_textView);
        time = (TextView) findViewById(R.id.medication_time_textView);
        howLong = (TextView) findViewById(R.id.how_long_textView);
        medicationReminder = (TextView) findViewById(R.id.medication_reminder_textView);

        setMedicationDataFromDb();
    }



    public void onDeleteMedicationButtonClicked(View v) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set dialog message
        alertDialogBuilder
                .setMessage(R.string.delete_medication_message)
                .setCancelable(false)
                .setPositiveButton(R.string.delete_dialog_positive,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, do that
                        dbHelper.deleteMedication(medicationId);

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

    public void onEditMedicationButtonClicked(View v) {
        Intent intent = new Intent(this, MedicationEditActivity.class);
        intent.putExtra(MEDICATION_ID, medicationId);
        startActivityForResult(intent, 2);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if(resultCode == MedicationEditActivity.RESULT_OK){
                setMedicationDataFromDb();
            }
        }
    }

    private void setMedicationDataFromDb() {
        Cursor medicationCursor = dbHelper.getMedicationIdCursor(medicationId);

        String nameText = medicationCursor.getString(medicationCursor.getColumnIndex(WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_NAME));
        medicationName.setText(nameText);
        String dosageText  = medicationCursor.getString(medicationCursor.getColumnIndex(WomansHealthContract.WomansHealthMedication.COLUMN_DOSAGE));
        dosage.setText(dosageText);
        int numberData  = medicationCursor.getInt(medicationCursor.getColumnIndex(WomansHealthContract.WomansHealthMedication.COLUMN_NUMBER));
        String numberText = "" + numberData;
        number.setText(numberText);
        int howTakenData = medicationCursor.getInt(medicationCursor.getColumnIndex(WomansHealthContract.WomansHealthMedication.COLUMN_HOW_TAKEN));
        String howTakenText = "" + howTakenData;
        howTaken.setText(howTakenText);
        int howOftenData = medicationCursor.getInt(medicationCursor.getColumnIndex(WomansHealthContract.WomansHealthMedication.COLUMN_HOW_OFTEN));
        String howOftenText = "" + howOftenData;
        howOften.setText(howOftenText);
        String commencementText  = medicationCursor.getString(medicationCursor.getColumnIndex(WomansHealthContract.WomansHealthMedication.COLUMN_COMMENCEMENT));
        commencement.setText(commencementText);
        String timeText  = medicationCursor.getString(medicationCursor.getColumnIndex(WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_TIME));
        time.setText(timeText);
        int howLongData = medicationCursor.getInt(medicationCursor.getColumnIndex(WomansHealthContract.WomansHealthMedication.COLUMN_HOW_LONG));
        String howLongText = "" + howLongData;
        howLong.setText(howLongText);
        int medicationReminderData  = medicationCursor.getInt(medicationCursor.getColumnIndex(WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_REMINDER));
        medicationReminder.setText("" + medicationReminderData);
    }
}
