package com.blashca.womanshealth;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.blashca.womanshealth.data.WomansHealthContract;


public class AppointmentCursorAdapter extends CursorAdapter {
    private LayoutInflater cursorInflater;

    public AppointmentCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = cursorInflater.inflate(R.layout.appointments_list_item, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView appointmentName = (TextView) view.findViewById(R.id.appoinment_name);
        TextView appointmentDetail = (TextView) view.findViewById(R.id.appointment_detail);

        String textName = cursor.getString(cursor.getColumnIndex(WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_NAME));
        appointmentName.setText(textName);

        String textDetail = cursor.getString(cursor.getColumnIndex(WomansHealthContract.WomansHealthAppointment.COLUMN_NEXT_APPOINTMENT));
        appointmentDetail.setText(textDetail);
    }
}
