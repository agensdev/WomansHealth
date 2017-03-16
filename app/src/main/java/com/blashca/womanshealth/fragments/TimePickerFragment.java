package com.blashca.womanshealth.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import com.blashca.womanshealth.TimeReceiver;

import java.util.Calendar;


public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private TimeReceiver timeReceiver;
    private int index;
    private Integer hour;
    private Integer minute;

    public TimePickerFragment() {}

    @SuppressLint("ValidFragment")
    public TimePickerFragment(TimeReceiver timeReceiver) {
        this.timeReceiver = timeReceiver;
    }

    @SuppressLint("ValidFragment")
    public TimePickerFragment(TimeReceiver timeReceiver, Integer hour, Integer minute) {
        this.timeReceiver = timeReceiver;
        this.hour = hour;
        this.minute = minute;
    }

    @SuppressLint("ValidFragment")
    public TimePickerFragment(TimeReceiver timeReceiver, int index, Integer hour, Integer minute) {
        this.timeReceiver = timeReceiver;
        this.index = index;
        this.hour = hour;
        this.minute = minute;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();

        if (hour != null) {
            c.set(Calendar.HOUR_OF_DAY, hour);
        }

        if (minute != null) {
            c.set(Calendar.MINUTE, minute);
        }

        int h = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, h, min,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        timeReceiver.onTimeReceive(index, hourOfDay, minute);
    }
}
