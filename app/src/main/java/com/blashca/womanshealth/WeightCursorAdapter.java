package com.blashca.womanshealth;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.blashca.womanshealth.data.WomansHealthContract;

public class WeightCursorAdapter extends CursorAdapter {
    private LayoutInflater cursorInflater;

    public WeightCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = cursorInflater.inflate(R.layout.weights_list_item, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView bmi = (TextView) view.findViewById(R.id.bmi_record_textView);
        TextView weightAndHeight = (TextView) view.findViewById(R.id.weight_and_height_record_textView);
        TextView date = (TextView) view.findViewById(R.id.weight_date_record_textView);


        String textWeight = cursor.getString(cursor.getColumnIndex(WomansHealthContract.WomansHealthWeight.COLUMN_WEIGHT));
        String textHeight = cursor.getString(cursor.getColumnIndex(WomansHealthContract.WomansHealthWeight.COLUMN_HEIGHT));
        weightAndHeight.setText(textWeight + " kg   " + textHeight + " cm");

        String textDate = cursor.getString(cursor.getColumnIndex(WomansHealthContract.WomansHealthWeight.COLUMN_WEIGHT_DATE));
        date.setText(textDate);
    }
}