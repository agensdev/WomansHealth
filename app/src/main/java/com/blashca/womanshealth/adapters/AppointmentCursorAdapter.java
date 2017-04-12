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
import java.text.SimpleDateFormat;
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
        TextView appointmentDate = (TextView) view.findViewById(R.id.appointment_date);
        TextView appointmentDetail = (TextView) view.findViewById(R.id.appointment_detail);

        Long dateLong = dbHelper.getLongFromCursor(cursor, WomansHealthContract.WomansHealthAppointment.COLUMN_NEXT_APPOINTMENT_DATE);
        String formattedDate = "";
        if (dateLong != null) {
            Date date = new Date(dateLong);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy");
            formattedDate = simpleDateFormat.format(date);
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

        if (!hour.equals("")) {
            appointmentDate.setText(new StringBuilder().append(hour).append(':')
                    .append(minute).append(" ").append(context.getResources().getString(R.string.on_day)).append(" ").append(formattedDate).toString());
        } else {
            appointmentDate.setText(new StringBuilder().append(formattedDate).toString());
        }

        String textName = dbHelper.getStringFromCursor(cursor, WomansHealthContract.WomansHealthAppointment.COLUMN_APPOINTMENT_NAME);
        String doctorName = dbHelper.getStringFromCursor(cursor, WomansHealthContract.WomansHealthAppointment.COLUMN_DOCTOR_NAME);

        if (doctorName != null) {
            appointmentDetail.setText(textName + "," + " Dr " + doctorName);
        } else {
            appointmentDetail.setText(textName);
        }
    }
}
