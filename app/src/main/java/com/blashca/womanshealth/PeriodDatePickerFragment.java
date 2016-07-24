package com.blashca.womanshealth;

import android.annotation.SuppressLint;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;


public class PeriodDatePickerFragment extends DatePickerFragment {
    private PeriodActivity periodActivity;

    public PeriodDatePickerFragment() {
        super();
    }

    @SuppressLint("ValidFragment")
    public PeriodDatePickerFragment(TextView v, PeriodActivity periodActivity) {
        super(v);
        this.periodActivity = periodActivity;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        super.onDateSet(view, year, monthOfYear, dayOfMonth);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        Date date = calendar.getTime();

        this.periodActivity.setExpectedDate(date);
        this.periodActivity.setDaysToGo(date);
    }
}
