package com.blashca.womanshealth;


import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.blashca.womanshealth.data.WomansHealthContract;
import com.blashca.womanshealth.data.WomansHealthDbHelper;

import java.text.DateFormat;
import java.util.Date;

public class MedicationEditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, TimePickerDialog.OnTimeSetListener, DateReceiver {
    private DateFormat dateFormat;
    private String formattedDate;
    private static String MEDICATION_ID = "medicationId";
    private long medicationId;
    private WomansHealthDbHelper dbHelper;
    private Cursor medicationCursor;
    private int numberNewValue;
    private int frequencyNewValue;
    private int numberLengthNewValue;
    private int lengthNewValue;
    private EditText nameEditText;
    private EditText dosageEditText;
    private EditText numberEditText;
    private Spinner howTakenSpinner;
    private TextView howOftenTextView;
    private TextView commencementTextView;
    private TextView medicationTimeTextView;
    private TextView howLongTextView;

    private String[] frequency;
    private String[] length;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medications_edit);

        if (getIntent().getExtras() != null) {
            medicationId = getIntent().getExtras().getLong(MEDICATION_ID);
        }

        dbHelper = new WomansHealthDbHelper(this);
        medicationCursor = dbHelper.getMedicationIdCursor(medicationId);

        dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        formattedDate = dateFormat.format(new Date());

        nameEditText = (EditText) findViewById(R.id.medicine_name_editText);
        dosageEditText = (EditText) findViewById(R.id.dosage_editText);
        numberEditText = (EditText) findViewById(R.id.number_editText);
        howTakenSpinner = (Spinner) findViewById(R.id.how_taken_spinner);
        howOftenTextView = (TextView) findViewById(R.id.how_often_set_textView);
        commencementTextView = (TextView) findViewById(R.id.commencement_set_textView);
        medicationTimeTextView = (TextView) findViewById(R.id.medication_time_set_textView);
        howLongTextView = (TextView) findViewById(R.id.how_long_set_textView);

        frequency = getResources().getStringArray(R.array.frequency_array);
        length = getResources().getStringArray(R.array.length_array);

        setMedicationDataFromDb();


        numberNewValue = 1;
        frequencyNewValue = 0;
        numberLengthNewValue = 1;
        lengthNewValue = 0;


        String[] howTakenArray = getResources().getStringArray(R.array.how_taken_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, howTakenArray) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView)v.findViewById(android.R.id.text1)).setText("");
                    ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }
                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1;
            }
        };
        howTakenSpinner.setAdapter(adapter);
        howTakenSpinner.setSelection(adapter.getCount());
        howTakenSpinner.setOnItemSelectedListener(this);


//        // Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.how_taken_array, android.R.layout.simple_spinner_item);
//
//
//        // Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        // Apply the adapter to the spinner
//        spinner.setAdapter(adapter);
//        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        medicationTimeTextView.setText(new StringBuilder().append(hourOfDay).append(':')
                .append(minute).append(" ").toString());
    }

    public void showFrequencyDialog(View v) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.frequency_spinner, null);

        final NumberPicker numberPicker = (NumberPicker) view.findViewById(R.id.number_picker);
        final NumberPicker frequencyPicker = (NumberPicker) view.findViewById(R.id.frequency_picker);

        numberPicker.setValue(1);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(31);

        frequencyPicker.setMinValue(0);
        frequencyPicker.setMaxValue(frequency.length - 1);
        frequencyPicker.setDisplayedValues(frequency);

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal != oldVal) {
                    numberNewValue = newVal;
                } else {
                    numberNewValue = oldVal;
                }
            }
        });

        frequencyPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal != oldVal) {
                    frequencyNewValue = newVal;
                } else {
                    frequencyNewValue = oldVal;
                }
            }
        });

        final LinearLayout commencementLinearLayout = (LinearLayout) findViewById(R.id.commencement_date_linearLayout);
        final LinearLayout medicationTimeLinearLayout = (LinearLayout) findViewById(R.id.medication_time_linearLayout);
        final View commencementLine = (View) findViewById(R.id.commencement_line);
        final View medicationTimeLine = (View) findViewById(R.id.medications_time_line);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (numberNewValue == 1) {
                            howOftenTextView.setText(getString(R.string.once) + " " + frequency[frequencyNewValue]);
                        } else if (numberNewValue == 2) {
                            howOftenTextView.setText(getString(R.string.twice) + " " + frequency[frequencyNewValue]);
                        } else if (numberNewValue == 3) {
                            howOftenTextView.setText(getString(R.string.thrice) + " " + frequency[frequencyNewValue]);
                        } else {
                            howOftenTextView.setText(numberNewValue + " " + getString(R.string.times) + " " + frequency[frequencyNewValue]);
                        }
                        commencementLinearLayout.setVisibility(View.VISIBLE);
                        medicationTimeLinearLayout.setVisibility(View.VISIBLE);
                        commencementLine.setVisibility(View.VISIBLE);
                        medicationTimeLine.setVisibility(View.VISIBLE);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder.create();
        builder.show();
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment(this, v.getId());
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void showLengthDialog(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.length_spinner, null);


        final NumberPicker numberLengthPicker = (NumberPicker) view.findViewById(R.id.number_length_picker);
        final NumberPicker lengthPicker = (NumberPicker) view.findViewById(R.id.length_picker);

        numberLengthPicker.setValue(1);
        numberLengthPicker.setMinValue(1);
        numberLengthPicker.setMaxValue(31);

        lengthPicker.setMinValue(0);
        lengthPicker.setMaxValue(length.length - 1);
        lengthPicker.setDisplayedValues(length);

        numberLengthPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal != oldVal) {
                    numberLengthNewValue = newVal;
                } else {
                    numberLengthNewValue = oldVal;
                }

            }
        });

        lengthPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal != oldVal) {
                    lengthNewValue = newVal;
                } else {
                    lengthNewValue = oldVal;
                }
            }
        });

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        howLongTextView.setText(numberLengthNewValue + " " + length[lengthNewValue]);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.create();
        builder.show();
    }

    public void onRecordMedicationButtonClicked(View v) {
        String newMedicineName = nameEditText.getText().toString().trim();
        if (medicationId != 0) {
            if (!newMedicineName.equals("")) {
                dbHelper.updateMedication(getMedicationContentValues(), medicationId);

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), R.string.record_button_toast_message, Toast.LENGTH_SHORT).show();
            }

        } else {
            if (!newMedicineName.equals("")) {
                dbHelper.insertMedication(getMedicationContentValues());

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), R.string.record_button_toast_message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setMedicationDataFromDb() {
        if (medicationId != 0) {
            String nameText = medicationCursor.getString(medicationCursor.getColumnIndex(WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_NAME));
            nameEditText.setText(nameText);
            nameEditText.setSelection(nameEditText.getText().length());
            String dosageText = medicationCursor.getString(medicationCursor.getColumnIndex(WomansHealthContract.WomansHealthMedication.COLUMN_DOSAGE));
            dosageEditText.setText(dosageText);
            dosageEditText.setSelection(dosageEditText.getText().length());
            int numberData = medicationCursor.getInt(medicationCursor.getColumnIndex(WomansHealthContract.WomansHealthMedication.COLUMN_NUMBER));
            String numberText = "" + numberData;
            numberEditText.setText(numberText);
            numberEditText.setSelection(numberEditText.getText().length());
            int howTakenData = medicationCursor.getInt(medicationCursor.getColumnIndex(WomansHealthContract.WomansHealthMedication.COLUMN_HOW_TAKEN));
            howTakenSpinner.setSelection(howTakenData);
            int howOftenData = medicationCursor.getInt(medicationCursor.getColumnIndex(WomansHealthContract.WomansHealthMedication.COLUMN_HOW_OFTEN));
            String howOftenText = "" + howOftenData;
            howOftenTextView.setText(howOftenText);
            String commencementText = medicationCursor.getString(medicationCursor.getColumnIndex(WomansHealthContract.WomansHealthMedication.COLUMN_COMMENCEMENT));
            commencementTextView.setText(commencementText);
            String timeText = medicationCursor.getString(medicationCursor.getColumnIndex(WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_TIME));
            medicationTimeTextView.setText(timeText);
            int howLongData = medicationCursor.getInt(medicationCursor.getColumnIndex(WomansHealthContract.WomansHealthMedication.COLUMN_HOW_LONG));
            String howLongText = "" + howLongData;
            howLongTextView.setText(howLongText);
        }
    }

    private ContentValues getMedicationContentValues() {
        String name = nameEditText.getText().toString();
        String dosage = dosageEditText.getText().toString();
        //int number = numberEditText.getText().toString();
        int howTaken = howTakenSpinner.getSelectedItemPosition();
        String howOften = howOftenTextView.getText().toString();
        String commencement = commencementTextView.getText().toString();
        String medicationTime = medicationTimeTextView.getText().toString();
        String howLong = howLongTextView.getText().toString();

        ContentValues values = new ContentValues();
        values.put(WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_NAME, name);
        values.put(WomansHealthContract.WomansHealthMedication.COLUMN_DOSAGE, dosage);
        values.put(WomansHealthContract.WomansHealthMedication.COLUMN_HOW_TAKEN, howTaken);
        values.put(WomansHealthContract.WomansHealthMedication.COLUMN_HOW_OFTEN, howOften);
        values.put(WomansHealthContract.WomansHealthMedication.COLUMN_COMMENCEMENT, commencement);
        values.put(WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_TIME, medicationTime);
        values.put(WomansHealthContract.WomansHealthMedication.COLUMN_HOW_LONG, howLong);

        return values;
    }

    @Override
    public void onDateReceive(Date date, int id) {
        formattedDate = dateFormat.format(date);
        commencementTextView.setText(formattedDate);
    }
}