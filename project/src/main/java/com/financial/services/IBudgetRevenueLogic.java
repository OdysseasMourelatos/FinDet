package com.financial.services;

import com.financial.entries.BudgetRevenue;

import java.util.ArrayList;

public interface IBudgetRevenueLogic {

    //Sum method

    long calculateSum();

    //Supercategories methods

    BudgetRevenue findSuperCategory();
    ArrayList<BudgetRevenue> getSuperCategories();
    void printSuperCategoriesTopDown();
    void printSuperCategoriesBottomsUp();

    //Subcategories methods

    ArrayList<BudgetRevenue> findAllSubCategories();
    void printAllSubCategories();
    ArrayList<BudgetRevenue> findNextLevelSubCategories();
    void printNextLevelSubCategories();
}
