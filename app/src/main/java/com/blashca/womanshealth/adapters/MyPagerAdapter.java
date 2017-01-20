package com.blashca.womanshealth.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;



public class MyPagerAdapter extends FragmentPagerAdapter {
    private Fragment weightList;
    private Fragment weightChart;
    private static final int NUM_ITEMS = 2;
    private String listTitle;
    private String chartTitle;

    public MyPagerAdapter(FragmentManager fm, Fragment fragment1, Fragment fragment2, String listTitle, String chartTitle) {
        super(fm);
        weightList = fragment1;
        weightChart = fragment2;
        this.listTitle = listTitle;
        this.chartTitle = chartTitle;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        if (position == 0) {
            return listTitle;
        } else if (position == 1) {
            return chartTitle;
        }

        return "";
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return weightList;
        } else {
            return weightChart;
        }
    }
}
