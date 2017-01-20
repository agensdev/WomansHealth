package com.blashca.womanshealth.fragments;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blashca.womanshealth.DayAxisValueFormatter;
import com.blashca.womanshealth.R;
import com.blashca.womanshealth.data.WomansHealthContract;
import com.blashca.womanshealth.data.WomansHealthDbHelper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Collections;


public class WeightChartFragment extends Fragment {
    private LineChart lineChart;
    private WomansHealthDbHelper dbHelper;

    /**
     * Create a new instance of WeightChartFragment
     */
    public static WeightChartFragment newInstance() {
        return new WeightChartFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weight_records_chart, container, false);

        lineChart = (LineChart) view.findViewById(R.id.weight_chart);

        dbHelper = new WomansHealthDbHelper(getContext());

        collectWeightData();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshChart();
    }

    private void collectWeightData() {
        Cursor weightCursor = dbHelper.getWeightsCursor();

        ArrayList<Entry> entries = new ArrayList<>();

        int i = 0;

        ArrayList<Long> dates = new ArrayList<>();
        ArrayList<Float> weights = new ArrayList<>();

        while (weightCursor.moveToNext()) {
            long date = weightCursor.getLong(weightCursor.getColumnIndex(WomansHealthContract.WomansHealthWeight.COLUMN_WEIGHT_DATE));
            float weight = weightCursor.getFloat(weightCursor.getColumnIndex(WomansHealthContract.WomansHealthWeight.COLUMN_WEIGHT));

            dates.add(i, date);
            weights.add(i, weight);
            i++;
        }

        Collections.reverse(dates);
        Collections.reverse(weights);

        for (i = 0; i < dates.size(); i++) {
            entries.add(new Entry(dates.get(i), weights.get(i)));
        }

        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();

        LineDataSet lineDataSet = new LineDataSet(entries, "weight");
        lineDataSet.setDrawCircles(false);
        lineDataSet.setColor(Color.BLUE);
        lineDataSets.add(lineDataSet);

        lineChart.setData(new LineData(lineDataSets));

        AxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(lineChart);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(xAxisFormatter);
    }

    public void refreshChart() {
        collectWeightData();
        lineChart.notifyDataSetChanged();
    }
}
