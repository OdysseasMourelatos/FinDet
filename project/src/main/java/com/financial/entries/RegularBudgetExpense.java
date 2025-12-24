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

    //Constructor & List of filtered regular budget expenses

    protected static ArrayList<RegularBudgetExpense> regularBudgetExpensesPerCategory = new ArrayList<>();

    private RegularBudgetExpense(String code, String description, String category, long amount) {
        super(code, description, category, amount);
        regularBudgetExpensesPerCategory.add(this);
    }

    //Create filtered regular budget expenses
    public static void createRegularBudgetExpensesPerCategory() {
        regularBudgetExpensesPerCategory.clear();
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

    public static ArrayList<RegularBudgetExpense> getRegularBudgetExpensesOfCategoryWithCode(String expenseCode) {
        return BudgetExpenseLogicService.getExpensesOfCategoryWithCode(expenseCode, regularBudgetExpenses);
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
        ExpensesHistory.keepHistory(getRegularBudgetExpensesOfCategoryWithCode(code), BudgetType.REGULAR_BUDGET);
        BudgetExpenseChangesService.implementGlobalChangesInCertainExpenseCategoryWithPercentageAllocation(code, percentage, fixedAmount, regularBudgetExpenses);
        updateFilteredRegularBudgetExpense(code);
    }

    //Scenario: Change of all regular budget expenses of all entities

    public static void implementGlobalChangesInAllExpenseCategoriesWithPercentageAllocation(double percentage, long fixedAmount) {
        ExpensesHistory.keepHistory(regularBudgetExpenses, BudgetType.REGULAR_BUDGET);
        BudgetExpenseChangesService.implementGlobalChangesInAllExpenseCategoriesWithPercentageAllocation(percentage, fixedAmount, regularBudgetExpenses);
        updateAllFilteredRegularBudgetExpenses();
    }

    //Update Filtered Objects

    public static void updateFilteredRegularBudgetExpense(String code) {
        RegularBudgetExpense regularBudgetExpense = findFilteredRegularBudgetExpenseWithCode(code);
        if (regularBudgetExpense != null) {
            // Αντί να υπολογίζουμε το ποσό χειροκίνητα, παίρνουμε το άθροισμα από τη βασική λίστα
            long newTotal = getSumOfEveryExpenseCategory().getOrDefault(code, 0L);
            regularBudgetExpense.setAmount(newTotal);
        }
    }

    public static void updateAllFilteredRegularBudgetExpenses() {
        // Παίρνουμε όλα τα νέα αθροίσματα μία φορά
        Map<String, Long> allNewSums = getSumOfEveryExpenseCategory();

        for (RegularBudgetExpense filteredExpense : regularBudgetExpensesPerCategory) {
            long newTotal = allNewSums.get(filteredExpense.getCode());
            filteredExpense.setAmount(newTotal);
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
