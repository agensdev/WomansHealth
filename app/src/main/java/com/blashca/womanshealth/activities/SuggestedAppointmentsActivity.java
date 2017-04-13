package com.blashca.womanshealth.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.blashca.womanshealth.R;
import com.blashca.womanshealth.adapters.SuggestedAppointmentCursorAdapter;
import com.blashca.womanshealth.data.WomansHealthDbHelper;
import com.blashca.womanshealth.models.MedicalTest;

import java.util.Date;


public class SuggestedAppointmentsActivity extends AppCompatActivity {
    private static String TEST_NAME = "test_name";
    private static final long MILLIS_IN_YEAR = 31536000000L;
    private SharedPreferences sharedPreferences;
    private WomansHealthDbHelper dbHelper;
    private CursorAdapter mAdapter;
    private MedicalTest medicalTest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_appointments);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        dbHelper = new WomansHealthDbHelper(this);

        ListView listView = (ListView) findViewById(R.id.suggested_appointments_listView);
        mAdapter = new SuggestedAppointmentCursorAdapter(this, dbHelper.getMedicalTestsCursorByAge(getAge()), 0);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                medicalTest = dbHelper.loadMedicationTest(id);

                Intent intent = new Intent(getApplicationContext(), AppointmentEditActivity.class);
                intent.putExtra(TEST_NAME, medicalTest.name);
                startActivity(intent);
            }
        });
    }

    private int getAge() {
        long birthday = sharedPreferences.getLong("birthday", 0);
        long today = new Date().getTime();
        long age = (today - birthday) / MILLIS_IN_YEAR;

        return (int) age;
    }
}
