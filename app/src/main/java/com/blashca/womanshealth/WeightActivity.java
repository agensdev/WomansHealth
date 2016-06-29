package com.blashca.womanshealth;


import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;


public class WeightActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private DateFormat dateFormat;
    private String formattedDate;
    private static TextView dateTextView;
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

        dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        formattedDate = dateFormat.format(new Date());

        dateTextView = (TextView) findViewById(R.id.date_textView1);
        dateTextView.setText(formattedDate);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        Date date = calendar.getTime();
        formattedDate = dateFormat.format(date);

        dateTextView.setText(formattedDate);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void calculateBMI(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

        EditText heightEditText = (EditText) findViewById(R.id.height_editText);
        String heightText = heightEditText.getText().toString();

        EditText weightEditText = (EditText) findViewById(R.id.weight_editText);
        String weightText = weightEditText.getText().toString();

        height = Double.parseDouble(heightText) / 100;
        weight = Double.parseDouble(weightText);

        if (heightText.equals("") || (height < 1 || height > 3)) {
            Toast.makeText(this, R.string.empty_height, Toast.LENGTH_SHORT).show();
            return;
        } else if (weightText.equals("") || (weight < 20 || weight > 200)) {
            Toast.makeText(this, R.string.empty_weight, Toast.LENGTH_SHORT).show();
            return;
        }

        bmi = weight / Math.pow(height, 2);

        if (bmi < 18.5) {
            category = R.string.underweight;
            optimum = getString(R.string.underweight_result);
            optimalWeight = Math.abs(weight - (18.5 * Math.pow(height, 2)));
        } else if (bmi >= 18.5 && bmi < 25) {
            category = R.string.normal_weight;
            optimum = getString(R.string.normal_result);
        } else if (bmi >= 25 && bmi < 30) {
            category = R.string.overweight;
            optimum = getString(R.string.overweight_result);
            optimalWeight = Math.abs((24.9 * Math.pow(height, 2)) - weight);
        } else {
            category = R.string.obese;
            optimum = getString(R.string.overweight_result);
            optimalWeight = Math.abs((24.9 * Math.pow(height, 2)) - weight);
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
}