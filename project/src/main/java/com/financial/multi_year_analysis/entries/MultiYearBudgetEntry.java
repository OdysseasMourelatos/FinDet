package com.financial.multi_year_analysis.entries;

public abstract class MultiYearBudgetEntry {
    private final String code;
    private final String description;
    private final String category;
    private final long amount;
    private final int year;

    public MultiYearBudgetEntry(String code, String description, String category, long amount, int year) {
        this.code = code;
        this.description = description;
        this.category = category;
        this.amount = amount;
        this.year = year;
    }
}
