package com.financial.entries;

import com.financial.services.*;
import com.financial.services.revenues.*;
import java.util.ArrayList;

/**
 * Represents a revenue entry specifically for the National part of the
 * Public Investment Budget (ΠΔΕ).
 * <p>
 * This class implements hierarchical navigation and automated change propagation
 * specifically for national investment resources, while ensuring synchronization
 * with the base public investment filtered data.
 */
public class PublicInvestmentBudgetNationalRevenue extends PublicInvestmentBudgetRevenue implements BudgetRevenueLogic, BudgetRevenueChanges {

    /**
     * Internal registry of all national public investment revenue instances.
     */
    protected static ArrayList<PublicInvestmentBudgetNationalRevenue> publicInvestmentBudgetNationalRevenues = new ArrayList<>();

    /**
     * Constructs a new National PDE revenue entry and creates its corresponding
     * filtered representation in the {@link PublicInvestmentBudgetRevenue} scope.
     *
     * @param code        the unique financial code
     * @param description a brief description of the revenue
     * @param category    the financial category (e.g., "ΕΣΟΔΑ")
     * @param type        the investment type (e.g., "ΕΘΝΙΚΟ")
     * @param amount      the initial amount in euros
     */
    public PublicInvestmentBudgetNationalRevenue(String code, String description, String category, String type, long amount) {
        super(code, description, category, type, amount);
        publicInvestmentBudgetNationalRevenues.add(this);

        // Creates a filtered object in the superclass to maintain resource-specific amounts
        new PublicInvestmentBudgetRevenue(code, description, category, type, amount, 0, amount);
    }

    /**
     * Returns the registry of all national public investment revenues.
     * @return an ArrayList of all instances
     */
    public static ArrayList<PublicInvestmentBudgetNationalRevenue> getAllPublicInvestmentBudgetNationalRevenues() {
        return publicInvestmentBudgetNationalRevenues;
    }

    /**
     * Filters and returns only the top-level (2-digit code) national investment revenues.
     * @return an ArrayList of root national entries
     */
    public static ArrayList<PublicInvestmentBudgetNationalRevenue> getMainPublicInvestmentBudgetNationalRevenues() {
        return BudgetRevenueLogicService.getMainBudgetRevenues(publicInvestmentBudgetNationalRevenues);
    }

    /**
     * Searches for a specific national investment revenue by its code.
     * @param code the code to search for
     * @return the matching entry or null if not found
     */
    public static PublicInvestmentBudgetNationalRevenue findPublicInvestmentBudgetNationalRevenueWithCode(String code) {
        return BudgetRevenueLogicService.findRevenueWithCode(code, publicInvestmentBudgetNationalRevenues);
    }

    /**
     * Retrieves all national investment revenues that start with the specified code prefix.
     * @param code the prefix to match
     * @return an ArrayList of matching BudgetRevenue objects
     */
    public static ArrayList<BudgetRevenue> getPublicInvestmentBudgetNationalRevenuesStartingWithCode(String code) {
        return BudgetRevenueLogicService.getRevenuesStartingWithCode(code, publicInvestmentBudgetNationalRevenues);
    }

    /**
     * Calculates the total sum of all main (top-level) national public investment revenues.
     * @return the total amount sum
     */
    public static long calculateSum() {
        return BudgetRevenueLogicService.calculateSum(publicInvestmentBudgetNationalRevenues);
    }

    /* Hierarchy Navigation Implementation */

    /** @return The parent category immediately above this entry in the national hierarchy. */
    @Override
    public PublicInvestmentBudgetNationalRevenue getAboveLevelSuperCategory() {
        return BudgetRevenueLogicService.getAboveLevelSuperCategory(this, publicInvestmentBudgetNationalRevenues);
    }

    /** @return A list of all ancestor categories in the national budget hierarchy. */
    @Override
    public ArrayList<BudgetRevenue> getAllSuperCategories() {
        return BudgetRevenueLogicService.getAllSuperCategories(this, publicInvestmentBudgetNationalRevenues);
    }

    /** @return A list of immediate sub-categories (children) for this entry. */
    @Override
    public ArrayList<BudgetRevenue> getNextLevelSubCategories() {
        return BudgetRevenueLogicService.getNextLevelSubCategories(this, publicInvestmentBudgetNationalRevenues);
    }

    /** @return A list of all descendant categories across all lower hierarchical levels. */
    @Override
    public ArrayList<BudgetRevenue> getAllSubCategories() {
        return BudgetRevenueLogicService.getAllSubCategories(this, publicInvestmentBudgetNationalRevenues);
    }

    /* Change Implementation */

    /**
     * Propagates financial changes to all parent categories in the hierarchy.
     * @param change The amount to adjust for all super categories.
     */
    @Override
    public void setAmountOfSuperCategories(long change) {
        BudgetRevenueChangesService.setAmountOfSuperCategories(this.getAllSuperCategories(), change);
    }

    /**
     * Distributes a fixed amount change equally among all sub-categories.
     * @param change The total amount to distribute equally.
     */
    @Override
    public void setAmountOfAllSubCategoriesWithEqualDistribution(long change) {
        BudgetRevenueChangesService.setAmountOfAllSubCategoriesWithEqualDistribution(this, publicInvestmentBudgetNationalRevenues, change);
    }

    /**
     * Applies a percentage-based adjustment factor to all sub-categories.
     * @param percentage The percentage to apply (e.g., 0.10 for 10%).
     */
    @Override
    public void setAmountOfAllSubCategoriesWithPercentageAdjustment(double percentage) {
        BudgetRevenueChangesService.setAmountOfAllSubCategoriesWithPercentageAdjustment(this, publicInvestmentBudgetNationalRevenues, percentage);
    }

    /**
     * Implements a mass change using equal distribution across the national investment hierarchy.
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
     * Implements a mass change using percentage adjustment across the national investment hierarchy.
     * @param percentage the percentage adjustment factor
     */
    @Override
    public void implementChangesOfPercentageAdjustment(double percentage) {
        keepAccountsAndBudgetTypeBeforeChange();
        setAmountOfSuperCategories((long) (getAmount() * (percentage)));
        setAmountOfAllSubCategoriesWithPercentageAdjustment(percentage);
        setAmount((long) (getAmount() * (1 + percentage)));
    }

    /**
     * Records the current state of the hierarchy branch in history before a modification.
     */
    @Override
    public void keepAccountsAndBudgetTypeBeforeChange() {
        ArrayList<BudgetRevenue> accountsForChange = new ArrayList<>(this.getAllSuperCategories());
        accountsForChange.add(this);
        accountsForChange.addAll(getAllSubCategories());
        RevenuesHistory.keepHistory(accountsForChange, BudgetType.PUBLIC_INVESTMENT_BUDGET_NATIONAL);
    }

    /**
     * Updates the national-specific amount field in the filtered superclass object
     * to ensure data consistency.
     * @param change the new national amount
     */
    @Override
    public void updateAmountOfSuperClassFilteredObject(long change) {
        PublicInvestmentBudgetRevenue publicInvestmentBudgetRevenue = PublicInvestmentBudgetRevenue.findPublicInvestmentBudgetRevenueWithCode(this.getCode());
        if (publicInvestmentBudgetRevenue != null ) {
            publicInvestmentBudgetRevenue.setNationalAmount(amount, true);
        }
    }

    /**
     * Sets the amount for this national revenue entry.
     * <p>
     * Performs rounding to the nearest hundred and validates that the amount is non-negative.
     * If validation fails, it triggers a history undo and throws an exception.
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

    /** @return A string representation of the national investment revenue entry. */
    @Override
    public String toString() {
        return super.toString();
    }
}