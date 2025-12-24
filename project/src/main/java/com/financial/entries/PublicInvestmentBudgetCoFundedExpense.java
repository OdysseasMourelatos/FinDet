package com.financial.entries;

import com.financial.services.BudgetType;
import com.financial.services.expenses.BudgetExpenseChangesService;
import com.financial.services.expenses.BudgetExpenseLogicService;
import com.financial.services.expenses.ExpensesHistory;
import java.util.*;

public class PublicInvestmentBudgetCoFundedExpense extends PublicInvestmentBudgetExpense {

    protected static ArrayList<PublicInvestmentBudgetCoFundedExpense> pibCoFundedExpenses = new ArrayList<>();

    public PublicInvestmentBudgetCoFundedExpense(String entityCode, String entityName, String serviceCode, String serviceName, String code, String description, String type, String category, long amount) {
        super(entityCode, entityName, serviceCode, serviceName, code, description, type, category, amount);
        pibCoFundedExpenses.add(this);
    }

    protected static ArrayList<PublicInvestmentBudgetCoFundedExpense> pibCoFundedExpensesPerCategory = new ArrayList<>();

    private PublicInvestmentBudgetCoFundedExpense(String code, String description, String type, String category, long amount) {
        super(code, description, type, category, amount);
        pibCoFundedExpensesPerCategory.add(this);
    }

    public static void createPublicInvestmentBudgetCoFundedExpensesPerCategory() {
        pibCoFundedExpensesPerCategory.clear();
        for (Map.Entry<String, Long> entry : getSumOfEveryExpenseCategory().entrySet()) {
            String description = pibCoFundedExpenses.stream().filter(e -> e.getCode().equals(entry.getKey())).findFirst().map(PublicInvestmentBudgetCoFundedExpense::getDescription).orElse("");
            new PublicInvestmentBudgetCoFundedExpense(entry.getKey(), description, "ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ", "ΕΞΟΔΑ", entry.getValue());
        }
    }

    public static PublicInvestmentBudgetCoFundedExpense findFilteredPublicInvestmentBudgetCoFundedExpenseWithCode(String code) {
        for (PublicInvestmentBudgetCoFundedExpense expense : pibCoFundedExpensesPerCategory) {
            if (expense.getCode().equals(code)) {
                return expense;
            }
        }
        return null;
    }

    public static ArrayList<PublicInvestmentBudgetCoFundedExpense> getAllPublicInvestmentBudgetCoFundedExpenses() {
        return pibCoFundedExpenses;
    }

    public static ArrayList<PublicInvestmentBudgetCoFundedExpense> getPublicInvestmentBudgetCoFundedExpensesOfEntityWithCode(String entityCode) {
        return BudgetExpenseLogicService.getExpensesOfEntityWithCode(entityCode, pibCoFundedExpenses);
    }

    public static ArrayList<PublicInvestmentBudgetCoFundedExpense> getPublicInvestmentBudgetCoFundedExpensesOfCategoryWithCode(String expenseCode) {
        return BudgetExpenseLogicService.getExpensesOfCategoryWithCode(expenseCode, pibCoFundedExpenses);
    }

    public static PublicInvestmentBudgetCoFundedExpense findPublicInvestmentBudgetCoFundedExpenseWithCodes(String entityCode, String serviceCode, String expenseCode) {
        return (PublicInvestmentBudgetCoFundedExpense) BudgetExpenseLogicService.findExpenseWithCode(entityCode, serviceCode, expenseCode, pibCoFundedExpenses);
    }

    public static long calculateSum() {
        return BudgetExpenseLogicService.calculateSum(pibCoFundedExpenses);
    }

    public static Map<String, Long> getSumOfEveryExpenseCategory() {
        return BudgetExpenseLogicService.getSumOfEveryExpenseCategory(pibCoFundedExpenses);
    }

    public static ArrayList<PublicInvestmentBudgetCoFundedExpense> getPublicInvestmentBudgetCoFundedExpensesPerCategory() {
        return pibCoFundedExpensesPerCategory;
    }

    public static void implementGlobalChangesInCertainPublicInvestmentBudgetCoFundedCategory(String code, double percentage, long fixedAmount) {
        ExpensesHistory.keepHistory(getPublicInvestmentBudgetCoFundedExpensesOfCategoryWithCode(code), BudgetType.PUBLIC_INVESTMENT_BUDGET_COFUNDED);
        BudgetExpenseChangesService.implementGlobalChangesInCertainExpenseCategoryWithPercentageAllocation(code, percentage, fixedAmount, pibCoFundedExpenses);
        updateFilteredPublicInvestmentBudgetCoFundedExpense(code);
    }

    public static void implementGlobalChangesInAllPublicInvestmentBudgetCoFundedCategories(double percentage, long fixedAmount) {
        ExpensesHistory.keepHistory(pibCoFundedExpenses, BudgetType.PUBLIC_INVESTMENT_BUDGET_COFUNDED);
        BudgetExpenseChangesService.implementGlobalChangesInAllExpenseCategoriesWithPercentageAllocation(percentage, fixedAmount, pibCoFundedExpenses);
        updateAllFilteredPIBCoFundedExpenses();
    }

    public static void updateFilteredPublicInvestmentBudgetCoFundedExpense(String code) {
        PublicInvestmentBudgetCoFundedExpense filtered = findFilteredPublicInvestmentBudgetCoFundedExpenseWithCode(code);
        if (filtered != null) {
            long newTotal = getSumOfEveryExpenseCategory().getOrDefault(code, 0L);
            filtered.setAmount(newTotal);
        }
    }

    public static void updateAllFilteredPIBCoFundedExpenses() {
        Map<String, Long> allNewSums = getSumOfEveryExpenseCategory();
        for (PublicInvestmentBudgetCoFundedExpense filteredExpense : pibCoFundedExpensesPerCategory) {
            long newTotal = allNewSums.getOrDefault(filteredExpense.getCode(), 0L);
            filteredExpense.setAmount(newTotal);
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }
}