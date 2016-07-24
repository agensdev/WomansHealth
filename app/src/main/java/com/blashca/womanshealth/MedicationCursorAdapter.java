package com.blashca.womanshealth;


import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.blashca.womanshealth.data.WomansHealthContract;

public class MedicationCursorAdapter extends CursorAdapter {
    private LayoutInflater cursorInflater;
    private TextView medicationName;
    private ColorStateList oldColor;

    public MedicationCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View newView(final Context context, Cursor cursor, ViewGroup parent) {
        View view = cursorInflater.inflate(R.layout.medications_list_item, parent, false);

        medicationName = (TextView) view.findViewById(R.id.medication_name);
        oldColor = medicationName.getTextColors(); //save original colors

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView medicationDetail = (TextView) view.findViewById(R.id.medication_detail);

        String textName = cursor.getString(cursor.getColumnIndex(WomansHealthContract.WomansHealthMedication.COLUMN_MEDICATION_NAME));
        int isAllergen = cursor.getInt(cursor.getColumnIndex(WomansHealthContract.WomansHealthMedication.COLUMN_IS_ALLERGEN));

        int howOften = cursor.getInt(cursor.getColumnIndex(WomansHealthContract.WomansHealthMedication.COLUMN_HOW_OFTEN));
        String sideEffects = cursor.getString(cursor.getColumnIndex(WomansHealthContract.WomansHealthMedication.COLUMN_ALLERGIES_EFFECTS));

        if (isAllergen == 0) {
            medicationName.setText(textName);
            medicationDetail.setText("" + howOften);

            if (medicationName.getTextColors() != oldColor) {
                medicationName.setTextColor(oldColor);//restore original colors
            }

        } else {
            medicationName.setText("Allergy to " + textName);

            if (medicationName.getTextColors() == oldColor) {
                medicationName.setTextColor(Color.RED);
            } else {
                medicationName.setTextColor(oldColor);//restore original colors
            }

            medicationDetail.setText(sideEffects);
        }
    }
}
