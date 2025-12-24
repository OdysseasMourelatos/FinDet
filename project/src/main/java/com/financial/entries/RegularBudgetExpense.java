package com.financial.entries;

import com.financial.services.BudgetType;
import com.financial.services.expenses.BudgetExpenseChangesService;
import com.financial.services.expenses.BudgetExpenseLogicService;
import com.financial.services.expenses.ExpensesHistory;

import java.util.*;

public class RegularBudgetExpense extends BudgetExpense {

    protected static ArrayList<RegularBudgetExpense> regularBudgetExpenses = new ArrayList<>();

    public RegularBudgetExpense(String entityCode, String entityName, String serviceCode, String serviceName, String code, String description, String category, long amount) {
        super(entityCode, entityName, serviceCode, serviceName, code, description, category, amount);
        regularBudgetExpenses.add(this);
    }

    //Class Methods

    public static ArrayList<RegularBudgetExpense> getAllRegularBudgetExpenses() {
        return regularBudgetExpenses;
    }

    public static ArrayList<RegularBudgetExpense> getRegularBudgetExpensesOfEntityWithCode(String entityCode) {
        return BudgetExpenseLogicService.getExpensesOfEntityWithCode(entityCode, regularBudgetExpenses);
    }

    public static ArrayList<RegularBudgetExpense> getRegularBudgetExpensesOfCategoryWithCode(String expenseCode) {
        return BudgetExpenseLogicService.getExpensesOfCategoryWithCode(expenseCode, regularBudgetExpenses);
    }

    public static RegularBudgetExpense findRegularBudgetExpenseWithCodes(String entityCode, String serviceCode, String expenseCode) {
        return (RegularBudgetExpense) (BudgetExpenseLogicService.findExpenseWithCode(entityCode, serviceCode, expenseCode, regularBudgetExpenses));
    }

    public static long calculateSum() {
        return BudgetExpenseLogicService.calculateSum(regularBudgetExpenses);
    }

    public static Map<String, Long> getSumOfEveryRegularExpenseCategory() {
        return BudgetExpenseLogicService.getSumOfEveryExpenseCategory(regularBudgetExpenses);
    }

    public static String getDescriptionWithCode(String code) {
        return BudgetExpenseLogicService.getDescriptionWithCode(code, regularBudgetExpenses);
    }

    //Applies changes to certain expense category (e.g. 21) of all entities (Global Change)

    public static void implementGlobalChangesInCertainRegularExpenseCategoryWithPercentageAllocation(String code, double percentage, long fixedAmount) {
        ExpensesHistory.keepHistory(getRegularBudgetExpensesOfCategoryWithCode(code), BudgetType.REGULAR_BUDGET);
        BudgetExpenseChangesService.implementGlobalChangesInCertainExpenseCategoryWithPercentageAllocation(code, percentage, fixedAmount, regularBudgetExpenses);
    }

    //Scenario: Change of all regular budget expenses of all entities

    public static void implementGlobalChangesInAllExpenseCategoriesWithPercentageAllocation(double percentage, long fixedAmount) {
        ExpensesHistory.keepHistory(regularBudgetExpenses, BudgetType.REGULAR_BUDGET);
        BudgetExpenseChangesService.implementGlobalChangesInAllExpenseCategoriesWithPercentageAllocation(percentage, fixedAmount, regularBudgetExpenses);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
