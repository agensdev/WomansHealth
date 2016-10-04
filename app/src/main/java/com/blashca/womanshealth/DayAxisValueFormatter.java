package com.blashca.womanshealth;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.AxisValueFormatter;

import java.text.DateFormat;
import java.util.Date;


public class DayAxisValueFormatter implements AxisValueFormatter {

    private BarLineChartBase<?> chart;

    public DayAxisValueFormatter(BarLineChartBase<?> chart) {
        this.chart = chart;
    }


    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        long timestamp = (long) value;

        Date date = new Date(timestamp);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
        String formattedDate = dateFormat.format(date);

        return formattedDate;
    }


    @Override
    public int getDecimalDigits() {
        return 0;
    }
}