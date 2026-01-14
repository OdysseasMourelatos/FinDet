package com.financial.entries;

import com.financial.services.expenses.BudgetExpenseLogicService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents an expenditure within the Public Investment Budget (PIB) framework.
 * <p>
 * This class extends {@link BudgetExpense} and serves as the parent for specialized
 * investment types such as National and Co-funded expenses. It provides
 * mechanisms for global instance tracking and hierarchical data aggregation.
 */
public class PublicInvestmentBudgetExpense extends BudgetExpense {
    private final String type;
    protected static ArrayList<PublicInvestmentBudgetExpense> publicInvestmentBudgetExpenses = new ArrayList<>();

    /**
     * Constructs a new Public Investment Budget (PIB) expense entry and
     * automatically registers it in the global PIB collection.
     * * @param entityCode      The unique identifier for the ministry or government entity.
     * @param entityName      The human-readable name of the ministry.
     * @param serviceCode     The specific identifier for the department or service.
     * @param serviceName     The name of the service associated with the expense.
     * @param code            The budget classification code.
     * @param description     Textual description of the expenditure.
     * @param type            The investment segment (e.g., "ΕΘΝΙΚΟ" or "ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ").
     * @param category        The broader classification category of the expense.
     * @param amount          The monetary value of the entry in long format (representing subunits).
     */
    public PublicInvestmentBudgetExpense(String entityCode, String entityName, String serviceCode, String serviceName, String code, String description, String type, String category, long amount) {
        super(entityCode, entityName, serviceCode, serviceName, code, description, category, amount);
        this.type = type;
        publicInvestmentBudgetExpenses.add(this);
    }

    /**
     * Retrieves the global registry of all PIB expenditure entries.
     * <p>
     * This list provides a centralized point of access for all {@code PublicInvestmentBudgetExpense}
     * instances created during the application session.
     * * @return An {@link ArrayList} containing all recorded PIB expense objects.
     */
    public static ArrayList<PublicInvestmentBudgetExpense> getAllPublicInvestmentBudgetExpenses() {
        return publicInvestmentBudgetExpenses;
    }

    /**
     * Aggregates expenditures across all PIB segments (National and Co-funded) by category.
     * <p>
     * This method performs a sophisticated merge of two source maps. It utilizes a
     * {@link LinkedHashMap} to maintain the order of categories. If a budget category
     * exists in both the National and Co-funded segments, the amounts are merged
     * using the {@link Long#sum} function.
     * * @return A consolidated {@link Map} where keys are category identifiers and
     * values are the total aggregated sums.
     */
    public static Map<String, Long> getSumOfEveryPublicInvestmentExpenseCategory() {
        Map<String, Long> nationalMap = PublicInvestmentBudgetNationalExpense.getSumOfEveryPublicInvestmentNationalExpenseCategory();
        Map<String, Long> coFundedMap = PublicInvestmentBudgetCoFundedExpense.getSumOfEveryPublicInvestmentCoFundedExpenseCategory();

        // Δημιουργούμε ένα νέο Map ξεκινώντας με τα δεδομένα του Εθνικού σκέλους
        Map<String, Long> combinedMap = new LinkedHashMap<>(nationalMap);

        // Προσθέτουμε τα δεδομένα του Συγχρηματοδοτούμενου
        coFundedMap.forEach((serviceCode, amount) ->
                combinedMap.merge(serviceCode, amount, Long::sum)
        );

        return combinedMap;
    }

    /**
     * Calculates the grand total of the entire Public Investment Budget.
     * <p>
     * This is computed by aggregating the totals from the National and Co-funded
     * sub-segments. The mathematical logic follows:
     * $$TotalSum_{PIB} = Sum_{National} + Sum_{CoFunded}$$
     * * @return The total sum of all PIB expenditures.
     */
    public static long calculateSum() {
        return PublicInvestmentBudgetNationalExpense.calculateSum() + PublicInvestmentBudgetCoFundedExpense.calculateSum();
    }

    /**
     * Looks up the description associated with a specific budget code within the PIB list.
     * <p>
     * This method delegates the search logic to {@link com.financial.services.expenses.BudgetExpenseLogicService}
     * to ensure consistent lookup behavior across the system.
     * * @param code The budget code to search for.
     * @return The descriptive string assigned to the provided code.
     */
    public static String getDescriptionWithCode(String code) {
        return BudgetExpenseLogicService.getDescriptionWithCode(code, publicInvestmentBudgetExpenses);
    }

    /**
     * Retrieves the specific investment segment type of the instance.
     * * @return A String representing the segment type (e.g., National or Co-funded).
     */
    public String getType() {
        return type;
    }

    /**
     * Returns a string representation of the PIB expenditure entry.
     * <p>
     * Extends the default {@link BudgetExpense#toString()} output by appending
     * the specific investment segment ("Σκέλος") information.
     * * @return A detailed string including core expense data and the investment type.
     */
    @Override
    public String toString() {
        return super.toString() + ", Σκέλος : " + type;
    }
}