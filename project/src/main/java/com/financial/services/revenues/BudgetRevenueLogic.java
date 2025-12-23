package com.financial.services.revenues;

import com.financial.entries.BudgetRevenue;

import java.util.ArrayList;

public interface BudgetRevenueLogic {

    //Supercategories methods

    BudgetRevenue getAboveLevelSuperCategory();
    ArrayList<BudgetRevenue> getAllSuperCategories();

    //Subcategories methods

    ArrayList<BudgetRevenue> getNextLevelSubCategories();
    ArrayList<BudgetRevenue> getAllSubCategories();
}
