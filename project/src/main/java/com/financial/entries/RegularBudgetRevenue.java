package com.financial.entries;

import com.financial.services.revenues.*;
import com.financial.services.*;
import java.util.ArrayList;

/**
 * Represents a revenue entry in the Regular Budget.
 * <p>
 * This class implements both {@link BudgetRevenueLogic} and {@link BudgetRevenueChanges},
 * providing specialized functionality for hierarchical navigation and automated
 * amount adjustments specific to regular budget accounts.
 */
public class RegularBudgetRevenue extends BudgetRevenue implements BudgetRevenueLogic, BudgetRevenueChanges {

    /**
     * Internal registry of all instances of RegularBudgetRevenue.
     */
    protected static ArrayList<RegularBudgetRevenue> regularBudgetRevenues = new ArrayList<>();

    /**
     * Constructs a new RegularBudgetRevenue and automatically synchronizes it
     * with the global {@link BudgetRevenue} filtered list.
     *
     * @param code        the unique financial code
     * @param description a brief description of the revenue
     * @param category    the financial category (e.g., "ΕΣΟΔΑ")
     * @param amount      the initial amount in euros
     */
    public RegularBudgetRevenue(String code, String description, String category, long amount) {
        super(code, description, category, amount);
        regularBudgetRevenues.add(this);

        // Creates a corresponding filtered object in the SuperClass to maintain consistency
        new BudgetRevenue(code, description, category, amount, 0, amount);
    }

    /**
     * Returns the registry of all regular budget revenue entries.
     * @return an ArrayList of all instances
     */
    public static ArrayList<RegularBudgetRevenue> getAllRegularBudgetRevenues() {
        return regularBudgetRevenues;
    }

    /**
     * Filters and returns only the top-level (2-digit code) regular budget revenues.
     * @return an ArrayList of main budget entries
     */
    public static ArrayList<RegularBudgetRevenue> getMainRegularBudgetRevenues() {
        return BudgetRevenueLogicService.getMainBudgetRevenues(getAllRegularBudgetRevenues());
    }

    /**
     * Searches for a specific regular budget revenue by its code.
     * @param code the code to search for
     * @return the matching entry or null if not found
     */
    public static RegularBudgetRevenue findRegularBudgetRevenueWithCode(String code) {
        return BudgetRevenueLogicService.findRevenueWithCode(code, regularBudgetRevenues);
    }

    /**
     * Retrieves all regular budget revenues that start with the specified code prefix.
     * @param code the prefix to match
     * @return an ArrayList of matching BudgetRevenue objects
     */
    public static ArrayList<BudgetRevenue> getRegularBudgetRevenuesStartingWithCode(String code) {
        return BudgetRevenueLogicService.getRevenuesStartingWithCode(code, regularBudgetRevenues);
    }

    /**
     * Calculates the total sum of all main (top-level) regular budget revenues.
     * @return the total amount sum
     */
    public static long calculateSum() {
        return BudgetRevenueLogicService.calculateSum(regularBudgetRevenues);
    }

    /* Hierarchy Navigation Implementation */

    /** @return The parent category immediately above this entry in the regular budget. */
    @Override
    public RegularBudgetRevenue getAboveLevelSuperCategory() {
        return BudgetRevenueLogicService.getAboveLevelSuperCategory(this, regularBudgetRevenues);
    }

    /** @return A list of all ancestor categories in the hierarchy. */
    @Override
    public ArrayList<BudgetRevenue> getAllSuperCategories() {
        return BudgetRevenueLogicService.getAllSuperCategories(this, regularBudgetRevenues);
    }

    /** @return A list of immediate sub-categories (children) for this entry. */
    @Override
    public ArrayList<BudgetRevenue> getNextLevelSubCategories() {
        return BudgetRevenueLogicService.getNextLevelSubCategories(this, regularBudgetRevenues);
    }

    /** @return A list of all descendant categories across all lower levels. */
    @Override
    public ArrayList<BudgetRevenue> getAllSubCategories() {
        return BudgetRevenueLogicService.getAllSubCategories(this, regularBudgetRevenues);
    }

    /* Change Implementation */

    /**
     * Updates the financial amounts of all parent categories in the hierarchy.
     * @param change The amount to add to or subtract from the super categories.
     */
    @Override
    public void setAmountOfSuperCategories(long change) {
        BudgetRevenueChangesService.setAmountOfSuperCategories(getAllSuperCategories(), change);
    }

    /**
     * Distributes a total change amount equally among all sub-categories.
     * @param change The total amount to be divided and distributed.
     */
    @Override
    public void setAmountOfAllSubCategoriesWithEqualDistribution(long change) {
        BudgetRevenueChangesService.setAmountOfAllSubCategoriesWithEqualDistribution(this, regularBudgetRevenues, change);
    }

    /**
     * Applies a percentage-based adjustment to all sub-categories.
     * @param percentage The percentage to apply (e.g., 0.10 for 10%).
     */
    @Override
    public void setAmountOfAllSubCategoriesWithPercentageAdjustment(double percentage) {
        BudgetRevenueChangesService.setAmountOfAllSubCategoriesWithPercentageAdjustment(this, regularBudgetRevenues, percentage);
    }

    /**
     * Implements a mass change using equal distribution.
     * Affects the account itself, its parents, and its children.
     * @param change the fixed amount to distribute
     */
    @Override
    public void implementChangesOfEqualDistribution(long change) {
        keepAccountsAndBudgetTypeBeforeChange();
        setAmount(getAmount() + change);
        setAmountOfSuperCategories(change);
        setAmountOfAllSubCategoriesWithEqualDistribution(change);
    }

    /**
     * Implements a mass change using percentage adjustment.
     * @param percentage the percentage to apply (e.g. 0.1 for 10%)
     */
    @Override
    public void implementChangesOfPercentageAdjustment(double percentage) {
        keepAccountsAndBudgetTypeBeforeChange();
        setAmountOfSuperCategories((long) (getAmount() * (percentage)));
        setAmountOfAllSubCategoriesWithPercentageAdjustment(percentage);
        setAmount((long) (getAmount() * (1 + percentage)));
    }

    /**
     * Captures the state of the entire hierarchy branch (parents, self, children)
     * before a change is applied.
     */
    @Override
    public void keepAccountsAndBudgetTypeBeforeChange() {
        ArrayList<BudgetRevenue> accountsForChange = new ArrayList<>();
        accountsForChange.addAll(getAllSuperCategories());
        accountsForChange.add(this);
        accountsForChange.addAll(getAllSubCategories());
        RevenuesHistory.keepHistory(accountsForChange, BudgetType.REGULAR_BUDGET);
    }

    /**
     * Updates the corresponding Regular Amount in the base {@link BudgetRevenue}
     * to maintain data synchronization.
     * @param change the new amount
     */
    @Override
    public void updateAmountOfSuperClassFilteredObject(long change) {
        BudgetRevenue budgetRevenue = BudgetRevenue.findBudgetRevenueWithCode(this.getCode());
        if (budgetRevenue != null) {
            budgetRevenue.setRegularAmount(amount, true);
        }
    }

    /**
     * Sets the amount for this revenue entry.
     * <p>
     * Includes validation to prevent negative amounts by triggering a history reversal
     * and performs automatic rounding to the nearest hundred.
     *
     * @param amount the new amount to set
     * @throws IllegalArgumentException if the provided amount is negative
     */
    @Override
    public void setAmount(long amount) {
        if (amount >= 0) {
            amount = BudgetRevenueChangesService.roundToNearestHundred(amount);
            this.amount = amount;
            updateAmountOfSuperClassFilteredObject(amount);
        } else {
            RevenuesHistory.returnToPreviousState();
            throw new IllegalArgumentException();
        }
    }

    /** @return A string representation of the regular budget revenue. */
    @Override
    public String toString () {
        return super.toString();
    }
}