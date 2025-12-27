package com.financial.services.revenues;

/**
 * Interface defining the necessary methods for modifying budget revenue amounts
 * and managing the propagation of these changes across the hierarchy.
 * <p>
 * Implementing classes must handle both upward and downward distribution of changes,
 * as well as maintaining state consistency and change history.
 */
public interface BudgetRevenueChanges {

    /**
     * Orchestrates the implementation of an amount change using the Equal Distribution strategy.
     * This typically involves saving history, updating the current account,
     * and propagating the fixed change value to all related categories.
     *
     * @param change the fixed amount to be added or subtracted.
     */
    void implementChangesOfEqualDistribution(long change);

    /**
     * Orchestrates the implementation of an amount change using the Percentage Adjustment strategy.
     * This typically involves saving history, updating the current account,
     * and propagating the percentage-based change to all related categories.
     *
     * @param percentage the percentage adjustment (e.g., 0.05 for +5%).
     */
    void implementChangesOfPercentageAdjustment(double percentage);

    /**
     * Propagates an amount change upwards to all parent categories (supercategories).
     *
     * @param change the amount to be added to each ancestor's total.
     */
    void setAmountOfSuperCategories(long change);

    /**
     * Propagates an amount change downwards to all descendant categories (subcategories)
     * using an equal distribution logic.
     *
     * @param change the total amount to be distributed among the descendants.
     */
    void setAmountOfAllSubCategoriesWithEqualDistribution(long change);

    /**
     * Propagates an amount change downwards to all descendant categories (subcategories)
     * using a percentage adjustment logic.
     *
     * @param percentage the percentage to apply to all descendant amounts.
     */
    void setAmountOfAllSubCategoriesWithPercentageAdjustment(double percentage);

    /**
     * Synchronizes the change with the filtered representation of this object
     * in the base {@code BudgetRevenue} class to ensure data consistency across views.
     *
     * @param change the new total amount for the filtered object.
     */
    void updateAmountOfSuperClassFilteredObject(long change);

    /**
     * Captures a snapshot of all accounts (current, parents, and children)
     * that are about to be modified, storing them in the {@code RevenuesHistory}.
     * This is critical for supporting Undo operations.
     */
    void keepAccountsAndBudgetTypeBeforeChange();
}