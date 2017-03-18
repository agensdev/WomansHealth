package com.blashca.womanshealth.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.blashca.womanshealth.R;
import com.blashca.womanshealth.models.Period;

import java.text.DateFormat;
import java.util.ArrayList;

// We use custom adapter, because we need to compare two subsequent records from db to calculate period interval
// that would be impossible with cursor adapter
public class PeriodAdapter extends ArrayAdapter<Period> {
    private String durationText;
    private String dayText;
    private String daysText;
    private String intervalText;


    public PeriodAdapter(Context context, ArrayList<Period> objects) {
        super(context, 0, objects);
        durationText = context.getResources().getString(R.string.duration);
        dayText = context.getResources().getString(R.string.day);
        daysText = context.getResources().getString(R.string.days);
        intervalText = context.getResources().getString(R.string.interval);
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
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        String formattedDate = dateFormat.format(period.date);
        dateTextView.setText(formattedDate);

        String[] durationArray = getContext().getResources().getStringArray(R.array.duration_array);

        if (period.duration != durationArray.length - 1) {
            durationTextView.setText(durationText + " " + durationArray[period.duration]);
        } else {
            durationTextView.setText("");
        }


        if (period.interval == null) {
            intervalTextView.setText("");
        } else if (period.interval == 1) {
            intervalTextView.setText(intervalText + " " + period.interval + " " + dayText);
        } else {
            intervalTextView.setText(intervalText + " " + period.interval + " " + daysText);
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
