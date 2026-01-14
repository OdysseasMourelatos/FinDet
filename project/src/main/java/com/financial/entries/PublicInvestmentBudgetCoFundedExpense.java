package com.financial.entries;

import com.financial.services.BudgetType;
import com.financial.services.expenses.BudgetExpenseChangesService;
import com.financial.services.expenses.BudgetExpenseLogicService;
import com.financial.services.expenses.ExpensesHistory;
import java.util.*;

/**
 * Represents an expenditure within the Co-Funded segment of the Public Investment Budget.
 * <p>
 * This class extends {@link PublicInvestmentBudgetExpense} to handle investment projects
 * that are shared between national and international funding sources. It manages a
 * localized registry of co-funded entries and provides specialized methods for
 * history tracking and global budget adjustments.
 */
public class PublicInvestmentBudgetCoFundedExpense extends PublicInvestmentBudgetExpense {

    // Static collection maintains all co-funded expense instances
    protected static ArrayList<PublicInvestmentBudgetCoFundedExpense> pibCoFundedExpenses = new ArrayList<>();

    /**
     * **Initializes** a new Co-Funded investment expense and **registers** it in the global registry.
     * <p>
     * This constructor invokes the superclass constructor to populate core financial fields
     * and then appends the newly created instance to the static {@code pibCoFundedExpenses}
     * collection for centralized data management.
     *
     * @param entityCode  **Unique identifier** for the Ministry or government Entity.
     * @param entityName  **Display name** of the Entity.
     * @param serviceCode **Unique identifier** for the specific Service or Department.
     * @param serviceName **Display name** of the Service.
     * @param code        **Budget classification code** assigned to this entry.
     * @param description **Textual summary** describing the expenditure.
     * @param type        **Investment segment type** (e.g., "ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ").
     * @param category    **Broad classification category** for budget reporting.
     * @param amount      **Monetary value** of the expense as a long.
     */
    public PublicInvestmentBudgetCoFundedExpense(String entityCode, String entityName, String serviceCode, String serviceName, String code, String description, String type, String category, long amount) {
        super(entityCode, entityName, serviceCode, serviceName, code, description, type, category, amount);
        pibCoFundedExpenses.add(this);
    }

    /**
     * **Returns** the complete static registry of all **co-funded** investment expenses.
     * @return An {@link ArrayList} containing all instantiated co-funded records.
     */
    public static ArrayList<PublicInvestmentBudgetCoFundedExpense> getAllPublicInvestmentBudgetCoFundedExpenses() {
        return pibCoFundedExpenses;
    }

    /**
     * **Retrieves** all co-funded expenses associated with a specific **Entity Code**.
     * @param entityCode The unique identifier of the Ministry or Agency.
     * @return A list of matching co-funded expenses.
     */
    public static ArrayList<PublicInvestmentBudgetCoFundedExpense> getPublicInvestmentBudgetCoFundedExpensesOfEntityWithCode(String entityCode) {
        return BudgetExpenseLogicService.getExpensesOfEntityWithCode(entityCode, pibCoFundedExpenses);
    }

    /**
     * **Retrieves** all co-funded expenses belonging to a specific **Budget Category**.
     * @param expenseCode The classification code of the expenditure category.
     * @return A list of entries within the specified category.
     */
    public static ArrayList<PublicInvestmentBudgetCoFundedExpense> getPublicInvestmentBudgetCoFundedExpensesOfCategoryWithCode(String expenseCode) {
        return BudgetExpenseLogicService.getExpensesOfCategoryWithCode(expenseCode, pibCoFundedExpenses);
    }

    /**
     * **Locates** a unique co-funded expense record using a combination of identifying codes.
     * @param entityCode Unique identifier for the Entity.
     * @param serviceCode Unique identifier for the Service.
     * @param expenseCode Unique identifier for the Expense Category.
     * @return The matching {@link PublicInvestmentBudgetCoFundedExpense} object.
     */
    public static PublicInvestmentBudgetCoFundedExpense findPublicInvestmentBudgetCoFundedExpenseWithCodes(String entityCode, String serviceCode, String expenseCode) {
        return (PublicInvestmentBudgetCoFundedExpense) BudgetExpenseLogicService.findExpenseWithCode(entityCode, serviceCode, expenseCode, pibCoFundedExpenses);
    }

    /**
     * **Calculates** the total monetary sum of all entries in the **co-funded registry**.
     * @return The aggregated total sum as a long.
     */
    public static long calculateSum() {
        return BudgetExpenseLogicService.calculateSum(pibCoFundedExpenses);
    }

    /**
     * **Generates** a breakdown of total expenditures grouped by **Category** for the co-funded segment.
     * @return A {@link Map} with category codes as keys and their total sums as values.
     */
    public static Map<String, Long> getSumOfEveryPublicInvestmentCoFundedExpenseCategory() {
        return BudgetExpenseLogicService.getSumOfEveryExpenseCategory(pibCoFundedExpenses);
    }

    /**
     * **Retrieves** the textual description assigned to a specific budget code.
     * @param code The classification code to look up.
     * @return The description associated with the code.
     */
    public static String getDescriptionWithCode(String code) {
        return BudgetExpenseLogicService.getDescriptionWithCode(code, pibCoFundedExpenses);
    }

    /**
     * **Executes** mass financial changes on a **specific category**.
     * **Stores** a snapshot of the data in {@link ExpensesHistory} before applying adjustments.
     * @param code The category code to modify.
     * @param percentage The percentage increase or decrease.
     * @param fixedAmount The fixed monetary amount to add or subtract.
     */
    public static void implementGlobalChangesInCertainPublicInvestmentBudgetCoFundedCategory(String code, double percentage, long fixedAmount) {
        ExpensesHistory.keepHistory(getPublicInvestmentBudgetCoFundedExpensesOfCategoryWithCode(code), BudgetType.PUBLIC_INVESTMENT_BUDGET_COFUNDED);
        BudgetExpenseChangesService.implementGlobalChangesInCertainExpenseCategoryWithPercentageAllocation(code, percentage, fixedAmount, pibCoFundedExpenses);
    }

    /**
     * **Applies** mass financial changes to **all co-funded categories**.
     * **Archives** the current state of all entries in history prior to the update.
     * @param percentage The percentage adjustment for all entries.
     * @param fixedAmount The fixed amount adjustment for all entries.
     */
    public static void implementGlobalChangesInAllPublicInvestmentBudgetCoFundedCategories(double percentage, long fixedAmount) {
        ExpensesHistory.keepHistory(pibCoFundedExpenses, BudgetType.PUBLIC_INVESTMENT_BUDGET_COFUNDED);
        BudgetExpenseChangesService.implementGlobalChangesInAllExpenseCategoriesWithPercentageAllocation(percentage, fixedAmount, pibCoFundedExpenses);
    }

    /** Returns a string representation of the PIBCoFunded expenditure entry. */
    @Override
    public String toString() {
        return super.toString();
    }
}