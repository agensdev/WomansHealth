package com.blashca.womanshealth.fragments;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import com.blashca.womanshealth.DateReceiver;
import com.blashca.womanshealth.DateUtil;

import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private DateReceiver dateReceiver;
    private int id;
    private Date selectedDate;

    public DatePickerFragment() {}

    // We pass an interface as an argument to the constructor because we only care if this object has onDateReceive method
    @SuppressLint("ValidFragment")
    public DatePickerFragment(DateReceiver dateReceiver, int id, Date selectedDate) {
        this.dateReceiver = dateReceiver;
        this.id = id;
        this.selectedDate = selectedDate;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        Calendar c = Calendar.getInstance();

        if (selectedDate != null) {
            c.setTime(selectedDate);
        }

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        Date date = calendar.getTime();

        dateReceiver.onDateReceive(DateUtil.resetTime(date), id);
    }
}
