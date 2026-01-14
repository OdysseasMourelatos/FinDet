package com.financial.services.expenses;

import com.financial.entries.BudgetExpense;

import java.util.*;
import java.util.stream.Stream;

/**
 * Defines the comprehensive financial logic contract for an organizational entity.
 * <p>
 * This interface provides a blueprint for aggregating, filtering, and calculating expenses
 * across different budget types (Regular, National PIB, and Co-funded PIB). It includes
 * extensive default methods to automatically compute composite totals and merged lists
 * from the individual budget segments.
 * </p>
 */
public interface EntityLogic {

    /* --- Total Sum Calculations --- */

    /** Calculates the total sum of expenses from the Regular Budget. */
    long calculateRegularSum();

    /** Calculates the total sum of expenses from the National part of the Public Investment Budget. */
    long calculatePublicInvestmentNationalSum();

    /** Calculates the total sum of expenses from the Co-funded part of the Public Investment Budget. */
    long calculatePublicInvestmentCoFundedSum();

    /** Calculates the combined sum of all Public Investment Budget (PIB) resources. */
    default long calculatePublicInvestmentSum() {
        return calculatePublicInvestmentCoFundedSum() + calculatePublicInvestmentNationalSum();
    }

    /** Calculates the grand total of all expenses from all budget sources. */
    default long calculateTotalSum() {
        return calculateRegularSum() + calculatePublicInvestmentSum();
    }

    /* --- Service Name Retrieval --- */

    /** Retrieves the name of a regular service by its code. */
    String getRegularServiceNameWithCode(String serviceCode);

    /** Retrieves the name of a national PIB service by its code. */
    String getPublicInvestmentNationalServiceNameWithCode(String serviceCode);

    /** Retrieves the name of a co-funded PIB service by its code. */
    String getPublicInvestmentCoFundedServiceNameWithCode(String serviceCode);

    /** Retrieves a service name from any PIB source (National or Co-funded). */
    default String getPublicInvestmentServiceNameWithCode(String serviceCode) {
        if (getPublicInvestmentCoFundedServiceNameWithCode(serviceCode) != null) {
            return getPublicInvestmentCoFundedServiceNameWithCode(serviceCode);
        }
        return getPublicInvestmentNationalServiceNameWithCode(serviceCode);
    }

    /** Retrieves a service name by searching through all available budget types. */
    default String getServiceNameWithCode(String serviceCode) {
        if (getPublicInvestmentServiceNameWithCode(serviceCode) != null) {
            return getPublicInvestmentServiceNameWithCode(serviceCode);
        }
        return getRegularServiceNameWithCode(serviceCode);
    }

    /* --- Service Code Management --- */

    /** Lists all unique service codes within the Regular Budget. */
    List<String> getAllRegularServiceCodes();

    /** Lists all unique service codes within the National PIB. */
    List<String> getAllPublicInvestmentNationalServiceCodes();

    /** Lists all unique service codes within the Co-funded PIB. */
    List<String> getAllPublicInvestmentCoFundedServiceCodes();

    /** Merges and returns all unique service codes from both PIB sectors. */
    default List<String> getAllPublicInvestmentServiceCodes() {
        List<String> national = getAllPublicInvestmentNationalServiceCodes();
        List<String> coFunded = getAllPublicInvestmentCoFundedServiceCodes();
        return Stream.concat(national.stream(), coFunded.stream()).distinct().sorted().toList();
    }

    /** Merges and returns all unique service codes across the entire entity. */
    default List<String> getAllServiceCodes() {
        List<String> regular = getAllRegularServiceCodes();
        List<String> publicInvestment = getAllPublicInvestmentServiceCodes();
        return Stream.concat(regular.stream(), publicInvestment.stream()).distinct().sorted().toList();
    }

    /* --- Service-Specific Data --- */

    /** Calculates the regular budget sum for a specific service. */
    long getRegularSumOfServiceWithCode(String serviceCode);

    /** Calculates the national PIB sum for a specific service. */
    long getPublicInvestmentNationalSumOfServiceWithCode(String serviceCode);

    /** Calculates the co-funded PIB sum for a specific service. */
    long getPublicInvestmentCoFundedSumOfServiceWithCode(String serviceCode);

    /** Returns the combined PIB sum for a specific service. */
    default long getPublicInvestmentSumOfServiceWithCode(String serviceCode) {
        return getPublicInvestmentNationalSumOfServiceWithCode(serviceCode) + getPublicInvestmentCoFundedSumOfServiceWithCode(serviceCode);
    }

    /** Returns the grand total sum (Regular + PIB) for a specific service. */
    default long getTotalSumOfServiceWithCode(String serviceCode) {
        return getPublicInvestmentSumOfServiceWithCode(serviceCode) + getRegularSumOfServiceWithCode(serviceCode);
    }

    /* --- Expense Collection Methods --- */

    /** Fetches a list of regular budget expenses for a given service. */
    ArrayList<BudgetExpense> getRegularExpensesOfServiceWithCode(String serviceCode);

    /** Fetches a list of national PIB expenses for a given service. */
    ArrayList<BudgetExpense> getPublicInvestmentNationalExpensesOfServiceWithCode(String serviceCode);

    /** Fetches a list of co-funded PIB expenses for a given service. */
    ArrayList<BudgetExpense> getPublicInvestmentCoFundedExpensesOfServiceWithCode(String serviceCode);

    /** Aggregates and sorts all PIB-related expenses for a specific service. */
    default ArrayList<BudgetExpense> getPublicInvestmentExpensesOfServiceWithCode(String serviceCode) {
        ArrayList<BudgetExpense> expenses = new ArrayList<>();
        if (getPublicInvestmentNationalExpensesOfServiceWithCode(serviceCode) != null) {
            expenses.addAll(getPublicInvestmentNationalExpensesOfServiceWithCode(serviceCode));
        }
        if (getPublicInvestmentCoFundedExpensesOfServiceWithCode(serviceCode) != null) {
            expenses.addAll(getPublicInvestmentCoFundedExpensesOfServiceWithCode(serviceCode));
        }
        expenses.sort(Comparator.comparing(BudgetExpense::getCode));
        return expenses;
    }

    /** Aggregates and sorts all expenses from all sources for a specific service. */
    default ArrayList<BudgetExpense> getBudgetExpensesOfServiceWithCode(String serviceCode) {
        ArrayList<BudgetExpense> expenses = new ArrayList<>();
        if (getRegularExpensesOfServiceWithCode(serviceCode) != null) {
            expenses.addAll(getRegularExpensesOfServiceWithCode(serviceCode));
        }
        if (getPublicInvestmentExpensesOfServiceWithCode(serviceCode) != null) {
            expenses.addAll(getPublicInvestmentExpensesOfServiceWithCode(serviceCode));
        }
        expenses.sort(Comparator.comparing(BudgetExpense::getCode));
        return expenses;
    }

    /* --- Bulk Aggregation (Maps) --- */

    /** Generates a map of service codes and their regular budget totals. */
    Map<String, Long> getRegularSumOfEveryService();

    /** Generates a map of service codes and their national PIB totals. */
    Map<String, Long> getPublicInvestmentNationalSumOfEveryService();

    /** Generates a map of service codes and their co-funded PIB totals. */
    Map<String, Long> getPublicInvestmentCoFundedSumOfEveryService();

    /** Creates a merged map of all service totals within the Public Investment Budget. */
    default Map<String, Long> getPublicInvestmentSumOfEveryService() {
        Map<String, Long> nationalMap = getPublicInvestmentNationalSumOfEveryService();
        Map<String, Long> coFundedMap = getPublicInvestmentCoFundedSumOfEveryService();

        // Δημιουργούμε ένα νέο Map ξεκινώντας με τα δεδομένα του Εθνικού σκέλους
        Map<String, Long> combinedMap = new LinkedHashMap<>(nationalMap);

        // Προσθέτουμε τα δεδομένα του Συγχρηματοδοτούμενου
        coFundedMap.forEach((serviceCode, amount) ->
                combinedMap.merge(serviceCode, amount, Long::sum)
        );

        return combinedMap;
    }

    /** Creates a grand total map of every service total across all budget types. */
    default Map<String, Long> getTotalSumOfEveryService() {
        Map<String, Long> regularMap = getPublicInvestmentSumOfEveryService();
        Map<String, Long> pibMap = getRegularSumOfEveryService();

        // Δημιουργούμε ένα νέο Map ξεκινώντας με τα δεδομένα του Τακτικού
        Map<String, Long> combinedMap = new LinkedHashMap<>(regularMap);

        // Προσθέτουμε τα δεδομένα του ΠΔΕ
        pibMap.forEach((serviceCode, amount) ->
                combinedMap.merge(serviceCode, amount, Long::sum)
        );

        return combinedMap;
    }

    /* --- Category-Specific Aggregation --- */

    /** Returns the regular budget sum for a specific expense category. */
    long getRegularSumOfExpenseCategoryWithCode(String code);

    /** Returns the national PIB sum for a specific expense category. */
    long getPublicInvestmentNationalSumOfExpenseCategoryWithCode(String code);

    /** Returns the co-funded PIB sum for a specific expense category. */
    long getPublicInvestmentCoFundedSumOfExpenseCategoryWithCode(String code);

    /** Returns the merged PIB sum for a specific expense category. */
    default long getPublicInvestmentSumOfExpenseCategoryWithCode(String code) {
        return getPublicInvestmentNationalSumOfExpenseCategoryWithCode(code) + getPublicInvestmentCoFundedSumOfExpenseCategoryWithCode(code);
    }

    /** Returns the grand total sum for a specific expense category across all budgets. */
    default long getTotalSumOfExpenseCategoryWithCode(String code) {
        return getRegularSumOfExpenseCategoryWithCode(code) + getPublicInvestmentSumOfExpenseCategoryWithCode(code);
    }

    /* --- Bulk Category Mapping --- */

    /** Map of category codes to regular budget sums. */
    Map<String, Long> getRegularSumOfEveryExpenseCategory();

    /** Map of category codes to national PIB sums. */
    Map<String, Long> getPublicInvestmentNationalSumOfEveryExpenseCategory();

    /** Map of category codes to co-funded PIB sums. */
    Map<String, Long> getPublicInvestmentCoFundedSumOfEveryExpenseCategory();

    /** Merged map of category codes to total PIB sums. */
    default Map<String, Long> getPublicInvestmentSumOfEveryExpenseCategory() {
        Map<String, Long> nationalMap = getPublicInvestmentNationalSumOfEveryExpenseCategory();
        Map<String, Long> coFundedMap = getPublicInvestmentCoFundedSumOfEveryExpenseCategory();

        // Χρήση LinkedHashMap για διατήρηση της ταξινόμησης
        Map<String, Long> combinedMap = new LinkedHashMap<>(nationalMap);
        coFundedMap.forEach((code, amount) ->
                combinedMap.merge(code, amount, Long::sum)
        );
        return combinedMap;
    }

    /** Merged map of category codes to grand total sums across all budgets. */
    default Map<String, Long> getTotalSumOfEveryExpenseCategory() {
        Map<String, Long> regularMap = getRegularSumOfEveryExpenseCategory();
        Map<String, Long> pibMap = getPublicInvestmentSumOfEveryExpenseCategory();

        Map<String, Long> combinedMap = new LinkedHashMap<>(regularMap);
        pibMap.forEach((code, amount) ->
                combinedMap.merge(code, amount, Long::sum)
        );
        return combinedMap;
    }
}
