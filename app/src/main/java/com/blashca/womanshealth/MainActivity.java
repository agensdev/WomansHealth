package com.blashca.womanshealth;


import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    SharedPreferences sharedPreferences;
    public static final String Overlay = "overlayKey";
    private TextView birthDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);

        if (sharedPreferences.getBoolean(Overlay, true)) {
            showWelcomeDialog();
        }

        Button appointmentButton = (Button) findViewById(R.id.appointment_button);
        appointmentButton.setOnClickListener(this);
        Button medicationsButton = (Button) findViewById(R.id.medications_button);
        medicationsButton.setOnClickListener(this);
        Button periodButton = (Button) findViewById(R.id.period_button);
        periodButton.setOnClickListener(this);
        Button weightButton = (Button) findViewById(R.id.weight_button);
        weightButton.setOnClickListener(this);
        Button settingsButton = (Button) findViewById(R.id.settins_button);
        settingsButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.appointment_button:
                makeIntent(AppointmentsActivity.class);
                break;

            case R.id.medications_button:
                makeIntent(MedicationsActivity.class);
                break;

            case R.id.period_button:
                makeIntent(PeriodActivity.class);
                break;

            case R.id.weight_button:
                makeIntent(WeightActivity.class);
                break;

            case R.id.settins_button:
                makeIntent(SettingsActivity.class);
                break;

            default:
                break;
        }
    }

    private void makeIntent(Class<?> nextIntentClass) {
        Intent intent = new Intent(getApplicationContext(), nextIntentClass);
        startActivity(intent);
    }

    private void showWelcomeDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.welcome_dialog, null);

        birthDateTextView = (TextView) view.findViewById(R.id.date_textView);
        birthDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateOfBirth();
            }
        });

        builder.setView(view)
                .setPositiveButton(R.string.begin, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (!birthDateTextView.getText().toString().equals("DD-MM-YYYY")) {
                            //Dodaj kod zapisujący dane do bazy

                            dialog.cancel();

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(Overlay, false);
                            editor.commit();


                        } else {
                            Toast.makeText(getApplicationContext(), R.string.empty_date_of_birth, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        builder.create();
        builder.show();
    }

    private void setDateOfBirth() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_normal:
                if (checked)

                    break;
            case R.id.radio_pregnant:
                if (checked)

                    break;
            case R.id.radio_menopause:
                if (checked)

                    break;
            default:
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        Date date = calendar.getTime();

        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        String formattedDate = dateFormat.format(date);
        birthDateTextView.setText(formattedDate);
    }
}
