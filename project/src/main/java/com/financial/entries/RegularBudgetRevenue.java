package com.financial.entries;

import com.financial.services.revenues.*;
import com.financial.services.*;

import java.util.ArrayList;

public class RegularBudgetRevenue extends BudgetRevenue implements BudgetRevenueLogic, BudgetRevenueChanges {

    protected static ArrayList<RegularBudgetRevenue> regularBudgetRevenues = new ArrayList<>();

    public RegularBudgetRevenue(String code, String description, String category, long amount) {
        super(code, description, category, amount);
        regularBudgetRevenues.add(this);

        //New filtered object of BudgetRevenue class
        BudgetRevenue budgetRevenue = new BudgetRevenue(code, description, category, amount, 0, amount);
    }

    //Class Methods

    public static ArrayList<RegularBudgetRevenue> getAllRegularBudgetRevenues() {
        return regularBudgetRevenues;
    }

    public static ArrayList<RegularBudgetRevenue> getMainRegularBudgetRevenues() {
        return BudgetRevenueLogicService.getMainBudgetRevenues(getAllRegularBudgetRevenues());
    }

    public static RegularBudgetRevenue findRegularBudgetRevenueWithCode(String code) {
        return BudgetRevenueLogicService.findRevenueWithCode(code, regularBudgetRevenues);
    }

    public static ArrayList<BudgetRevenue> getRegularBudgetRevenuesStartingWithCode(String code) {
        return BudgetRevenueLogicService.getRevenuesStartingWithCode(code, regularBudgetRevenues);
    }

    //Sum Method

    public static long calculateSum() {
        return BudgetRevenueLogicService.calculateSum(regularBudgetRevenues);
    }

    //*Implementation of methods (Logic)*

    //Supercategories methods

    @Override
    public RegularBudgetRevenue getAboveLevelSuperCategory() {
        return BudgetRevenueLogicService.getAboveLevelSuperCategory(this, regularBudgetRevenues);
    }

    @Override
    public ArrayList<BudgetRevenue> getAllSuperCategories() {
        return BudgetRevenueLogicService.getAllSuperCategories(this, regularBudgetRevenues);
    }

    //Subcategories methods

    @Override
    public ArrayList<BudgetRevenue> getNextLevelSubCategories() {
        return BudgetRevenueLogicService.getNextLevelSubCategories(this, regularBudgetRevenues);
    }

    @Override
    public ArrayList<BudgetRevenue> getAllSubCategories() {
        return BudgetRevenueLogicService.getAllSubCategories(this, regularBudgetRevenues);
    }

    //*Implementation Of Methods (Changes)*

    //SuperCategories update

    @Override
    public void setAmountOfSuperCategories(long change) {
        BudgetRevenueChangesService.setAmountOfSuperCategories(getAllSuperCategories(), change);
    }

    //SubCategories update

    @Override
    public void setAmountOfAllSubCategoriesWithEqualDistribution(long change) {
        BudgetRevenueChangesService.setAmountOfAllSubCategoriesWithEqualDistribution(this, regularBudgetRevenues, change);
    }

    @Override
    public void setAmountOfAllSubCategoriesWithPercentageAdjustment(double percentage) {
        BudgetRevenueChangesService.setAmountOfAllSubCategoriesWithPercentageAdjustment(this, regularBudgetRevenues, percentage);
    }

    //Methods that are called from outside for mass changes
    //1 - Update SuperCategories
    //2 - Update SubCategories with certain strategy
    //3 - Update the account itself

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
        ArrayList<BudgetRevenue> accountsForChange = new ArrayList<>();
        accountsForChange.addAll(getAllSuperCategories());
        accountsForChange.add(this);
        accountsForChange.addAll(getAllSubCategories());
        RevenuesHistory.keepHistory(accountsForChange, BudgetType.REGULAR_BUDGET);
    }

    //Updating the filtered objects in SuperClass
    @Override
    public void updateAmountOfSuperClassFilteredObject(long change) {
        BudgetRevenue budgetRevenue = BudgetRevenue.findBudgetRevenueWithCode(this.getCode());
        if (budgetRevenue != null) {
            budgetRevenue.setRegularAmount(amount, true);
        }
    }

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

    //ToString

    @Override
    public String toString () {
        return super.toString();
    }
}
