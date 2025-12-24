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

    //Find Service Names
    String getRegularServiceNameWithCode(String serviceCode);
    String getPublicInvestmentNationalServiceNameWithCode(String serviceCode);
    String getPublicInvestmentCoFundedServiceNameWithCode(String serviceCode);

    List<String> getRegularServiceCodes();
    List<String> getPublicInvestmentNationalServiceCodes();
    List<String> getPublicInvestmentCoFundedServiceCodes();
}
