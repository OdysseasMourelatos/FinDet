package com.financial.services.expenses;

import java.util.ArrayList;
import java.util.List;
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
}
