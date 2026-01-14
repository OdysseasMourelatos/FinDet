package com.financial.statistical_analysis;

/**
 * Represents a single row in a frequency distribution table.
 * Encapsulates interval labeling, frequency counts, and percentage calculations.
 */
public class FrequencyRow {

    /** The label for the financial interval (e.g., "1000000-10000000"). */
    public String interval;

    /** The number of entities falling within this specific interval. */
    public int frequency;

    /** The percentage of the total sample represented by this interval. */
    public double percentage;

    /** The running total of frequency counts up to this interval. */
    public int cumulativeFrequency;

    /** The running total of percentages up to this interval. */
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

    
