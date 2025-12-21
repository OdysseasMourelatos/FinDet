package com.financial.entries;

import com.financial.services.*;
import com.financial.services.revenues.*;

import java.util.ArrayList;

public class PublicInvestmentBudgetNationalRevenue extends PublicInvestmentBudgetRevenue implements BudgetRevenueLogic, BudgetRevenueChanges {

    //Constructor & Fields
    protected static ArrayList<PublicInvestmentBudgetNationalRevenue> publicInvestmentBudgetNationalRevenues = new ArrayList<>();

    public PublicInvestmentBudgetNationalRevenue(String code, String description, String category, String type, long amount) {
        super(code, description, category, type, amount);
        publicInvestmentBudgetNationalRevenues.add(this);
    }

    //Class Methods

    public static ArrayList<PublicInvestmentBudgetNationalRevenue> getAllPublicInvestmentBudgetNationalRevenues() {
        return publicInvestmentBudgetNationalRevenues;
    }


    public static ArrayList<PublicInvestmentBudgetNationalRevenue> getMainPublicInvestmentBudgetNationalRevenues() {
        return BudgetRevenueLogicService.getMainBudgetRevenues(publicInvestmentBudgetNationalRevenues);
    }


    public static PublicInvestmentBudgetNationalRevenue findPublicInvestmentBudgetNationalRevenueWithCode(String code) {
        return BudgetRevenueLogicService.findRevenueWithCode(code, publicInvestmentBudgetNationalRevenues);
    }

    public static ArrayList<BudgetRevenue> getPublicInvestmentBudgetNationalRevenuesStartingWithCode(String code) {
        return BudgetRevenueLogicService.getRevenuesStartingWithCode(code, publicInvestmentBudgetNationalRevenues);
    }

    //Sum Method

    public static long calculateSum() {
        return BudgetRevenueLogicService.calculateSum(publicInvestmentBudgetNationalRevenues);
    }

    //*Implementation of methods (Logic)*

    //Supercategories methods

    @Override
    public PublicInvestmentBudgetNationalRevenue getAboveLevelSuperCategory() {
        return BudgetRevenueLogicService.getAboveLevelSuperCategory(this, publicInvestmentBudgetNationalRevenues);
    }

    @Override
    public ArrayList<BudgetRevenue> getAllSuperCategories() {
        return BudgetRevenueLogicService.getAllSuperCategories(this, publicInvestmentBudgetNationalRevenues);
    }

    //Subcategories methods

    @Override
    public ArrayList<BudgetRevenue> getNextLevelSubCategories() {
        return BudgetRevenueLogicService.getNextLevelSubCategories(this, publicInvestmentBudgetNationalRevenues);
    }

    @Override
    public ArrayList<BudgetRevenue> getAllSubCategories() {
        return BudgetRevenueLogicService.getAllSubCategories(this, publicInvestmentBudgetNationalRevenues);
    }

    //*Implementation Of Methods (Changes)*

    //SuperCategories update

    @Override
    public void setAmountOfSuperCategories(long change) {
        BudgetRevenueChangesService.setAmountOfSuperCategories(this.getAllSuperCategories(), change);
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
        accountsForChange.addAll(this.getAllSuperCategories());
        accountsForChange.add(this);
        accountsForChange.addAll(getAllSubCategories());
        RevenuesHistory.keepHistory(accountsForChange, BudgetType.PUBLIC_INVESTMENT_BUDGET_NATIONAL);
    }

    //Updating the filtered objects in SuperClass
    @Override
    public void updateAmountOfSuperClassFilteredObjects(long change) {
        PublicInvestmentBudgetRevenue publicInvestmentBudgetRevenue = PublicInvestmentBudgetRevenue.findPublicInvestmentBudgetRevenueWithCode(this.getCode());
        if (publicInvestmentBudgetRevenue != null ) {
            publicInvestmentBudgetRevenue.setNationalAmount(amount, true);
        }
    }

    //Setter Override

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
