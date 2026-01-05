package com.financial.multi_year_analysis.entries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents a revenue entry in a multi-year budget analysis system.
 * This class extends MultiYearBudgetEntry and maintains a static collection
 * of all historical budget revenues for analysis and reporting purposes.
 * 
 * @author Financial Analysis System
 * @version 1.0
 */
public class MultiYearBudgetRevenue extends MultiYearBudgetEntry {
    protected static List<MultiYearBudgetRevenue> historicalBudgetRevenues = new ArrayList<>();

    /**
     * Constructs a new MultiYearBudgetRevenue and automatically adds it to the
     * historical revenues collection for tracking purposes.
     * 
     * @param code the unique identifier code for this revenue entry
     * @param description a textual description of the revenue source
     * @param category the category this revenue belongs to
     * @param amount the revenue amount (in minor currency units, e.g., cents)
     * @param year the fiscal year this revenue applies to
     */
    public MultiYearBudgetRevenue(String code, String description, String category, long amount, int year) {
        super(code, description, category, amount, year);
        historicalBudgetRevenues.add(this);
    }

    /**
     * Returns the complete list of all historical budget revenues.
     *
     * @return a list containing all revenue entries created
     */
    public static List<MultiYearBudgetRevenue> getHistoricalBudgetRevenues() {
        return historicalBudgetRevenues;
    }

    /**
     * Alias for getHistoricalBudgetRevenues() for API consistency.
     *
     * @return a list containing all revenue entries created
     */
    public static List<MultiYearBudgetRevenue> getMultiYearBudgetRevenues() {
        return historicalBudgetRevenues;
    }

    /**
     * Retrieves all revenue entries for a specific fiscal year.
     * 
     * @param year the fiscal year to filter by
     * @return a list of revenue entries matching the specified year
     */
    public static List<MultiYearBudgetRevenue> getHistoricalBudgetRevenuesOfSpecificYear(int year) {
        List<MultiYearBudgetRevenue> historicalBudgetRevenuesOfSpecificYear = new ArrayList<>();
        for (MultiYearBudgetRevenue hbr : historicalBudgetRevenues) {
            if (hbr.getYear() == year) {
                historicalBudgetRevenuesOfSpecificYear.add(hbr);
            }
        }
        return historicalBudgetRevenuesOfSpecificYear;
    }

    /**
     * Retrieves all revenue entries with a specific code across all years.
     * This is useful for tracking a particular revenue source over time.
     * 
     * @param code the revenue code to filter by
     * @return a list of revenue entries matching the specified code
     */
    public static List<MultiYearBudgetRevenue> getHistoricalBudgetRevenuesOfSpecificCode(String code) {
        List<MultiYearBudgetRevenue> historicalBudgetRevenuesOfSpecificCode = new ArrayList<>();
        for (MultiYearBudgetRevenue hbr : historicalBudgetRevenues) {
            if (hbr.getCode().equals(code)) {
                historicalBudgetRevenuesOfSpecificCode.add(hbr);
            }
        }
        return historicalBudgetRevenuesOfSpecificCode;
    }

    /**
     * Calculates the total revenue amount for a specific fiscal year
     * by summing all revenue entries for that year.
     * 
     * @param year the fiscal year to calculate the total for
     * @return the total revenue amount for the specified year
     */
    public static long getSumOfSpecificYear(int year) {
        long sum = 0;
        for (MultiYearBudgetRevenue hbr : historicalBudgetRevenues) {
            if (hbr.getYear() == year) {
                sum += hbr.getAmount();
            }
        }
        return sum;
    }

    /**
     * Calculates the total revenue for each year present in the historical data.
     * Returns a map where keys are years and values are the total revenue amounts.
     * Years are sorted in ascending order.
     * 
     * @return a map of year to total revenue amount for all years with data
     */
    public static Map<Integer, Long> getSumOfAllYears() {
        Map<Integer, Long> sumOfAllYears = new HashMap<>();
        List<Integer> uniqueYears = historicalBudgetRevenues.stream().map(MultiYearBudgetRevenue::getYear).distinct().sorted().collect(Collectors.toList());
        for (Integer year : uniqueYears) {
            sumOfAllYears.put(year, getSumOfSpecificYear(year));
        }
        return sumOfAllYears;
    }

    /**
     * Returns a formatted string representation of this revenue entry.
     * 
     * @return a string containing all revenue entry details
     */
    @Override
    public String toString () {
        return super.toString();
    }
}
