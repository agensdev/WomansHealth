package com.blashca.womanshealth;


public class Period {
    public final int id;
    public final long date;
    public final int duration;
    public final int interval;

    public Period(int id, long date, int duration, int interval) {
        this.id = id;
        this.date = date;
        this.duration = duration;
        this.interval = interval;
    }
}