package com.financial.entries;

import com.financial.services.*;
import com.financial.services.revenues.*;

import java.util.ArrayList;

public class PublicInvestmentBudgetCoFundedRevenue extends PublicInvestmentBudgetRevenue implements BudgetRevenueLogic, BudgetRevenueChanges {

    //Constructor & Fields

    protected static ArrayList<PublicInvestmentBudgetCoFundedRevenue> publicInvestmentBudgetCoFundedRevenues = new ArrayList<>();

    public PublicInvestmentBudgetCoFundedRevenue(String code, String description, String category, String type, long amount) {
        super(code, description, category, type, amount);
        publicInvestmentBudgetCoFundedRevenues.add(this);

        //SuperClass Filtered Object
        PublicInvestmentBudgetRevenue publicInvestmentBudgetRevenueFiltered = new PublicInvestmentBudgetRevenue(code, description, category, type, 0, amount, amount);
    }

    //Class Methods

    public static ArrayList<PublicInvestmentBudgetCoFundedRevenue> getAllPublicInvestmentBudgetCoFundedRevenues() {
        return publicInvestmentBudgetCoFundedRevenues;
    }

    public static ArrayList<PublicInvestmentBudgetCoFundedRevenue> getMainPublicInvestmentBudgetCoFundedRevenues() {
        return BudgetRevenueLogicService.getMainBudgetRevenues(publicInvestmentBudgetCoFundedRevenues);
    }

    public static PublicInvestmentBudgetCoFundedRevenue findPublicInvestmentBudgetCoFundedRevenueWithCode(String code) {
        return BudgetRevenueLogicService.findRevenueWithCode(code, publicInvestmentBudgetCoFundedRevenues);
    }

    public static ArrayList<BudgetRevenue> getPublicInvestmentBudgetCoFundedRevenuesStartingWithCode(String code) {
        return BudgetRevenueLogicService.getRevenuesStartingWithCode(code, publicInvestmentBudgetCoFundedRevenues);
    }

    //Sum Method

    public static long calculateSum() {
        return BudgetRevenueLogicService.calculateSum(publicInvestmentBudgetCoFundedRevenues);
    }

    //*Implementation of methods (Logic)*

    //Supercategories methods

    @Override
    public PublicInvestmentBudgetCoFundedRevenue getAboveLevelSuperCategory() {
        return BudgetRevenueLogicService.getAboveLevelSuperCategory(this, publicInvestmentBudgetCoFundedRevenues);
    }

    @Override
    public ArrayList<BudgetRevenue> getAllSuperCategories() {
        return BudgetRevenueLogicService.getAllSuperCategories(this, publicInvestmentBudgetCoFundedRevenues);
    }

    //Subcategories methods

    @Override
    public ArrayList<BudgetRevenue> getNextLevelSubCategories() {
        return BudgetRevenueLogicService.getNextLevelSubCategories(this, publicInvestmentBudgetCoFundedRevenues);
    }

    @Override
    public ArrayList<BudgetRevenue> getAllSubCategories() {
        return BudgetRevenueLogicService.getAllSubCategories(this, publicInvestmentBudgetCoFundedRevenues);
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
        accountsForChange.addAll(this.getAllSuperCategories());
        accountsForChange.add(this);
        accountsForChange.addAll(getAllSubCategories());
        RevenuesHistory.keepHistory(accountsForChange, BudgetType.PUBLIC_INVESTMENT_BUDGET_COFUNDED);
    }

    //Updating the filtered objects in SuperClass
    @Override
    public void updateAmountOfSuperClassFilteredObject(long change) {
        PublicInvestmentBudgetRevenue publicInvestmentBudgetRevenue = PublicInvestmentBudgetRevenue.findPublicInvestmentBudgetRevenueWithCode(this.getCode());
        if (publicInvestmentBudgetRevenue != null ) {
            publicInvestmentBudgetRevenue.setCoFundedAmount(amount, true);
        }
    }

    //Setter Override

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

    @Override
    public String toString() {
        return super.toString();
    }
}
