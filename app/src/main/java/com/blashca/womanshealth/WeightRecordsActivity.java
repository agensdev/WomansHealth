package com.blashca.womanshealth;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.blashca.womanshealth.external.SlidingTabLayout;


public class WeightRecordsActivity extends FragmentActivity {
    private WeightListFragment weightList;
    private WeightChartFragment weightChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_records);

        //We're creating instances of two fragments
        weightList = WeightListFragment.newInstance();
        weightChart = WeightChartFragment.newInstance();

        String listTitle = getResources().getString(R.string.list);
        String chartTitle = getResources().getString(R.string.chart);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);
        //We're passing two fragment instances to MyAdapter
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), weightList, weightChart, listTitle, chartTitle));

        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(mViewPager);
    }

    public void onDataChange() {
        weightList.refresh();
        weightChart.refreshChart();
    }
}
