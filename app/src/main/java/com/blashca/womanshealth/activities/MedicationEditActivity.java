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

import com.blashca.womanshealth.AlarmHelper;
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
    private AlarmHelper alarmHelper = new AlarmHelper();

    private String[] howTakenArray;
    private String[] frequencyArray;
    private String[] lengthArray;

    private LinearLayout commencementLinearLayout;
    private LinearLayout medicationTimeLinearLayout;
    private LinearLayout[] linearLayoutsArray = new LinearLayout[12];
    private View commencementLine;
    private View medicationTimeLine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medications_edit);

        dbHelper = new WomansHealthDbHelper(this);
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG);

        Long medicationId = null;
        if (getIntent().getExtras() != null) {
            medicationId = getIntent().getExtras().getLong(MEDICATION_ID);
        }

        if (medicationId != null) {
            medication = dbHelper.loadMedication(medicationId);
        } else {
            medication = new Medication();
        }

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
        linearLayoutsArray[0] = (LinearLayout) findViewById(R.id.medication_time_set1_linearLayout);
        linearLayoutsArray[1] = (LinearLayout) findViewById(R.id.medication_time_set2_linearLayout);
        linearLayoutsArray[2] = (LinearLayout) findViewById(R.id.medication_time_set3_linearLayout);
        linearLayoutsArray[3] = (LinearLayout) findViewById(R.id.medication_time_set4_linearLayout);
        linearLayoutsArray[4] = (LinearLayout) findViewById(R.id.medication_time_set5_linearLayout);
        linearLayoutsArray[5] = (LinearLayout) findViewById(R.id.medication_time_set6_linearLayout);
        linearLayoutsArray[6] = (LinearLayout) findViewById(R.id.medication_time_set7_linearLayout);
        linearLayoutsArray[7] = (LinearLayout) findViewById(R.id.medication_time_set8_linearLayout);
        linearLayoutsArray[8] = (LinearLayout) findViewById(R.id.medication_time_set9_linearLayout);
        linearLayoutsArray[9] = (LinearLayout) findViewById(R.id.medication_time_set10_linearLayout);
        linearLayoutsArray[10] = (LinearLayout) findViewById(R.id.medication_time_set11_linearLayout);
        linearLayoutsArray[11] = (LinearLayout) findViewById(R.id.medication_time_set12_linearLayout);
        commencementLine = (View) findViewById(R.id.commencement_line);
        medicationTimeLine = (View) findViewById(R.id.medications_time_line);
        medicationReminderSwitch = (Switch) findViewById(R.id.medication_reminder_switch);

        initHowTakenSpinner();
        refreshUI();
        addChangedTextListeners();
    }

    private void initHowTakenSpinner() {
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

        if (medication.howTaken == null) {
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

        if (medication.howOftenNumber != null) {
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
                            medication.medicationHourArray[i] = null;
                            medication.medicationMinuteArray[i] = null;
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
                        medication.howOftenNumber = null;
                        medication.howOftenPeriod = null;
                        medication.commencementDate = null;

                        for (int i = 0; i < medication.medicationHourArray.length; i++) {
                            medication.medicationHourArray[i] = null;
                            medication.medicationMinuteArray[i] = null;
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
        medication.commencementDate = DateUtil.resetTime(date);
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

        if (medication.howLongNumber != null) {
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
                        medication.howLongNumber = null;
                        medication.howLongPeriod = null;

                        refreshUI();
                    }
                });

        builder.create();
        builder.show();
    }


    public void onRecordMedicationButtonClicked(View v) {

        if (medication.name == null) {
            Toast.makeText(getApplicationContext(), R.string.empty_name_toast_message, Toast.LENGTH_SHORT).show();
            return;
        }

        if (medication.id != null) {
            dbHelper.updateMedication(getMedicationContentValues(), medication.id);
        } else {
            medication.id = dbHelper.insertMedication(getMedicationContentValues());
        }

        alarmHelper.cancelDailyMedicationAlarms(this, medication);
        alarmHelper.cancelWeeklyMedicationAlarm(this, medication);
        alarmHelper.cancelMonthlyMedicationAlarm(this, medication);
        alarmHelper.cancelYearlyMedicationAlarm(this, medication);

        if (medication.howOftenNumber != null) {
            if (medication.reminder) {
                alarmHelper.setMedicationAlarms(this, medication);
            }
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

        if (medication.number != null) {
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

        if (medication.howOftenNumber != null) {
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

            if (frequencyArray[medication.howOftenPeriod] == frequencyArray[0]) {
                for (int i = 0; i < linearLayoutsArray.length; i++) {
                    if (i < medication.howOftenNumber) {
                        linearLayoutsArray[i].setVisibility(View.VISIBLE);
                        medicationTimeTextViewsArray[i].setText(medication.getMedicationTime(i));
                    } else {
                        linearLayoutsArray[i].setVisibility(View.GONE);
                    }
                }
            } else {
                for (int i = 0; i < linearLayoutsArray.length; i++) {
                    if (i == 0) {
                        linearLayoutsArray[0].setVisibility(View.VISIBLE);
                        medicationTimeTextViewsArray[0].setText(medication.getMedicationTime(0));
                    } else {
                        linearLayoutsArray[i].setVisibility(View.GONE);
                    }
                }
            }

            medicationTimeLine.setVisibility(View.VISIBLE);
        } else {
            howOftenTextView.setText("");

            commencementLinearLayout.setVisibility(View.GONE);
            commencementLine.setVisibility(View.GONE);
            medicationTimeLinearLayout.setVisibility(View.GONE);
            medicationTimeLine.setVisibility(View.GONE);

            for (int i = 0; i < linearLayoutsArray.length; i++) {
                linearLayoutsArray[i].setVisibility(View.GONE);
            }
        }

        if (medication.commencementDate != null) {
            commencementTextView.setText(dateFormat.format(medication.commencementDate));
        } else {
            commencementTextView.setText("");
            commencementTextView.setHint(R.string.commencement_date);
        }

        if (medication.howLongNumber != null) {
            howLongTextView.setText(medication.howLongNumber + " " + lengthArray[medication.howLongPeriod]);
        } else {
            howLongTextView.setText("");
        }


        if (medication.howOftenPeriod != null
                && medication.commencementDate != null
                && (frequencyArray[medication.howOftenPeriod] == frequencyArray[0] || medication.howOftenNumber == 1)) {
            medicationReminderSwitch.setVisibility(View.VISIBLE);

            if (medication.reminder) {
                medicationReminderSwitch.setChecked(true);
            } else {
                medicationReminderSwitch.setChecked(false);
            }
        } else {
            medicationReminderSwitch.setVisibility(View.GONE);
        }
    }


    private ContentValues getMedicationContentValues() {
        Long commencementDateLong = null;
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
                    medication.name = null;
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

                if (!dosageEditText.getText().toString().equals("")) {
                    medication.dosage = dosageEditText.getText().toString().trim();
                } else {
                    medication.dosage = null;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        numberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!numberEditText.getText().toString().equals("")) {
                    medication.number = Integer.parseInt(numberEditText.getText().toString().trim());
                } else {
                    medication.dosage = null;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
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

    private int mapDeleteTimeViewIdOnIndex(View view) {
        int index = -1;

        if (view.getId() == R.id.medication_time1_delete) {
            index = 0;
        } else if (view.getId() == R.id.medication_time2_delete) {
            index = 1;
        } else if (view.getId() == R.id.medication_time3_delete) {
            index = 2;
        } else if (view.getId() == R.id.medication_time4_delete) {
            index = 3;
        } else if (view.getId() == R.id.medication_time5_delete) {
            index = 4;
        } else if (view.getId() == R.id.medication_time6_delete) {
            index = 5;
        } else if (view.getId() == R.id.medication_time7_delete) {
            index = 6;
        } else if (view.getId() == R.id.medication_time8_delete) {
            index = 7;
        } else if (view.getId() == R.id.medication_time9_delete) {
            index = 8;
        } else if (view.getId() == R.id.medication_time10_delete) {
            index = 9;
        } else if (view.getId() == R.id.medication_time11_delete) {
            index = 10;
        } else if (view.getId() == R.id.medication_time12_delete) {
            index = 11;
        }

        return index;
    }

    public void onCommencementDateDelete(View view) {
        medication.commencementDate = null;
        medication.reminder = false;
        refreshUI();
    }

    public void onMedicationTimeDelete(View view) {
        int index = mapDeleteTimeViewIdOnIndex(view);
        medication.medicationHourArray[index] = null;
        medication.medicationMinuteArray[index] = null;
        refreshUI();
    }
}