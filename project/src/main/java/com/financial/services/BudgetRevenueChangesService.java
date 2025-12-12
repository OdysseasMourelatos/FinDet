package com.financial.services;

import com.financial.entries.BudgetRevenue;

import java.util.ArrayList;

import static com.financial.services.BudgetRevenueLogicService.findNextLevelSubCategories;

public class BudgetRevenueChangesService {
    public static <T extends BudgetRevenue> void setAmountOfSuperCategories(ArrayList<T> superCategories, long change) {
        for (BudgetRevenue superCategory : superCategories) {
            superCategory.setAmount(superCategory.getAmount() + change);
        }
    }

    public static <T extends BudgetRevenue> void setAmountOfNextLevelSubCategoriesWithEqualDistribution(T parent, ArrayList<T> revenues, long change) {
        ArrayList<BudgetRevenue> nextLevelSubCategories = findNextLevelSubCategories(parent, revenues);
        if (!nextLevelSubCategories.isEmpty()) {
            long changeOfCategory = change / nextLevelSubCategories.size();
            for (BudgetRevenue subCategory : nextLevelSubCategories) {
                subCategory.setAmount(subCategory.getAmount() + changeOfCategory);
            }
        }
    }
}
