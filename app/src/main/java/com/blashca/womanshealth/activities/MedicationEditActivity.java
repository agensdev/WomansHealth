package com.blashca.womanshealth.activities;


import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.blashca.womanshealth.DateReceiver;
import com.blashca.womanshealth.DateUtil;
import com.blashca.womanshealth.R;
import com.blashca.womanshealth.data.WomansHealthContract;
import com.blashca.womanshealth.data.WomansHealthDbHelper;
import com.blashca.womanshealth.fragments.DatePickerFragment;
import com.blashca.womanshealth.fragments.TimePickerFragment;
import com.blashca.womanshealth.models.Medication;

import java.text.DateFormat;
import java.util.Date;


public class MedicationEditActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DateReceiver {
    private DateFormat dateFormat;
    private static String MEDICATION_ID = "medicationId";
    private long medicationId;
    private WomansHealthDbHelper dbHelper;

    private EditText nameEditText;
    private EditText dosageEditText;
    private EditText numberEditText;
    private TextView howOftenTextView;
    private TextView commencementTextView;
    private TextView medicationTimeTextView;
    private TextView howLongTextView;
    private Spinner howTakenSpinner;
    private Switch medicationReminderSwitch;

    private Medication medication;

    private String[] howTakenArray;
    private String[] frequencyArray;
    private String[] lengthArray;

    private LinearLayout commencementLinearLayout;
    private LinearLayout medicationTimeLinearLayout;
    private View commencementLine;
    private View medicationTimeLine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medications_edit);

        dbHelper = new WomansHealthDbHelper(this);
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG);

        if (getIntent().getExtras() != null) {
            medicationId = getIntent().getExtras().getLong(MEDICATION_ID);
        }
        medication = dbHelper.loadMedicationDataFromDb(medicationId);

        nameEditText = (EditText) findViewById(R.id.medicine_name_editText);
        dosageEditText = (EditText) findViewById(R.id.dosage_editText);
        numberEditText = (EditText) findViewById(R.id.number_editText);
        howOftenTextView = (TextView) findViewById(R.id.how_often_set_textView);
        commencementTextView = (TextView) findViewById(R.id.commencement_set_textView);
        medicationTimeTextView = (TextView) findViewById(R.id.medication_time_set_textView);
        howLongTextView = (TextView) findViewById(R.id.how_long_set_textView);
        frequencyArray = getResources().getStringArray(R.array.frequency_array);
        lengthArray = getResources().getStringArray(R.array.length_array);

        commencementLinearLayout = (LinearLayout) findViewById(R.id.commencement_date_layout);
        medicationTimeLinearLayout = (LinearLayout) findViewById(R.id.medication_time_layout);
        commencementLine = (View) findViewById(R.id.commencement_line);
        medicationTimeLine = (View) findViewById(R.id.medications_time_line);
        medicationReminderSwitch = (Switch) findViewById(R.id.medication_reminder_switch);

        addChangedTextListeners();

        initHowTakenSpinner();
        refreshUI();
    }

    public void initHowTakenSpinner() {

        howTakenSpinner = (Spinner) findViewById(R.id.how_taken_spinner);
        howTakenArray = getResources().getStringArray(R.array.how_taken_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, howTakenArray) {

            //Override below two methods to display hint in the spinner
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
        if (medication.howTaken == -1) {
            medication.howTaken = howTakenSpinner.getCount();
        }

        howTakenSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // An item was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)
                medication.howTaken = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        medication.medicationHour = hourOfDay;
        medication.medicationMinute = minute;

        medicationTimeTextView.setText(medication.getMedicationTime());

        refreshUI();
    }

    public void showHowOftenPicker(View v) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.frequency_spinner, null);

        final NumberPicker howOftenNumberPicker = (NumberPicker) view.findViewById(R.id.number_picker);
        final NumberPicker howOftenPeriodPicker = (NumberPicker) view.findViewById(R.id.frequency_picker);

        howOftenNumberPicker.setMinValue(1);
        howOftenNumberPicker.setMaxValue(31);

        howOftenPeriodPicker.setMaxValue(frequencyArray.length - 1);
        howOftenPeriodPicker.setDisplayedValues(frequencyArray);

        if (medication.howOftenNumber != 0) {
            howOftenNumberPicker.setValue(medication.howOftenNumber);
            howOftenPeriodPicker.setValue(medication.howOftenPeriod);

            refreshUI();
        }


        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        medication.howOftenNumber = howOftenNumberPicker.getValue();
                        medication.howOftenPeriod = howOftenPeriodPicker.getValue();

                        refreshUI();
                    }
                })
                .setNeutralButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        medication.howOftenNumber = 0;
                        medication.howOftenPeriod = 0;
                        medication.commencementDate = null;
                        medication.medicationHour = 0;
                        medication.medicationMinute = 0;

                        refreshUI();
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
        DialogFragment newFragment = new DatePickerFragment(this, v.getId(), medication.commencementDate);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment(medication.medicationHour, medication.medicationMinute);
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void showHowLongPicker(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.length_spinner, null);

        final NumberPicker lengthNumberPicker = (NumberPicker) view.findViewById(R.id.number_length_picker);
        final NumberPicker lengthPeriodPicker = (NumberPicker) view.findViewById(R.id.length_picker);

        lengthNumberPicker.setMinValue(1);
        lengthNumberPicker.setMaxValue(31);

        lengthPeriodPicker.setMaxValue(lengthArray.length - 1);
        lengthPeriodPicker.setDisplayedValues(lengthArray);

        if (medication.howLongNumber != 0) {
            lengthNumberPicker.setValue(medication.howLongNumber);
            lengthPeriodPicker.setValue(medication.howLongPeriod);

            refreshUI();
        }


        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        medication.howLongNumber = lengthNumberPicker.getValue();
                        medication.howLongPeriod = lengthPeriodPicker.getValue();

                        refreshUI();
                    }
                })
                .setNeutralButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        medication.howLongNumber = 0;
                        medication.howLongPeriod = 0;

                        refreshUI();
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

        if (medication.name.equals("")) {
            Toast.makeText(getApplicationContext(), R.string.record_button_toast_message, Toast.LENGTH_SHORT).show();
            return;
        }

        if (medicationId != 0) {
            dbHelper.updateMedication(getMedicationContentValues(), medicationId);
        } else {
            dbHelper.insertMedication(getMedicationContentValues());
        }

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    public void refreshUI() {

        nameEditText.setText(medication.name);
        nameEditText.setSelection(nameEditText.getText().length());

        dosageEditText.setText(medication.dosage);
        dosageEditText.setSelection(dosageEditText.getText().length());

        if (medication.number != 0) {
            numberEditText.setText("" + medication.number);
            numberEditText.setSelection(numberEditText.getText().length());
        } else {
            numberEditText.setText("");
        }


        if (medication.howTaken != howTakenArray.length - 1) {
            howTakenSpinner.setSelection(medication.howTaken);
        } else {
            howTakenSpinner.setSelection(howTakenSpinner.getCount());
        }


        if (medication.howOftenNumber != 0) {
            if (medication.howOftenNumber == 1) {
                howOftenTextView.setText(getString(R.string.once) + " " + frequencyArray[medication.howOftenPeriod]);
            } else if (medication.howOftenNumber == 2) {
                howOftenTextView.setText(getString(R.string.twice) + " " + frequencyArray[medication.howOftenPeriod]);
            } else if (medication.howOftenNumber == 3) {
                howOftenTextView.setText(getString(R.string.thrice) + " " + frequencyArray[medication.howOftenPeriod]);
            } else {
                howOftenTextView.setText(medication.howOftenNumber + " " + getString(R.string.times) + " " + frequencyArray[medication.howOftenPeriod]);
            }

            commencementLinearLayout.setVisibility(View.VISIBLE);
            medicationTimeLinearLayout.setVisibility(View.VISIBLE);
            commencementLine.setVisibility(View.VISIBLE);
            medicationTimeLine.setVisibility(View.VISIBLE);
        } else {
            howOftenTextView.setText("");

//            commencementLinearLayout.setVisibility(View.INVISIBLE);
//            medicationTimeLinearLayout.setVisibility(View.INVISIBLE);
//            commencementLine.setVisibility(View.INVISIBLE);
//            medicationTimeLine.setVisibility(View.INVISIBLE);
        }


        if (medication.commencementDate != null) {
            commencementTextView.setText(dateFormat.format(medication.commencementDate));
        }

        medicationTimeTextView.setText(medication.getMedicationTime());

        if (medication.howLongNumber != 0) {
            howLongTextView.setText(medication.howLongNumber + " " + lengthArray[medication.howLongPeriod]);
        } else {
            howLongTextView.setText("");
        }

        if (medication.reminder) {
            medicationReminderSwitch.setChecked(true);
        }

    }


    private ContentValues getMedicationContentValues() {
        long commencementDateLong = 0;
        if (medication.commencementDate != null) {
            commencementDateLong = medication.commencementDate.getTime();
        }

        medication.reminder = medicationReminderSwitch.isChecked();
        int medicationReminderInt;
        if (medication.reminder) {
            medicationReminderInt = 1;
        } else {
            medicationReminderInt = 0;
        }


        ContentValues values = new ContentValues();
        values.put(WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_NAME, medication.name);
        values.put(WomansHealthContract.WomansHealthMedication.COLUMN_DOSAGE, medication.dosage);
        values.put(WomansHealthContract.WomansHealthMedication.COLUMN_NUMBER, medication.number);
        values.put(WomansHealthContract.WomansHealthMedication.COLUMN_HOW_TAKEN, medication.howTaken);
        values.put(WomansHealthContract.WomansHealthMedication.COLUMN_HOW_OFTEN_NUMBER, medication.howOftenNumber);
        values.put(WomansHealthContract.WomansHealthMedication.COLUMN_HOW_OFTEN_PERIOD, medication.howOftenPeriod);
        values.put(WomansHealthContract.WomansHealthMedication.COLUMN_COMMENCEMENT_DATE, commencementDateLong);
        values.put(WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOUR, medication.medicationHour);
        values.put(WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTE, medication.medicationMinute);
        values.put(WomansHealthContract.WomansHealthMedication.COLUMN_HOW_LONG_NUMBER, medication.howLongNumber);
        values.put(WomansHealthContract.WomansHealthMedication.COLUMN_HOW_LONG_PERIOD, medication.howLongPeriod);
        values.put(WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_REMINDER, medicationReminderInt);

        return values;
    }

    @Override
    public void onDateReceive(Date date, int id) {
        medication.commencementDate = DateUtil.removeTime(date);
        refreshUI();
    }

    private void addChangedTextListeners() {
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!nameEditText.getText().toString().equals("")) {
                    medication.name = nameEditText.getText().toString().trim();
                } else {
                    medication.name = "";
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        dosageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                medication.dosage = dosageEditText.getText().toString().trim();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        numberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    medication.number = Integer.parseInt(numberEditText.getText().toString());
                } catch (NumberFormatException e) {
                    medication.number = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });




//        commencementTextView.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                commencementDate = commencementTextView.getText().toString();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {}
//        });
//
//        medicationTimeTextView.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                medicationTime = medicationTimeTextView.getText().toString();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {}
//        });
    }
}