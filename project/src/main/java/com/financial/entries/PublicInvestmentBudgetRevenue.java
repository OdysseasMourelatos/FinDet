package com.financial.entries;

import com.financial.services.BudgetRevenueHandling;
import com.financial.services.DataInput;
import com.financial.services.DataOutput;
import com.financial.services.IBudgetRevenueLogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PublicInvestmentBudgetRevenue extends BudgetRevenue implements IBudgetRevenueLogic {


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
            if (publicInvestmentBudgetRevenues.get(i).getCode().equals(publicInvestmentBudgetRevenues.get(i-1).getCode())) {
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
            PublicInvestmentBudgetRevenue b2 = publicInvestmentBudgetRevenues.get(i-1);
            publicInvestmentBudgetRevenues.remove(b1);
            b2.setNationalAmount(b2.getNationalAmount() + b1.getNationalAmount());
            b2.setCoFundedAmount(b2.getCoFundedAmount() + b1.getCoFundedAmount());
            b2.setAmount(b2.getAmount() + b1.getAmount());
        }
        DataInput.createBudgetRevenueFilteredFromPublicInvestmentBudgetRevenue();
    }

    //Class Methods

    public static ArrayList<PublicInvestmentBudgetRevenue> getPublicInvestmentBudgetRevenues() {
        return publicInvestmentBudgetRevenues;
    }

    public static void printPublicInvestmentBudgetRevenues() {
        DataOutput.printPublicInvestmentBudgetRevenueWithAsciiTable(publicInvestmentBudgetRevenues);
    }

    public static ArrayList<PublicInvestmentBudgetRevenue> getMainPublicInvestmentBudgetRevenues() {
        return BudgetRevenueHandling.getMainBudgetRevenues(publicInvestmentBudgetRevenues);
    }

    public static void printMainPublicInvestmentBudgetRevenues() {
        BudgetRevenueHandling.printMainBudgetRevenues(getMainPublicInvestmentBudgetRevenues());
    }
    
    public static PublicInvestmentBudgetRevenue findPublicInvestmentBudgetRevenueWithCode(String code) {
        return BudgetRevenueHandling.findRevenueWithCode(code, publicInvestmentBudgetRevenues);
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

    private void setNationalAmount(long nationalAmount) {
        this.nationalAmount = nationalAmount;
    }

    private void setCoFundedAmount(long coFundedAmount) {
        this.coFundedAmount = coFundedAmount;
    }

    @Override
    public String toString() {
        return "Code: " + getCode() + ", Description: " + getDescription() + ", Category: " + getCategory() + ", Type: " + type + ", Amount: " + String.format("%,d", getAmount());
    }

    @Override
    public long calculateSum() {
        return BudgetRevenueHandling.calculateSum(publicInvestmentBudgetRevenues);
    }

    @Override
    public PublicInvestmentBudgetRevenue findSuperCategory() {
        return BudgetRevenueHandling.findSuperCategory(this, publicInvestmentBudgetRevenues);
    }

    @Override
    public ArrayList<BudgetRevenue> getSuperCategories() {
        return BudgetRevenueHandling.getSuperCategories(this, publicInvestmentBudgetRevenues);
    }

    @Override
    public void printSuperCategoriesTopDown() {
        ArrayList<BudgetRevenue> superCategories = new ArrayList<>();
        if (getSuperCategories().isEmpty()) {
            System.out.println("Δεν υπάρχουν κατηγορίες σε υψηλότερη ιεραρχία");
        } else {
            for (int i = getSuperCategories().size() - 1; i >= 0; i--) {
                superCategories.add(getSuperCategories().get(i));
            }
            DataOutput.printRevenueWithAsciiTable(superCategories, 0);
        }
    }

    @Override
    public void printSuperCategoriesBottomsUp() {
        if (getSuperCategories().isEmpty()) {
            System.out.println("Δεν υπάρχουν κατηγορίες σε υψηλότερη ιεραρχία");
        } else {
            DataOutput.printRevenueWithAsciiTable(getSuperCategories(), 0);
        }
    }

    @Override
    public ArrayList<BudgetRevenue> findAllSubCategories() {
        return BudgetRevenueHandling.findAllSubCategories(this, publicInvestmentBudgetRevenues);
    }

    @Override
    public void printAllSubCategories() {
        DataOutput.printRevenueWithAsciiTable(findAllSubCategories(),0);
    }

    @Override
    public ArrayList<BudgetRevenue> findNextLevelSubCategories() {
        return BudgetRevenueHandling.findNextLevelSubCategories(this, publicInvestmentBudgetRevenues);
    }

    @Override
    public void printNextLevelSubCategories() {
        DataOutput.printRevenueWithAsciiTable(findNextLevelSubCategories(), 0);
    }
}