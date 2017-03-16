package com.blashca.womanshealth.activities;


import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blashca.womanshealth.Bmi;
import com.blashca.womanshealth.fragments.DatePickerFragment;
import com.blashca.womanshealth.DateReceiver;
import com.blashca.womanshealth.DateUtil;
import com.blashca.womanshealth.R;
import com.blashca.womanshealth.data.WomansHealthContract;
import com.blashca.womanshealth.data.WomansHealthDbHelper;

import java.text.DateFormat;
import java.util.Date;


public class WeightActivity extends AppCompatActivity implements DateReceiver {
    private static TextView dateTextView;
    private WomansHealthDbHelper dbHelper;
    private Date chosenDate;
    private DateFormat dateFormat;
    private int height;
    private double weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);

        chosenDate = DateUtil.removeTime(new Date());
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        dateTextView = (TextView) findViewById(R.id.date_set_textView);
        dateTextView.setText(dateFormat.format(chosenDate));

        dbHelper = new WomansHealthDbHelper(this);
    }

    @Override
    public void onDateReceive(Date date, int id) {
        this.chosenDate = DateUtil.removeTime(date);
        // Since there is only one text view with date we don't use the id
        dateTextView.setText(dateFormat.format(chosenDate));
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment(this, R.id.date_set_textView, chosenDate);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void calculateBMI(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

        EditText heightEditText = (EditText) findViewById(R.id.height_editText);
        String heightText = heightEditText.getText().toString();

        EditText weightEditText = (EditText) findViewById(R.id.weight_editText);
        String weightText = weightEditText.getText().toString();

        try {
            height = Integer.parseInt(heightText);
        } catch (Exception e) {
            Toast.makeText(this, R.string.empty_height, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            weight = Double.parseDouble(weightText);
        } catch (Exception e) {
            Toast.makeText(this, R.string.empty_weight, Toast.LENGTH_SHORT).show();
            return;
        }

        if ((height < 100 || height > 300)) {
            Toast.makeText(this, R.string.empty_height, Toast.LENGTH_SHORT).show();
            return;
        } else if ((weight < 20 || weight > 200)) {
            Toast.makeText(this, R.string.empty_weight, Toast.LENGTH_SHORT).show();
            return;
        }

        Bmi bmi = new Bmi(height, weight);
        String bmiValue = bmi.getBmiValue();
        int category = bmi.getCategory();
        String optimum = getString(bmi.getOptimum());
        String optimalWeight = bmi.getOptimalWeight();

        TextView bmiResult = (TextView) findViewById(R.id.bmi_result_textView);
        bmiResult.setText(bmiValue);

        TextView categoryResult = (TextView) findViewById(R.id.category_result_textView);
        categoryResult.setText(category);

        TextView optimumResult = (TextView) findViewById(R.id.optimum_result_textView);
        if (category == R.string.normal_weight) {
            optimumResult.setText(optimum);
        } else {
            optimumResult.setText(optimum + " " + optimalWeight + " " + getString(R.string.weight_units));
        }

        LinearLayout bmiLayout = (LinearLayout) findViewById(R.id.bmi_result_linearLayout);
        bmiLayout.setVisibility(View.VISIBLE);

        Button calculateButton = (Button) findViewById(R.id.calculate_button);
        calculateButton.setText(R.string.recalculate);
    }

    public void showWeightRecords(View view) {
        Intent intent = new Intent(this, WeightRecordsActivity.class);
        startActivity(intent);
    }

    public void onRecordWeightButtonClicked(View view) {

        if (dbHelper.getWeightsCount(chosenDate) == 0) {
            dbHelper.insertWeight(getWeightContentValues());
            resetScreen();
            Toast.makeText(this, R.string.record_added, Toast.LENGTH_SHORT).show();
        } else {
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);

            alertDialogBuilder
                    .setMessage(R.string.record_exists)
                    .setCancelable(false)
                    .setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dbHelper.updateWeight(getWeightContentValues(), chosenDate);
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

    private ContentValues getWeightContentValues() {

        ContentValues values = new ContentValues();
        values.put(WomansHealthContract.WomansHealthWeight.COLUMN_WEIGHT_DATE, chosenDate.getTime());
        values.put(WomansHealthContract.WomansHealthWeight.COLUMN_HEIGHT, height);
        values.put(WomansHealthContract.WomansHealthWeight.COLUMN_WEIGHT, weight);

        return values;
    }

    private void resetScreen() {
        EditText height = (EditText) findViewById(R.id.height_editText);
        height.setText("");
        EditText weight = (EditText) findViewById(R.id.weight_editText);
        weight.setText("");
        Button calculate = (Button) findViewById(R.id.calculate_button);
        calculate.setText(R.string.calculate);

        LinearLayout bmiLayout = (LinearLayout) findViewById(R.id.bmi_result_linearLayout);
        bmiLayout.setVisibility(View.GONE);
    }
}