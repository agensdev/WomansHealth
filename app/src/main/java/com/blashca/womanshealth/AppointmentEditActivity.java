package com.blashca.womanshealth;

import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.blashca.womanshealth.data.WomansHealthContract;
import com.blashca.womanshealth.data.WomansHealthDbHelper;


public class AppointmentEditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, TimePickerDialog.OnTimeSetListener {
    private static String APPOINTMENT_ID = "appointmentId";
    private long appointmentId;
    private WomansHealthDbHelper dbHelper;
    private Cursor appointmentCursor;
    private EditText appointmentNameEditText;
    private EditText doctorsNameEditText;
    private EditText addressEditText;
    private EditText telephone1EditText;
    private EditText telephone2EditText;
    private EditText emailEditText;
    private TextView lastDateTextView;
    private Spinner nextDateSpinner;
    private TextView nextDateTextView;
    private TextView nextDateTimeTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_edit);

        if (getIntent().getExtras() != null) {
            appointmentId = getIntent().getExtras().getLong(APPOINTMENT_ID);
        }

        dbHelper = new WomansHealthDbHelper(this);
        appointmentCursor = dbHelper.getAppointmentIdCursor(appointmentId);

        appointmentNameEditText = (EditText) findViewById(R.id.appointment_name_editText);
        doctorsNameEditText = (EditText) findViewById(R.id.doctors_name_editText);
        addressEditText = (EditText) findViewById(R.id.address_editText);
        telephone1EditText = (EditText) findViewById(R.id.tephone_number1_editText);
        telephone2EditText = (EditText) findViewById(R.id.tephone_number2_editText);
        emailEditText = (EditText) findViewById(R.id.email_editText);
        lastDateTextView = (TextView) findViewById(R.id.last_date_set_textView);
        nextDateSpinner = (Spinner) findViewById(R.id.next_date_spinner);
        nextDateTextView= (TextView) findViewById(R.id.next_date_set_textView);
        nextDateTimeTextView= (TextView) findViewById(R.id.next_date_time_set_textView);

        setAppointmentDataFromDb();


        //Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.appointment_reminder_array, android.R.layout.simple_spinner_item);


        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        nextDateSpinner.setAdapter(adapter);
        nextDateSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView appointmentTime = (TextView) findViewById(R.id.next_date_time_set_textView);
        appointmentTime.setText(new StringBuilder().append(hourOfDay).append(':')
                .append(minute).append(" ").toString());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment((TextView) v);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void onRecordAppointmentButtonClicked(View v) {
        String newAppointmentName = appointmentNameEditText.getText().toString().trim();

        if (appointmentId != 0) {
            if (!newAppointmentName.equals("")) {
                dbHelper.updateAppointment(getAppointmentContentValues(), appointmentId);

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), R.string.record_button_toast_message, Toast.LENGTH_SHORT).show();
            }

        } else {
            if (!newAppointmentName.equals("")) {
                dbHelper.insertAppointment(getAppointmentContentValues());

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), R.string.record_button_toast_message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setAppointmentDataFromDb() {
        if (appointmentId != 0) {
            String appointmentText = appointmentCursor.getString(appointmentCursor.getColumnIndex(WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_NAME));
            appointmentNameEditText.setText(appointmentText);
            appointmentNameEditText.setSelection(appointmentNameEditText.getText().length());
            String doctorText = appointmentCursor.getString(appointmentCursor.getColumnIndex(WomansHealthContract.WomansHealthAppointment.COLUMN_DOCTOR_NAME));
            doctorsNameEditText.setText(doctorText);
            doctorsNameEditText.setSelection(doctorsNameEditText.getText().length());
            String addressText = appointmentCursor.getString(appointmentCursor.getColumnIndex(WomansHealthContract.WomansHealthAppointment.COLUMN_ADDRESS));
            addressEditText.setText(addressText);
            addressEditText.setSelection(addressEditText.getText().length());
            String tel1Text = appointmentCursor.getString(appointmentCursor.getColumnIndex(WomansHealthContract.WomansHealthAppointment.COLUMN_TELEPHONE1));
            telephone1EditText.setText(tel1Text);
            telephone1EditText.setSelection(telephone1EditText.getText().length());
            String tel2Text = appointmentCursor.getString(appointmentCursor.getColumnIndex(WomansHealthContract.WomansHealthAppointment.COLUMN_TELEPHONE2));
            telephone2EditText.setText(tel2Text);
            telephone2EditText.setSelection(telephone2EditText.getText().length());
            String emailText = appointmentCursor.getString(appointmentCursor.getColumnIndex(WomansHealthContract.WomansHealthAppointment.COLUMN_EMAIL));
            emailEditText.setText(emailText);
            emailEditText.setSelection(emailEditText.getText().length());
            String lastDateText = appointmentCursor.getString(appointmentCursor.getColumnIndex(WomansHealthContract.WomansHealthAppointment.COLUMN_LAST_APPOINTMENT));
            lastDateTextView.setText(lastDateText);
            String nextDateText = appointmentCursor.getString(appointmentCursor.getColumnIndex(WomansHealthContract.WomansHealthAppointment.COLUMN_NEXT_APPOINTMENT));
            nextDateTextView.setText(nextDateText);
            String timeText = appointmentCursor.getString(appointmentCursor.getColumnIndex(WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_TIME));
            nextDateTimeTextView.setText(timeText);
        }
    }

    private ContentValues getAppointmentContentValues() {
        String appointmentName = appointmentNameEditText.getText().toString();
        String doctorsName = doctorsNameEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String telephone1 = telephone1EditText.getText().toString();
        String telephone2 = telephone2EditText.getText().toString();
        String email = emailEditText.getText().toString();
        String lastDate = lastDateTextView.getText().toString();
        String nextDate = nextDateTextView.getText().toString();
        String nextDateTime = nextDateTimeTextView.getText().toString();

        ContentValues values = new ContentValues();
        values.put(WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_NAME, appointmentName);
        values.put(WomansHealthContract.WomansHealthAppointment.COLUMN_DOCTOR_NAME, doctorsName);
        values.put(WomansHealthContract.WomansHealthAppointment.COLUMN_ADDRESS, address);
        values.put(WomansHealthContract.WomansHealthAppointment.COLUMN_TELEPHONE1, telephone1);
        values.put(WomansHealthContract.WomansHealthAppointment.COLUMN_TELEPHONE2, telephone2);
        values.put(WomansHealthContract.WomansHealthAppointment.COLUMN_EMAIL, email);
        values.put(WomansHealthContract.WomansHealthAppointment.COLUMN_LAST_APPOINTMENT, lastDate);
        values.put(WomansHealthContract.WomansHealthAppointment.COLUMN_NEXT_APPOINTMENT, nextDate);
        values.put(WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_TIME, nextDateTime);

        return values;
    }
}
