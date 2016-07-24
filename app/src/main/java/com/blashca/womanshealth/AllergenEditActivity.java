package com.blashca.womanshealth;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.blashca.womanshealth.data.WomansHealthContract;
import com.blashca.womanshealth.data.WomansHealthDbHelper;


public class AllergenEditActivity extends AppCompatActivity {
    private static String MEDICATION_ID = "medicationId";
    private long medicationId;
    private WomansHealthDbHelper dbHelper;
    private Cursor medicationCursor;
    private EditText allergenEditText;
    private EditText effectsEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergen_edit);

        if (getIntent().getExtras() != null) {
            medicationId = getIntent().getExtras().getLong(MEDICATION_ID);
        }

        dbHelper = new WomansHealthDbHelper(this);
        medicationCursor = dbHelper.getMedicationIdCursor(medicationId);

        allergenEditText = (EditText) findViewById(R.id.allergen_editText);
        effectsEditText = (EditText) findViewById(R.id.effects_editText);

        setAllergenDataFromDb();
    }

    public void onRecordAllergenButtonClicked(View v) {
        String newAllergenName = allergenEditText.getText().toString().trim();
        if (medicationId != 0) {
            if (!newAllergenName.equals("")) {
                dbHelper.updateMedication(getAllergenContentValues(), medicationId);

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), R.string.record_button_toast_message, Toast.LENGTH_SHORT).show();
            }

        } else {
            if (!newAllergenName.equals("")) {
                dbHelper.insertMedication(getAllergenContentValues());

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), R.string.record_button_toast_message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setAllergenDataFromDb() {
        if (medicationId != 0) {
            String nameText = medicationCursor.getString(medicationCursor.getColumnIndex(WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_NAME));
            allergenEditText.setText(nameText);
            allergenEditText.setSelection(allergenEditText.getText().length());
            String effectsText = medicationCursor.getString(medicationCursor.getColumnIndex(WomansHealthContract.WomansHealthMedication.COLUMN_ALLERGIES_EFFECTS));
            effectsEditText.setText(effectsText);
            effectsEditText.setSelection(effectsEditText.getText().length());
        }
    }

    private ContentValues getAllergenContentValues() {
        String name = allergenEditText.getText().toString();
        String effects = effectsEditText.getText().toString();

        ContentValues values = new ContentValues();
        values.put(WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_NAME, name);
        values.put(WomansHealthContract.WomansHealthMedication.COLUMN_ALLERGIES_EFFECTS, effects);
        values.put(WomansHealthContract.WomansHealthMedication.COLUMN_IS_ALLERGEN, 1);

        return values;
    }
}
