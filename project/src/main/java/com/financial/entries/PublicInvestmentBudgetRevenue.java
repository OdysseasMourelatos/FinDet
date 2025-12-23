package com.financial.entries;

import com.financial.services.revenues.BudgetRevenueLogicService;
import com.financial.services.data.DataInput;
import com.financial.services.revenues.BudgetRevenueLogic;
import com.financial.services.revenues.RevenuesHistory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PublicInvestmentBudgetRevenue extends BudgetRevenue implements BudgetRevenueLogic {

    //Constructor & Fields

    private final String type;
    protected static ArrayList<PublicInvestmentBudgetRevenue> publicInvestmentBudgetRevenues = new ArrayList<>();

    public PublicInvestmentBudgetRevenue(String code, String description, String category, String type, long amount) {
        super(code, description, category, amount);
        this.type = type;
    }

    //Extra constructor that creates PublicInvestmentBudgetRevenues (filtered)
    private long nationalAmount;
    private long coFundedAmount;

    public PublicInvestmentBudgetRevenue(String code, String description, String category, String type, long nationalAmount, long coFundedAmount, long amount) {
        super(code, description, category, amount);
        this.type = type;
        this.nationalAmount = nationalAmount;
        this.coFundedAmount = coFundedAmount;
        publicInvestmentBudgetRevenues.add(this);
    }

    //Creation of PublicInvestmentBudgetRevenuesSorted
    public static void sortPublicInvestmentBudgetRevenuesByCode() {
        Collections.sort(publicInvestmentBudgetRevenues, new Comparator<PublicInvestmentBudgetRevenue>() {
            @Override
            public int compare(PublicInvestmentBudgetRevenue b1, PublicInvestmentBudgetRevenue b2) {
                return b1.getCode().compareTo(b2.getCode());
            }
        });
    }

    public static void filterPublicInvestmentBudgetRevenues() {
        ArrayList<Integer> repeatedRevenues = new ArrayList<>();

        //Finds codes that are repeated in the list
        // Adds to a new list the 2 placements in the list where we have repeated codes
        for (int i = 1; i < publicInvestmentBudgetRevenues.size(); i++) {
            if (publicInvestmentBudgetRevenues.get(i).getCode().equals(publicInvestmentBudgetRevenues.get(i - 1).getCode())) {
                repeatedRevenues.add(i);
            }
        }

       /*With a loop starting from the end of the list
         we in a way merge those values that are repeated into a single object
         then remove the other one
       */
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

    //Class Methods

    public static ArrayList<PublicInvestmentBudgetRevenue> getAllPublicInvestmentBudgetRevenues() {
        return publicInvestmentBudgetRevenues;
    }

    public static ArrayList<PublicInvestmentBudgetRevenue> getMainPublicInvestmentBudgetRevenues() {
        return BudgetRevenueLogicService.getMainBudgetRevenues(publicInvestmentBudgetRevenues);
    }

    public static PublicInvestmentBudgetRevenue findPublicInvestmentBudgetRevenueWithCode(String code) {
        return BudgetRevenueLogicService.findRevenueWithCode(code, publicInvestmentBudgetRevenues);
    }

    public static ArrayList<BudgetRevenue> getPublicInvestmentBudgetRevenuesStartingWithCode(String code) {
        return BudgetRevenueLogicService.getRevenuesStartingWithCode(code, publicInvestmentBudgetRevenues);
    }

    //Sum Method

    public static long calculateSum() {
        return BudgetRevenueLogicService.calculateSum(publicInvestmentBudgetRevenues);
    }

    //*Implementation of methods (Logic)*

    //Supercategories methods

    @Override
    public PublicInvestmentBudgetRevenue getAboveLevelSuperCategory() {
        return BudgetRevenueLogicService.getAboveLevelSuperCategory(this, publicInvestmentBudgetRevenues);
    }

    @Override
    public ArrayList<BudgetRevenue> getAllSuperCategories() {
        return BudgetRevenueLogicService.getAllSuperCategories(this, publicInvestmentBudgetRevenues);
    }

    //Subcategories methods

    @Override
    public ArrayList<BudgetRevenue> getNextLevelSubCategories() {
        return BudgetRevenueLogicService.getNextLevelSubCategories(this, publicInvestmentBudgetRevenues);
    }

    @Override
    public ArrayList<BudgetRevenue> getAllSubCategories() {
        return BudgetRevenueLogicService.getAllSubCategories(this, publicInvestmentBudgetRevenues);
    }

    //Updating the filtered objects in SuperClass

    public void updateAmountOfSuperClassFilteredObjects(long change) {
        BudgetRevenue budgetRevenue = BudgetRevenue.findBudgetRevenueWithCode(this.getCode());
        if (budgetRevenue != null ) {
            budgetRevenue.setPublicInvestmentAmount(change, true);
        }
    }

    //Getters & Setters

    public String getType() {
        return type;
    }

    public long getNationalAmount() {
        return nationalAmount;
    }

    public long getCoFundedAmount() {
        return coFundedAmount;
    }

    public void setNationalAmount(long newNationalAmount, boolean update) {
        if (newNationalAmount >= 0) {
            this.nationalAmount = newNationalAmount;
            if (update) {
                this.amount = nationalAmount + coFundedAmount;
                updateAmountOfSuperClassFilteredObjects(amount);
            }
        } else {
            RevenuesHistory.returnToPreviousState();
            throw new IllegalArgumentException();
        }
    }

    public void setCoFundedAmount(long newCoFundedAmount, boolean update) {
        if (newCoFundedAmount >= 0) {
            this.coFundedAmount = newCoFundedAmount;
            if (update) {
                this.amount = nationalAmount + coFundedAmount;
                updateAmountOfSuperClassFilteredObjects(amount);
            }
        } else {
            RevenuesHistory.returnToPreviousState();
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String toString() {
        return "Code: " + getCode() + ", Description: " + getDescription() + ", Category: " + getCategory() + ", Type: " + type + ", Amount: " + String.format("%,d", getAmount());
    }
}