package com.financial.services;

import com.financial.entries.BudgetRevenue;

import java.util.ArrayList;

public class BudgetRevenueChangesService {
    public static <T extends BudgetRevenue> void setAmountOfSuperCategories(ArrayList<T> superCategories, long change) {
        for (BudgetRevenue superCategory : superCategories) {
            superCategory.setAmount(superCategory.getAmount() + change);
        }
    }
}
