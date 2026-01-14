package com.financial.entries;

import com.financial.services.*;
import com.financial.services.revenues.*;
import java.util.ArrayList;

/**
 * Represents a revenue entry specifically for the Co-funded (Συγχρηματοδοτούμενο) part
 * of the Public Investment Budget (ΠΔΕ).
 * <p>
 * This class handles co-financed resources, ensuring that any financial changes
 * are correctly propagated through the co-funded hierarchy and synchronized
 * with the base public investment data.
 */
public class PublicInvestmentBudgetCoFundedRevenue extends PublicInvestmentBudgetRevenue implements BudgetRevenueLogic, BudgetRevenueChanges {

    /**
     * Internal registry of all co-funded public investment revenue instances.
     */
    protected static ArrayList<PublicInvestmentBudgetCoFundedRevenue> publicInvestmentBudgetCoFundedRevenues = new ArrayList<>();

    /**
     * Constructs a new Co-funded PDE revenue entry and creates its corresponding
     * filtered representation in the {@link PublicInvestmentBudgetRevenue} scope.
     *
     * @param code        the unique financial code
     * @param description a brief description of the revenue
     * @param category    the financial category (e.g., "ΕΣΟΔΑ")
     * @param type        the investment type (e.g., "ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ")
     * @param amount      the initial amount in euros
     */
    public PublicInvestmentBudgetCoFundedRevenue(String code, String description, String category, String type, long amount) {
        super(code, description, category, type, amount);
        publicInvestmentBudgetCoFundedRevenues.add(this);

        // Creates a filtered object in the superclass, setting the co-funded field
        new PublicInvestmentBudgetRevenue(code, description, category, type, 0, amount, amount);
    }

    /**
     * Returns the registry of all co-funded public investment revenues.
     * @return an ArrayList of all instances
     */
    public static ArrayList<PublicInvestmentBudgetCoFundedRevenue> getAllPublicInvestmentBudgetCoFundedRevenues() {
        return publicInvestmentBudgetCoFundedRevenues;
    }

    /**
     * Filters and returns only the top-level (2-digit code) co-funded revenues.
     * @return an ArrayList of root co-funded entries
     */
    public static ArrayList<PublicInvestmentBudgetCoFundedRevenue> getMainPublicInvestmentBudgetCoFundedRevenues() {
        return BudgetRevenueLogicService.getMainBudgetRevenues(publicInvestmentBudgetCoFundedRevenues);
    }

    /**
     * Searches for a specific co-funded revenue by its code.
     * @param code the code to search for
     * @return the matching entry or null if not found
     */
    public static PublicInvestmentBudgetCoFundedRevenue findPublicInvestmentBudgetCoFundedRevenueWithCode(String code) {
        return BudgetRevenueLogicService.findRevenueWithCode(code, publicInvestmentBudgetCoFundedRevenues);
    }

    /**
     * Retrieves all co-funded revenues that start with the specified code prefix.
     * @param code the prefix to match
     * @return an ArrayList of matching BudgetRevenue objects
     */
    public static ArrayList<BudgetRevenue> getPublicInvestmentBudgetCoFundedRevenuesStartingWithCode(String code) {
        return BudgetRevenueLogicService.getRevenuesStartingWithCode(code, publicInvestmentBudgetCoFundedRevenues);
    }

    /**
     * Calculates the total sum of all main (top-level) co-funded public investment revenues.
     * @return the total amount sum
     */
    public static long calculateSum() {
        return BudgetRevenueLogicService.calculateSum(publicInvestmentBudgetCoFundedRevenues);
    }

    /* Hierarchy Navigation Implementation */

    @Override
    public PublicInvestmentBudgetCoFundedRevenue getAboveLevelSuperCategory() {
        return BudgetRevenueLogicService.getAboveLevelSuperCategory(this, publicInvestmentBudgetCoFundedRevenues);
    }

    @Override
    public ArrayList<BudgetRevenue> getAllSuperCategories() {
        return BudgetRevenueLogicService.getAllSuperCategories(this, publicInvestmentBudgetCoFundedRevenues);
    }

    @Override
    public ArrayList<BudgetRevenue> getNextLevelSubCategories() {
        return BudgetRevenueLogicService.getNextLevelSubCategories(this, publicInvestmentBudgetCoFundedRevenues);
    }

    @Override
    public ArrayList<BudgetRevenue> getAllSubCategories() {
        return BudgetRevenueLogicService.getAllSubCategories(this, publicInvestmentBudgetCoFundedRevenues);
    }

    /* Change Implementation */

    @Override
    public void setAmountOfSuperCategories(long change) {
        BudgetRevenueChangesService.setAmountOfSuperCategories(this.getAllSuperCategories(), change);
    }

    @Override
    public void setAmountOfAllSubCategoriesWithEqualDistribution(long change) {
        BudgetRevenueChangesService.setAmountOfAllSubCategoriesWithEqualDistribution(this, publicInvestmentBudgetCoFundedRevenues, change);
    }

    @Override
    public void setAmountOfAllSubCategoriesWithPercentageAdjustment(double percentage) {
        BudgetRevenueChangesService.setAmountOfAllSubCategoriesWithPercentageAdjustment(this, publicInvestmentBudgetCoFundedRevenues, percentage);
    }

    @Override
    public void implementChangesOfEqualDistribution(long change) {
        keepAccountsAndBudgetTypeBeforeChange();
        setAmount(getAmount() + change);
        setAmountOfSuperCategories(change);
        setAmountOfAllSubCategoriesWithEqualDistribution(change);
    }

    @Override
    public void implementChangesOfPercentageAdjustment(double percentage) {
        keepAccountsAndBudgetTypeBeforeChange();
        setAmountOfSuperCategories((long) (getAmount() * (percentage)));
        setAmountOfAllSubCategoriesWithPercentageAdjustment(percentage);
        setAmount((long) (getAmount() * (1 + percentage)));
    }

    @Override
    public void keepAccountsAndBudgetTypeBeforeChange() {
        ArrayList<BudgetRevenue> accountsForChange = new ArrayList<>(this.getAllSuperCategories());
        accountsForChange.add(this);
        accountsForChange.addAll(getAllSubCategories());
        RevenuesHistory.keepHistory(accountsForChange, BudgetType.PUBLIC_INVESTMENT_BUDGET_COFUNDED);
    }

    /**
     * Updates the co-funded amount field in the filtered {@link PublicInvestmentBudgetRevenue}
     * object to maintain data consistency.
     * @param change the new co-funded amount
     */
    @Override
    public void updateAmountOfSuperClassFilteredObject(long change) {
        PublicInvestmentBudgetRevenue publicInvestmentBudgetRevenue = PublicInvestmentBudgetRevenue.findPublicInvestmentBudgetRevenueWithCode(this.getCode());
        if (publicInvestmentBudgetRevenue != null ) {
            publicInvestmentBudgetRevenue.setCoFundedAmount(amount, true);
        }
    }

    /**
     * Sets the amount for this co-funded revenue entry.
     * <p>
     * Performs financial rounding and validates the value. If negative,
     * the system reverts to the previous state using the history deque.
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

    @Override
    public String toString() {
        return super.toString();
    }
}