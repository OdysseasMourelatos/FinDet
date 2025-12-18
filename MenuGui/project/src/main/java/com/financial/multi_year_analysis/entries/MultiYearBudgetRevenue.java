package com.financial.multi_year_analysis.entries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MultiYearBudgetRevenue extends MultiYearBudgetEntry {
    protected static List<MultiYearBudgetRevenue> historicalBudgetRevenues = new ArrayList<>();

    public MultiYearBudgetRevenue(String code, String description, String category, long amount, int year) {
        super(code, description, category, amount, year);
        historicalBudgetRevenues.add(this);
    }

    public static List<MultiYearBudgetRevenue> getHistoricalBudgetRevenues() {
        return historicalBudgetRevenues;
    }

    public static List<MultiYearBudgetRevenue> getHistoricalBudgetRevenuesOfSpecificYear(int year) {
        List<MultiYearBudgetRevenue> historicalBudgetRevenuesOfSpecificYear = new ArrayList<>();
        for (MultiYearBudgetRevenue hbr : historicalBudgetRevenues) {
            if (hbr.getYear() == year) {
                historicalBudgetRevenuesOfSpecificYear.add(hbr);
            }
        }
        return historicalBudgetRevenuesOfSpecificYear;
    }

    public static List<MultiYearBudgetRevenue> getHistoricalBudgetRevenuesOfSpecificCode(String code) {
        List<MultiYearBudgetRevenue> historicalBudgetRevenuesOfSpecificCode = new ArrayList<>();
        for (MultiYearBudgetRevenue hbr : historicalBudgetRevenues) {
            if (hbr.getCode().equals(code)) {
                historicalBudgetRevenuesOfSpecificCode.add(hbr);
            }
        }
        return historicalBudgetRevenuesOfSpecificCode;
    }

    public static long getSumOfSpecificYear(int year) {
        long sum = 0;
        for (MultiYearBudgetRevenue hbr : historicalBudgetRevenues) {
            if (hbr.getYear() == year) {
                sum += hbr.getAmount();
            }
        }
        return sum;
    }

    public static Map<Integer, Long> getSumOfAllYears() {
        Map<Integer, Long> sumOfAllYears = new HashMap<>();
        List<Integer> uniqueYears = historicalBudgetRevenues.stream().map(MultiYearBudgetRevenue::getYear).distinct().sorted().collect(Collectors.toList());
        for (Integer year : uniqueYears) {
            sumOfAllYears.put(year, getSumOfSpecificYear(year));
        }
        return sumOfAllYears;
    }

    @Override
    public String toString () {
        return super.toString();
    }
}
