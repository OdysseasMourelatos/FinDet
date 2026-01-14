package com.financial.entries;

import com.financial.services.expenses.BudgetExpenseLogicService;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a specific expense entry within the financial system.
 * <p>
 * This class extends {@link BudgetEntry} and adds organizational context,
 * specifically linking expenses to unique Entities and Services. It provides
 * static methods to aggregate data from both Regular and Public Investment Budgets.
 * </p>
 */
public class BudgetExpense extends BudgetEntry {

    /** The unique identifier for the government or organizational entity */
    private final String entityCode;

    /** The descriptive name of the entity */
    private final String entityName;

    /** The unique identifier for the specific service within the entity */
    private final String serviceCode;

    /** The name of the service associated with this expense */
    private final String serviceName;

    /** Global list maintaining all instantiated budget expenses in memory */
    protected static ArrayList<BudgetExpense> budgetExpenses = new ArrayList<>();

    /**
     * Constructs a new BudgetExpense with full organizational and financial details.
     *
     * @param entityCode   The code of the entity.
     * @param entityName   The name of the entity.
     * @param serviceCode  The code of the service.
     * @param serviceName  The name of the service.
     * @param code         The specific expense/account code.
     * @param description  A brief description of the expense.
     * @param category     The category of the expense (e.g., 21, 24).
     * @param amount       The monetary value of the expense.
     */
    public BudgetExpense(String entityCode, String entityName, String serviceCode, String serviceName, String code, String description, String category, long amount) {
        super(code, description, category, amount);
        this.entityCode = entityCode;
        this.entityName = entityName;
        this.serviceCode = serviceCode;
        this.serviceName = serviceName;
        budgetExpenses.add(this);
    }

    /**
     * Aggregates and returns the sum of all expense categories, combining
     * Regular Budget and Public Investment Budget (PIB) data.
     *
     * @return A {@link Map} where the key is the category code and the value is the total sum.
     */
    public static Map<String, Long> getSumOfEveryBudgetExpenseCategory() {
        Map<String, Long> regularMap = RegularBudgetExpense.getSumOfEveryRegularExpenseCategory();
        Map<String, Long> pibMap = PublicInvestmentBudgetExpense.getSumOfEveryPublicInvestmentExpenseCategory();

        // Create a new map starting with Regular Budget data
        Map<String, Long> combinedMap = new LinkedHashMap<>(regularMap);

        // Merge PIB data into the combined map
        pibMap.forEach((serviceCode, amount) ->
                combinedMap.merge(serviceCode, amount, Long::sum)
        );

        return combinedMap;
    }

    /**
     * Calculates the total sum of all expenses across all budget types.
     *
     * @return The total combined sum as a {@code long}.
     */
    public static long calculateSum() {
        return RegularBudgetExpense.calculateSum() + PublicInvestmentBudgetExpense.calculateSum();
    }

    /**
     * Retrieves the description for a given expense code by delegating
     * the search to the logic service.
     *
     * @param code The expense code to search for.
     * @return The description of the code, or an empty string/null if not found.
     */
    public static String getDescriptionWithCode(String code) {
        return BudgetExpenseLogicService.getDescriptionWithCode(code, budgetExpenses);
    }

    /** @return The entity code of this expense */
    public String getEntityCode() {
        return entityCode;
    }

    /** @return The name of the entity associated with this expense */
    public String getEntityName() {
        return entityName;
    }

    /** @return The service code associated with this expense */
    public String getServiceCode() {
        return serviceCode;
    }

    /** @return The name of the service associated with this expense */
    public String getServiceName() {
        return serviceName;
    }

    /** @return The static list of all registered budget expenses */
    public static ArrayList<BudgetExpense> getBudgetExpenses() {
        return budgetExpenses;
    }

    /**
     * Compares this expense with another object for equality.
     * Two expenses are considered equal if they share the same Entity Code,
     * Service Code, and Expense Code (Composite Key).
     *
     * @param o The object to compare with.
     * @return {@code true} if the objects represent the same expense record.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BudgetExpense that = (BudgetExpense) o;
        return Objects.equals(getEntityCode(), that.getEntityCode()) &&
                Objects.equals(getServiceCode(), that.getServiceCode()) &&
                Objects.equals(getCode(), that.getCode());
    }

    /**
     * Generates a hash code based on the unique composite key of the expense.
     * * @return The hash code value.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getClass(), getEntityCode(), getServiceCode(), getCode());
    }

    /**
     * Returns a formatted string representation of the budget expense,
     * including organizational details and the formatted amount.
     *
     * @return A string containing all field values.
     */
    @Override
    public String toString() {
        return "Entity Code: " + getEntityCode() +
                ", Entity Name: " + entityName +
                ", Service Code: " + serviceCode +
                ", Service Name: " + serviceName +
                ", Expense Code: " + getCode() +
                ", Description: " + getDescription() +
                ", Category: " + getCategory() +
                ", Amount: " + String.format("%,d", getAmount());
    }
}
