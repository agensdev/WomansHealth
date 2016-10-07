package com.blashca.womanshealth;


import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, DateReceiver {
    private SharedPreferences sharedPreferences;
    public static final String Overlay = "overlayKey";
    private String password;
    private TextView birthDateTextView;
    private int radioButtonIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (sharedPreferences.getBoolean(Overlay, true)) {
            showWelcomeDialog();
        } else {
            password = sharedPreferences.getString("password", "");

            if (!password.equals("")) {
                showPasswordDialog();
            }
        }

        Button appointmentButton = (Button) findViewById(R.id.appointment_button);
        appointmentButton.setOnClickListener(this);
        Button medicationsButton = (Button) findViewById(R.id.medications_button);
        medicationsButton.setOnClickListener(this);
        Button periodButton = (Button) findViewById(R.id.weight_button);
        periodButton.setOnClickListener(this);
        Button weightButton = (Button) findViewById(R.id.period_button);
        weightButton.setOnClickListener(this);
        Button settingsButton = (Button) findViewById(R.id.settings_button);
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

            case R.id.weight_button:
                makeIntent(WeightActivity.class);
                break;

            case R.id.period_button:
                makeIntent(PeriodActivity.class);
                break;

            case R.id.settings_button:
                makeIntent(ProfileActivity.class);
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

        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.welcome_radioGroup);
        radioGroup.setOnCheckedChangeListener(this);

        birthDateTextView = (TextView) view.findViewById(R.id.date_of_birth_textView);

        builder.setView(view)
                .setPositiveButton(R.string.begin, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //Do nothing here because we override this button later to change the close behaviour.
                        //However, we still need this because on older versions of Android unless we
                        //pass a handler the button doesn't get instantiated
                    }
                });

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);

        //Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean wantToCloseDialog = false;
                //Do stuff, possibly set wantToCloseDialog to true then...
                String birthday = birthDateTextView.getText().toString();

                if (!birthday.equals("DD-MM-YYYY")) {

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("birthday", birthday);
                    editor.putInt("radioButtonIndex", radioButtonIndex);
                    editor.putBoolean(Overlay, false);
                    editor.commit();

                    wantToCloseDialog = true;
                } else {
                    Toast.makeText(getApplicationContext(), R.string.empty_date_of_birth, Toast.LENGTH_SHORT).show();
                }

                if(wantToCloseDialog)
                    dialog.dismiss();
                //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
            }
        });
    }

    private void showPasswordDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.login_dialog, null);

        builder.setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean wantToCloseDialog = false;
                EditText login = (EditText) view.findViewById(R.id.login_editText);
                String loginText = login.getText().toString();

                if (loginText.equals(password)) {
                    wantToCloseDialog = true;
                } else {
                    Toast.makeText(getApplicationContext(), R.string.incorrect_password, Toast.LENGTH_SHORT).show();
                    login.setText("");
                }

                if(wantToCloseDialog)
                    dialog.dismiss();
            }
        });
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment(this, v.getId());
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
        if(radioButton != null && checkedId > -1){
            switch (checkedId) {
                case R.id.welcome_radio_normal:
                    radioButtonIndex = 0;
                    break;
                case R.id.welcome_radio_pregnant:
                    radioButtonIndex = 1;
                    break;
                case R.id.welcome_radio_menopause:
                    radioButtonIndex = 2;
                    break;
            }
        }
    }

    @Override
    public void onDateReceive(Date date, int id) {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        birthDateTextView.setText(dateFormat.format(date));
    }
}
