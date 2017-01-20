package com.blashca.womanshealth.models;


import java.util.Date;

public class Medication {
    public String name;
    public String dosage;
    public int number;
    public int howTaken = -1;
    public int howOftenNumber;
    public int howOftenPeriod;
    public Date commencementDate;
    public int medicationHour = -1;
    public int medicationMinute = -1;
    public int howLongNumber;
    public int howLongPeriod;
    public boolean reminder;

    public String getMedicationTime() {

        if (medicationHour != -1) {
            String hour;
            String minute;
            if (medicationHour <= 9) {
                hour = "0" + medicationHour;
            } else {
                hour = "" + medicationHour;
            }

            if (medicationMinute <= 9) {
                minute = "0" + medicationMinute;
            } else {
                minute = "" + medicationMinute;
            }

            return new StringBuilder().append(hour).append(':').append(minute).toString();
        } else {
            return "";
        }
    }
}
