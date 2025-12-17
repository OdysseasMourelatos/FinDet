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

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public long getAmount() {
        return amount;
    }

    public int getYear() {
        return year;
    }

    @Override
    public String toString() {
        return "Code: " + code + ", Description: " + description + ", Category: " + category + ", Amount: " + String.format("%,d", amount) + ", Year: " + year;
    }
}
