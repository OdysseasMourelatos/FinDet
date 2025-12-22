package com.financial.entries;

import com.financial.services.revenues.BudgetRevenueLogicService;
import com.financial.services.revenues.BudgetRevenueLogic;

import java.util.*;

public class BudgetRevenue extends BudgetEntry implements BudgetRevenueLogic {

    //Constructors & Fields
    protected static ArrayList <BudgetRevenue> budgetRevenues = new ArrayList<>();

    public BudgetRevenue(String code, String description, String category, long amount) {
        super(code, description, category, amount);
    }

    private long regularAmount;
    private long publicInvestmentAmount;

    public BudgetRevenue(String code, String description, String category, long regularAmount, long publicInvestmentAmount, long amount) {
        super(code, description, category, amount);
        this.regularAmount = regularAmount;
        this.publicInvestmentAmount = publicInvestmentAmount;
        budgetRevenues.add(this);
    }

    public static void sortBudgetRevenuesByCode() {
        Collections.sort(budgetRevenues, new Comparator<BudgetRevenue>() {
            @Override
            public int compare(BudgetRevenue b1, BudgetRevenue b2) {
                return b1.getCode().compareTo(b2.getCode());
            }
        });
    }

    public static void filterBudgetRevenues() {
        ArrayList<Integer> repeatedRevenues = new ArrayList<>();
        //Finds codes that are repeated in the list
        // Adds to a new list the 2 placements in the list where we have repeated codes
        for (int i = 1; i < budgetRevenues.size(); i++) {
            if (budgetRevenues.get(i).getCode().equals(budgetRevenues.get(i - 1).getCode())) {
                repeatedRevenues.add(i);
            }
        }
         /*With a loop starting from the end of the list
         we in a way merge those values that are repeated into a single object
         then remove the other one
       */
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

    //Class Methods

    public static ArrayList<BudgetRevenue> getAllBudgetRevenues() {
        return budgetRevenues;
    }

    public static ArrayList<BudgetRevenue> getMainBudgetRevenues() {
        return BudgetRevenueLogicService.getMainBudgetRevenues(getAllBudgetRevenues());
    }

    public static BudgetRevenue findBudgetRevenueWithCode(String code) {
        return BudgetRevenueLogicService.findRevenueWithCode(code, budgetRevenues);
    }

    public static ArrayList<BudgetRevenue> getBudgetRevenuesStartingWithCode(String code) {
        return BudgetRevenueLogicService.getRevenuesStartingWithCode(code, budgetRevenues);
    }

    //Sum Method
    public static long calculateSum() {
        return BudgetRevenueLogicService.calculateSum(budgetRevenues);
    }

    //Method that all subclasses easily inherit

    public int getLevelOfHierarchy() {
        return switch (getCode().length()) {
            case 2 -> 1;   // "11" - top level
            case 3 -> 2;   // "111" - second level
            case 5 -> 3;   // "11101" - third level
            case 7 -> 4;   // "1110103" - fourth level
            case 10 -> 5;  // "1110103001" - fifth level
            default -> 0;  // unknown
        };
    }

    //*Implementation of methods (Logic)*

    //Supercategories methods

    @Override
    public BudgetRevenue getAboveLevelSuperCategory() {
        return BudgetRevenueLogicService.getAboveLevelSuperCategory(this, budgetRevenues);
    }

    @Override
    public ArrayList<BudgetRevenue> getAllSuperCategories() {
        return BudgetRevenueLogicService.getAllSuperCategories(this, budgetRevenues);
    }


    //Subcategories methods

    @Override
    public ArrayList<BudgetRevenue> getNextLevelSubCategories() {
        return BudgetRevenueLogicService.getNextLevelSubCategories(this, budgetRevenues);
    }

    @Override
    public ArrayList<BudgetRevenue> getAllSubCategories() {
        return BudgetRevenueLogicService.getAllSubCategories(this, budgetRevenues);
    }

    //Getters & Setters

    public long getRegularAmount() {
        return regularAmount;
    }

    public long getPublicInvestmentAmount() {
        return publicInvestmentAmount;
    }

    public void setPublicInvestmentAmount(long amount, boolean update) {
        this.publicInvestmentAmount = amount;
        if (update) {
            this.amount = regularAmount + publicInvestmentAmount;
        }
    }

    protected void setRegularAmount(long amount, boolean update) {
        this.regularAmount = amount;
        if (update) {
            this.amount = regularAmount + publicInvestmentAmount;
        }
    }

    @Override
    public String toString () {
        return super.toString();
    }
}
