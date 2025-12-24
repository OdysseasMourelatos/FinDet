package com.financial.services.expenses;

import java.util.List;

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
