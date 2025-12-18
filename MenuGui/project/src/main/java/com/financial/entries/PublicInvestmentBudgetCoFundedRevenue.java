package com.financial.entries;

import com.financial.services.*;
import com.financial.services.data.DataOutput;
import com.financial.services.revenues.*;

import java.util.ArrayList;

public class PublicInvestmentBudgetCoFundedRevenue extends PublicInvestmentBudgetRevenue implements BudgetRevenueLogic, BudgetRevenueChanges {

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

    public static ArrayList<BudgetRevenue> getPublicInvestmentBudgetCoFundedRevenuesStartingWithCode(String code) {
        return BudgetRevenueLogicService.getRevenuesStartingWithCode(code, publicInvestmentBudgetCoFundedRevenues);
    }

    public static void printPublicInvestmentBudgetCoFundedRevenuesStartingWithCode(String code) {
        DataOutput.printGeneralBudgetEntriesWithAsciiTable(getPublicInvestmentBudgetCoFundedRevenuesStartingWithCode(code), 0);
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

    //*Implementation Of Methods (Changes)*

    //SuperCategories update

    @Override
    public void setAmountOfSuperCategories(long change) {
        BudgetRevenueChangesService.setAmountOfSuperCategories(getSuperCategories(), change);
    }

    //SubCategories update

    @Override
    public void setAmountOfAllSubCategoriesWithEqualDistribution(long change) {
        BudgetRevenueChangesService.setAmountOfAllSubCategoriesWithEqualDistribution(this, publicInvestmentBudgetCoFundedRevenues, change);
    }

    @Override
    public void setAmountOfAllSubCategoriesWithPercentageAdjustment(double percentage) {
        BudgetRevenueChangesService.setAmountOfAllSubCategoriesWithPercentageAdjustment(this, publicInvestmentBudgetCoFundedRevenues, percentage);
    }

    //Methods that are called from outside for mass changes
    //1 - Update SuperCategories
    //2 - Update SubCategories with certain strategy
    //3 - Update the account itself

    @Override
    public void implementChangesOfEqualDistribution(long change) {
        keepAccountsAndBudgetTypeBeforeChange();
        setAmountOfSuperCategories(change);
        setAmountOfAllSubCategoriesWithEqualDistribution(change);
        setAmount(getAmount() + change);
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
        ArrayList<BudgetRevenue> accountsForChange = new ArrayList<>();
        accountsForChange.addAll(getSuperCategories());
        accountsForChange.add(this);
        accountsForChange.addAll(findAllSubCategories());
        RevenuesHistory.keepHistory(accountsForChange, BudgetType.PUBLIC_INVESTMENT_BUDGET_COFUNDED);
    }

    //Updating the filtered objects in SuperClass
    @Override
    public void updateAmountOfSuperClassFilteredObjects(long change) {
        PublicInvestmentBudgetRevenue publicInvestmentBudgetRevenue = PublicInvestmentBudgetRevenue.findPublicInvestmentBudgetRevenueWithCode(this.getCode());
        if (publicInvestmentBudgetRevenue != null ) {
            publicInvestmentBudgetRevenue.setCoFundedAmount(amount, true);
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
