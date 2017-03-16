package com.blashca.womanshealth.models;


import java.util.Date;

public class Medication {
    public Long id;
    public String name;
    public String dosage;
    public Integer number;
    public Integer howTaken;
    public Integer howOftenNumber;
    public Integer howOftenPeriod;
    public Date commencementDate;
    public Integer[] medicationHourArray = new Integer[12];
    public Integer[] medicationMinuteArray = new Integer[12];
    public Integer howLongNumber;
    public Integer howLongPeriod;
    public boolean reminder;
    public boolean isAllergen;
    public String allergyEffects;


    public String getMedicationTime(int index) {

        if (medicationHourArray[index] != null) {
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
