package com.blashca.womanshealth.models;

import java.util.Date;

public class Appointment {
    public Long id;
    public String name;
    public String doctorsName;
    public String address;
    public String telephone;
    public String email;
    public Date lastDate;
    public Integer nextDateSpinnerPosition = 0;
    public Date nextDate;
    public Integer nextAppointmentHour;
    public Integer nextAppointmentMinute;
    public boolean reminder;


    public String getAppointmentTime() {

        if (nextAppointmentHour != null) {
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

    public long getAlarmId() {
        return 10000000000L + id;
    }
}
