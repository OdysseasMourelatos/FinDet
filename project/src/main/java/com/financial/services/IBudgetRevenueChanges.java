package com.financial.services;

public interface IBudgetRevenueChanges {

    //Implementation Methods
    void implementChangesOfEqualDistribution(long change);
    void implementChangesOfPercentageAdjustment(double percentage);

    //Distribution of changes
    void setAmountOfSuperCategories(long change);
    void setAmountOfAllSubCategoriesWithEqualDistribution(long change);
    void setAmountOfAllSubCategoriesWithPercentageAdjustment(double percentage);
}