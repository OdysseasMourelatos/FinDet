package com.financial.entries;

import com.financial.services.BudgetType;
import com.financial.services.expenses.BudgetExpenseChangesService;
import com.financial.services.expenses.BudgetExpenseLogicService;
import com.financial.services.expenses.ExpensesHistory;
import java.util.*;

/**
 * Represents an expenditure within the National segment of the Public Investment Budget.
 * <p>
 * This class extends {@link PublicInvestmentBudgetExpense} to manage projects funded
 * solely by domestic resources. It mirrors the structure of the co-funded segment
 * while maintaining an independent registry and specialized logic for national
 * budget modifications.
 */
public class PublicInvestmentBudgetNationalExpense extends PublicInvestmentBudgetExpense {

    // Static collection maintains all national expense instances
    protected static ArrayList<PublicInvestmentBudgetNationalExpense> pibNationalExpenses = new ArrayList<>();


    /**
     * **Initializes** a new National investment expense and **registers** it in the global registry.
     * <p>
     * This constructor delegates the initialization of shared attributes to the parent class
     * and automatically adds the instance to the static {@code pibNationalExpenses} registry
     * to facilitate global aggregation and lookup services.
     *
     * @param entityCode  **Unique identifier** for the Ministry or government Entity.
     * @param entityName  **Display name** of the Entity.
     * @param serviceCode **Unique identifier** for the specific Service or Department.
     * @param serviceName **Display name** of the Service.
     * @param code        **Budget classification code** assigned to this entry.
     * @param description **Textual summary** describing the expenditure.
     * @param type        **Investment segment type** (e.g., "ΕΘΝΙΚΟ").
     * @param category    **Broad classification category** for budget reporting.
     * @param amount      **Monetary value** of the expense as a long.
     */
    public PublicInvestmentBudgetNationalExpense(String entityCode, String entityName, String serviceCode, String serviceName, String code, String description, String type, String category, long amount) {
        super(entityCode, entityName, serviceCode, serviceName, code, description, type, category, amount);
        pibNationalExpenses.add(this);
    }

    /**
     * **Returns** the complete static registry of all **national** investment expenses.
     * @return An {@link ArrayList} of all recorded national expenditure instances.
     */
    public static ArrayList<PublicInvestmentBudgetNationalExpense> getAllPublicInvestmentBudgetNationalExpenses() {
        return pibNationalExpenses;
    }

    /**
     * **Filters** the national registry to find expenses related to a specific **Entity**.
     * @param entityCode The unique code for the Ministry or Entity.
     * @return A list of national expenses for the given entity.
     */
    public static ArrayList<PublicInvestmentBudgetNationalExpense> getPublicInvestmentBudgetNationalExpensesOfEntityWithCode(String entityCode) {
        return BudgetExpenseLogicService.getExpensesOfEntityWithCode(entityCode, pibNationalExpenses);
    }

    /**
     * **Retrieves** all national expenses assigned to a specific **Expense Category**.
     * @param expenseCode The unique code of the budget category.
     * @return A list of matching national expense entries.
     */
    public static ArrayList<PublicInvestmentBudgetNationalExpense> getPublicInvestmentBudgetNationalExpensesOfCategoryWithCode(String expenseCode) {
        return BudgetExpenseLogicService.getExpensesOfCategoryWithCode(expenseCode, pibNationalExpenses);
    }

    /**
     * **Performs** a unique lookup for a national expense based on Entity, Service, and Category codes.
     * @param entityCode Unique Entity identifier.
     * @param serviceCode Unique Service identifier.
     * @param expenseCode Unique Expense Category identifier.
     * @return The specific {@link PublicInvestmentBudgetNationalExpense} record.
     */
    public static PublicInvestmentBudgetNationalExpense findPublicInvestmentBudgetNationalExpenseWithCodes(String entityCode, String serviceCode, String expenseCode) {
        return (PublicInvestmentBudgetNationalExpense) BudgetExpenseLogicService.findExpenseWithCode(entityCode, serviceCode, expenseCode, pibNationalExpenses);
    }

    /**
     * **Computes** the total aggregated sum of all expenditures in the **national investment** segment.
     * @return The total sum of national investment projects.
     */
    public static long calculateSum() {
        return BudgetExpenseLogicService.calculateSum(pibNationalExpenses);
    }

    /**
     * **Aggregates** expenditure totals for every **Category** within the national segment.
     * @return A {@link Map} mapping category codes to their total financial sums.
     */
    public static Map<String, Long> getSumOfEveryPublicInvestmentNationalExpenseCategory() {
        return BudgetExpenseLogicService.getSumOfEveryExpenseCategory(pibNationalExpenses);
    }

    /**
     * **Looks up** the descriptive text associated with a classification code.
     * @param code The budget classification code.
     * @return The textual description of the code.
     */
    public static String getDescriptionWithCode(String code) {
        return BudgetExpenseLogicService.getDescriptionWithCode(code, pibNationalExpenses);
    }

    /**
     * **Implements** financial adjustments for a **target category** in the national segment.
     * **Guarantees** data auditability by logging the state in {@link ExpensesHistory} first.
     * @param code The target category code.
     * @param percentage The percentage shift to apply.
     * @param fixedAmount The fixed monetary shift to apply.
     */
    public static void implementGlobalChangesInCertainPublicInvestmentBudgetNationalCategory(String code, double percentage, long fixedAmount) {
        ExpensesHistory.keepHistory(getPublicInvestmentBudgetNationalExpensesOfCategoryWithCode(code), BudgetType.PUBLIC_INVESTMENT_BUDGET_NATIONAL);
        BudgetExpenseChangesService.implementGlobalChangesInCertainExpenseCategoryWithPercentageAllocation(code, percentage, fixedAmount, pibNationalExpenses);
    }

    /**
     * **Executes** a global financial update across **all national categories**.
     * **Persists** the current data state in history to allow for future auditing.
     * @param percentage The global percentage adjustment.
     * @param fixedAmount The global fixed amount adjustment.
     */
    public static void implementGlobalChangesInAllPublicInvestmentBudgetNationalCategories(double percentage, long fixedAmount) {
        ExpensesHistory.keepHistory(pibNationalExpenses, BudgetType.PUBLIC_INVESTMENT_BUDGET_NATIONAL);
        BudgetExpenseChangesService.implementGlobalChangesInAllExpenseCategoriesWithPercentageAllocation(percentage, fixedAmount, pibNationalExpenses);
    }

    /** Returns a string representation of the PIBNational expenditure entry. */
    @Override
    public String toString() {
        return super.toString();
    }
}