package com.blashca.womanshealth.activities;


import android.app.DialogFragment;
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
import android.widget.Toast;

import com.blashca.womanshealth.DateReceiver;
import com.blashca.womanshealth.DateUtil;
import com.blashca.womanshealth.R;
import com.blashca.womanshealth.TimeReceiver;
import com.blashca.womanshealth.data.WomansHealthContract;
import com.blashca.womanshealth.data.WomansHealthDbHelper;
import com.blashca.womanshealth.fragments.DatePickerFragment;
import com.blashca.womanshealth.fragments.TimePickerFragment;
import com.blashca.womanshealth.models.Medication;

import java.text.DateFormat;
import java.util.Date;


public class MedicationEditActivity extends AppCompatActivity implements DateReceiver, TimeReceiver {

    private DateFormat dateFormat;
    private static String MEDICATION_ID = "medicationId";
    private long medicationId;
    private WomansHealthDbHelper dbHelper;

    private EditText nameEditText;
    private EditText dosageEditText;
    private EditText numberEditText;
    private TextView howOftenTextView;
    private TextView commencementTextView;
    private TextView[] medicationTimeTextViewsArray = new TextView[12];
    private TextView howLongTextView;
    private Spinner howTakenSpinner;
    private Switch medicationReminderSwitch;

    private Medication medication;

    private String[] howTakenArray;
    private String[] frequencyArray;
    private String[] lengthArray;

    private LinearLayout commencementLinearLayout;
    private LinearLayout medicationTimeLinearLayout;
    private LinearLayout[] linearLayoutsArrayList = new LinearLayout[12];
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
        medicationTimeTextViewsArray[0] = (TextView) findViewById(R.id.medication_time_set1_textView);
        medicationTimeTextViewsArray[1] = (TextView) findViewById(R.id.medication_time_set2_textView);
        medicationTimeTextViewsArray[2] = (TextView) findViewById(R.id.medication_time_set3_textView);
        medicationTimeTextViewsArray[3] = (TextView) findViewById(R.id.medication_time_set4_textView);
        medicationTimeTextViewsArray[4] = (TextView) findViewById(R.id.medication_time_set5_textView);
        medicationTimeTextViewsArray[5] = (TextView) findViewById(R.id.medication_time_set6_textView);
        medicationTimeTextViewsArray[6] = (TextView) findViewById(R.id.medication_time_set7_textView);
        medicationTimeTextViewsArray[7] = (TextView) findViewById(R.id.medication_time_set8_textView);
        medicationTimeTextViewsArray[8] = (TextView) findViewById(R.id.medication_time_set9_textView);
        medicationTimeTextViewsArray[9] = (TextView) findViewById(R.id.medication_time_set10_textView);
        medicationTimeTextViewsArray[10] = (TextView) findViewById(R.id.medication_time_set11_textView);
        medicationTimeTextViewsArray[11] = (TextView) findViewById(R.id.medication_time_set12_textView);
        howLongTextView = (TextView) findViewById(R.id.how_long_set_textView);
        frequencyArray = getResources().getStringArray(R.array.frequency_array);
        lengthArray = getResources().getStringArray(R.array.length_array);

        commencementLinearLayout = (LinearLayout) findViewById(R.id.commencement_date_layout);
        medicationTimeLinearLayout = (LinearLayout) findViewById(R.id.medication_time_layout);
        linearLayoutsArrayList[0] = (LinearLayout) findViewById(R.id.medication_time_set1_linearLayout);
        linearLayoutsArrayList[1] = (LinearLayout) findViewById(R.id.medication_time_set2_linearLayout);
        linearLayoutsArrayList[2] = (LinearLayout) findViewById(R.id.medication_time_set3_linearLayout);
        linearLayoutsArrayList[3] = (LinearLayout) findViewById(R.id.medication_time_set4_linearLayout);
        linearLayoutsArrayList[4] = (LinearLayout) findViewById(R.id.medication_time_set5_linearLayout);
        linearLayoutsArrayList[5] = (LinearLayout) findViewById(R.id.medication_time_set6_linearLayout);
        linearLayoutsArrayList[6] = (LinearLayout) findViewById(R.id.medication_time_set7_linearLayout);
        linearLayoutsArrayList[7] = (LinearLayout) findViewById(R.id.medication_time_set8_linearLayout);
        linearLayoutsArrayList[8] = (LinearLayout) findViewById(R.id.medication_time_set9_linearLayout);
        linearLayoutsArrayList[9] = (LinearLayout) findViewById(R.id.medication_time_set10_linearLayout);
        linearLayoutsArrayList[10] = (LinearLayout) findViewById(R.id.medication_time_set11_linearLayout);
        linearLayoutsArrayList[11] = (LinearLayout) findViewById(R.id.medication_time_set12_linearLayout);
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


    public void showHowOftenPicker(View v) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.frequency_spinner, null);

        final NumberPicker howOftenNumberPicker = (NumberPicker) view.findViewById(R.id.number_picker);
        final NumberPicker howOftenPeriodPicker = (NumberPicker) view.findViewById(R.id.frequency_picker);

        howOftenNumberPicker.setMinValue(1);
        howOftenNumberPicker.setMaxValue(12);

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

                        for (int i = 0; i < medication.medicationHourArray.length; i++) {
                            medication.medicationHourArray[i] = -1;
                            medication.medicationMinuteArray[i] = -1;
                        }

                        refreshUI();
                    }
                })
                .setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        medication.howOftenNumber = 0;
                        medication.howOftenPeriod = 0;
                        medication.commencementDate = null;

                        for (int i = 0; i < medication.medicationHourArray.length; i++) {
                            medication.medicationHourArray[i] = -1;
                            medication.medicationMinuteArray[i] = -1;
                        }

                        refreshUI();
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
        int index = mapViewIdOnIndex(v);

        DialogFragment newFragment = new TimePickerFragment(this, index, medication.medicationHourArray[index],
                medication.medicationMinuteArray[index]);
        newFragment.show(getFragmentManager(), "timePicker");
    }

    @Override
    public void onDateReceive(Date date, int id) {
        medication.commencementDate = DateUtil.removeTime(date);
        refreshUI();
    }

    @Override
    public void onTimeReceive(int index, int hour, int minute) {
        medication.medicationHourArray[index] = hour;
        medication.medicationMinuteArray[index] = minute;
        medicationTimeTextViewsArray[index].setText(medication.getMedicationTime(index));

        refreshUI();
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
                .setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        medication.howLongNumber = 0;
                        medication.howLongPeriod = 0;

                        refreshUI();

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
            commencementLine.setVisibility(View.VISIBLE);
            medicationTimeLinearLayout.setVisibility(View.VISIBLE);

            for (int i = 0; i < linearLayoutsArrayList.length; i++) {
                if (i < medication.howOftenNumber) {
                    linearLayoutsArrayList[i].setVisibility(View.VISIBLE);
                    medicationTimeTextViewsArray[i].setText(medication.getMedicationTime(i));
                } else {
                    linearLayoutsArrayList[i].setVisibility(View.GONE);
                }
            }

            medicationTimeLine.setVisibility(View.VISIBLE);
        } else {
            howOftenTextView.setText("");

            commencementLinearLayout.setVisibility(View.GONE);
            commencementLine.setVisibility(View.GONE);
            medicationTimeLinearLayout.setVisibility(View.GONE);
            medicationTimeLine.setVisibility(View.GONE);

            for (int i = 0; i < linearLayoutsArrayList.length; i++) {
                linearLayoutsArrayList[i].setVisibility(View.GONE);
            }
        }


        if (medication.commencementDate != null) {
            commencementTextView.setText(dateFormat.format(medication.commencementDate));
        }


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
            onReminderEnabled();
        } else {
            medicationReminderInt = 0;
            onReminderDisabled();
        }


        ContentValues values = new ContentValues();
        values.put(WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_NAME, medication.name);
        values.put(WomansHealthContract.WomansHealthMedication.COLUMN_DOSAGE, medication.dosage);
        values.put(WomansHealthContract.WomansHealthMedication.COLUMN_NUMBER, medication.number);
        values.put(WomansHealthContract.WomansHealthMedication.COLUMN_HOW_TAKEN, medication.howTaken);
        values.put(WomansHealthContract.WomansHealthMedication.COLUMN_HOW_OFTEN_NUMBER, medication.howOftenNumber);
        values.put(WomansHealthContract.WomansHealthMedication.COLUMN_HOW_OFTEN_PERIOD, medication.howOftenPeriod);
        values.put(WomansHealthContract.WomansHealthMedication.COLUMN_COMMENCEMENT_DATE, commencementDateLong);

        for (int i = 0; i < medication.medicationHourArray.length; i++) {
            values.put(WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_HOURS[i], medication.medicationHourArray[i]);
        }

        for (int i = 0; i < medication.medicationMinuteArray.length; i++) {
            values.put(WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_MINUTES[i], medication.medicationMinuteArray[i]);
        }

        values.put(WomansHealthContract.WomansHealthMedication.COLUMN_HOW_LONG_NUMBER, medication.howLongNumber);
        values.put(WomansHealthContract.WomansHealthMedication.COLUMN_HOW_LONG_PERIOD, medication.howLongPeriod);
        values.put(WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_REMINDER, medicationReminderInt);

        return values;
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

    private int mapViewIdOnIndex(View v) {
        int index = -1;

        if (v.getId() == R.id.medication_time_set1_textView) {
            index = 0;
        } else if (v.getId() == R.id.medication_time_set2_textView) {
            index = 1;
        } else if (v.getId() == R.id.medication_time_set3_textView) {
            index = 2;
        } else if (v.getId() == R.id.medication_time_set4_textView) {
            index = 3;
        } else if (v.getId() == R.id.medication_time_set5_textView) {
            index = 4;
        } else if (v.getId() == R.id.medication_time_set6_textView) {
            index = 5;
        } else if (v.getId() == R.id.medication_time_set7_textView) {
            index = 6;
        } else if (v.getId() == R.id.medication_time_set8_textView) {
            index = 7;
        } else if (v.getId() == R.id.medication_time_set9_textView) {
            index = 8;
        } else if (v.getId() == R.id.medication_time_set10_textView) {
            index = 9;
        } else if (v.getId() == R.id.medication_time_set11_textView) {
            index = 10;
        } else if (v.getId() == R.id.medication_time_set12_textView) {
            index = 11;
        }

        return index;
    }

    private void onReminderEnabled() {

    }

    private void onReminderDisabled() {

    }
}