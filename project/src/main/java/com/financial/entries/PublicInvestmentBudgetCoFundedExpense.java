package com.financial.entries;

import com.financial.services.BudgetType;
import com.financial.services.expenses.BudgetExpenseChangesService;
import com.financial.services.expenses.BudgetExpenseLogicService;
import com.financial.services.expenses.ExpensesHistory;
import java.util.*;

public class PublicInvestmentBudgetCoFundedExpense extends PublicInvestmentBudgetExpense {

    // Static collection maintains all co-funded expense instances
    protected static ArrayList<PublicInvestmentBudgetCoFundedExpense> pibCoFundedExpenses = new ArrayList<>();

    public PublicInvestmentBudgetCoFundedExpense(String entityCode, String entityName, String serviceCode, String serviceName, String code, String description, String type, String category, long amount) {
        super(entityCode, entityName, serviceCode, serviceName, code, description, type, category, amount);
        pibCoFundedExpenses.add(this);
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

    public static Map<String, Long> getSumOfEveryPublicInvestmentCoFundedExpenseCategory() {
        return BudgetExpenseLogicService.getSumOfEveryExpenseCategory(pibCoFundedExpenses);
    }

    public static String getDescriptionWithCode(String code) {
        return BudgetExpenseLogicService.getDescriptionWithCode(code, pibCoFundedExpenses);
    }

    // Stores history before applying percentage and fixed amount changes to a specific category
    public static void implementGlobalChangesInCertainPublicInvestmentBudgetCoFundedCategory(String code, double percentage, long fixedAmount) {
        ExpensesHistory.keepHistory(getPublicInvestmentBudgetCoFundedExpensesOfCategoryWithCode(code), BudgetType.PUBLIC_INVESTMENT_BUDGET_COFUNDED);
        BudgetExpenseChangesService.implementGlobalChangesInCertainExpenseCategoryWithPercentageAllocation(code, percentage, fixedAmount, pibCoFundedExpenses);
    }

    // Stores history before applying percentage and fixed amount changes to all categories
    public static void implementGlobalChangesInAllPublicInvestmentBudgetCoFundedCategories(double percentage, long fixedAmount) {
        ExpensesHistory.keepHistory(pibCoFundedExpenses, BudgetType.PUBLIC_INVESTMENT_BUDGET_COFUNDED);
        BudgetExpenseChangesService.implementGlobalChangesInAllExpenseCategoriesWithPercentageAllocation(percentage, fixedAmount, pibCoFundedExpenses);
    }


    @Override
    public String toString() {
        return super.toString();
    }
}