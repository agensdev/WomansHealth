package com.blashca.womanshealth.adapters;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.blashca.womanshealth.Bmi;
import com.blashca.womanshealth.R;
import com.blashca.womanshealth.data.WomansHealthContract;

import java.text.DateFormat;
import java.util.Date;

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

        TextView bmiTextView = (TextView) view.findViewById(R.id.bmi_record_textView);
        TextView categoryTextView = (TextView) view.findViewById(R.id.category_record_textView);
        TextView weightAndHeight = (TextView) view.findViewById(R.id.weight_and_height_record_textView);
        TextView dateTextView = (TextView) view.findViewById(R.id.weight_date_record_textView);

        int height = cursor.getInt(cursor.getColumnIndex(WomansHealthContract.WomansHealthWeight.COLUMN_HEIGHT));
        float weight = cursor.getFloat(cursor.getColumnIndex(WomansHealthContract.WomansHealthWeight.COLUMN_WEIGHT));

        Bmi bmi = new Bmi(height, weight);
        String bmiValue = bmi.getBmiValue();
        int category = bmi.getCategory();
        bmiTextView.setText(bmiValue + "  -");
        categoryTextView.setText(category);
        weightAndHeight.setText(weight + " kg       " + height + " cm");

        long timestamp = cursor.getLong(cursor.getColumnIndex(WomansHealthContract.WomansHealthWeight.COLUMN_WEIGHT_DATE));
        Date date = new Date(timestamp);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        String formattedDate = dateFormat.format(date);
        dateTextView.setText(formattedDate);
    }
}
