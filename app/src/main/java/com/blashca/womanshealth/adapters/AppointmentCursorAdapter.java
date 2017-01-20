package com.blashca.womanshealth.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.blashca.womanshealth.R;
import com.blashca.womanshealth.data.WomansHealthContract;

import java.text.DateFormat;
import java.util.Date;


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

        long dateLong = cursor.getLong(cursor.getColumnIndex(WomansHealthContract.WomansHealthAppointment.COLUMN_NEXT_APPOINTMENT_DATE));
        String formattedDate = "";
        if (dateLong != 0) {
            Date date = new Date(dateLong);
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
            formattedDate = dateFormat.format(date);
        }

        int hourInt = cursor.getInt(cursor.getColumnIndex(WomansHealthContract.WomansHealthAppointment.COLUMN_NEXT_APPOINTMENT_HOUR));
        int minuteInt = cursor.getInt(cursor.getColumnIndex(WomansHealthContract.WomansHealthAppointment.COLUMN_NEXT_APPOINTMENT_MINUTE));
        if (hourInt != -1) {
            appointmentDetail.setText(new StringBuilder().append(formattedDate).append("   ").append(hourInt).append(':')
                    .append(minuteInt).toString());
        } else {
            appointmentDetail.setText(new StringBuilder().append(formattedDate).toString());
        }
    }
}
