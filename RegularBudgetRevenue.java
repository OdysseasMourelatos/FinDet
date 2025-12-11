package com.financial.entries;

import com.financial.services.BudgetRevenueHandling;
import com.financial.services.DataOutput;

import java.util.ArrayList;

public class RegularBudgetRevenue extends BudgetRevenue implements IBudgetRevenueLogic{

    protected static ArrayList<RegularBudgetRevenue> regularBudgetRevenues = new ArrayList<>();

    public RegularBudgetRevenue(String code, String description, String category, long amount) {
        super(code, description, category, amount);
        regularBudgetRevenues.add(this);
    }

    //Class Methods

    public static ArrayList<RegularBudgetRevenue> getAllRegularBudgetRevenues() {
        return regularBudgetRevenues;
    }

    public static void printAllRegularBudgetRevenues() {
        DataOutput.printRevenueWithAsciiTable(regularBudgetRevenues, BudgetRevenueHandling.calculateSum(regularBudgetRevenues));
    }

    public static ArrayList<RegularBudgetRevenue> getMainRegularBudgetRevenues() {
        return BudgetRevenueHandling.getMainBudgetRevenues(getAllRegularBudgetRevenues());
    }

    public static void printMainRegularBudgetRevenues() {
        BudgetRevenueHandling.printMainBudgetRevenues(getMainRegularBudgetRevenues());
    }

    public static RegularBudgetRevenue findRegularBudgetRevenueWithCode(String code) {
        return BudgetRevenueHandling.findRevenueWithCode(code, regularBudgetRevenues);
    }

    @Override
    public String toString () {
        return super.toString();
    }

    @Override
    public long calculateSum() {
        return BudgetRevenueHandling.calculateSum(regularBudgetRevenues);
    }

     @Override
    public RegularBudgetRevenue findSuperCategory() {
        return BudgetRevenueHandling.findSuperCategory(this, regularBudgetRevenues);
    }

     @Override
    public ArrayList<RegularBudgetRevenue> getSuperCategories() {
        return BudgetRevenueHandling.getSuperCategories(this, regularBudgetRevenues);
    }

    
    @Override
    public void printSuperCategoriesTopDown() {
        ArrayList<RegularBudgetRevenue> superCategories = new ArrayList<>();
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
    public ArrayList<RegularBudgetRevenue> findAllSubCategories() {
        return BudgetRevenueHandling.findAllSubCategories(this, regularBudgetRevenues);
    }

    @Override
    public void printAllSubCategories() {
        DataOutput.printRevenuesWithAsciiTable(findAllSubCategories(),0);
    }

    @Override
    public ArrayList<RegularBudgetRevenue> findNextLevelSubCategories() {
        return BudgetRevenueHandling.findNextLevelSubCategories(this, regularBudgetRevenues);
    }

    @Override
    public void printNextLevelSubCategories() {
        DataOutput.printRevenueWithAsciiTable(findNextLevelSubCategories(), 0);
    }







}
