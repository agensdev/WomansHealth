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

import com.blashca.womanshealth.fragments.DatePickerFragment;
import com.blashca.womanshealth.DateReceiver;
import com.blashca.womanshealth.DateUtil;
import com.blashca.womanshealth.R;
import com.blashca.womanshealth.data.WomansHealthContract;
import com.blashca.womanshealth.data.WomansHealthDbHelper;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;


public class PeriodActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DateReceiver {
    private WomansHealthDbHelper dbHelper;
    private Date chosenDate;
    private DateFormat dateFormat;
    private String formattedDate;
    private TextView periodDate;
    private TextView expectedDate;
    private Date futureDate;
    private TextView daysToGo;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private int duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period);

        dbHelper = new WomansHealthDbHelper(this);

        chosenDate = DateUtil.removeTime(new Date());
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        formattedDate = dateFormat.format(chosenDate);

        setDuration();

        periodDate = (TextView) findViewById(R.id.period_date);
        periodRecordsButton = (ImageButton) findViewById(R.id.period_records_button);
        expectedDate = (TextView) findViewById(R.id.expeced_date_data);
        daysToGo = (TextView) findViewById(R.id.days_to_go_data);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

        displayPeriodRecordsButton();
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment(this, v.getId(), chosenDate);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private void setDuration() {
        spinner = (Spinner) findViewById(R.id.duration_spinner);
        String[] durationArray = getResources().getStringArray(R.array.duration_array);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, durationArray) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                duration = position + 1;

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
    }

    private void setExpectedDate(Date userDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(userDate);
        calendar.add(Calendar.DAY_OF_YEAR, 28); //Adding 28 days to the time set by user
        futureDate = calendar.getTime();
        String newFormattedDate = dateFormat.format(futureDate);
        expectedDate.setText(newFormattedDate);
    }

    private void setDaysToGo() {
        Date today = new Date();
        int difference = Math.abs ((int) ((today.getTime()/(24*60*60*1000)) - (int)(futureDate.getTime()/(24*60*60*1000))));
        daysToGo.setText("" + difference);
    }

    @Override
    public void onDateReceive(Date date, int id) {
        this.chosenDate = date;
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        periodDate.setText(dateFormat.format(date));
        setExpectedDate(date);
        setDaysToGo();
    }

    public void showPeriodRecords(View view) {
        Intent intent = new Intent(this, PeriodRecordsActivity.class);
        startActivity(intent);
    }

    public void onRecordPeriodButtonClicked(View view) {

        if (dbHelper.getPeriodsCount(chosenDate) == 0) {
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
                            dbHelper.updatePeriod(getPeriodContentValues(), chosenDate);
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

    private ContentValues getPeriodContentValues() {

        ContentValues values = new ContentValues();
        values.put(WomansHealthContract.WomansHealthPeriod.COLUMN_PERIOD_DATE, chosenDate.getTime());
        values.put(WomansHealthContract.WomansHealthPeriod.COLUMN_DURATION, duration);

        return values;
    }

    private void resetScreen() {
        periodDate.setText(R.string.select_date);
        spinner.setSelection(adapter.getCount());
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
