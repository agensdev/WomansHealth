package com.blashca.womanshealth;


public class Bmi {
    private int height;
    private float weight;
    private float bmi;

    public Bmi(int height, float weight) {
        this.height = height;
        this.weight = weight;
        bmi = weight / (float) Math.pow(height/100.0, 2);
    }

    public String getBmiValue() {
        return String.format("%.1f", bmi);
    }

    public int getCategory() {
        int category;

        if (bmi < 18.5) {
            category = R.string.underweight;
        } else if (bmi >= 18.5 && bmi < 25) {
            category = R.string.normal_weight;
        } else if (bmi >= 25 && bmi < 30) {
            category = R.string.overweight;
        } else {
            category = R.string.obese;
        }

        return category;
    }

    public int getOptimum() {
        int optimum;

        if (bmi < 18.5) {
            optimum = R.string.underweight_result;
        } else if (bmi >= 18.5 && bmi < 25) {
            optimum = R.string.normal_result;
        } else {
            optimum = R.string.overweight_result;
        }

        return optimum;
    }

    public String getOptimalWeight() {
        double optimalWeight;

        if (bmi < 18.5) {
            optimalWeight = Math.abs(weight - (18.5 * Math.pow(height/100.0, 2)));
        } else {
            optimalWeight = Math.abs((24.9 * Math.pow(height/100.0, 2)) - weight);
        }

        return String.format("%.1f", optimalWeight);
    }
}
