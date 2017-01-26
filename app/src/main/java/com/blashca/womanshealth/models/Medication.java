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
    public int[] medicationHourArray = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
    public int[] medicationMinuteArray = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
    public int howLongNumber;
    public int howLongPeriod;
    public boolean reminder;


    public String getMedicationTime(int index) {

        if (medicationHourArray[index] != -1) {
            String hour;
            String minute;
            if (medicationHourArray[index] <= 9) {
                hour = "0" + medicationHourArray[index];
            } else {
                hour = "" + medicationHourArray[index];
            }

            if (medicationMinuteArray[index] <= 9) {
                minute = "0" + medicationMinuteArray[index];
            } else {
                minute = "" + medicationMinuteArray[index];
            }

            return new StringBuilder().append(hour).append(':').append(minute).toString();
        } else {
            return "";
        }
    }
}
