package com.blashca.womanshealth.activities;


import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.blashca.womanshealth.DateReceiver;
import com.blashca.womanshealth.DateUtil;
import com.blashca.womanshealth.DigestUtil;
import com.blashca.womanshealth.R;
import com.blashca.womanshealth.fragments.DatePickerFragment;

import java.text.DateFormat;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, DateReceiver {
    private SharedPreferences sharedPreferences;
    private long birthday;
    private Date birthDate;
    private Date newBirthDate;
    private DateFormat dateFormat;
    private TextView dateTextView;
    private RadioGroup radioGroup;
    private int radioButtonIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
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
                case R.id.profile_radio_pregnant:
                    radioButtonIndex = 0;
                    break;
                case R.id.profile_radio_menopause:
                    radioButtonIndex = 1;
                    break;
                case R.id.profile_radio_normal:
                    radioButtonIndex = 2;
                    break;
            }
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment(this, v.getId(), birthDate);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private void setProfileData() {

        birthday = sharedPreferences.getLong("birthday", 0);
        birthDate = new Date(birthday);
        dateTextView.setText(dateFormat.format(birthDate));

        radioButtonIndex = sharedPreferences.getInt("radioButtonIndex", 2);
        int radioButtonId = R.id.profile_radio_normal;

        switch (radioButtonIndex) {
            case 0:
                radioButtonId = R.id.profile_radio_pregnant;
                break;
            case 1:
                radioButtonId = R.id.profile_radio_menopause;
                break;
            case 2:
                radioButtonId = R.id.profile_radio_normal;
                break;
        }

        RadioButton radioButton = (RadioButton) radioGroup.findViewById(radioButtonId);
        radioButton.setChecked(true);

        TextView passwordTextView = (TextView) findViewById(R.id.password_textView);
        TextView repeatPasswordTextView = (TextView) findViewById(R.id.repeat_password_textView);
        String password = sharedPreferences.getString("digestPassword", "");
        if (!password.equals("")) {
            passwordTextView.setText(R.string.new_password);
            repeatPasswordTextView.setText(R.string.repeat_new_password);
        }
    }

    public void onRecordProfileButtonClicked(View view) {
        EditText password = (EditText) findViewById(R.id.password_editText);
        EditText repeatPassword = (EditText) findViewById(R.id.repeat_password_editText);

        String passwordText = password.getText().toString();
        String repeatText = repeatPassword.getText().toString();

        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (newBirthDate != null) {
            editor.putLong("birthday", newBirthDate.getTime());
        }

        editor.putInt("radioButtonIndex", radioButtonIndex);

        if (!passwordText.equals("")) {
            if (passwordText.equals(repeatText)) {
                editor.putString("digestPassword", DigestUtil.digestPassword(passwordText));
            } else {
                Toast.makeText(getApplicationContext(), R.string.incorrect_password_confirmation, Toast.LENGTH_SHORT).show();
            }
        }

        editor.commit();
        finish();
    }

    @Override
    public void onDateReceive(Date date, int id) {
        newBirthDate = DateUtil.resetTime(date);
        dateTextView.setText(dateFormat.format(newBirthDate));
    }
}
