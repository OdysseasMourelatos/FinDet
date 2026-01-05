package com.financial.statistical_analysis;

public class FrequencyRow {
    public String interval;
    public int frequency;
    public double percentage;
    public int cumulativeFrequency;
    public double cumulativePercentage;

    public FrequencyRow(String interval, int frequency, double percentage, int cumulativeFrequency, double cumulativePercentage) {
        this.interval = interval;
        this.frequency = frequency;
        this.percentage = percentage;
        this.cumulativeFrequency = cumulativeFrequency;
        this.cumulativePercentage = cumulativePercentage;
    }

    public String getInterval() {
        return interval;
    }

    public int getFrequency() {
        return frequency;
    }

    public double getPercentage() {
        return percentage;
    }

    public int getCumulativeFrequency() {
        return cumulativeFrequency;
    }

    public double getCumulativePercentage() {
        return cumulativePercentage;
    }
}

    
