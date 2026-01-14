package com.financial.statistical_analysis;

import com.financial.entries.Entity;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Utility class for performing statistical analysis on entity expenditures.
 * <p>
 * Leverages the Apache Commons Math library to compute descriptive statistics
 * and provides specialized algorithms for financial anomaly detection.
 */public class StatisticalExpenses {

    private StatisticalExpenses() {
        //Utility class - no instances
    }

    /**
     * Aggregates the total calculated sums for all registered entities.
     * @return A Map containing Entity names as keys and their total expenditure as values.
     */
    public static Map<String, Long> getMinistrySums() {
        Map<String, Long> sum = new HashMap<>();
        for (Entity entity : Entity.getEntities()) {
            sum.put(entity.getEntityName(), entity.calculateTotalSum());
        }
        return sum;
    }

    /** Sorts ministries in asc order */
    public static Map<String, Long> sortMinistrySumsAscendingOrder(Map<String, Long> sum) {
        Map<String, Long> sortedMapasc = new LinkedHashMap<>();
        sum.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(entry -> sortedMapasc.put(entry.getKey(), entry.getValue()));
        return sortedMapasc;
    }

    /** Sorts ministries in desc order */
    public static Map<String, Long> sortMinistrySumsDescendingOrder(Map<String, Long> sum) {
        Map<String, Long> sortedMapdesc = new LinkedHashMap<>();
        sum.entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed()).forEach(entry -> sortedMapdesc.put(entry.getKey(), entry.getValue()));
        return sortedMapdesc;
    }

    /** Retrieves descriptive statistics */
    public static DescriptiveStatistics descriptiveStats(Map<String, Long> sum) {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (long sums : sum.values()) {
            stats.addValue(sums);
        }
        return stats;
    }

    /** Identifies outliers using Z-score method (values beyond 2 standard deviations from mean) */
    public static Map<String, Long> findOutliersZScore(Map<String, Long> sum, DescriptiveStatistics stats) {
        double mean = stats.getMean();
        double std = stats.getStandardDeviation();
        Map<String, Long> outliersz = new HashMap<>();
        if (std == 0) {
            return outliersz;
        } else {
            for (Map.Entry<String, Long> entry : sum.entrySet()) {
                double z = (entry.getValue() - mean) / std;
                if (Math.abs(z) > 2) {
                    outliersz.put(entry.getKey(), entry.getValue());
                }
            }
            return outliersz;
        }
    }
    
    /** Identifies outliers using IQR method (values beyond 1.5 * IQR from quartiles) */
    public static Map<String, Long> findOutliersIQR(Map<String, Long> sum, DescriptiveStatistics stats) {
        Map<String, Long> outliersiq = new HashMap<>();
        double q1 = stats.getPercentile(25);
        double q3 = stats.getPercentile(75);
        double iqr = q3 - q1;
        double lowerBound = q1 - 1.5 * iqr;
        double upperBound = q3 + 1.5 * iqr;
        for (Map.Entry<String, Long> entry : sum.entrySet()) {
            long value = entry.getValue();
            if (value < lowerBound || value > upperBound) {
                outliersiq.put(entry.getKey(), value);
            }
        }
        return outliersiq;
    }
}
