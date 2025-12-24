package com.financial.entries;

import com.financial.services.BudgetType;
import com.financial.services.expenses.BudgetExpenseLogicService;
import com.financial.services.expenses.EntityLogic;
import com.financial.services.expenses.EntityLogicService;
import com.financial.services.expenses.ExpensesHistory;
import com.financial.strategies.ExpenseAdjustmentStrategy;
import com.financial.strategies.PercentageAllocationAdjustmentStrategy;
import com.financial.strategies.filters.AccountFilter;
import com.financial.strategies.filters.MatchAllFilter;
import com.financial.strategies.filters.ServiceFilter;
import com.financial.strategies.operations.PercentageOperation;

import java.util.*;

public class Entity implements EntityLogic {
    private final String entityCode;
    private final String entityName;
    protected ArrayList<RegularBudgetExpense> regularBudgetExpenses;
    protected ArrayList<PublicInvestmentBudgetNationalExpense> publicInvestmentBudgetNationalExpenses;
    protected ArrayList<PublicInvestmentBudgetCoFundedExpense> publicInvestmentBudgetCoFundedExpenses;

    protected static ArrayList<Entity> entities = new ArrayList<>();

    public Entity(String entityCode, String entityName) {
        this.entityCode = entityCode;
        this.entityName = entityName;
        regularBudgetExpenses = RegularBudgetExpense.getRegularBudgetExpensesOfEntityWithCode(entityCode);
        publicInvestmentBudgetNationalExpenses = PublicInvestmentBudgetNationalExpense.getPublicInvestmentBudgetNationalExpensesOfEntityWithCode(entityCode);
        publicInvestmentBudgetCoFundedExpenses = PublicInvestmentBudgetCoFundedExpense.getPublicInvestmentBudgetCoFundedExpensesOfEntityWithCode(entityCode);
        entities.add(this);
    }

    //Class Method
    public static Entity findEntityWithEntityCode(String entityCode) {
        for (Entity entity : entities) {
            if (entity.getEntityCode().equals(entityCode)) {
                return entity;
            }
        }
        return null;
    }

    //Calculate Sums Of Entity

    @Override
    public long calculateRegularSum() {
        return BudgetExpenseLogicService.calculateSum(regularBudgetExpenses);
    }

    @Override
    public long calculatePublicInvestmentNationalSum() {
        return BudgetExpenseLogicService.calculateSum(publicInvestmentBudgetNationalExpenses);
    }

    @Override
    public long calculatePublicInvestmentCoFundedSum() {
        return BudgetExpenseLogicService.calculateSum(publicInvestmentBudgetCoFundedExpenses);
    }

    //Get service name with code

    @Override
    public String getRegularServiceNameWithCode(String serviceCode) {
        return EntityLogicService.getServiceNameWithCode(serviceCode, regularBudgetExpenses);
    }

    @Override
    public String getPublicInvestmentNationalServiceNameWithCode(String serviceCode) {
        return EntityLogicService.getServiceNameWithCode(serviceCode, publicInvestmentBudgetNationalExpenses);
    }

    @Override
    public String getPublicInvestmentCoFundedServiceNameWithCode(String serviceCode) {
        return EntityLogicService.getServiceNameWithCode(serviceCode, publicInvestmentBudgetCoFundedExpenses);
    }

    // Get Service Codes

    @Override
    public List<String> getAllRegularServiceCodes() {
        return EntityLogicService.getAllServiceCodes(regularBudgetExpenses);
    }

    @Override
    public List<String> getAllPublicInvestmentNationalServiceCodes() {
        return EntityLogicService.getAllServiceCodes(publicInvestmentBudgetNationalExpenses);
    }

    @Override
    public List<String> getAllPublicInvestmentCoFundedServiceCodes() {
        return EntityLogicService.getAllServiceCodes(publicInvestmentBudgetCoFundedExpenses);
    }

    // Get Sums Of Service With Code

    @Override
    public long getRegularSumOfServiceWithCode(String serviceCode) {
        return EntityLogicService.getSumOfServiceWithCode(serviceCode, regularBudgetExpenses);
    }

    @Override
    public long getPublicInvestmentNationalSumOfServiceWithCode(String serviceCode) {
        return EntityLogicService.getSumOfServiceWithCode(serviceCode, publicInvestmentBudgetNationalExpenses);
    }

    @Override
    public long getPublicInvestmentCoFundedSumOfServiceWithCode(String serviceCode) {
        return EntityLogicService.getSumOfServiceWithCode(serviceCode, publicInvestmentBudgetCoFundedExpenses);
    }

    //Get Expenses Of Service With Code

    @Override
    public ArrayList<BudgetExpense> getRegularExpensesOfServiceWithCode(String serviceCode) {
        return EntityLogicService.getExpensesOfServiceWithCode(serviceCode, regularBudgetExpenses);
    }

    @Override
    public ArrayList<BudgetExpense> getPublicInvestmentNationalExpensesOfServiceWithCode(String serviceCode) {
        return EntityLogicService.getExpensesOfServiceWithCode(serviceCode, publicInvestmentBudgetNationalExpenses);
    }

    @Override
    public ArrayList<BudgetExpense> getPublicInvestmentCoFundedExpensesOfServiceWithCode(String serviceCode) {
        return EntityLogicService.getExpensesOfServiceWithCode(serviceCode, publicInvestmentBudgetCoFundedExpenses);
    }

    //Get Sums Of Every Service

    @Override
    public Map<String, Long> getRegularSumOfEveryService() {
        return EntityLogicService.getSumOfEveryService(regularBudgetExpenses);
    }

    @Override
    public Map<String, Long> getPublicInvestmentNationalSumOfEveryService() {
        return EntityLogicService.getSumOfEveryService(publicInvestmentBudgetNationalExpenses);
    }

    @Override
    public Map<String, Long> getPublicInvestmentCoFundedSumOfEveryService() {
        return EntityLogicService.getSumOfEveryService(publicInvestmentBudgetCoFundedExpenses);
    }

    // Get Sums Of Specific Expense Category

    @Override
    public long getRegularSumOfExpenseCategoryWithCode(String code) {
        return EntityLogicService.getSumOfExpenseCategoryWithCode(code, regularBudgetExpenses);
    }

    @Override
    public long getPublicInvestmentNationalSumOfExpenseCategoryWithCode(String code) {
        return EntityLogicService.getSumOfExpenseCategoryWithCode(code, publicInvestmentBudgetNationalExpenses);
    }

    @Override
    public long getPublicInvestmentCoFundedSumOfExpenseCategoryWithCode(String code) {
        return EntityLogicService.getSumOfExpenseCategoryWithCode(code, publicInvestmentBudgetCoFundedExpenses);
    }

    //Get Sums Of Every Expense Category

    @Override
    public Map<String, Long> getRegularSumOfEveryExpenseCategory() {
        return EntityLogicService.getSumOfEveryExpenseCategory(regularBudgetExpenses);
    }

    @Override
    public Map<String, Long> getPublicInvestmentNationalSumOfEveryExpenseCategory() {
        return EntityLogicService.getSumOfEveryExpenseCategory(publicInvestmentBudgetNationalExpenses);
    }

    @Override
    public Map<String, Long> getPublicInvestmentCoFundedSumOfEveryExpenseCategory() {
        return EntityLogicService.getSumOfEveryExpenseCategory(publicInvestmentBudgetCoFundedExpenses);
    }

    //IMPLEMENTATION OF CHANGES

    public void implementChangesInAllExpenseCategoriesOfAllServicesWithPercentageAllocation(double percentage, long fixedAmount, BudgetType budgetType) {
        ExpenseAdjustmentStrategy strategy = new PercentageAllocationAdjustmentStrategy(new MatchAllFilter(), new PercentageOperation());
        applyChangesAndKeepHistory(strategy, percentage, fixedAmount, budgetType);
    }

    public void implementChangesInSpecificExpenseCategoryOfAllServicesWithPercentageAllocation(String expenseCode, double percentage, long fixedAmount, BudgetType budgetType) {
        ExpenseAdjustmentStrategy strategy = new PercentageAllocationAdjustmentStrategy(new AccountFilter(expenseCode), new PercentageOperation());
        applyChangesAndKeepHistory(strategy, percentage, fixedAmount, budgetType);
    }

    public void implementChangesInAllExpenseCategoriesOfSpecificServiceWithPercentageAllocation(String serviceCode, double percentage, long fixedAmount, BudgetType budgetType) {
        ExpenseAdjustmentStrategy strategy = new PercentageAllocationAdjustmentStrategy(new ServiceFilter(serviceCode), new PercentageOperation());
        applyChangesAndKeepHistory(strategy, percentage, fixedAmount, budgetType);
    }

    private void applyChangesAndKeepHistory(ExpenseAdjustmentStrategy strategy, double percentage, long fixedAmount, BudgetType budgetType) {
        if (budgetType == BudgetType.REGULAR_BUDGET) {
            ExpensesHistory.keepHistory(regularBudgetExpenses, budgetType);
            strategy.applyAdjustment(regularBudgetExpenses, percentage, fixedAmount);
        } else if (budgetType == BudgetType.PUBLIC_INVESTMENT_BUDGET_NATIONAL) {
            ExpensesHistory.keepHistory(publicInvestmentBudgetNationalExpenses, budgetType);
            strategy.applyAdjustment(publicInvestmentBudgetNationalExpenses, percentage, fixedAmount);
        } else if (budgetType == BudgetType.PUBLIC_INVESTMENT_BUDGET_COFUNDED) {
            ExpensesHistory.keepHistory(publicInvestmentBudgetCoFundedExpenses, budgetType);
            strategy.applyAdjustment(publicInvestmentBudgetCoFundedExpenses, percentage, fixedAmount);
        } else {
            throw new IllegalStateException("BUDGET_TYPE NOT SUPPORTED");
        }
    }

    //Getters

    public String getEntityCode() {
        return entityCode;
    }

    public String getEntityName() {
        return entityName;
    }

    public ArrayList<RegularBudgetExpense> getRegularBudgetExpenses() {
        return regularBudgetExpenses;
    }

    public ArrayList<PublicInvestmentBudgetNationalExpense> getPublicInvestmentBudgetNationalExpenses() {
        return publicInvestmentBudgetNationalExpenses;
    }

    public ArrayList<PublicInvestmentBudgetCoFundedExpense> getPublicInvestmentBudgetCoFundedExpenses() {
        return publicInvestmentBudgetCoFundedExpenses;
    }

    public static ArrayList<Entity> getEntities() {
        return entities;
    }

    @Override
    public String toString() {
        return "Entity Code: " + entityCode + ", Name: " + entityName;
    }
}
