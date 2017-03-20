package com.blashca.womanshealth.activities;


import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.blashca.womanshealth.R;
import com.blashca.womanshealth.data.WomansHealthContract;
import com.blashca.womanshealth.data.WomansHealthDbHelper;
import com.blashca.womanshealth.models.Medication;


public class AllergenEditActivity extends AppCompatActivity {
    private static String MEDICATION_ID = "medicationId";
    private WomansHealthDbHelper dbHelper;
    private Medication medication;
    private EditText allergenEditText;
    private EditText effectsEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergen_edit);

        dbHelper = new WomansHealthDbHelper(this);

        Long medicationId = null;
        if (getIntent().getExtras() != null) {
            medicationId = getIntent().getExtras().getLong(MEDICATION_ID);
        }

        if (medicationId != null) {
            medication = dbHelper.loadMedication(medicationId);
        } else {
            medication = new Medication();
        }

        allergenEditText = (EditText) findViewById(R.id.allergen_editText);
        effectsEditText = (EditText) findViewById(R.id.effects_editText);

        refreshUI();
    }

    public void onRecordAllergenButtonClicked(View v) {
        String newAllergenName = allergenEditText.getText().toString().trim();
        String allergyEffects = effectsEditText.getText().toString().trim();

        if (!newAllergenName.equals("")) {
            medication.name = newAllergenName;
        } else {
            Toast.makeText(getApplicationContext(), R.string.record_button_toast_message, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!allergyEffects.equals("")) {
            medication.allergyEffects = allergyEffects;
        } else {
            medication.allergyEffects = null;
        }

        if (medication.id != null) {
            dbHelper.updateMedication(getAllergenContentValues(), medication.id);
        } else {
            medication.id = dbHelper.insertMedication(getAllergenContentValues());
        }

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    private void refreshUI() {
        allergenEditText.setText(medication.name);
        allergenEditText.setSelection(allergenEditText.getText().length());

        effectsEditText.setText(medication.allergyEffects);
        effectsEditText.setSelection(effectsEditText.getText().length());
    }

    private ContentValues getAllergenContentValues() {
        ContentValues values = new ContentValues();
        values.put(WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_NAME, medication.name);
        values.put(WomansHealthContract.WomansHealthMedication.COLUMN_ALLERGY_EFFECTS, medication.allergyEffects);
        values.put(WomansHealthContract.WomansHealthMedication.COLUMN_IS_ALLERGEN, 1);

        return values;
    }
}
