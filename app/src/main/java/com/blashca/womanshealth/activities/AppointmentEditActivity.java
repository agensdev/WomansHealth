package com.blashca.womanshealth.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.blashca.womanshealth.AlarmHelper;
import com.blashca.womanshealth.DateReceiver;
import com.blashca.womanshealth.DateUtil;
import com.blashca.womanshealth.R;
import com.blashca.womanshealth.TimeReceiver;
import com.blashca.womanshealth.data.WomansHealthContract;
import com.blashca.womanshealth.data.WomansHealthDbHelper;
import com.blashca.womanshealth.fragments.DatePickerFragment;
import com.blashca.womanshealth.fragments.TimePickerFragment;
import com.blashca.womanshealth.models.Appointment;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;


public class AppointmentEditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DateReceiver, TimeReceiver {
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private final static int PICK_CONTACT = 777;
    private static String APPOINTMENT_ID = "appointmentId";
    private static String TEST_NAME = "test_name";
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

        appointment = new Appointment();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            if (extras.containsKey(APPOINTMENT_ID)) {
                Long appointmentId = extras.getLong(APPOINTMENT_ID);
                appointment = dbHelper.loadAppointment(appointmentId);
            } else {
                String appointmentName = extras.getString(TEST_NAME);

                if (appointmentName != null) {
                    appointment.name = appointmentName;
                }
            }
        }

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

        //Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.next_appointment_date_array, android.R.layout.simple_spinner_item);


        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        nextDateSpinner.setAdapter(adapter);
        nextDateSpinner.setOnItemSelectedListener(this);

        refreshUI();
        addTextChangedListeners();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openContacts();
                } else {
                    // Permission Denied
                    Toast.makeText(this, R.string.contacts_denied, Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_CONTACT && resultCode == Activity.RESULT_OK) {
            Uri contactUri = data.getData();
            readContacts(contactUri);
            refreshUI();
        }
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

        if (appointment.lastDate != null) {
            appointment.nextDateSpinnerPosition = position;
        } else {
            appointment.nextDateSpinnerPosition = 0;
        }

        refreshUI();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    public void showDatePickerDialog(View v) {
        Date date = null;

        if (v.getId() == R.id.last_date_set_textView) {
            date = appointment.lastDate;
        } else if (v.getId() == R.id.next_date_set_textView) {
            date = appointment.nextDate;
        }
        DialogFragment newFragment = new DatePickerFragment(this, v.getId(), date);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment(this, appointment.nextAppointmentHour, appointment.nextAppointmentMinute);
        newFragment.show(getFragmentManager(), "timePicker");
    }

    @Override
    public void onDateReceive(Date date, int id) {

        if (id == R.id.last_date_set_textView) {
            appointment.lastDate = DateUtil.resetTime(date);
        } else if (id == R.id.next_date_set_textView) {
            appointment.nextDate = DateUtil.resetTime(date);
            appointment.nextDateSpinnerPosition = 0;
        }
        refreshUI();
    }

    @Override
    public void onTimeReceive(int index, int hour, int minute) {
        appointment.nextAppointmentHour = hour;
        appointment.nextAppointmentMinute = minute;

        appointment.getAppointmentTime();

        TextView appointmentTime = (TextView) findViewById(R.id.next_date_time_set_textView);
        appointmentTime.setText(appointment.getAppointmentTime());

        refreshUI();
    }

    public void onOpenContactsButtonClicked(View v) {
        contactsPermissionCheck();
    }

    public void onRecordAppointmentButtonClicked(View v) {

        if (appointment.name == null) {
            Toast.makeText(getApplicationContext(), R.string.empty_name_toast_message, Toast.LENGTH_SHORT).show();
            return;
        }

        if (appointment.nextDate == null) {
            Toast.makeText(getApplicationContext(), R.string.empty_appointment_date_toast_message, Toast.LENGTH_SHORT).show();
            return;
        }

        if (appointment.id != null) {
            dbHelper.updateAppointment(getAppointmentContentValues(), appointment.id);
        } else {
            appointment.id = dbHelper.insertAppointment(getAppointmentContentValues());
        }

        if (appointment.reminder) {
            alarmHelper.setAppointmentAlarms(this, appointment);
        } else {
            alarmHelper.cancelFirstAppointmentAlarm(v.getContext(), appointment);
            alarmHelper.cancelSecondAppointmentAlarm(v.getContext(), appointment);
        }

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
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
            reminderSwitch.setVisibility(View.VISIBLE);
        } else {
            nextDateTextView.setText("");
            reminderSwitch.setVisibility(View.GONE);
        }

        nextDateTimeTextView.setText(appointment.getAppointmentTime());

        if (appointment.reminder) {
            reminderSwitch.setChecked(true);
        } else {
            reminderSwitch.setChecked(false);
        }

        Button saveButton = (Button) findViewById(R.id.save_appointment_button);

        if (appointment.name != null && appointment.nextDate != null) {
            saveButton.setBackgroundResource(R.drawable.button_violet);
            saveButton.setTextColor(Color.WHITE);
        } else {
            saveButton.setBackgroundResource(R.drawable.button_grey);
            saveButton.setTextColor(Color.BLACK);
        }
    }

    private ContentValues getAppointmentContentValues() {
        Long lastDateLong = null;
        Long nextDateLong = null;

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


    private void calculateNextDateFromSpinner(Date lastDate, int calendarPeriod, int periodFromSpinner) {
        if (lastDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(lastDate);
            calendar.add(calendarPeriod, periodFromSpinner); //Adding period from spinner to the last date
            Date nextDate = calendar.getTime();
            onDateReceive(nextDate, R.id.next_date_set_textView);
        } else {
            Toast.makeText(getApplicationContext(), R.string.last_date_not_selected, Toast.LENGTH_SHORT).show();
            appointment.nextDateSpinnerPosition = 0;
        }
        refreshUI();
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
                    appointment.name = null;
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
                if (!doctorsNameEditText.getText().toString().trim().equals("")) {
                    appointment.doctorsName = doctorsNameEditText.getText().toString().trim();
                } else {
                    appointment.doctorsName = null;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        addressEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!addressEditText.getText().toString().trim().equals("")) {
                    appointment.address = addressEditText.getText().toString().trim();
                } else {
                    appointment.address = null;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        telephoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!telephoneEditText.getText().toString().trim().equals("")) {
                    appointment.telephone = telephoneEditText.getText().toString().trim();
                } else {
                    appointment.telephone = null;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!emailEditText.getText().toString().trim().equals("")) {
                    appointment.email = emailEditText.getText().toString().trim();
                } else {
                    appointment.email = null;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void contactsPermissionCheck() {
        int contactPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);

        if (contactPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }

        openContacts();
    }

    private void openContacts() {
        Intent intent = new Intent(Intent.ACTION_PICK,  ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
    }

    private void readContacts(Uri contactUri) {
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(contactUri, null, null, null, null);

        if (cursor.moveToNext()) {
            appointment.doctorsName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

            if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                // get phone number
                Cursor phoneCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        new String[]{id},
                        null);

                if (phoneCursor.moveToNext()) {
                    appointment.telephone = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
                phoneCursor.close();
            }

            // get email
            Cursor emailCursor = contentResolver.query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                    new String[]{id},
                    null);

            if (emailCursor.moveToNext()) {
                appointment.email = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            }
            emailCursor.close();

            // get address
            Cursor addressCursor = contentResolver.query(
                    ContactsContract.Data.CONTENT_URI,
                    null,
                    ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?",
                    new String[]{id, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE},
                    null);

            if (addressCursor.moveToNext()) {
                String street = addressCursor.getString(addressCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
                String city = addressCursor.getString(addressCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
                String postalCode = addressCursor.getString(addressCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));

                appointment.address = new StringBuilder().append(street).append(", ").append(postalCode).append(" ").append(city).toString();
            }
            addressCursor.close();
        }
        cursor.close();
    }
}
