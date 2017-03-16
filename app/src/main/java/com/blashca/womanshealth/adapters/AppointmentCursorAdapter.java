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
import com.blashca.womanshealth.data.WomansHealthDbHelper;

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
        WomansHealthDbHelper dbHelper = new WomansHealthDbHelper(context);
        TextView appointmentName = (TextView) view.findViewById(R.id.appoinment_name);
        TextView appointmentDetail = (TextView) view.findViewById(R.id.appointment_detail);

        String textName = dbHelper.getStringFromCursor(cursor, WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_NAME);
        appointmentName.setText(textName);

        Long dateLong = dbHelper.getLongFromCursor(cursor, WomansHealthContract.WomansHealthAppointment.COLUMN_NEXT_APPOINTMENT_DATE);
        String formattedDate = "";
        if (dateLong != null) {
            Date date = new Date(dateLong);
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
            formattedDate = dateFormat.format(date);
        }

        Integer hourInt = dbHelper.getIntegerFromCursor(cursor, WomansHealthContract.WomansHealthAppointment.COLUMN_NEXT_APPOINTMENT_HOUR);
        Integer minuteInt = dbHelper.getIntegerFromCursor(cursor, WomansHealthContract.WomansHealthAppointment.COLUMN_NEXT_APPOINTMENT_MINUTE);
        String hour = "";
        String minute = "";
        if (hourInt != null) {

            if (hourInt <= 9) {
                hour = "0" + hourInt;
            } else {
                hour = "" + hourInt;
            }
        }

        if (minuteInt != null) {

            if (minuteInt <= 9) {
                minute = "0" + minuteInt;
            } else {
                minute = "" + minuteInt;
            }
        }

        if (!formattedDate.equals("") && !hour.equals("")) {
            appointmentDetail.setText(new StringBuilder().append(formattedDate).append("   ").append(hour).append(':')
                    .append(minute).toString());
        } else if (formattedDate.equals("") && !hour.equals(""))  {
            appointmentDetail.setText(new StringBuilder().append(hour).append(':').append(minute).toString());
        } else {
            appointmentDetail.setText(new StringBuilder().append(formattedDate).toString());
        }
    }
}
