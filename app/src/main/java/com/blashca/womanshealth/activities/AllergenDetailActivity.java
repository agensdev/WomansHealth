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

public class AllergenDetailActivity extends AppCompatActivity {
    private static String MEDICATION_ID = "medicationId";
    private Long medicationId;
    private WomansHealthDbHelper dbHelper;
    private TextView allergenName;
    private TextView effects;

    private Medication medication;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergen_detail);

        dbHelper = new WomansHealthDbHelper(this);

        if (getIntent().getExtras() != null) {
            medicationId = getIntent().getExtras().getLong(MEDICATION_ID);
        }

        medication = dbHelper.loadMedicationDataFromDb(medicationId);

        allergenName = (TextView) findViewById(R.id.allergen_textView);
        effects = (TextView) findViewById(R.id.effects_textView);

        refreshUI();
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
                medication = dbHelper.loadMedicationDataFromDb(medicationId);
                refreshUI();
            }
        }
    }

    private void refreshUI() {
        LinearLayout allergyTypeLayout = (LinearLayout) findViewById(R.id.allergy_type_linearLayout);

        if (medication.isAllergen) {
            allergenName.setText(medication.name);

            if (medication.allergyEffects != null) {
                effects.setText(medication.allergyEffects);
                allergyTypeLayout.setVisibility(View.VISIBLE);
            } else {
                allergyTypeLayout.setVisibility(View.GONE);
            }
        }
    }
}
