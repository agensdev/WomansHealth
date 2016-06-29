package com.blashca.womanshealth;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;


public class PeriodActivity extends AppCompatActivity {
    private static TextView periodDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period);

        periodDate = (TextView) findViewById(R.id.period_date);
        periodDate.setText(DateFormat.getDateInstance().format(new Date()));
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }
}
