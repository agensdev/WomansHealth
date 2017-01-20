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

        TextView medicationName = (TextView) view.findViewById(R.id.medication_name);

        TextView medicationDetail = (TextView) view.findViewById(R.id.medication_detail);

        String textName = cursor.getString(cursor.getColumnIndex(WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_NAME));
        int isAllergen = cursor.getInt(cursor.getColumnIndex(WomansHealthContract.WomansHealthMedication.COLUMN_IS_ALLERGEN));

        int howOften = cursor.getInt(cursor.getColumnIndex(WomansHealthContract.WomansHealthMedication.COLUMN_HOW_OFTEN_PERIOD));
        String sideEffects = cursor.getString(cursor.getColumnIndex(WomansHealthContract.WomansHealthMedication.COLUMN_ALLERGIES_EFFECTS));

        if (isAllergen == 0) {
            medicationName.setText(textName);
            medicationDetail.setText("" + howOften);

            medicationName.setTextColor(defaultTextColor);//restore original color

        } else {
            medicationName.setText("Allergy to " + textName);
            medicationName.setTextColor(Color.RED);

            medicationDetail.setText(sideEffects);
        }
    }
}
