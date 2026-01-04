package com.financial.entries;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.inference.TTest;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.math3.distribution.NormalDistribution;

public class FrequencyTable {

    public static List<FrequencyRow> buildFromStat() {
        Map<String, Long> sums = Stat.getMinistrySums();
        DescriptiveStatistics stats = Stat.descriptiveStats(sums);
        List<FrequencyRow> table = buildFrequencyTable(sums, stats);
        return table;
}


    public static List<FrequencyRow> buildFrequencyTable(Map<String, Long> sums, DescriptiveStatistics stats) {
        double min = stats.getMin();
        double max = stats.getMax();
        double q1 = stats.getPercentile(25);
        double q3 = stats.getPercentile(75);
        int n = sums.size();
        double iqr = q3 - q1;
        double width = 2 * iqr / Math.cbrt(n);
        if (width <= 0) {
            width = (max - min) / Math.ceil(Math.sqrt(n));
        }
        int numberOfintervals = (int) Math.ceil((max - min) / width);

        String [] intervals = new String [numberOfintervals];
        double left = min;
        for (int i = 0; i < numberOfintervals; i++) {
            intervals[i] = left + "-" + (left + width);
            left = left + width;
        }

        int [] frequencies = new int[numberOfintervals];
        for (long value : sums.values()) {
            int index = (int) ((value - min) / width);
             if (index == numberOfintervals) {
                index--;
            }
            frequencies[index]++;
        }

        List<FrequencyRow> table = new ArrayList<>();
        int sumfr = 0;
        double sump = 0;
        for (int i = 0; i < numberOfintervals; i++) {
            FrequencyRow row = new FrequencyRow();
            sumfr += frequencies[i];
            sump += (frequencies[i] * 100.0) / n;
            table.add(new FrequencyRow(intervals[i],frequencies[i],(frequencies[i] * 100.0) / n,sumfr,sump));
        }
        return table;

    }
}


