package com.blashca.womanshealth;


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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blashca.womanshealth.data.WomansHealthContract;
import com.blashca.womanshealth.data.WomansHealthDbHelper;

import java.text.DateFormat;
import java.util.Date;


public class WeightActivity extends AppCompatActivity {
    private String formattedDate;
    private static TextView dateTextView;
    private WomansHealthDbHelper dbHelper;
    private double height;
    private double weight;
    private double bmi;
    private double optimalWeight;
    private int category;
    private String optimum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);

        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        formattedDate = dateFormat.format(new Date());

        dateTextView = (TextView) findViewById(R.id.date_set_textView);
        dateTextView.setText(formattedDate);

        dbHelper = new WomansHealthDbHelper(this);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment((TextView) v);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void calculateBMI(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

        EditText heightEditText = (EditText) findViewById(R.id.height_editText);
        String heightText = heightEditText.getText().toString();

        EditText weightEditText = (EditText) findViewById(R.id.weight_editText);
        String weightText = weightEditText.getText().toString();

        if (heightText.equals("") || (Integer.parseInt(heightText) < 100 || Integer.parseInt(heightText) > 300)) {
            Toast.makeText(this, R.string.empty_height, Toast.LENGTH_SHORT).show();
            return;
        } else if (weightText.equals("") || (Double.parseDouble(weightText) < 20 || Double.parseDouble(weightText) > 200)) {
            Toast.makeText(this, R.string.empty_weight, Toast.LENGTH_SHORT).show();
            return;
        }

        height = Integer.parseInt(heightText);
        weight = Double.parseDouble(weightText);

        bmi = weight / Math.pow(height/100.0, 2);

        if (bmi < 18.5) {
            category = R.string.underweight;
            optimum = getString(R.string.underweight_result);
            optimalWeight = Math.abs(weight - (18.5 * Math.pow(height/100.0, 2)));
        } else if (bmi >= 18.5 && bmi < 25) {
            category = R.string.normal_weight;
            optimum = getString(R.string.normal_result);
        } else if (bmi >= 25 && bmi < 30) {
            category = R.string.overweight;
            optimum = getString(R.string.overweight_result);
            optimalWeight = Math.abs((24.9 * Math.pow(height/100.0, 2)) - weight);
        } else {
            category = R.string.obese;
            optimum = getString(R.string.overweight_result);
            optimalWeight = Math.abs((24.9 * Math.pow(height/100.0, 2)) - weight);
        }

        TextView bmiResult = (TextView) findViewById(R.id.bmi_result_textView);
        bmiResult.setText(String.format("%.1f", bmi));

        TextView categoryResult = (TextView) findViewById(R.id.category_result_textView);
        categoryResult.setText(category);

        TextView optimumResult = (TextView) findViewById(R.id.optimum_result_textView);
        if (category == R.string.normal_weight) {
            optimumResult.setText(optimum);
        } else {
            optimumResult.setText(optimum + " " + String.format("%.1f", optimalWeight) + " " + getString(R.string.weight_units));
        }

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.bmi_result_relativeLayout);
        relativeLayout.setVisibility(View.VISIBLE);

        Button calculateButton = (Button) findViewById(R.id.calculate_button);
        calculateButton.setText(R.string.recalculate);
    }

    public void showWeightRecords(View view) {
        Intent intent = new Intent(this, WeightRecordsActivity.class);
        startActivity(intent);
    }

    public void onRecordWeightButtonClicked(View view) {

        if (dbHelper.getWeightsCount(chosenDate()) == 0) {
            dbHelper.insertWeight(getWeightContentValues());
            resetScreen();
            Toast.makeText(this, R.string.weight_recorded, Toast.LENGTH_SHORT).show();
        } else {
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);

            alertDialogBuilder
                    .setMessage(R.string.record_exists)
                    .setCancelable(false)
                    .setPositiveButton(R.string.update,new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dbHelper.updateWeight(getWeightContentValues(), chosenDate());
                            resetScreen();
                            Toast.makeText(getApplicationContext(), R.string.weight_recorded, Toast.LENGTH_SHORT).show();
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
        values.put(WomansHealthContract.WomansHealthWeight.COLUMN_WEIGHT_DATE, chosenDate());
        values.put(WomansHealthContract.WomansHealthWeight.COLUMN_HEIGHT, height);
        values.put(WomansHealthContract.WomansHealthWeight.COLUMN_WEIGHT, weight);

        return values;
    }

    private String chosenDate() {
        return dateTextView.getText().toString();
    }

    private void resetScreen() {
        EditText height = (EditText) findViewById(R.id.height_editText);
        height.setText("");
        EditText weight = (EditText) findViewById(R.id.weight_editText);
        weight.setText("");
        Button calculate = (Button) findViewById(R.id.calculate_button);
        calculate.setText(R.string.calculate);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.bmi_result_relativeLayout);
        relativeLayout.setVisibility(View.INVISIBLE);
    }
}