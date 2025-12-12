package com.financial.entries;

import com.financial.services.BudgetRevenueLogicService;
import com.financial.services.DataOutput;

import java.util.ArrayList;

public class PublicInvestmentBudgetCoFundedRevenue extends PublicInvestmentBudgetRevenue {

    //Constructor & Fields

    protected static ArrayList<PublicInvestmentBudgetCoFundedRevenue> publicInvestmentBudgetCoFundedRevenues = new ArrayList<>();

    public PublicInvestmentBudgetCoFundedRevenue(String code, String description, String category, String type, long amount) {
        super(code, description, category, type, amount);
        publicInvestmentBudgetCoFundedRevenues.add(this);
    }

    //Class Methods

    public static ArrayList<PublicInvestmentBudgetCoFundedRevenue> getPublicInvestmentBudgetCoFundedRevenues() {
        return publicInvestmentBudgetCoFundedRevenues;
    }

    public static void printPublicInvestmentBudgetCoFundedRevenues() {
        DataOutput.printPublicInvestmentBudgetRevenueWithAsciiTable(getPublicInvestmentBudgetCoFundedRevenues());
    }

    public static ArrayList<PublicInvestmentBudgetCoFundedRevenue> getMainPublicInvestmentBudgetCoFundedRevenues() {
        return BudgetRevenueLogicService.getMainBudgetRevenues(publicInvestmentBudgetCoFundedRevenues);
    }

    public static void printMainPublicInvestmentBudgetCoFundedRevenues() {
        BudgetRevenueLogicService.printMainBudgetRevenues(getMainPublicInvestmentBudgetCoFundedRevenues());
    }

    public static PublicInvestmentBudgetCoFundedRevenue findPublicInvestmentBudgetCoFundedRevenueWithCode(String code) {
        return BudgetRevenueLogicService.findRevenueWithCode(code, publicInvestmentBudgetCoFundedRevenues);
    }

    //Sum Method

    public static long calculateSum() {
        return BudgetRevenueLogicService.calculateSum(publicInvestmentBudgetCoFundedRevenues);
    }

    //*Implementation of methods (Logic)*

    //Supercategories methods

    @Override
    public PublicInvestmentBudgetCoFundedRevenue findSuperCategory() {
        return BudgetRevenueLogicService.findSuperCategory(this, publicInvestmentBudgetCoFundedRevenues);
    }

    @Override
    public ArrayList<BudgetRevenue> getSuperCategories() {
        return BudgetRevenueLogicService.getSuperCategories(this, publicInvestmentBudgetCoFundedRevenues);
    }

    @Override
    public void printSuperCategoriesTopDown() {
        ArrayList<RegularBudgetRevenue> superCategories = new ArrayList<>();
        if (getSuperCategories().isEmpty()) {
            System.out.println("Δεν υπάρχουν κατηγορίες σε υψηλότερη ιεραρχία");
        } else {
            for (int i = getSuperCategories().size() - 1; i >= 0; i--) {
                superCategories.add((RegularBudgetRevenue) getSuperCategories().get(i));
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
        return BudgetRevenueLogicService.findAllSubCategories(this, publicInvestmentBudgetCoFundedRevenues);
    }

    @Override
    public void printAllSubCategories() {
        DataOutput.printGeneralBudgetEntriesWithAsciiTable(findAllSubCategories(), 0);
    }

    @Override
    public ArrayList<BudgetRevenue> findNextLevelSubCategories() {
        return BudgetRevenueLogicService.findNextLevelSubCategories(this, publicInvestmentBudgetCoFundedRevenues);
    }

    @Override
    public void printNextLevelSubCategories() {
        DataOutput.printGeneralBudgetEntriesWithAsciiTable(findNextLevelSubCategories(), 0);
    }
}
