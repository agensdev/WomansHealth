package com.blashca.womanshealth;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.blashca.womanshealth.data.WomansHealthDbHelper;

public class WeightRecordsActivity extends AppCompatActivity {
    private WomansHealthDbHelper dbHelper;
    private CursorAdapter mAdapter;
    private long weightId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_records);

        dbHelper = new WomansHealthDbHelper(this);

        ListView listView = (ListView) findViewById(R.id.weights_listView);
        mAdapter = new WeightCursorAdapter(this, dbHelper.getWeightsCursor(), 0);
        listView.setAdapter(mAdapter);

        listView.setOnLongClickListener(new AdapterView.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                weightId = v.getId();

                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getApplicationContext());

                alertDialogBuilder
                        .setMessage(R.string.delete_record)
                        .setCancelable(false)
                        .setPositiveButton(R.string.delete,new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dbHelper.deleteWeight(weightId);
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

                android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        mAdapter.changeCursor(dbHelper.getWeightsCursor());
        // notifies adapter on the change
        mAdapter.notifyDataSetChanged();
    }
}
