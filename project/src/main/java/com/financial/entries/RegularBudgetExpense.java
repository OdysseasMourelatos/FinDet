package com.financial.entries;

import com.financial.services.expenses.BudgetExpenseChangesService;
import com.financial.services.expenses.BudgetExpenseLogicService;

import java.util.*;

public class RegularBudgetExpense extends BudgetExpense {
    protected static ArrayList<RegularBudgetExpense> regularBudgetExpenses = new ArrayList<>();

    public RegularBudgetExpense (String entityCode, String entityName, String serviceCode, String serviceName, String code, String description, String category, long amount) {
        super(entityCode, entityName, serviceCode, serviceName, code, description, category, amount);
        regularBudgetExpenses.add(this);
    }

    public static ArrayList<RegularBudgetExpense> getAllRegularBudgetExpenses() {
        return regularBudgetExpenses;
    }

    public static ArrayList<RegularBudgetExpense> getRegularBudgetExpensesOfEntityWithCode(String entityCode) {
        return BudgetExpenseLogicService.getExpensesOfEntityWithCode(entityCode, regularBudgetExpenses);
    }

    public static Map<String, String> getUniqueExpenseMap() {
        return BudgetExpenseLogicService.getUniqueExpenseMap(regularBudgetExpenses);
    }

    public static Map<Map<String, String>, Long> getSumOfEveryExpenseCategory() {
        return BudgetExpenseLogicService.getSumOfEveryExpenseCategory(getUniqueExpenseMap(), regularBudgetExpenses);
    }

    //Applies changes to certain expense category (e.g. 21) of all entities (Global Change)

    public static void implementGlobalChangesInCertainRegularExpenseCategoryWithPercentageAllocation(String code, double percentage, long fixedAmount) {
        BudgetExpenseChangesService.implementGlobalChangesInCertainExpenseCategoryWithPercentageAllocation(code, percentage, fixedAmount, regularBudgetExpenses);
    }

    public static void implementGlobalChangesInAllExpenseCategoriesWithPercentageAllocation(double percentage, long fixedAmount) {
        BudgetExpenseChangesService.implementGlobalChangesInAllExpenseCategoriesWithPercentageAllocation(percentage, fixedAmount, regularBudgetExpenses);
    }


    @Override
    public String toString() {
        return super.toString();
    }
}
