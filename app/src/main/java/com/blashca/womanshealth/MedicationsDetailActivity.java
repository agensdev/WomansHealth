package com.blashca.womanshealth;


import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MedicationsDetailActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static String ID = "myIndex";
    private long itemId;
    private int numberNewValue;
    private int frequencyNewValue;
    private int numberLengthNewValue;
    private int lengthNewValue;
    private EditText nameEditText;

    private static String[] FREQUENCY = {"day", "week", "month", "year"};
    private static String[] LENGTH = {"days", "weeks", "months", "years"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medications_detail);

        itemId = getIntent().getExtras().getLong(ID);

        nameEditText = (EditText) findViewById(R.id.medicine_name_editText);
        numberNewValue = 1;
        frequencyNewValue = 0;
        numberLengthNewValue = 1;
        lengthNewValue = 0;


        Spinner spinner = (Spinner) findViewById(R.id.how_taken_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.how_taken_array, android.R.layout.simple_spinner_item);

        
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

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
        frequencyPicker.setMaxValue(FREQUENCY.length - 1);
        frequencyPicker.setDisplayedValues(FREQUENCY);

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

        final TextView howOftenTextView = (TextView) findViewById(R.id.how_often_set_textView);
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
                            howOftenTextView.setText(numberNewValue + " time a " + FREQUENCY[frequencyNewValue]);
                        } else {
                            howOftenTextView.setText(numberNewValue + " times a " + FREQUENCY[frequencyNewValue]);
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
        DialogFragment newFragment = new DatePickerFragment();
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
        lengthPicker.setMaxValue(LENGTH.length - 1);
        lengthPicker.setDisplayedValues(LENGTH);

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

        final TextView howLongTextView = (TextView) findViewById(R.id.how_long_set_textView);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        howLongTextView.setText(numberLengthNewValue + " " + LENGTH[lengthNewValue]);
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
        String newMedicineName = nameEditText.getText().toString();
        if (!newMedicineName.equals("")) {
            //dbHelper.updateItemText(newMedicineName, itemId);

            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), R.string.record_medication_button_toast_message, Toast.LENGTH_SHORT).show();
        }
    }
}
