package com.blashca.womanshealth;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PeriodAdapter extends ArrayAdapter<Period> {
    private String duration;
    private String day;
    private String days;
    private String interval;


    public PeriodAdapter(Context context, ArrayList<Period> objects) {
        super(context, 0, objects);
        duration = context.getResources().getString(R.string.duration);
        day = context.getResources().getString(R.string.day);
        days = context.getResources().getString(R.string.days);
        interval = context.getResources().getString(R.string.interval);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Period period = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.periods_list_item, parent, false);
        }

        // Lookup view for data population
        TextView dateTextView = (TextView) convertView.findViewById(R.id.period_date_record_textView);
        TextView durationTextView = (TextView) convertView.findViewById(R.id.period_duration_record_textView);
        TextView intervalTextView = (TextView) convertView.findViewById(R.id.period_interval_record_textView);

        // Populate the data into the template view using the data object
        Date date = new Date(period.date);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        String formattedDate = dateFormat.format(date);
        dateTextView.setText(formattedDate);

        if (period.duration == 1) {
            durationTextView.setText(duration + " " + period.duration + " " + day);
        } else {
            durationTextView.setText(duration + " " + period.duration + " " + days);
        }

        if (period.interval == 0) {
            intervalTextView.setText("");
        } else if (period.interval == 1) {
            intervalTextView.setText(interval + " " + period.interval + " " + day);
        } else {
            intervalTextView.setText(interval + " " + period.interval + " " + days);
        }

        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        Period period = getItem(position);

        return period.id;
    }
}
