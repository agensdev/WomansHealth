package com.blashca.womanshealth.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.blashca.womanshealth.adapters.MedicationCursorAdapter;
import com.blashca.womanshealth.R;
import com.blashca.womanshealth.data.WomansHealthDbHelper;
import com.blashca.womanshealth.models.Medication;

public class MedicationsActivity extends AppCompatActivity {
    private WomansHealthDbHelper dbHelper;
    private static String MEDICATION_ID = "medicationId";
    private CursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medications);

        dbHelper = new WomansHealthDbHelper(this);

        ListView listView = (ListView) findViewById(R.id.medications_listView);
        mAdapter = new MedicationCursorAdapter(this, dbHelper.getMedicationsCursor(), 0);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Medication medication = dbHelper.loadMedication(id);

                Intent intent;
                if (medication.isAllergen) {
                    intent = new Intent(getApplicationContext(), AllergenDetailActivity.class);
                } else {
                    intent = new Intent(getApplicationContext(), MedicationDetailActivity.class);
                }
                intent.putExtra(MEDICATION_ID, id);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    public void onAddAllergyButtonClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), AllergenEditActivity.class);
        startActivity(intent);
    }

    public void onAddMedicationButtonClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), MedicationEditActivity.class);
        startActivity(intent);
    }

    private void refresh() {
        mAdapter.changeCursor(dbHelper.getMedicationsCursor());
        // notifies adapter on the change
        mAdapter.notifyDataSetChanged();
    }
}
