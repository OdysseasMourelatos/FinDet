package com.financial.multi_year_analysis.entries;

import java.util.*;

public class MultiYearBudgetRevenue extends MultiYearBudgetEntry {
    protected static List<MultiYearBudgetRevenue> historicalBudgetRevenues = new ArrayList<>();

    public MultiYearBudgetRevenue(String code, String description, String category, long amount, int year) {
        super(code, description, category, amount, year);
        historicalBudgetRevenues.add(this);
    }

    public static List<MultiYearBudgetRevenue> getHistoricalBudgetRevenues() {
        return historicalBudgetRevenues;
    }
}
