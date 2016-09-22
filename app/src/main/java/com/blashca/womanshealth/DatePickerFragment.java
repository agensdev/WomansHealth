package com.blashca.womanshealth;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.R.attr.id;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private DateReceiver dateReceiver;
    private int id;

    public DatePickerFragment() {

    }

    // We pass an interface as an argument to the constructor because we only care if this object has onDateReceive method
    @SuppressLint("ValidFragment")
    public DatePickerFragment(DateReceiver dateReceiver, int id) {
        this.dateReceiver = dateReceiver;
        this.id = id;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
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

        dateReceiver.onDateReceive(DateUtil.removeTime(date), id);
    }
}
