package com.financial.entries;

import com.financial.services.expenses.BudgetExpenseChangesService;
import com.financial.services.expenses.BudgetExpenseLogicService;

import java.util.*;

public class RegularBudgetExpense extends BudgetExpense {
    protected static ArrayList<RegularBudgetExpense> regularBudgetExpenses = new ArrayList<>();

    public RegularBudgetExpense(String entityCode, String entityName, String serviceCode, String serviceName, String code, String description, String category, long amount) {
        super(entityCode, entityName, serviceCode, serviceName, code, description, category, amount);
        regularBudgetExpenses.add(this);
    }

    //Constructor & List of filtered regular budget expenses

    protected static ArrayList<RegularBudgetExpense> regularBudgetExpensesPerCategory = new ArrayList<>();

    private RegularBudgetExpense(String code, String description, String category, long amount) {
        super(code, description, category, amount);
        regularBudgetExpensesPerCategory.add(this);
    }

    //Create filtered regular budget expenses
    public static void createRegularBudgetExpensesPerCategory() {
        for (Map.Entry<String, Long> entry : getSumOfEveryExpenseCategory().entrySet()) {
            String description = regularBudgetExpenses.stream().filter(e -> e.getCode().equals(entry.getKey())).findFirst().map(RegularBudgetExpense::getDescription).orElse(null);
            new RegularBudgetExpense(entry.getKey(), description, "ΕΞΟΔΑ", entry.getValue());
        }
    }

    //Find filtered regular budget expense
    public static RegularBudgetExpense findFilteredRegularBudgetExpenseWithCode(String code) {
        for (RegularBudgetExpense regularBudgetExpense : regularBudgetExpensesPerCategory) {
            if (regularBudgetExpense.getCode().equals(code)) {
                return regularBudgetExpense;
            }
        }
        return null;
    }

    //Class Methods

    public static ArrayList<RegularBudgetExpense> getAllRegularBudgetExpenses() {
        return regularBudgetExpenses;
    }

    public static ArrayList<RegularBudgetExpense> getRegularBudgetExpensesOfEntityWithCode(String entityCode) {
        return BudgetExpenseLogicService.getExpensesOfEntityWithCode(entityCode, regularBudgetExpenses);
    }

    public static RegularBudgetExpense findRegularBudgetExpenseWithCodes(String entityCode, String serviceCode, String expenseCode) {
        return (RegularBudgetExpense) (BudgetExpenseLogicService.findExpenseWithCode(entityCode, serviceCode, expenseCode, regularBudgetExpenses));
    }

    public static long calculateSum() {
        return BudgetExpenseLogicService.calculateSum(regularBudgetExpenses);
    }

    public static Map<String, Long> getSumOfEveryExpenseCategory() {
        return BudgetExpenseLogicService.getSumOfEveryExpenseCategory(regularBudgetExpenses);
    }

    public static ArrayList<RegularBudgetExpense> getRegularBudgetExpensesPerCategory() {
        return regularBudgetExpensesPerCategory;
    }

    //Applies changes to certain expense category (e.g. 21) of all entities (Global Change)

    public static void implementGlobalChangesInCertainRegularExpenseCategoryWithPercentageAllocation(String code, double percentage, long fixedAmount) {
        BudgetExpenseChangesService.implementGlobalChangesInCertainExpenseCategoryWithPercentageAllocation(code, percentage, fixedAmount, regularBudgetExpenses);
        updateFilteredRegularBudgetExpense(code, percentage, fixedAmount);
    }

    //Scenario: Change of all regular budget expenses of all entities

    public static void implementGlobalChangesInAllExpenseCategoriesWithPercentageAllocation(double percentage, long fixedAmount) {
        BudgetExpenseChangesService.implementGlobalChangesInAllExpenseCategoriesWithPercentageAllocation(percentage, fixedAmount, regularBudgetExpenses);
        updateAllFilteredRegularBudgetExpenses(percentage, fixedAmount);
    }

    //Update Filtered Objects

    public static void updateFilteredRegularBudgetExpense(String code, double percentage, long fixedAmount) {
        RegularBudgetExpense regularBudgetExpense = findFilteredRegularBudgetExpenseWithCode(code);
        if (fixedAmount == 0) {
            regularBudgetExpense.setAmount((long) (regularBudgetExpense.getAmount() * (1 + percentage)));
        } else {
            regularBudgetExpense.setAmount(regularBudgetExpense.getAmount() + fixedAmount);
        }
    }

    public static void updateAllFilteredRegularBudgetExpenses(double percentage, long fixedAmount) {
        for (RegularBudgetExpense regularBudgetExpense : regularBudgetExpensesPerCategory) {
            if (fixedAmount == 0) {
                regularBudgetExpense.setAmount((long) (regularBudgetExpense.getAmount() * (1 + percentage)));
            } else {
                regularBudgetExpense.setAmount(regularBudgetExpense.getAmount() + fixedAmount);
            }
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
