package com.blashca.womanshealth.models;


import java.util.Date;

public class Period {
    public Long id;
    public Date date;
    public Integer duration;
    public Integer interval;

    public Period() {}

    public Period(Long id, Long date, Integer duration, Integer interval) {
        this.id = id;
        this.date = new Date(date);
        this.duration = duration;
        this.interval = interval;
    }
}