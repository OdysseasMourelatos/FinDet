package com.financial.services.expenses;

import com.financial.entries.BudgetExpense;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    //Get sum of expenses of specific service
    Map<String, Long> getRegularSumOfEveryService();
    Map<String, Long> getPublicInvestmentNationalSumOfEveryService();
    Map<String, Long> getPublicInvestmentCoFundedSumOfEveryService();

    default Map<String, Long> getPublicInvestmentSumOfEveryService() {
        Map<String, Long> nationalMap = getPublicInvestmentNationalSumOfEveryService();
        Map<String, Long> coFundedMap = getPublicInvestmentCoFundedSumOfEveryService();

        // Δημιουργούμε ένα νέο Map ξεκινώντας με τα δεδομένα του Εθνικού σκέλους
        Map<String, Long> combinedMap = new HashMap<>(nationalMap);

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
        Map<String, Long> combinedMap = new HashMap<>(regularMap);

        // Προσθέτουμε τα δεδομένα του ΠΔΕ
        pibMap.forEach((serviceCode, amount) ->
                combinedMap.merge(serviceCode, amount, Long::sum)
        );

        return combinedMap;
    }
}
