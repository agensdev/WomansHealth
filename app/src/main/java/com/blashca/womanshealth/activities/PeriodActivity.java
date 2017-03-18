package com.blashca.womanshealth.activities;

import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.blashca.womanshealth.DateReceiver;
import com.blashca.womanshealth.DateUtil;
import com.blashca.womanshealth.R;
import com.blashca.womanshealth.data.WomansHealthContract;
import com.blashca.womanshealth.data.WomansHealthDbHelper;
import com.blashca.womanshealth.fragments.DatePickerFragment;
import com.blashca.womanshealth.models.Period;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;


public class PeriodActivity extends AppCompatActivity implements DateReceiver {
    private WomansHealthDbHelper dbHelper;
    private TextView periodDate;
    private Spinner durationSpinner;
    private ImageButton periodRecordsButton;
    private TextView expectedDate;
    private TextView daysToGo;

    private DateFormat dateFormat;
    private Date futureDate;
    private Period period;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period);

        dbHelper = new WomansHealthDbHelper(this);

        period = new Period();
        period.date = DateUtil.resetTime(new Date());

        periodDate = (TextView) findViewById(R.id.period_date);
        periodRecordsButton = (ImageButton) findViewById(R.id.period_records_button);
        expectedDate = (TextView) findViewById(R.id.expeced_date_data);
        daysToGo = (TextView) findViewById(R.id.days_to_go_data);

        dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        periodDate.setText(dateFormat.format(period.date));

        initDurationSpinner();
        displayPeriodRecordsButton();
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment(this, v.getId(), period.date);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onDateReceive(Date date, int id) {
        period.date = DateUtil.resetTime(date);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        periodDate.setText(dateFormat.format(period.date));
        setExpectedDate(period.date);
        setDaysToGo();
    }

    private void initDurationSpinner() {
        durationSpinner = (Spinner) findViewById(R.id.duration_spinner);
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
        durationSpinner.setAdapter(adapter);
        durationSpinner.setSelection(adapter.getCount());

        if (period.duration == null) {
            period.duration = durationSpinner.getCount();
        }
        durationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                period.duration = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setExpectedDate(Date userDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(userDate);
        calendar.add(Calendar.DAY_OF_YEAR, 28); //Adding 28 days to the time set by user
        futureDate = calendar.getTime();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        String newFormattedDate = dateFormat.format(futureDate);
        expectedDate.setText(newFormattedDate);
    }

    private void setDaysToGo() {
        Date today = new Date();
        int difference = Math.abs ((int) ((today.getTime()/(24*60*60*1000)) - (int)(futureDate.getTime()/(24*60*60*1000))));
        daysToGo.setText("" + difference);
    }

    public void showPeriodRecords(View view) {
        Intent intent = new Intent(this, PeriodRecordsActivity.class);
        startActivity(intent);
    }

    public void onRecordPeriodButtonClicked(View view) {

        if (dbHelper.getPeriodsCount(period.date) == 0) {
            dbHelper.insertPeriod(getPeriodContentValues());
            resetScreen();
            Toast.makeText(this, R.string.record_added, Toast.LENGTH_SHORT).show();
        } else {
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);

            alertDialogBuilder
                    .setMessage(R.string.record_exists)
                    .setCancelable(false)
                    .setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dbHelper.updatePeriod(getPeriodContentValues(), period.date);
                            resetScreen();
                            Toast.makeText(getApplicationContext(), R.string.record_added, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.cancel();
                        }
                    });

            android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        resetScreen();
    }

    private ContentValues getPeriodContentValues() {

        ContentValues values = new ContentValues();
        values.put(WomansHealthContract.WomansHealthPeriod.COLUMN_PERIOD_DATE, period.date.getTime());
        values.put(WomansHealthContract.WomansHealthPeriod.COLUMN_DURATION, period.duration);
        return values;
    }

    private void resetScreen() {
        period.date = DateUtil.resetTime(new Date());
        periodDate.setText(dateFormat.format(period.date));
        durationSpinner.setSelection(durationSpinner.getCount());
        expectedDate.setText(R.string.double_pause_icon);
        daysToGo.setText(R.string.double_pause_icon);
        displayPeriodRecordsButton();
    }

    private void displayPeriodRecordsButton() {

        if (dbHelper.getAllPeriodsCount() > 0) {
            periodRecordsButton.setVisibility(View.VISIBLE);
        } else {
            periodRecordsButton.setVisibility(View.GONE);
        }
    }
}
