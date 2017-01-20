package com.blashca.womanshealth.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.blashca.womanshealth.R;
import com.blashca.womanshealth.activities.WeightRecordsActivity;
import com.blashca.womanshealth.adapters.WeightCursorAdapter;
import com.blashca.womanshealth.data.WomansHealthDbHelper;

public class WeightListFragment extends Fragment {
    private WomansHealthDbHelper dbHelper;
    private CursorAdapter myCursorAdapter;
    private long weightId;
    private WeightRecordsActivity activity;


    /**
     * Create a new instance of WeightListFragment
     */
    public static WeightListFragment newInstance() {
        return new WeightListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weight_records_list, container, false);
        dbHelper = new WomansHealthDbHelper(getContext());

        ListView listView = (ListView) view.findViewById(R.id.weights_listView);
        myCursorAdapter = new WeightCursorAdapter(getContext(), dbHelper.getWeightsCursor(), 0);
        listView.setAdapter(myCursorAdapter);


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                weightId = id;

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

                alertDialogBuilder
                        .setMessage(R.string.delete_record)
                        .setCancelable(false)
                        .setPositiveButton(R.string.delete,new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dbHelper.deleteWeight(weightId);
                                activity = (WeightRecordsActivity) getActivity();
                                activity.onDataChange();
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
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    public void refresh() {
        myCursorAdapter.changeCursor(dbHelper.getWeightsCursor());
        // notifies adapter on the change
        myCursorAdapter.notifyDataSetChanged();
    }
}
