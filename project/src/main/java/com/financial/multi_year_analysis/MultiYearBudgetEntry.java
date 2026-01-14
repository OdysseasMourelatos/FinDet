package com.financial.multi_year_analysis;

/**
 * Abstract base class representing a budget entry that spans multiple years.
 * This class provides common properties and behavior for both revenue and expense entries
 * in a multi-year budget analysis system.
 * 
 * @author Financial Analysis System
 * @version 1.0
 */
public abstract class MultiYearBudgetEntry {
    private final String code;
    private final String description;
    private final String category;
    private final long amount;
    private final int year;

    /**
     * Constructs a new MultiYearBudgetEntry with the specified parameters.
     * All fields are immutable once set.
     * 
     * @param code the unique identifier code for this budget entry
     * @param description a textual description of the budget entry
     * @param category the category this budget entry belongs to
     * @param amount the monetary amount for this entry (in minor currency units, e.g., cents)
     * @param year the fiscal year this entry applies to
     */
    public MultiYearBudgetEntry(String code, String description, String category, long amount, int year) {
        this.code = code;
        this.description = description;
        this.category = category;
        this.amount = amount;
        this.year = year;
    }

    public MultiYearBudgetEntry(long amount, int year) {
        this.code = null;
        this.description = null;
        this.category = null;
        this.amount = amount;
        this.year = year;
    }

    /**
     * Returns the unique identifier code for this budget entry.
     * 
     * @return the budget entry code
     */
    public String getCode() {
        return code;
    }

    /**
     * Returns the description of this budget entry.
     * 
     * @return the budget entry description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the category this budget entry belongs to.
     * 
     * @return the budget entry category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Returns the monetary amount for this budget entry.
     * 
     * @return the amount in minor currency units (e.g., cents)
     */
    public long getAmount() {
        return amount;
    }

    /**
     * Returns the fiscal year this budget entry applies to.
     * 
     * @return the year as an integer
     */
    public int getYear() {
        return year;
    }

    /**
     * Returns a formatted string representation of this budget entry.
     * The amount is formatted with thousand separators for readability.
     * 
     * @return a string containing all budget entry details
     */
    @Override
    public String toString() {
        return "Code: " + code + ", Description: " + description + ", Category: " + category + ", Amount: " + String.format("%,d", amount) + ", Year: " + year;
    }
}
