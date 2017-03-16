package com.blashca.womanshealth.adapters;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.blashca.womanshealth.R;
import com.blashca.womanshealth.data.WomansHealthContract;
import com.blashca.womanshealth.data.WomansHealthDbHelper;

public class MedicationCursorAdapter extends CursorAdapter {
    private LayoutInflater cursorInflater;
    private int defaultTextColor;

    public MedicationCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View newView(final Context context, Cursor cursor, ViewGroup parent) {
        View view = cursorInflater.inflate(R.layout.medications_list_item, parent, false);

        if (defaultTextColor == 0) {
            TextView medicationName = (TextView) view.findViewById(R.id.medication_name);
            defaultTextColor = medicationName.getCurrentTextColor();
        }

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        WomansHealthDbHelper dbHelper = new WomansHealthDbHelper(context);

        TextView medicationName = (TextView) view.findViewById(R.id.medication_name);
        TextView medicationDetail = (TextView) view.findViewById(R.id.medication_detail);

        String name = dbHelper.getStringFromCursor(cursor, WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_NAME);
        int isAllergen = cursor.getInt(cursor.getColumnIndex(WomansHealthContract.WomansHealthMedication.COLUMN_IS_ALLERGEN));
        Integer howOftenNumber = dbHelper.getIntegerFromCursor(cursor, WomansHealthContract.WomansHealthMedication.COLUMN_HOW_OFTEN_NUMBER);
        Integer howOftenPeriod = dbHelper.getIntegerFromCursor(cursor, WomansHealthContract.WomansHealthMedication.COLUMN_HOW_OFTEN_PERIOD);
        String sideEffects = dbHelper.getStringFromCursor(cursor, WomansHealthContract.WomansHealthMedication.COLUMN_ALLERGY_EFFECTS);
        String[] frequencyArray = context.getResources().getStringArray(R.array.frequency_array);


        if (isAllergen == 0) {
            medicationName.setText(name);
            if (howOftenNumber != null) {
                if (howOftenNumber == 1) {
                    medicationDetail.setText(context.getResources().getString(R.string.once) + " " + frequencyArray[howOftenPeriod]);
                } else if (howOftenNumber == 2) {
                    medicationDetail.setText(context.getResources().getString(R.string.twice) + " " + frequencyArray[howOftenPeriod]);
                } else if (howOftenNumber == 3) {
                    medicationDetail.setText(context.getResources().getString(R.string.thrice) + " " + frequencyArray[howOftenPeriod]);
                } else {
                    medicationDetail.setText(howOftenNumber + " " + context.getResources().getString(R.string.times) + " "
                            + frequencyArray[howOftenPeriod]);
                }
            } else {
                medicationDetail.setText("");
            }

            medicationName.setTextColor(defaultTextColor); //restore original color

        } else {
            medicationName.setText("Allergy to " + name);
            medicationName.setTextColor(Color.RED);

            medicationDetail.setText(sideEffects);
        }
    }
}
