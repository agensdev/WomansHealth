package com.blashca.womanshealth;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.blashca.womanshealth.data.WomansHealthContract;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class PeriodCursorAdapter extends CursorAdapter {
    private LayoutInflater cursorInflater;

    public PeriodCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = cursorInflater.inflate(R.layout.periods_list_item, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView dateTextView = (TextView) view.findViewById(R.id.period_date_record_textView);
        TextView durationTextView = (TextView) view.findViewById(R.id.period_duration_record_textView);
        TextView intervalTextView = (TextView) view.findViewById(R.id.period_interval_record_textView);

        Long timestamp = cursor.getLong(cursor.getColumnIndex(WomansHealthContract.WomansHealthPeriod.COLUMN_LAST_PERIOD));
        Date date = new Date(timestamp);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        String formattedDate = dateFormat.format(date);
        dateTextView.setText(formattedDate);

        int duration = cursor.getInt(cursor.getColumnIndex(WomansHealthContract.WomansHealthPeriod.COLUMN_DURATION));
        if (duration == 1) {
            durationTextView.setText(duration + " " );
        } else {
            durationTextView.setText(duration + " " );
        }

        intervalTextView.setText("28");
    }
}
