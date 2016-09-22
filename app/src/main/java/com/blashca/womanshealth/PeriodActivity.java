package com.blashca.womanshealth;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;


public class PeriodActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DateReceiver {
    private DateFormat dateFormat;
    private TextView periodDate;
    private static TextView expectedDate;
    private Date futureDate;
    private static TextView daysToGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period);

        dateFormat = DateFormat.getDateInstance(DateFormat.LONG);

        final Spinner spinner = (Spinner) findViewById(R.id.duration_spinner);
        String[] durationArray = getResources().getStringArray(R.array.duration_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, durationArray) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView)v.findViewById(android.R.id.text1)).setText("");
                    ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }
                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1;
            }
        };
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getCount());
        spinner.setOnItemSelectedListener(this);

        periodDate = (TextView) findViewById(R.id.period_date);
        expectedDate = (TextView) findViewById(R.id.expeced_date_data);
        daysToGo = (TextView) findViewById(R.id.days_to_go_data);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment(this, v.getId());
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void setExpectedDate(Date userDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(userDate);
        calendar.add(Calendar.DAY_OF_YEAR, 28); //Adding 28 days to the time set by user
        futureDate = calendar.getTime();
        String newFormattedDate = dateFormat.format(futureDate);
        expectedDate.setText(newFormattedDate);
    }

    public void setDaysToGo(Date userDate) {
        int difference = Math.abs ((int) ((userDate.getTime()/(24*60*60*1000)) - (int)(futureDate.getTime()/(24*60*60*1000))));
        daysToGo.setText("" + difference);
    }

    @Override
    public void onDateReceive(Date date, int id) {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        periodDate.setText(dateFormat.format(date));
        setExpectedDate(date);
        setDaysToGo(date);
    }
}
