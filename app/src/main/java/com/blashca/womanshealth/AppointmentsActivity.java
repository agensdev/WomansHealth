package com.blashca.womanshealth;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AppointmentsActivity extends AppCompatActivity {
    String[] appointmentsList;
    private static String ID = "myIndex";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

        ListView listView = (ListView) findViewById(R.id.appointments_listView);

        appointmentsList = new String[] {"Appointment1", "Appointment2", "Appointment3", "Appointment4","Appointment5","Appointment6",
                "Appointment7", "Appointment8", "Appointment9", "Appointment10", "Appointment11", "Appointment12"};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.appointments_list_item, appointmentsList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AppointmentsActivity.this, AppointmentsDetailActivity.class);
                intent.putExtra(ID, id);
                startActivityForResult(intent, 1);
            }
        });
    }
}
