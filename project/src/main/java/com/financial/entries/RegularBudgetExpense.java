package com.financial.entries;

import com.financial.services.BudgetType;
import com.financial.services.expenses.BudgetExpenseChangesService;
import com.financial.services.expenses.BudgetExpenseLogicService;
import com.financial.services.expenses.ExpensesHistory;

import java.util.*;

/**
 * Represents a specialized expense entry specifically for the Regular Budget.
 * <p>
 * This class manages the lifecycle and logic for regular budget expenditures,
 * maintaining an internal static registry for efficient searching and aggregation.
 * It also provides methods for "Global Changes"—mass financial updates that
 * affect specific categories across all organizational entities while
 * automatically maintaining state history.
 * </p>
 */
public class RegularBudgetExpense extends BudgetExpense {

    /**
     * Internal registry of all instantiated {@code RegularBudgetExpense} objects.
     * This list is used for global calculations and data retrieval operations.
     */
    protected static ArrayList<RegularBudgetExpense> regularBudgetExpenses = new ArrayList<>();

    /**
     * Constructs a new RegularBudgetExpense and registers it in the internal registry.
     *
     * @param entityCode   The unique identifier for the entity.
     * @param entityName   The descriptive name of the entity.
     * @param serviceCode  The identifier for the specific service.
     * @param serviceName  The name of the associated service.
     * @param code         The expense/account code.
     * @param description  A brief description of the expense.
     * @param category     The expense category classification.
     * @param amount       The monetary value of the expense.
     */
    public RegularBudgetExpense(String entityCode, String entityName, String serviceCode, String serviceName, String code, String description, String category, long amount) {
        super(entityCode, entityName, serviceCode, serviceName, code, description, category, amount);
        regularBudgetExpenses.add(this);
    }

    // Class Methods

    /**
     * Retrieves the global list of all registered regular budget expenses.
     * * @return An {@link ArrayList} of all {@code RegularBudgetExpense} instances.
     */
    public static ArrayList<RegularBudgetExpense> getAllRegularBudgetExpenses() {
        return regularBudgetExpenses;
    }

    /**
     * Filters and returns regular budget expenses associated with a specific entity.
     * * @param entityCode The code of the entity to filter by.
     * @return A list of expenses belonging to the specified entity.
     */
    public static ArrayList<RegularBudgetExpense> getRegularBudgetExpensesOfEntityWithCode(String entityCode) {
        return BudgetExpenseLogicService.getExpensesOfEntityWithCode(entityCode, regularBudgetExpenses);
    }

    /**
     * Filters and returns regular budget expenses belonging to a specific category code.
     * * @param expenseCode The category code (e.g., "21") to filter by.
     * @return A list of expenses within that category.
     */
    public static ArrayList<RegularBudgetExpense> getRegularBudgetExpensesOfCategoryWithCode(String expenseCode) {
        return BudgetExpenseLogicService.getExpensesOfCategoryWithCode(expenseCode, regularBudgetExpenses);
    }

    /**
     * Searches for a specific regular budget expense using its composite unique key.
     * * @param entityCode  The code of the entity.
     * @param serviceCode The code of the service.
     * @param expenseCode The specific account/expense code.
     * @return The matching {@code RegularBudgetExpense}, or {@code null} if not found.
     */
    public static RegularBudgetExpense findRegularBudgetExpenseWithCodes(String entityCode, String serviceCode, String expenseCode) {
        return (RegularBudgetExpense) (BudgetExpenseLogicService.findExpenseWithCode(entityCode, serviceCode, expenseCode, regularBudgetExpenses));
    }

    /**
     * Calculates the total sum of all regular budget expenses currently in the registry.
     * * @return The total sum as a {@code long}.
     */
    public static long calculateSum() {
        return BudgetExpenseLogicService.calculateSum(regularBudgetExpenses);
    }

    /**
     * Aggregates financial totals for every expense category within the Regular Budget.
     * * @return A {@link Map} where keys are category codes and values are their respective total sums.
     */
    public static Map<String, Long> getSumOfEveryRegularExpenseCategory() {
        return BudgetExpenseLogicService.getSumOfEveryExpenseCategory(regularBudgetExpenses);
    }

    /**
     * Retrieves the description associated with a specific expense code from the regular budget list.
     * * @param code The expense code to search for.
     * @return The description string, or an empty string if not found.
     */
    public static String getDescriptionWithCode(String code) {
        return BudgetExpenseLogicService.getDescriptionWithCode(code, regularBudgetExpenses);
    }

    /**
     * Implements a global change to a specific expense category across all entities.
     * <p>
     * This method captures the current state of affected accounts in the
     * {@link ExpensesHistory} before applying changes using percentage
     * allocation and/or a fixed amount.
     * </p>
     * *
     * * @param code         The expense category code to be modified (e.g., "24").
     * @param percentage   The percentage adjustment (e.g., 0.05 for 5% increase).
     * @param fixedAmount  A fixed monetary amount to be applied.
     */
    public static void implementGlobalChangesInCertainRegularExpenseCategoryWithPercentageAllocation(String code, double percentage, long fixedAmount) {
        ExpensesHistory.keepHistory(getRegularBudgetExpensesOfCategoryWithCode(code), BudgetType.REGULAR_BUDGET);
        BudgetExpenseChangesService.implementGlobalChangesInCertainExpenseCategoryWithPercentageAllocation(code, percentage, fixedAmount, regularBudgetExpenses);
    }

    /**
     * Implements a global change affecting all regular budget expenses across all entities.
     * <p>
     * Use this for broad budgetary adjustments. It captures the history of all regular
     * expenses before the transformation.
     * </p>
     * * @param percentage  The percentage adjustment to apply globally.
     * @param fixedAmount The fixed amount adjustment to apply globally.
     */
    public static void implementGlobalChangesInAllExpenseCategoriesWithPercentageAllocation(double percentage, long fixedAmount) {
        ExpensesHistory.keepHistory(regularBudgetExpenses, BudgetType.REGULAR_BUDGET);
        BudgetExpenseChangesService.implementGlobalChangesInAllExpenseCategoriesWithPercentageAllocation(percentage, fixedAmount, regularBudgetExpenses);
    }

    /**
     * Returns a string representation of the regular budget expense.
     * * @return String details from the superclass.
     */
    @Override
    public String toString() {
        return super.toString();
    }
}