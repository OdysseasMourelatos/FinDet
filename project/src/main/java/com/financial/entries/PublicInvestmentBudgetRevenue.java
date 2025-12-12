package com.financial.entries;

import com.financial.services.BudgetRevenueLogicService;
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
            b2.setNationalAmount(b2.getNationalAmount() + b1.getNationalAmount(), false);
            b2.setCoFundedAmount(b2.getCoFundedAmount() + b1.getCoFundedAmount(), false);
            b2.setAmount(b2.getAmount() + b1.getAmount());
        }
        DataInput.createBudgetRevenueFilteredFromPublicInvestmentBudgetRevenue();
    }

    //Class Methods

    public static ArrayList<PublicInvestmentBudgetRevenue> getPublicInvestmentBudgetRevenues() {
        return publicInvestmentBudgetRevenues;
    }

    public static void printPublicInvestmentBudgetRevenues() {
        DataOutput.printPublicInvestmentBudgetRevenuesFilteredWithAsciiTable(publicInvestmentBudgetRevenues, calculateSum());
    }

    public static ArrayList<PublicInvestmentBudgetRevenue> getMainPublicInvestmentBudgetRevenues() {
        return BudgetRevenueLogicService.getMainBudgetRevenues(publicInvestmentBudgetRevenues);
    }

    public static void printMainPublicInvestmentBudgetRevenues() {
        DataOutput.printPublicInvestmentBudgetRevenuesFilteredWithAsciiTable(getMainPublicInvestmentBudgetRevenues(), calculateSum());
    }

    public static PublicInvestmentBudgetRevenue findPublicInvestmentBudgetRevenueWithCode(String code) {
        return BudgetRevenueLogicService.findRevenueWithCode(code, publicInvestmentBudgetRevenues);
    }

    public static ArrayList<BudgetRevenue> getPublicInvestmentBudgetRevenuesStartingWithCode(String code) {
        return BudgetRevenueLogicService.getRevenuesStartingWithCode(code, publicInvestmentBudgetRevenues);
    }

    public static void printPublicInvestmentRevenuesStartingWithCode(String code) {
        DataOutput.printGeneralBudgetEntriesWithAsciiTable(getPublicInvestmentBudgetRevenuesStartingWithCode(code), 0);
    }

    //Sum Method
    public static long calculateSum() {
        return BudgetRevenueLogicService.calculateSum(publicInvestmentBudgetRevenues);
    }

    //*Implementation of methods*

    //Supercategories methods

    @Override
    public PublicInvestmentBudgetRevenue findSuperCategory() {
        return BudgetRevenueLogicService.findSuperCategory(this, publicInvestmentBudgetRevenues);
    }

    @Override
    public ArrayList<BudgetRevenue> getSuperCategories() {
        return BudgetRevenueLogicService.getSuperCategories(this, publicInvestmentBudgetRevenues);
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
            DataOutput.printGeneralBudgetEntriesWithAsciiTable(superCategories, 0);
        }
    }

    @Override
    public void printSuperCategoriesBottomsUp() {
        if (getSuperCategories().isEmpty()) {
            System.out.println("Δεν υπάρχουν κατηγορίες σε υψηλότερη ιεραρχία");
        } else {
            DataOutput.printGeneralBudgetEntriesWithAsciiTable(getSuperCategories(), 0);
        }
    }

    //Subcategories methods

    @Override
    public ArrayList<BudgetRevenue> findAllSubCategories() {
        return BudgetRevenueLogicService.findAllSubCategories(this, publicInvestmentBudgetRevenues);
    }

    @Override
    public void printAllSubCategories() {
        DataOutput.printGeneralBudgetEntriesWithAsciiTable(findAllSubCategories(), 0);
    }

    @Override
    public ArrayList<BudgetRevenue> findNextLevelSubCategories() {
        return BudgetRevenueLogicService.findNextLevelSubCategories(this, publicInvestmentBudgetRevenues);
    }

    @Override
    public void printNextLevelSubCategories() {
        DataOutput.printGeneralBudgetEntriesWithAsciiTable(findNextLevelSubCategories(), 0);
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

    protected void setNationalAmount(long nationalAmount, boolean update) {
        this.nationalAmount = nationalAmount;
        if (update) {
            this.amount = nationalAmount + coFundedAmount;
            updateAmountOfSuperClassFilteredObjects(amount);
        }
    }

    protected void setCoFundedAmount(long coFundedAmount, boolean update) {
        this.coFundedAmount = coFundedAmount;
        if (update) {
            this.amount = nationalAmount + coFundedAmount;
            updateAmountOfSuperClassFilteredObjects(amount);
        }
    }

    //Updating the filtered objects in SuperClass

    public void updateAmountOfSuperClassFilteredObjects(long change) {
        BudgetRevenue budgetRevenue = BudgetRevenue.findBudgetRevenueWithCode(this.getCode());
        if (budgetRevenue != null ) {
            budgetRevenue.setPublicInvestmentAmount(change, true);
        }
    }

    @Override
    public String toString() {
        return "Code: " + getCode() + ", Description: " + getDescription() + ", Category: " + getCategory() + ", Type: " + type + ", Amount: " + String.format("%,d", getAmount());
    }
}