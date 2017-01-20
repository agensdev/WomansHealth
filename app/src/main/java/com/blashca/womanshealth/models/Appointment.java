package com.blashca.womanshealth.models;

import java.util.Date;

public class Appointment {
    public String name;
    public String doctorsName;
    public String address;
    public String telephone;
    public String email;
    public Date lastDate;
    public int nextDateSpinnerPosition;
    public Date nextDate;
    public int nextAppointmentHour = -1;
    public int nextAppointmentMinute = -1;
    public boolean reminder;


    public String getAppointmentTime() {

        if (nextAppointmentHour != -1) {
            String hour;
            String minute;
            if (nextAppointmentHour <= 9) {
                hour = "0" + nextAppointmentHour;
            } else {
                hour = "" + nextAppointmentHour;
            }

            if (nextAppointmentMinute <= 9) {
                minute = "0" + nextAppointmentMinute;
            } else {
                minute = "" + nextAppointmentMinute;
            }

            return new StringBuilder().append(hour).append(':').append(minute).toString();
        } else {
            return "";
        }
    }
}
