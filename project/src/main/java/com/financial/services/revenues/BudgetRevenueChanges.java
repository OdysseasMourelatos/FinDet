package com.financial.services.revenues;

public interface BudgetRevenueChanges {

    //Implementation Methods
    void implementChangesOfEqualDistribution(long change);
    void implementChangesOfPercentageAdjustment(double percentage);

    //Distribution of changes
    void setAmountOfSuperCategories(long change);
    void setAmountOfAllSubCategoriesWithEqualDistribution(long change);
    void setAmountOfAllSubCategoriesWithPercentageAdjustment(double percentage);

    //Update all filtered objects in superclass
    void updateAmountOfSuperClassFilteredObject(long change);

    //Keep accounts that will change for history
    void keepAccountsAndBudgetTypeBeforeChange();
}