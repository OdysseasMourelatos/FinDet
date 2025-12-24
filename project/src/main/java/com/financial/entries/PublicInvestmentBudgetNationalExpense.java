package com.financial.entries;

import com.financial.services.BudgetType;
import com.financial.services.expenses.BudgetExpenseChangesService;
import com.financial.services.expenses.BudgetExpenseLogicService;
import com.financial.services.expenses.ExpensesHistory;
import java.util.*;

public class PublicInvestmentBudgetNationalExpense extends PublicInvestmentBudgetExpense {
    protected static ArrayList<PublicInvestmentBudgetNationalExpense> pibNationalExpenses = new ArrayList<>();

    public PublicInvestmentBudgetNationalExpense(String entityCode, String entityName, String serviceCode, String serviceName, String code, String description, String type, String category, long amount) {
        super(entityCode, entityName, serviceCode, serviceName, code, description, type, category, amount);
        pibNationalExpenses.add(this);
    }

    protected static ArrayList<PublicInvestmentBudgetNationalExpense> pibNationalExpensesPerCategory = new ArrayList<>();

    private PublicInvestmentBudgetNationalExpense(String code, String description, String type, String category, long amount) {
        super(code, description, type, category, amount);
        pibNationalExpensesPerCategory.add(this);
    }

    public static ArrayList<PublicInvestmentBudgetNationalExpense> getAllPublicInvestmentBudgetNationalExpenses() {
        return pibNationalExpenses;
    }

    public static void createPublicInvestmentBudgetNationalExpensesPerCategory() {
        pibNationalExpensesPerCategory.clear();
        for (Map.Entry<String, Long> entry : getSumOfEveryExpenseCategory().entrySet()) {
            String description = pibNationalExpenses.stream().filter(e -> e.getCode().equals(entry.getKey())).findFirst().map(PublicInvestmentBudgetNationalExpense::getDescription).orElse("");
            new PublicInvestmentBudgetNationalExpense(entry.getKey(), description, "ΕΘΝΙΚΟ", "ΕΞΟΔΑ", entry.getValue());
        }
    }

    public static void updateAllFilteredPIBNationalExpenses() {
        Map<String, Long> allNewSums = getSumOfEveryExpenseCategory();
        for (PublicInvestmentBudgetNationalExpense filteredExpense : pibNationalExpensesPerCategory) {
            long newTotal = allNewSums.getOrDefault(filteredExpense.getCode(), 0L);
            filteredExpense.setAmount(newTotal);
        }
    }

    public static ArrayList<PublicInvestmentBudgetNationalExpense> getPublicInvestmentBudgetNationalExpensesOfEntityWithCode(String entityCode) {
        return BudgetExpenseLogicService.getExpensesOfEntityWithCode(entityCode, pibNationalExpenses);
    }

    public static ArrayList<PublicInvestmentBudgetNationalExpense> getPublicInvestmentBudgetNationalExpensesOfCategoryWithCode(String expenseCode) {
        return BudgetExpenseLogicService.getExpensesOfCategoryWithCode(expenseCode, pibNationalExpenses);
    }

    public static PublicInvestmentBudgetNationalExpense findPublicInvestmentBudgetNationalExpenseWithCodes(String entityCode, String serviceCode, String expenseCode) {
        return (PublicInvestmentBudgetNationalExpense) BudgetExpenseLogicService.findExpenseWithCode(entityCode, serviceCode, expenseCode, pibNationalExpenses);
    }

    public static Map<String, Long> getSumOfEveryExpenseCategory() {
        return BudgetExpenseLogicService.getSumOfEveryExpenseCategory(pibNationalExpenses);
    }
}