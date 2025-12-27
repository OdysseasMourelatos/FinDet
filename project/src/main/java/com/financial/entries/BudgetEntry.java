package com.financial.entries;

/**
 * Abstract base class representing a generic entry in the financial budget.
 * <p>
 * This class provides the fundamental attributes for any budget item, including
 * its unique code, description, category, and monetary amount. It enforces basic
 * validation rules for financial values.
 */
public abstract class BudgetEntry {
    /** The unique identification code of the budget entry. */
    private final String code;

    /** A descriptive name or label for the entry. */
    private final String description;

    /** The broad financial category the entry belongs to (e.g., "REVENUE", "EXPENSE"). */
    private final String category;

    /** The monetary value of the entry, stored as a long to avoid floating-point errors. */
    protected long amount;

    /**
     * Constructs a new BudgetEntry with the specified details.
     *
     * @param code        the unique string code
     * @param description the name or description of the entry
     * @param category    the category classification
     * @param amount      the initial monetary amount
     */
    public BudgetEntry(String code, String description, String category, long amount) {
        this.code = code;
        this.description = description;
        this.category = category;
        this.amount = amount;
    }

    /**
     * Gets the unique code of the entry.
     * @return the entry code
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Gets the description of the entry.
     * @return the entry description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Gets the category of the entry.
     * @return the entry category
     */
    public String getCategory() {
        return this.category;
    }

    /**
     * Gets the current monetary amount of the entry.
     * @return the amount as a long
     */
    public long getAmount() {
        return this.amount;
    }

    /**
     * Sets the monetary amount of the entry.
     * <p>
     * Validation: The amount must be non-negative.
     *
     * @param amount the new amount to set
     * @throws IllegalArgumentException if the provided amount is negative
     */
    public void setAmount(long amount) {
        if (amount >= 0) {
            this.amount = amount;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Returns a string representation of the budget entry,
     * formatting the amount with thousands separators for readability.
     * * @return a formatted string containing entry details
     */
    @Override
    public String toString() {
        return "Code: " + code + ", Description: " + description + ", Category: " + category + ", Amount: " + String.format("%,d", amount);
    }
}