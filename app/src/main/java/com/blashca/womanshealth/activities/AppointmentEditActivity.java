package com.blashca.womanshealth.activities;

import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.blashca.womanshealth.AlarmHelper;
import com.blashca.womanshealth.DateReceiver;
import com.blashca.womanshealth.DateUtil;
import com.blashca.womanshealth.R;
import com.blashca.womanshealth.data.WomansHealthContract;
import com.blashca.womanshealth.data.WomansHealthDbHelper;
import com.blashca.womanshealth.fragments.DatePickerFragment;
import com.blashca.womanshealth.fragments.TimePickerFragment;
import com.blashca.womanshealth.models.Appointment;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;


    private final static String FIRST_APPOINTMENT_NOTIFICATION = "com.blashca.womanshealth.FIRST_APPOINTMENT_NOTIFICATION";
    private final static String SECOND_APPOINTMENT_NOTIFICATION = "com.blashca.womanshealth.SECOND_APPOINTMENT_NOTIFICATION";
    private final static int NOTIFICATION_OFFSET = 1000000;

public class AppointmentEditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, TimePickerDialog.OnTimeSetListener, DateReceiver {
    private static String APPOINTMENT_ID = "appointmentId";
    private long appointmentId;
    private WomansHealthDbHelper dbHelper;
    private DateFormat dateFormat;

    private EditText appointmentNameEditText;
    private EditText doctorsNameEditText;
    private EditText addressEditText;
    private EditText telephoneEditText;
    private EditText emailEditText;
    private TextView lastDateTextView;
    private Spinner nextDateSpinner;
    private TextView nextDateTextView;
    private TextView nextDateTimeTextView;
    private Switch reminderSwitch;

    private Appointment appointment;
    private AlarmHelper alarmHelper = new AlarmHelper();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_edit);

        dbHelper = new WomansHealthDbHelper(this);
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG);

        if (getIntent().getExtras() != null) {
            appointmentId = getIntent().getExtras().getLong(APPOINTMENT_ID);
        }
        appointment = dbHelper.loadAppointmentDataFromDb(appointmentId);

        appointmentNameEditText = (EditText) findViewById(R.id.appointment_name_editText);
        doctorsNameEditText = (EditText) findViewById(R.id.doctors_name_editText);
        addressEditText = (EditText) findViewById(R.id.address_editText);
        telephoneEditText = (EditText) findViewById(R.id.telephone_number_editText);
        emailEditText = (EditText) findViewById(R.id.email_editText);
        lastDateTextView = (TextView) findViewById(R.id.last_date_set_textView);
        nextDateSpinner = (Spinner) findViewById(R.id.next_date_spinner);
        nextDateTextView = (TextView) findViewById(R.id.next_date_set_textView);
        nextDateTimeTextView = (TextView) findViewById(R.id.next_date_time_set_textView);
        reminderSwitch = (Switch) findViewById(R.id.appointment_reminder_switch);

        addTextChangedListeners();

        //Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.next_appointment_date_array, android.R.layout.simple_spinner_item);


        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        nextDateSpinner.setAdapter(adapter);
        nextDateSpinner.setOnItemSelectedListener(this);

        refreshUI();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        appointment.nextAppointmentHour = hourOfDay;
        appointment.nextAppointmentMinute = minute;

        appointment.getAppointmentTime();

        TextView appointmentTime = (TextView) findViewById(R.id.next_date_time_set_textView);
        appointmentTime.setText(appointment.getAppointmentTime());

        refreshUI();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                break;
            case 1:
                calculateNextDateFromSpinner(appointment.lastDate, Calendar.DAY_OF_YEAR, 7);
                break;
            case 2:
                calculateNextDateFromSpinner(appointment.lastDate, Calendar.MONTH, 1);
                break;
            case 3:
                calculateNextDateFromSpinner(appointment.lastDate, Calendar.MONTH, 3);
                break;
            case 4:
                calculateNextDateFromSpinner(appointment.lastDate, Calendar.MONTH, 6);
                break;
            case 5:
                calculateNextDateFromSpinner(appointment.lastDate, Calendar.YEAR, 1);
                break;
            case 6:
                calculateNextDateFromSpinner(appointment.lastDate, Calendar.YEAR, 2);
                break;
            case 7:
                calculateNextDateFromSpinner(appointment.lastDate, Calendar.YEAR, 3);
                break;
            default:
                break;
        }
        nextDateSpinner.setSelection(position);
        appointment.nextDateSpinnerPosition = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    public void showDatePickerDialog(View v) {
        if (v.getId() == R.id.last_date_set_textView) {
            DialogFragment newFragment = new DatePickerFragment(this, v.getId(), appointment.lastDate);
            newFragment.show(getFragmentManager(), "datePicker");
        } else if (v.getId() == R.id.next_date_set_textView) {
            DialogFragment newFragment = new DatePickerFragment(this, v.getId(), appointment.nextDate);
            newFragment.show(getFragmentManager(), "datePicker");
        }
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment;
        if (appointment.nextAppointmentHour != -1) {
            newFragment = new TimePickerFragment(appointment.nextAppointmentHour, appointment.nextAppointmentMinute);
        } else {
            newFragment = new TimePickerFragment();
        }

        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void onRecordAppointmentButtonClicked(View v) {
        appointment.name = appointmentNameEditText.getText().toString().trim();
        appointment.doctorsName = doctorsNameEditText.getText().toString();
        appointment.address = addressEditText.getText().toString();
        appointment.telephone = telephoneEditText.getText().toString();
        appointment.email = emailEditText.getText().toString();

        if (!appointment.name.equals("")) {
            if (appointmentId != 0) {
                dbHelper.updateAppointment(getAppointmentContentValues(), appointmentId);
            } else {
                dbHelper.insertAppointment(getAppointmentContentValues());
            }

            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), R.string.record_button_toast_message, Toast.LENGTH_SHORT).show();
        }
    }


    private void refreshUI() {
        appointmentNameEditText.setText(appointment.name);
        appointmentNameEditText.setSelection(appointmentNameEditText.getText().length());
        doctorsNameEditText.setText(appointment.doctorsName);
        doctorsNameEditText.setSelection(doctorsNameEditText.getText().length());
        addressEditText.setText(appointment.address);
        addressEditText.setSelection(addressEditText.getText().length());
        telephoneEditText.setText(appointment.telephone);
        telephoneEditText.setSelection(telephoneEditText.getText().length());
        emailEditText.setText(appointment.email);
        emailEditText.setSelection(emailEditText.getText().length());
        if (appointment.lastDate != null) {
            lastDateTextView.setText(dateFormat.format(appointment.lastDate));
        } else {
            lastDateTextView.setText("");
        }

        nextDateSpinner.setSelection(appointment.nextDateSpinnerPosition);

        if (appointment.nextDate != null) {
            nextDateTextView.setText(dateFormat.format(appointment.nextDate));
        } else {
            nextDateTextView.setText("");
        }

        nextDateTimeTextView.setText(appointment.getAppointmentTime());

        if (appointment.reminder) {
            reminderSwitch.setChecked(true);
        }
    }


    private ContentValues getAppointmentContentValues() {
        long lastDateLong = 0;
        long nextDateLong = 0;

        if (appointment.lastDate != null) {
            lastDateLong = appointment.lastDate.getTime();
        }

        if (appointment.nextDate != null) {
            nextDateLong = appointment.nextDate.getTime();
        }

        appointment.reminder = reminderSwitch.isChecked();
        int reminderInt;
        if (appointment.reminder) {
            reminderInt = 1;
        } else {
            reminderInt = 0;
        }

        ContentValues values = new ContentValues();
        values.put(WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_NAME, appointment.name);
        values.put(WomansHealthContract.WomansHealthAppointment.COLUMN_DOCTOR_NAME, appointment.doctorsName);
        values.put(WomansHealthContract.WomansHealthAppointment.COLUMN_ADDRESS, appointment.address);
        values.put(WomansHealthContract.WomansHealthAppointment.COLUMN_TELEPHONE, appointment.telephone);
        values.put(WomansHealthContract.WomansHealthAppointment.COLUMN_EMAIL, appointment.email);
        values.put(WomansHealthContract.WomansHealthAppointment.COLUMN_LAST_APPOINTMENT_DATE, lastDateLong);
        values.put(WomansHealthContract.WomansHealthAppointment.COLUMN_NEXT_APPOINTMENT_SPINNER_POSITION, appointment.nextDateSpinnerPosition );
        values.put(WomansHealthContract.WomansHealthAppointment.COLUMN_NEXT_APPOINTMENT_DATE, nextDateLong);
        values.put(WomansHealthContract.WomansHealthAppointment.COLUMN_NEXT_APPOINTMENT_HOUR, appointment.nextAppointmentHour);
        values.put(WomansHealthContract.WomansHealthAppointment.COLUMN_NEXT_APPOINTMENT_MINUTE, appointment.nextAppointmentMinute);
        values.put(WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_REMINDER, reminderInt);

        return values;
    }

    @Override
    public void onDateReceive(Date date, int id) {
        if (id == R.id.last_date_set_textView) {
            appointment.lastDate = DateUtil.removeTime(date);
        } else if (id == R.id.next_date_set_textView) {
            appointment.nextDate = DateUtil.removeTime(date);
        }

        nextDateSpinner.setSelection(0);
        appointment.nextDateSpinnerPosition = 0;

        refreshUI();
    }

    private void calculateNextDateFromSpinner(Date lastDate, int calendarPeriod, int periodFromSpinner) {
        if (lastDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(lastDate);
            calendar.add(calendarPeriod, periodFromSpinner); //Adding period from spinner to the last date
            Date nextDate = calendar.getTime();
            onDateReceive(nextDate, R.id.next_date_set_textView);
        } else {
            Toast.makeText(getApplicationContext(), R.string.last_date_not_selected, Toast.LENGTH_SHORT).show();
            nextDateSpinner.setSelection(0);
        }
    }

    private void addTextChangedListeners() {
        appointmentNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!appointmentNameEditText.getText().toString().equals("")) {
                    appointment.name = appointmentNameEditText.getText().toString().trim();
                } else {
                    appointment.name = "";
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        doctorsNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                appointment.doctorsName = doctorsNameEditText.getText().toString().trim();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        addressEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                appointment.address = addressEditText.getText().toString().trim();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        telephoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                appointment.telephone = telephoneEditText.getText().toString().trim();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                appointment.email = emailEditText.getText().toString().trim();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}
