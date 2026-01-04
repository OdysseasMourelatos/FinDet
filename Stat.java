package com.financial.entries;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.inference.TTest;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.math3.distribution.NormalDistribution;


public class Stat {
    public static Map<String, Long> getMinistrySums() {
        Map<String, Long> sum = new HashMap<>();
        for (Entity entity : Entity.entities) {
            sum.put(entity.getEntityName(), entity.getTotalSum());
        }
        return sum;
}

    public static Map<String, Long> sortMinistrySumsascendingorder(Map<String, Long> sum) {
        Map<String, Long> sortedMapasc = new LinkedHashMap<>();
        sum.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(entry -> sortedMapasc.put(entry.getKey(), entry.getValue()));
        return sortedMapasc;
    } 

    public static Map<String, Long> sortMinistrySumsdescendingorder(Map<String, Long> sum) {
         Map<String, Long> sortedMapdesc = new LinkedHashMap<>();
        sum.entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed()).forEach(entry -> sortedMapdesc.put(entry.getKey(), entry.getValue()));
        return sortedMapdesc;
    }

    public static DescriptiveStatistics descriptiveStats(Map<String, Long> sum) {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        for(long sums : sum.values()) {
            stats.addValue(sums);
        }
        System.out.println("Mean: " + stats.getMean());
        System.out.println("Median: " + stats.getPercentile(50));
        System.out.println("Standard Deviation: " + stats.getStandardDeviation());
        return stats;
    }

    public static void findOutliers(Map<String, Long> sum, DescriptiveStatistics stats) {
        double mean = stats.getMean();
        double std = stats.getStandardDeviation();
        if (std == 0) {
            System.out.println("No variance, no outliers.");
            return;
        } else {
            for (Map.Entry<String, Long> entry : sum.entrySet()) {
                double z = (entry.getValue() - mean)/std;
                if (Math.abs(z) > 2) {
                    System.out.println(entry.getKey());
                }
            }  
        }
    }


}
