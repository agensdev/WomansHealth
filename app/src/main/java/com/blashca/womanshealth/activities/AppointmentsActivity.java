package com.blashca.womanshealth.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.blashca.womanshealth.R;
import com.blashca.womanshealth.adapters.AppointmentCursorAdapter;
import com.blashca.womanshealth.data.WomansHealthDbHelper;

public class AppointmentsActivity extends AppCompatActivity {
    private WomansHealthDbHelper dbHelper;
    private static String APPOINTMENT_ID = "appointmentId";
    private CursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

        dbHelper = new WomansHealthDbHelper(this);

        ListView listView = (ListView) findViewById(R.id.appointments_listView);
        mAdapter = new AppointmentCursorAdapter(this, dbHelper.getAppointmentsCursor(), 0);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), AppointmentDetailActivity.class);
                intent.putExtra(APPOINTMENT_ID, id);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    public void onAddAppointmentButtonClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), SuggestedAppointmentsActivity.class);
        startActivity(intent);
    }

    private void refresh() {
        mAdapter.changeCursor(dbHelper.getAppointmentsCursor());
        // notifies adapter on the change
        mAdapter.notifyDataSetChanged();

        TextView textView = (TextView) findViewById(R.id.appointment_welcome_textView);

        if (mAdapter.getCount() == 0) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }
}
