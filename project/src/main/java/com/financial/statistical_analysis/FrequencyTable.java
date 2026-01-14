package com.financial.statistical_analysis;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * Utility class for constructing frequency tables for financial data.
 * <p>
 * Categorizes expenditures into "bins" based on a base-10 logarithmic scale,
 * facilitating the visualization of budget concentrations ranging from $10^6$ to $10^{13}$.
 */
public class FrequencyTable {

    private FrequencyTable() {
        //Utility class - no instances
    }

    public static List<FrequencyRow> buildFromStat() {
        Map<String, Long> sums = StatisticalExpenses.getMinistrySums();
        DescriptiveStatistics stats = StatisticalExpenses.descriptiveStats(sums);
        List<FrequencyRow> table = buildFrequencyTable(sums, stats);
        return table;
    }

    /**
     * Builds a frequency table based on predefined logarithmic bounds.
     * <p>
     * For each interval, it calculates the absolute frequency, the relative percentage,
     * and the cumulative totals for both.
     *
     * @param sums The aggregated expenditure data.
     * @param stats Descriptive statistics used to determine sample size (n).
     * @return A List of {@link FrequencyRow} objects representing the table.
     */
    public static List<FrequencyRow> buildFrequencyTable(Map<String, Long> sums, DescriptiveStatistics stats) {
        int n = sums.size();
        List<FrequencyRow> table = new ArrayList<>();

        // Ορίζουμε τα "στρογγυλά" όρια σε λογαριθμική κλίμακα βάσης 10
        // Ξεκινάμε από 1.000.000 (10^6) έως 10.000.000.000.000 (10^13)
        long[] bounds = {1_000_000L, 10_000_000L, 100_000_000L, 1_000_000_000L, 10_000_000_000L, 100_000_000_000L, 1_000_000_000_000L, 10_000_000_000_000L};

        int sumfr = 0;
        double sump = 0;

        for (int i = 0; i < bounds.length - 1; i++) {
            long low = bounds[i];
            long high = bounds[i + 1];


            long count = sums.values().stream().filter(v -> v >= low && v < high).count();

            if (count > 0) {
                int freq = (int) count;
                sumfr += freq;
                double p = (freq * 100.0) / n;
                sump += p;

                String intervalLabel = low + "-" + high;
                table.add(new FrequencyRow(intervalLabel, freq, p, sumfr, sump));
            }
        }
        return table;
    }
}