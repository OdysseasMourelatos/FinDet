package com.financial.services.expenses;

import com.financial.entries.BudgetExpense;

import java.util.*;
import java.util.stream.Stream;

public interface EntityLogic {

    //Total Sums of Entity
    long calculateRegularSum();
    long calculatePublicInvestmentNationalSum();
    long calculatePublicInvestmentCoFundedSum();
    default long calculatePublicInvestmentSum() {
        return calculatePublicInvestmentCoFundedSum() + calculatePublicInvestmentNationalSum();
    }
    default long calculateTotalSum() {
        return calculateRegularSum() + calculatePublicInvestmentSum();
    }

    //Get Service Names
    String getRegularServiceNameWithCode(String serviceCode);
    String getPublicInvestmentNationalServiceNameWithCode(String serviceCode);
    String getPublicInvestmentCoFundedServiceNameWithCode(String serviceCode);

    //Get all service codes
    List<String> getAllRegularServiceCodes();
    List<String> getAllPublicInvestmentNationalServiceCodes();
    List<String> getAllPublicInvestmentCoFundedServiceCodes();

    default List<String> getAllPublicInvestmentServiceCodes() {
        List<String> national = getAllPublicInvestmentNationalServiceCodes();
        List<String> coFunded = getAllPublicInvestmentCoFundedServiceCodes();
        return Stream.concat(national.stream(), coFunded.stream()).distinct().sorted().toList();
    }

    default List<String> getAllServiceCodes() {
        List<String> regular = getAllRegularServiceCodes();
        List<String> publicInvestment = getAllPublicInvestmentServiceCodes();
        return Stream.concat(regular.stream(), publicInvestment.stream()).distinct().sorted().toList();
    }

    //Get sums of specific service inside an entity
    long getRegularSumOfServiceWithCode(String serviceCode);
    long getPublicInvestmentNationalSumOfServiceWithCode(String serviceCode);
    long getPublicInvestmentCoFundedSumOfServiceWithCode(String serviceCode);

    default long getPublicInvestmentSumOfServiceWithCode(String serviceCode) {
        return getPublicInvestmentNationalSumOfServiceWithCode(serviceCode) + getPublicInvestmentCoFundedSumOfServiceWithCode(serviceCode);
    }

    default long getTotalSumOfServiceWithCode(String serviceCode) {
        return getPublicInvestmentSumOfServiceWithCode(serviceCode) + getRegularSumOfServiceWithCode(serviceCode);
    }

    //Get expenses of specific service
    ArrayList<BudgetExpense> getRegularExpensesOfServiceWithCode(String serviceCode);
    ArrayList<BudgetExpense> getPublicInvestmentNationalExpensesOfServiceWithCode(String serviceCode);
    ArrayList<BudgetExpense> getPublicInvestmentCoFundedExpensesOfServiceWithCode(String serviceCode);

    //Get sum of expenses of every service
    Map<String, Long> getRegularSumOfEveryService();
    Map<String, Long> getPublicInvestmentNationalSumOfEveryService();
    Map<String, Long> getPublicInvestmentCoFundedSumOfEveryService();

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

    //Get sums of specific expense category
    long getRegularSumOfExpenseCategoryWithCode(String code);
    long getPublicInvestmentNationalSumOfExpenseCategoryWithCode(String code);
    long getPublicInvestmentCoFundedSumOfExpenseCategoryWithCode(String code);

    default long getPublicInvestmentSumOfExpenseCategoryWithCode(String code) {
        return getPublicInvestmentNationalSumOfExpenseCategoryWithCode(code) + getPublicInvestmentCoFundedSumOfExpenseCategoryWithCode(code);
    }

    default long getTotalSumOfExpenseCategoryWithCode(String code) {
        return getRegularSumOfExpenseCategoryWithCode(code) + getPublicInvestmentSumOfExpenseCategoryWithCode(code);
    }

    //Get sum of expenses of every expense category
    Map<String, Long> getRegularSumOfEveryExpenseCategory();
    Map<String, Long> getPublicInvestmentNationalSumOfEveryExpenseCategory();
    Map<String, Long> getPublicInvestmentCoFundedSumOfEveryExpenseCategory();

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
