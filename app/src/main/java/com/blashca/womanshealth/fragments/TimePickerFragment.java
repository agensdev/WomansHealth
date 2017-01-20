package com.blashca.womanshealth.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import java.util.Calendar;


public class TimePickerFragment extends DialogFragment {
    private int hour;
    private int minute;

    public TimePickerFragment() {}

    @SuppressLint("ValidFragment")
    public TimePickerFragment(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();

        if (hour != 0) {
            c.set(Calendar.HOUR_OF_DAY, hour);
        }

        if (minute != 0) {
            c.set(Calendar.MINUTE, minute);
        }

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener) getActivity(), hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }
}
