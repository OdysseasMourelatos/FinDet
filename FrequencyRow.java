package com.financial.entries;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.inference.TTest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.math3.distribution.NormalDistribution;

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
}

    
