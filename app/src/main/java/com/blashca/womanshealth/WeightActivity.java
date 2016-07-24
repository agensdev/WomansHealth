package com.blashca.womanshealth;


import android.app.DialogFragment;
import android.content.ContentValues;
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
        String formattedDate = dateFormat.format(new Date());

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

        height = Integer.parseInt(heightText) / 100.00;
        weight = Double.parseDouble(weightText);

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

    public void onRecordWeightButtonClicked(View view) {
        dbHelper.insertWeight(getWeightContentValues());
    }

    private ContentValues getWeightContentValues() {
        String date = dateTextView.getText().toString();

        ContentValues values = new ContentValues();
        values.put(WomansHealthContract.WomansHealthWeight.COLUMN_WEIGHT_DATE, date);
        values.put(WomansHealthContract.WomansHealthWeight.COLUMN_HEIGHT, height);
        values.put(WomansHealthContract.WomansHealthWeight.COLUMN_WEIGHT, weight);

        return values;
    }
}