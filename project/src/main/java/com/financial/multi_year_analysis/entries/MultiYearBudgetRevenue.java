package com.financial.multi_year_analysis.entries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MultiYearBudgetRevenue extends MultiYearBudgetEntry {
    protected static List<MultiYearBudgetRevenue> multiYearBudgetRevenues = new ArrayList<>();

    public MultiYearBudgetRevenue(String code, String description, String category, long amount, int year) {
        super(code, description, category, amount, year);
        multiYearBudgetRevenues.add(this);
    }

    public static List<MultiYearBudgetRevenue> getMultiYearBudgetRevenues() {
        return multiYearBudgetRevenues;
    }

    public static List<MultiYearBudgetRevenue> getMultiYearBudgetRevenuesOfSpecificYear(int year) {
        List<MultiYearBudgetRevenue> multiYearBudgetRevenuesOfSpecificYear = new ArrayList<>();
        for (MultiYearBudgetRevenue hbr : multiYearBudgetRevenues) {
            if (hbr.getYear() == year) {
                multiYearBudgetRevenuesOfSpecificYear.add(hbr);
            }
        }
        return multiYearBudgetRevenuesOfSpecificYear;
    }

    public static List<MultiYearBudgetRevenue> getMultiYearBudgetRevenuesOfSpecificCode(String code) {
        List<MultiYearBudgetRevenue> multiYearBudgetRevenuesOfSpecificCode = new ArrayList<>();
        for (MultiYearBudgetRevenue hbr : multiYearBudgetRevenues) {
            if (hbr.getCode().equals(code)) {
                multiYearBudgetRevenuesOfSpecificCode.add(hbr);
            }
        }
        return multiYearBudgetRevenuesOfSpecificCode;
    }

    public static long getSumOfSpecificYear(int year) {
        long sum = 0;
        for (MultiYearBudgetRevenue hbr : multiYearBudgetRevenues) {
            if (hbr.getYear() == year) {
                sum += hbr.getAmount();
            }
        }
        return sum;
    }

    public static Map<Integer, Long> getSumOfAllYears() {
        Map<Integer, Long> sumOfAllYears = new HashMap<>();
        List<Integer> uniqueYears = multiYearBudgetRevenues.stream().map(MultiYearBudgetRevenue::getYear).distinct().sorted().collect(Collectors.toList());
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
