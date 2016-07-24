package com.blashca.womanshealth;


import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private SharedPreferences sharedPreferences;
    private TextView dateTextView;
    private RadioGroup radioGroup;
    private int radioButtonIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        dateTextView = (TextView) findViewById(R.id.date_set_textView);

        radioGroup = (RadioGroup) findViewById(R.id.profile_radioGroup);
        radioGroup.setOnCheckedChangeListener(this);

        setProfileData();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
        if(radioButton != null && checkedId > -1){
            switch (checkedId) {
                case R.id.profile_radio_normal:
                    radioButtonIndex = 0;
                    break;
                case R.id.profile_radio_pregnant:
                    radioButtonIndex = 1;
                    break;
                case R.id.profile_radio_menopause:
                    radioButtonIndex = 2;
                    break;
            }
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment((TextView) v);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private void setProfileData() {

        String date = sharedPreferences.getString("birthday", null);
        dateTextView.setText(date);

        radioButtonIndex = sharedPreferences.getInt("radioButtonIndex", 0);

        int radioButtonId = R.id.profile_radio_normal;

        switch (radioButtonIndex) {
            case 0:
                radioButtonId = R.id.profile_radio_normal;
                break;
            case 1:
                radioButtonId = R.id.profile_radio_pregnant;
                break;
            case 2:
                radioButtonId = R.id.profile_radio_menopause;
                break;
        }

        RadioButton radioButton = (RadioButton) radioGroup.findViewById(radioButtonId);
        radioButton.setChecked(true);

        EditText passwordEditText = (EditText) findViewById(R.id.password_editText);
        String password = sharedPreferences.getString("password", null);
        passwordEditText.setText(password);

        EditText repeatEditText = (EditText) findViewById(R.id.repeat_password_editText);
        String repeatPassword = sharedPreferences.getString("password", null);
        repeatEditText.setText(repeatPassword);
    }

    public void onRecordProfileButtonClicked(View view) {

        String newDate = dateTextView.getText().toString();

        EditText password = (EditText) findViewById(R.id.password_editText);
        EditText repeatPassword = (EditText) findViewById(R.id.repeat_password_editText);

        String passwordText = password.getText().toString();
        String repeatText = repeatPassword.getText().toString();

        if (passwordText.equals(repeatText)) {

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("birthday", newDate);
            editor.putInt("radioButtonIndex", radioButtonIndex);
            editor.putString("password", passwordText);
            editor.commit();
            finish();

        } else {
            Toast.makeText(getApplicationContext(), R.string.incorrect_password_confirmation, Toast.LENGTH_SHORT).show();
        }
    }
}
