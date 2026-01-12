package com.financial.entries;

import com.financial.services.revenues.BudgetRevenueLogicService;
import com.financial.services.data.DataInput;
import com.financial.services.revenues.BudgetRevenueLogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

/**
 * Intermediate class representing revenues from the Public Investment Budget (ΠΔΕ).
 * <p>
 * This class serves as a container for both National and Co-funded amounts,
 * providing mechanisms to merge duplicate entries and synchronize the total
 * investment amount with the base {@link BudgetRevenue} class.
 */
public class PublicInvestmentBudgetRevenue extends BudgetRevenue implements BudgetRevenueLogic {

    /**
     * The type of investment (e.g., "ΕΘΝΙΚΟ" or "ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ").
     */
    private final String type;

    /**
     * Internal registry of all public investment revenue instances.
     */
    protected static ArrayList<PublicInvestmentBudgetRevenue> publicInvestmentBudgetRevenues = new ArrayList<>();

    /**
     * Basic constructor for public investment revenue.
     *
     * @param code        the unique financial code
     * @param description a brief description
     * @param category    the financial category
     * @param type        the investment type
     * @param amount      the total amount
     */
    public PublicInvestmentBudgetRevenue(String code, String description, String category, String type, long amount) {
        super(code, description, category, amount);
        this.type = type;
    }

    private long nationalAmount;
    private long coFundedAmount;

    /**
     * Detailed constructor used primarily for creating filtered/merged investment objects.
     * Automatically registers the instance in the static tracking list.
     *
     * @param code           the unique financial code
     * @param description    a brief description
     * @param category       the financial category
     * @param type           the investment type
     * @param nationalAmount the portion from national resources
     * @param coFundedAmount the portion from co-funded resources
     * @param amount         the total combined amount
     */
    public PublicInvestmentBudgetRevenue(String code, String description, String category, String type, long nationalAmount, long coFundedAmount, long amount) {
        super(code, description, category, amount);
        this.type = type;
        this.nationalAmount = nationalAmount;
        this.coFundedAmount = coFundedAmount;
        publicInvestmentBudgetRevenues.add(this);
    }

    /**
     * Sorts the public investment revenues list alphabetically by their code.
     * Essential before performing filtering/merging operations.
     */
    public static void sortPublicInvestmentBudgetRevenuesByCode() {
        Collections.sort(publicInvestmentBudgetRevenues, new Comparator<PublicInvestmentBudgetRevenue>() {
            @Override
            public int compare(PublicInvestmentBudgetRevenue b1, PublicInvestmentBudgetRevenue b2) {
                return b1.getCode().compareTo(b2.getCode());
            }
        });
    }

    /**
     * Merges entries with identical codes into single objects by summing their components.
     * After merging, it triggers the creation of filtered budget revenues in {@link DataInput}.
     */
    public static void filterPublicInvestmentBudgetRevenues() {
        ArrayList<Integer> repeatedRevenues = new ArrayList<>();

        // Identify duplicate codes in the sorted list
        for (int i = 1; i < publicInvestmentBudgetRevenues.size(); i++) {
            if (publicInvestmentBudgetRevenues.get(i).getCode().equals(publicInvestmentBudgetRevenues.get(i - 1).getCode())) {
                repeatedRevenues.add(i);
            }
        }

        // Merge duplicates starting from the end of the list
        for (int j = repeatedRevenues.size() - 1; j >= 0; j--) {
            Integer i = repeatedRevenues.get(j);
            PublicInvestmentBudgetRevenue b1 = publicInvestmentBudgetRevenues.get(i);
            PublicInvestmentBudgetRevenue b2 = publicInvestmentBudgetRevenues.get(i - 1);
            publicInvestmentBudgetRevenues.remove(b1);
            b2.setNationalAmount(b2.getNationalAmount() + b1.getNationalAmount(), false);
            b2.setCoFundedAmount(b2.getCoFundedAmount() + b1.getCoFundedAmount(), false);
            b2.setAmount(b2.getAmount() + b1.getAmount());
        }
        DataInput.createBudgetRevenueFilteredFromPublicInvestmentBudgetRevenue();
    }

    /**
     * Returns the full list of public investment revenues.
     * @return an ArrayList of all instances
     */
    public static ArrayList<PublicInvestmentBudgetRevenue> getAllPublicInvestmentBudgetRevenues() {
        return publicInvestmentBudgetRevenues;
    }

    /**
     * Retrieves top-level (2-digit code) investment revenues.
     * @return an ArrayList of main investment categories
     */
    public static ArrayList<PublicInvestmentBudgetRevenue> getMainPublicInvestmentBudgetRevenues() {
        return BudgetRevenueLogicService.getMainBudgetRevenues(publicInvestmentBudgetRevenues);
    }

    /**
     * Finds a specific investment revenue by its code.
     * @param code the code to look for
     * @return the matching object or null
     */
    public static PublicInvestmentBudgetRevenue findPublicInvestmentBudgetRevenueWithCode(String code) {
        return BudgetRevenueLogicService.findRevenueWithCode(code, publicInvestmentBudgetRevenues);
    }

    /**
     * Retrieves all investment revenues starting with a given prefix.
     * @param code the code prefix
     * @return a list of matching BudgetRevenue objects
     */
    public static ArrayList<BudgetRevenue> getPublicInvestmentBudgetRevenuesStartingWithCode(String code) {
        return BudgetRevenueLogicService.getRevenuesStartingWithCode(code, publicInvestmentBudgetRevenues);
    }

    /**
     * Calculates the total sum of amounts for all main investment categories.
     * @return the total sum
     */
    public static long calculateSum() {
        return BudgetRevenueLogicService.calculateSum(publicInvestmentBudgetRevenues);
    }

    /* Hierarchy Logic Implementation */

    @Override
    public PublicInvestmentBudgetRevenue getAboveLevelSuperCategory() {
        return BudgetRevenueLogicService.getAboveLevelSuperCategory(this, publicInvestmentBudgetRevenues);
    }

    @Override
    public ArrayList<BudgetRevenue> getAllSuperCategories() {
        return BudgetRevenueLogicService.getAllSuperCategories(this, publicInvestmentBudgetRevenues);
    }

    @Override
    public ArrayList<BudgetRevenue> getNextLevelSubCategories() {
        return BudgetRevenueLogicService.getNextLevelSubCategories(this, publicInvestmentBudgetRevenues);
    }

    @Override
    public ArrayList<BudgetRevenue> getAllSubCategories() {
        return BudgetRevenueLogicService.getAllSubCategories(this, publicInvestmentBudgetRevenues);
    }

    /**
     * Updates the global {@link BudgetRevenue} registry with changes from this investment entry.
     * This ensures that the public investment totals are reflected in the overall budget view.
     *
     * @param change the new total investment amount for this code
     */
    public void updateAmountOfSuperClassFilteredObjects(long change) {
        BudgetRevenue budgetRevenue = BudgetRevenue.findBudgetRevenueWithCode(this.getCode());
        if (budgetRevenue != null ) {
            budgetRevenue.setPublicInvestmentAmount(change, true);
        }
    }

    /* Getters and Setters */

    public String getType() {
        return type;
    }

    public long getNationalAmount() {
        return nationalAmount;
    }

    public long getCoFundedAmount() {
        return coFundedAmount;
    }

    /**
     * Sets the national portion of the investment.
     *
     * @param newNationalAmount the new amount (must be non-negative)
     * @param update if true, recalculates total amount and notifies the superclass view
     */
    public void setNationalAmount(long newNationalAmount, boolean update) {
        if (newNationalAmount >= 0) {
            this.nationalAmount = newNationalAmount;
            if (update) {
                this.amount = nationalAmount + coFundedAmount;
                updateAmountOfSuperClassFilteredObjects(amount);
            }
        }
    }

    /**
     * Sets the co-funded portion of the investment.
     *
     * @param newCoFundedAmount the new amount (must be non-negative)
     * @param update if true, recalculates total amount and notifies the superclass view
     */
    public void setCoFundedAmount(long newCoFundedAmount, boolean update) {
        if (newCoFundedAmount >= 0) {
            this.coFundedAmount = newCoFundedAmount;
            if (update) {
                this.amount = nationalAmount + coFundedAmount;
                updateAmountOfSuperClassFilteredObjects(amount);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PublicInvestmentBudgetRevenue that = (PublicInvestmentBudgetRevenue) o;

        return Objects.equals(getCode(), that.getCode()) &&
                Objects.equals(type, that.type); // Διάκριση ΕΘΝΙΚΟ vs ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), getCode(), type);
    }

    @Override
    public String toString() {
        return "Code: " + getCode() + ", Description: " + getDescription() + ", Category: " + getCategory() + ", Type: " + type + ", Amount: " + String.format("%,d", getAmount());
    }
}