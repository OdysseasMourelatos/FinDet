package com.financial.entries;

import com.financial.services.revenues.BudgetRevenueLogicService;
import com.financial.services.revenues.BudgetRevenueLogic;

import java.util.*;

/**
 * The base class for all budget revenue entries in the system.
 * <p>
 * This class serves as the central point for aggregating financial data from
 * multiple sources (Regular and Public Investment). It maintains a global registry
 * of all revenue entries and provides fundamental logic for hierarchical levels,
 * code-based sorting, and duplicate merging.
 */
public class BudgetRevenue extends BudgetEntry implements BudgetRevenueLogic {

    /**
     * Global registry of all consolidated budget revenue instances.
     */
    protected static ArrayList<BudgetRevenue> budgetRevenues = new ArrayList<>();

    /**
     * Basic constructor for general budget entries.
     *
     * @param code        the unique financial code
     * @param description a brief description of the entry
     * @param category    the financial category
     * @param amount      the total financial amount
     */
    public BudgetRevenue(String code, String description, String category, long amount) {
        super(code, description, category, amount);
    }

    /** The portion of revenue derived from the regular budget. */
    private long regularAmount;

    /** The portion of revenue derived from the public investment budget. */
    private long publicInvestmentAmount;

    /**
     * Detailed constructor for consolidated budget revenues.
     * Automatically registers the instance in the global static registry.
     *
     * @param code                   the unique financial code
     * @param description            a brief description
     * @param category               the financial category
     * @param regularAmount          the amount from regular resources
     * @param publicInvestmentAmount the amount from investment resources
     * @param amount                 the total combined amount
     */
    public BudgetRevenue(String code, String description, String category, long regularAmount, long publicInvestmentAmount, long amount) {
        super(code, description, category, amount);
        this.regularAmount = regularAmount;
        this.publicInvestmentAmount = publicInvestmentAmount;
        budgetRevenues.add(this);
    }

    /**
     * Sorts the global revenue list alphabetically by code.
     * This is a prerequisite for the duplicate filtering process.
     */
    public static void sortBudgetRevenuesByCode() {
        Collections.sort(budgetRevenues, new Comparator<BudgetRevenue>() {
            @Override
            public int compare(BudgetRevenue b1, BudgetRevenue b2) {
                return b1.getCode().compareTo(b2.getCode());
            }
        });
    }

    /**
     * Merges duplicate revenue entries in the global registry.
     * <p>
     * When multiple objects share the same code (e.g., from different data inputs),
     * their amounts are summed into a single consolidated object to ensure
     * budget integrity.
     */
    public static void filterBudgetRevenues() {
        ArrayList<Integer> repeatedRevenues = new ArrayList<>();
        //Finds codes that are repeated in the list
        for (int i = 1; i < budgetRevenues.size(); i++) {
            if (budgetRevenues.get(i).getCode().equals(budgetRevenues.get(i - 1).getCode())) {
                repeatedRevenues.add(i);
            }
        }

        // Merge repeated values into a single object and remove the redundant ones
        for (int j = repeatedRevenues.size() - 1; j >= 0; j--) {
            Integer i = repeatedRevenues.get(j);
            BudgetRevenue b1 = budgetRevenues.get(i);
            BudgetRevenue b2 = budgetRevenues.get(i - 1);
            budgetRevenues.remove(b1);
            b2.setRegularAmount(b2.getRegularAmount() + b1.getRegularAmount(), false);
            b2.setPublicInvestmentAmount(b2.getPublicInvestmentAmount() + b1.getPublicInvestmentAmount(), false);
            b2.setAmount(b2.getAmount() + b1.getAmount());
        }
    }

    /**
     * Returns the global list of all consolidated budget revenues.
     * @return an ArrayList of all instances
     */
    public static ArrayList<BudgetRevenue> getAllBudgetRevenues() {
        return budgetRevenues;
    }

    /**
     * Retrieves top-level (2-digit code) revenues.
     * @return an ArrayList of main budget categories
     */
    public static ArrayList<BudgetRevenue> getMainBudgetRevenues() {
        return BudgetRevenueLogicService.getMainBudgetRevenues(getAllBudgetRevenues());
    }

    /**
     * Searches for a consolidated revenue entry by its code.
     * @param code the code to search for
     * @return the matching entry or null
     */
    public static BudgetRevenue findBudgetRevenueWithCode(String code) {
        return BudgetRevenueLogicService.findRevenueWithCode(code, budgetRevenues);
    }

    /**
     * Retrieves all revenues starting with a specific code prefix.
     * @param code the code prefix
     * @return a list of matching entries
     */
    public static ArrayList<BudgetRevenue> getBudgetRevenuesStartingWithCode(String code) {
        return BudgetRevenueLogicService.getRevenuesStartingWithCode(code, budgetRevenues);
    }

    /**
     * Calculates the total sum of all main budget categories.
     * @return the total budget sum
     */
    public static long calculateSum() {
        return BudgetRevenueLogicService.calculateSum(budgetRevenues);
    }

    /**
     * Determines the hierarchical level of the entry based on the length of its code.
     * <ul>
     * <li>Level 1: 2 digits (e.g., "11")</li>
     * <li>Level 2: 3 digits</li>
     * <li>Level 3: 5 digits</li>
     * <li>Level 4: 7 digits</li>
     * <li>Level 5: 10 digits</li>
     * </ul>
     *
     * @return the level index (1-5), or 0 for unknown lengths
     */
    public int getLevelOfHierarchy() {
        return switch (getCode().length()) {
            case 2 -> 1;
            case 3 -> 2;
            case 5 -> 3;
            case 7 -> 4;
            case 10 -> 5;
            default -> 0;
        };
    }

    /* Hierarchy Navigation Implementation */

    /** @return The parent category immediately above this entry in the hierarchy. */
    @Override
    public BudgetRevenue getAboveLevelSuperCategory() {
        return BudgetRevenueLogicService.getAboveLevelSuperCategory(this, budgetRevenues);
    }

    /** @return A list of all ancestor categories reaching up to Level 1. */
    @Override
    public ArrayList<BudgetRevenue> getAllSuperCategories() {
        return BudgetRevenueLogicService.getAllSuperCategories(this, budgetRevenues);
    }

    /** @return A list of immediate child categories directly below this entry. */
    @Override
    public ArrayList<BudgetRevenue> getNextLevelSubCategories() {
        return BudgetRevenueLogicService.getNextLevelSubCategories(this, budgetRevenues);
    }

    /** @return A list of all descendant sub-categories at all lower levels. */
    @Override
    public ArrayList<BudgetRevenue> getAllSubCategories() {
        return BudgetRevenueLogicService.getAllSubCategories(this, budgetRevenues);
    }

    /* Getters & Setters */

    /** @return The amount of revenue from the regular budget. */
    public long getRegularAmount() {
        return regularAmount;
    }

    /** @return The amount of revenue from the public investment budget. */
    public long getPublicInvestmentAmount() {
        return publicInvestmentAmount;
    }

    /**
     * Sets the public investment portion of the revenue.
     * @param amount the new amount (non-negative)
     * @param update if true, synchronizes the total amount field
     */
    public void setPublicInvestmentAmount(long amount, boolean update) {
        if (amount >= 0) {
            this.publicInvestmentAmount = amount;
            if (update) {
                this.amount = regularAmount + publicInvestmentAmount;
            }
        }
    }

    /**
     * Sets the regular budget portion of the revenue.
     * @param amount the new amount (non-negative)
     * @param update if true, synchronizes the total amount field
     */
    public void setRegularAmount(long amount, boolean update) {
        if (amount >= 0) {
            this.regularAmount = amount;
            if (update) {
                this.amount = regularAmount + publicInvestmentAmount;
            }
        }
    }

    /**
     * Compares this revenue entry with another for equality based on code and component amounts.
     * @param o The object to compare.
     * @return {@code true} if codes and amounts match exactly.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BudgetRevenue that = (BudgetRevenue) o;
        return Objects.equals(getCode(), that.getCode()) &&
                this.regularAmount == that.regularAmount &&
                this.publicInvestmentAmount == that.publicInvestmentAmount;
    }

    /**
     * Generates a hash code for this entry using its class and unique code.
     * @return The calculated hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getClass(), getCode());
    }

    /** @return A string representation of the entry from the superclass. */
    @Override
    public String toString () {
        return super.toString();
    }
}