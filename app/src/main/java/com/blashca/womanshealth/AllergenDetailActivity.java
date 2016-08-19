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

public class AllergenDetailActivity extends AppCompatActivity {
    private static String MEDICATION_ID = "medicationId";
    private long medicationId;
    private WomansHealthDbHelper dbHelper;
    private TextView allergenName;
    private TextView effects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergen_detail);

        medicationId = getIntent().getExtras().getLong(MEDICATION_ID);

        dbHelper = new WomansHealthDbHelper(this);

        allergenName = (TextView) findViewById(R.id.allergen_textView);
        effects = (TextView) findViewById(R.id.effects_textView);

        setAllergenDataFromDb();
    }

    public void onDeleteAllergenButtonClicked(View v) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set dialog message
        alertDialogBuilder
                .setMessage(R.string.delete_allergen_message)
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

    public void onEditAllergenButtonClicked(View v) {
        Intent intent = new Intent(this, AllergenEditActivity.class);
        intent.putExtra(MEDICATION_ID, medicationId);
        startActivityForResult(intent, 3);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3) {
            if(resultCode == AllergenEditActivity.RESULT_OK){
                setAllergenDataFromDb();
            }
        }
    }

    private void setAllergenDataFromDb() {
        Cursor medicationCursor = dbHelper.getMedicationIdCursor(medicationId);

        String nameText = medicationCursor.getString(medicationCursor.getColumnIndex(WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_NAME));
        allergenName.setText(nameText);
        String effectsText  = medicationCursor.getString(medicationCursor.getColumnIndex(WomansHealthContract.WomansHealthMedication.COLUMN_ALLERGIES_EFFECTS));
        effects.setText(effectsText);
    }
}
