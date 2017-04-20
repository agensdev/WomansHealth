package com.blashca.womanshealth.adapters;

import android.content.Context;
import android.database.Cursor;
import android.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blashca.womanshealth.R;
import com.blashca.womanshealth.data.WomansHealthContract;
import com.blashca.womanshealth.data.WomansHealthDbHelper;


public class SuggestedAppointmentCursorAdapter extends CursorAdapter {
    private LayoutInflater cursorInflater;

    public SuggestedAppointmentCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = cursorInflater.inflate(R.layout.suggested_appointments_list_item, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        WomansHealthDbHelper dbHelper = new WomansHealthDbHelper(context);
        TextView appointmentName = (TextView) view.findViewById(R.id.suggested_appointment_name);
        TextView appointmentDetail = (TextView) view.findViewById(R.id.suggested_appointment_detail);

        String name = dbHelper.getStringFromCursor(cursor, WomansHealthContract.WomansHealthMedicalTest.COLUMN_TEST_NAME);
        String frequency = dbHelper.getStringFromCursor(cursor, WomansHealthContract.WomansHealthMedicalTest.COLUMN_FREQUENCY);

        appointmentName.setText(name);
        appointmentDetail.setText(frequency);
    }
}
