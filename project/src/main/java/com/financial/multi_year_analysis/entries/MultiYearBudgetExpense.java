package com.financial.multi_year_analysis.entries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents an expense entry in a multi-year budget analysis system.
 * This class extends MultiYearBudgetEntry and supports both simple expenses
 * and entity-specific expenses with regular and public investment breakdowns.
 * 
 * @author Financial Analysis System
 * @version 1.0
 */
public class MultiYearBudgetExpense extends MultiYearBudgetEntry {
    protected static List<MultiYearBudgetExpense> multiYearBudgetExpenses = new ArrayList<>();

    /**
     * Constructs a simple MultiYearBudgetExpense without entity association.
     * The expense is automatically added to the global expenses collection.
     * 
     * @param code the unique identifier code for this expense entry
     * @param description a textual description of the expense
     * @param category the category this expense belongs to
     * @param amount the expense amount (in minor currency units, e.g., cents)
     * @param year the fiscal year this expense applies to
     */
    public MultiYearBudgetExpense(String code, String description, String category, long amount, int year) {
        super(code, description, category, amount, year);
        multiYearBudgetExpenses.add(this);
    }

    /**
     * Returns the complete list of all multi-year budget expenses.
     * 
     * @return a list containing all expense entries created
     */
    public static List<MultiYearBudgetExpense> getMultiYearBudgetExpenses() {
        return multiYearBudgetExpenses;
    }

    protected static List<MultiYearBudgetExpense> multiYearBudgetExpensesOfEntities = new ArrayList<>();

    private String entityCode;
    private String entityName;
    private long regularAmount;
    private long publicInvestmentAmount;

    /**
     * Constructs an entity-specific MultiYearBudgetExpense with separate tracking
     * for regular and public investment amounts. The total amount is calculated
     * as the sum of regular and public investment amounts.
     * 
     * @param entityCode the unique identifier code for the entity incurring this expense
     * @param entityName the name of the entity
     * @param code the unique identifier code for this expense entry
     * @param description a textual description of the expense
     * @param category the category this expense belongs to
     * @param regularAmount the regular expense amount (non-investment spending)
     * @param publicInvestmentAmount the public investment expense amount
     * @param year the fiscal year this expense applies to
     */
    public MultiYearBudgetExpense(String entityCode, String entityName, String code, String description, String category, long regularAmount, long publicInvestmentAmount, int year) {
        super(code, description, category, regularAmount + publicInvestmentAmount,  year);
        this.entityCode = entityCode;
        this.entityName = entityName;
        this.regularAmount = regularAmount;
        this.publicInvestmentAmount = publicInvestmentAmount;
        multiYearBudgetExpenses.add(this);
    }

    /**
     * Retrieves all expense entries for a specific fiscal year.
     * 
     * @param year the fiscal year to filter by
     * @return a list of expense entries matching the specified year
     */
    public static List<MultiYearBudgetExpense> getMultiYearBudgetExpensesOfSpecificYear(int year) {
        List<MultiYearBudgetExpense> multiYearBudgetExpensesOfSpecificYear = new ArrayList<>();
        for (MultiYearBudgetExpense multiYearBudgetExpense : multiYearBudgetExpenses) {
            if (multiYearBudgetExpense.getYear() == year) {
                multiYearBudgetExpensesOfSpecificYear.add(multiYearBudgetExpense);
            }
        }
        return multiYearBudgetExpensesOfSpecificYear;
    }

    /**
     * Retrieves all expense entries with a specific code across all years.
     * This is useful for tracking a particular expense type over time.
     * 
     * @param code the expense code to filter by
     * @return a list of expense entries matching the specified code
     */
    public static List<MultiYearBudgetExpense> getMultiYearBudgetExpensesOfSpecificCode(String code) {
        List<MultiYearBudgetExpense> multiYearBudgetExpensesOfSpecificCode = new ArrayList<>();
        for (MultiYearBudgetExpense multiYearBudgetExpense : multiYearBudgetExpenses) {
            if (multiYearBudgetExpense.getCode().equals(code)) {
                multiYearBudgetExpensesOfSpecificCode.add(multiYearBudgetExpense);
            }
        }
        return multiYearBudgetExpensesOfSpecificCode;
    }

    /**
     * Retrieves all expense entries associated with a specific entity
     * by filtering the entity-specific expenses collection.
     * 
     * @param entityCode the entity code to filter by
     * @return a list of expense entries for the specified entity
     */
    public static List<MultiYearBudgetExpense> getMultiYearExpensesOfEntityWithEntityCode(String entityCode) {
        List<MultiYearBudgetExpense> multiYearExpensesOfEntity = new ArrayList<>();
        for (MultiYearBudgetExpense multiYearBudgetExpense : multiYearBudgetExpensesOfEntities) {
            if (multiYearBudgetExpense.entityCode.equals(entityCode)) {
                multiYearExpensesOfEntity.add(multiYearBudgetExpense);
            }
        }
        return multiYearExpensesOfEntity;
    }

    /**
     * Calculates the total expense amount for a specific fiscal year
     * by summing all expense entries for that year.
     * 
     * @param year the fiscal year to calculate the total for
     * @return the total expense amount for the specified year
     */
    public static long getSumOfSpecificYear(int year) {
        long sum = 0;
        for (MultiYearBudgetExpense multiYearBudgetExpense : multiYearBudgetExpenses) {
            if (multiYearBudgetExpense.getYear() == year) {
                sum += multiYearBudgetExpense.getAmount();
            }
        }
        return sum;
    }

    /**
     * Calculates the total expense for each year present in the historical data.
     * Returns a map where keys are years and values are the total expense amounts.
     * Years are sorted in ascending order.
     * 
     * @return a map of year to total expense amount for all years with data
     */
    public static Map<Integer, Long> getSumOfAllYears() {
        Map<Integer, Long> sumOfAllYears = new HashMap<>();
        List<Integer> uniqueYears = multiYearBudgetExpenses.stream().map(MultiYearBudgetExpense::getYear).distinct().sorted().collect(Collectors.toList());
        for (Integer year : uniqueYears) {
            sumOfAllYears.put(year, getSumOfSpecificYear(year));
        }
        return sumOfAllYears;
    }

    /**
     * Returns the regular (non-investment) portion of this expense.
     * 
     * @return the regular expense amount
     */
    public long getRegularAmount() {
        return regularAmount;
    }

    /**
     * Returns the public investment portion of this expense.
     * 
     * @return the public investment expense amount
     */
    public long getPublicInvestmentAmount() {
        return publicInvestmentAmount;
    }

    /**
     * Returns a formatted string representation of this expense entry.
     * 
     * @return a string containing all expense entry details
     */
    @Override
    public String toString () {
        return super.toString();
    }
}
