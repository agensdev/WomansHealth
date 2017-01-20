package com.blashca.womanshealth.activities;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.blashca.womanshealth.adapters.PeriodAdapter;
import com.blashca.womanshealth.R;
import com.blashca.womanshealth.data.WomansHealthDbHelper;

public class PeriodRecordsActivity extends AppCompatActivity {
    private WomansHealthDbHelper dbHelper;
    private PeriodAdapter periodAdapter;
    private long periodId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period_records);

        dbHelper = new WomansHealthDbHelper(this);

        ListView listView = (ListView) findViewById(R.id.periods_listView);
        periodAdapter = new PeriodAdapter(this, dbHelper.getPeriods(dbHelper.getPeriodsCursor()));
        listView.setAdapter(periodAdapter);


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                periodId = id;

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PeriodRecordsActivity.this);

                alertDialogBuilder
                        .setMessage(R.string.delete_record)
                        .setCancelable(false)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dbHelper.deletePeriod(periodId);
                                refresh();
                            }
                        })
                        .setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    public void refresh() {
        periodAdapter.clear();
        periodAdapter.addAll(dbHelper.getPeriods(dbHelper.getPeriodsCursor()));
        periodAdapter.notifyDataSetChanged();
    }
}
