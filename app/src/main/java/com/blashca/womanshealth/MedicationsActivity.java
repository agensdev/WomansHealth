package com.blashca.womanshealth;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MedicationsActivity extends AppCompatActivity {
    String[] medicationsNameList;
    private static String ID = "myIndex";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medications);

        ListView listView = (ListView) findViewById(R.id.medications_listView);

        medicationsNameList = new String[] {"Medicine1", "Medicine2", "Medicine3", "Medicine4","Medicine5","Medicine6",
                "Medicine7", "Medicine8", "Medicine9", "Medicine10", "Medicine11", "Medicine12"};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.medications_list_item, medicationsNameList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MedicationsActivity.this, MedicationsDetailActivity.class);
                intent.putExtra(ID, id);
                startActivityForResult(intent, 1);
            }
        });
    }
}
