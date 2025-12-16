package com.financial.entries;

import com.financial.services.*;

import java.util.ArrayList;

public class PublicInvestmentBudgetNationalRevenue extends PublicInvestmentBudgetRevenue implements IBudgetRevenueLogic, IBudgetRevenueChanges {

    //Constructor & Fields
    protected static ArrayList<PublicInvestmentBudgetNationalRevenue> publicInvestmentBudgetNationalRevenues = new ArrayList<>();

    public PublicInvestmentBudgetNationalRevenue(String code, String description, String category, String type, long amount) {
        super(code, description, category, type, amount);
        publicInvestmentBudgetNationalRevenues.add(this);
    }

    //Class Methods

    public static ArrayList<PublicInvestmentBudgetNationalRevenue> getPublicInvestmentBudgetNationalRevenues() {
        return publicInvestmentBudgetNationalRevenues;
    }

    public static void printPublicInvestmentBudgetNationalRevenues() {
        DataOutput.printPublicInvestmentBudgetRevenueWithAsciiTable(getPublicInvestmentBudgetNationalRevenues());
    }

    public static ArrayList<PublicInvestmentBudgetNationalRevenue> getMainPublicInvestmentBudgetNationalRevenues() {
        return BudgetRevenueLogicService.getMainBudgetRevenues(publicInvestmentBudgetNationalRevenues);
    }

    public static void printMainPublicInvestmentBudgetNationalRevenues() {
        BudgetRevenueLogicService.printMainBudgetRevenues(getMainPublicInvestmentBudgetNationalRevenues());
    }

    public static PublicInvestmentBudgetNationalRevenue findPublicInvestmentBudgetNationalRevenueWithCode(String code) {
        return BudgetRevenueLogicService.findRevenueWithCode(code, publicInvestmentBudgetNationalRevenues);
    }

    public static ArrayList<BudgetRevenue> getPublicInvestmentBudgetNationalRevenuesStartingWithCode(String code) {
        return BudgetRevenueLogicService.getRevenuesStartingWithCode(code, publicInvestmentBudgetNationalRevenues);
    }

    public static void printPublicInvestmentBudgetNationalRevenuesStartingWithCode(String code) {
        DataOutput.printGeneralBudgetEntriesWithAsciiTable(getPublicInvestmentBudgetNationalRevenuesStartingWithCode(code), 0);
    }


    //Sum Method

    public static long calculateSum() {
        return BudgetRevenueLogicService.calculateSum(publicInvestmentBudgetNationalRevenues);
    }

    //*Implementation of methods (Logic)*

    //Supercategories methods

    @Override
    public PublicInvestmentBudgetNationalRevenue findSuperCategory() {
        return BudgetRevenueLogicService.findSuperCategory(this, publicInvestmentBudgetNationalRevenues);
    }

    @Override
    public ArrayList<BudgetRevenue> getSuperCategories() {
        return BudgetRevenueLogicService.getSuperCategories(this, publicInvestmentBudgetNationalRevenues);
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
        return BudgetRevenueLogicService.findAllSubCategories(this, publicInvestmentBudgetNationalRevenues);
    }

    @Override
    public void printAllSubCategories() {
        DataOutput.printGeneralBudgetEntriesWithAsciiTable(findAllSubCategories(), 0);
    }

    @Override
    public ArrayList<BudgetRevenue> findNextLevelSubCategories() {
        return BudgetRevenueLogicService.findNextLevelSubCategories(this, publicInvestmentBudgetNationalRevenues);
    }

    @Override
    public void printNextLevelSubCategories() {
        DataOutput.printGeneralBudgetEntriesWithAsciiTable(findNextLevelSubCategories(), 0);
    }

    //*Implementation Of Methods (Changes)*

    //SuperCategories update

    @Override
    public void setAmountOfSuperCategories(long change) {
        BudgetRevenueChangesService.setAmountOfSuperCategories(getSuperCategories(), change);
    }

    //SubCategories update

    @Override
    public void setAmountOfAllSubCategoriesWithEqualDistribution(long change) {
        BudgetRevenueChangesService.setAmountOfAllSubCategoriesWithEqualDistribution(this, publicInvestmentBudgetNationalRevenues, change);
    }

    @Override
    public void setAmountOfAllSubCategoriesWithPercentageAdjustment(double percentage) {
        BudgetRevenueChangesService.setAmountOfAllSubCategoriesWithPercentageAdjustment(this, publicInvestmentBudgetNationalRevenues, percentage);
    }

    //Methods that are called from outside for mass changes
    //1 - Update SuperCategories
    //2 - Update SubCategories with certain strategy
    //3 - Update the account itself

    @Override
    public void implementChangesOfEqualDistribution(long change) {
        setAmountOfSuperCategories(change);
        setAmountOfAllSubCategoriesWithEqualDistribution(change);
        setAmount(getAmount() + change);
    }

    @Override
    public void implementChangesOfPercentageAdjustment(double percentage) {
        setAmountOfSuperCategories((long) (getAmount() * (percentage)));
        setAmountOfAllSubCategoriesWithPercentageAdjustment(percentage);
        setAmount((long) (getAmount() * (1 + percentage)));
    }

    //Updating the filtered objects in SuperClass
    @Override
    public void updateAmountOfSuperClassFilteredObjects(long change) {
        PublicInvestmentBudgetRevenue publicInvestmentBudgetRevenue = PublicInvestmentBudgetRevenue.findPublicInvestmentBudgetRevenueWithCode(this.getCode());
        if (publicInvestmentBudgetRevenue != null ) {
            publicInvestmentBudgetRevenue.setNationalAmount(amount, true);
        }
    }

    @Override
    public void setAmount(long amount) {
        this.amount = amount;
        updateAmountOfSuperClassFilteredObjects(amount);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
