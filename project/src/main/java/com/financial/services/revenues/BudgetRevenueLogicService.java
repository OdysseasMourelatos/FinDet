package com.financial.services.revenues;

import com.financial.entries.BudgetRevenue;

import java.util.ArrayList;

public class BudgetRevenueLogicService {

    //Returns only revenues with 2 digits
    public static <T extends BudgetRevenue> ArrayList<T> getMainBudgetRevenues(ArrayList<T> budgetRevenues) {
        ArrayList<T> mainBudgetRevenues = new ArrayList<>();
        for (T budgetRevenue : budgetRevenues) {
            if (budgetRevenue.getCode().length() == 2) {
                mainBudgetRevenues.add(budgetRevenue);
            }
        }
        return mainBudgetRevenues;
    }

    //Finds revenue
    public static <T extends BudgetRevenue> T findRevenueWithCode(String code, ArrayList<T> revenues) {
        for (T revenue : revenues) {
            if (revenue.getCode().equals(code)) {
                return revenue;
            }
        }
        return null;
    }

    //Get certain revenues

    public static ArrayList<BudgetRevenue> getRevenuesStartingWithCode(String code, ArrayList<? extends BudgetRevenue> revenues) {
        ArrayList<BudgetRevenue> mainRevenues = new ArrayList<>();
        for (BudgetRevenue revenue : revenues) {
            if (revenue.getCode().startsWith(code)) {
                mainRevenues.add(revenue);
            }
        }
        return mainRevenues;
    }

    //Calculates sum based on the main codes (2-digits)
    public static long calculateSum(ArrayList<? extends BudgetRevenue> budgetRevenues) {
        long sum = 0;
        for (BudgetRevenue budgetRevenue : budgetRevenues) {
            if (budgetRevenue.getCode().length() == 2) {
                sum += budgetRevenue.getAmount();
            }
        }
        return sum;
    }

    //Finds super category based on the level of hierarchy of the currentRevenue
    //e.g. revenue with code 113 has revenue with code 11 as super category
    public static <T extends BudgetRevenue> T getAboveLevelSuperCategory(T currentRevenue, ArrayList<T> revenues) {
        int level = currentRevenue.getLevelOfHierarchy();
        String tempCode;

        switch (level) {
            case 2 -> tempCode = currentRevenue.getCode().substring(0, 2);
            case 3 -> tempCode = currentRevenue.getCode().substring(0, 3);
            case 4 -> tempCode = currentRevenue.getCode().substring(0, 5);
            case 5 -> tempCode = currentRevenue.getCode().substring(0, 7);
            default -> tempCode = "0";
        }

        return findRevenueWithCode(tempCode, revenues);
    }

    //Gets all super categories by using getAboveLevelSuperCategory as long as there is one
    public static <T extends BudgetRevenue> ArrayList<BudgetRevenue> getAllSuperCategories(T currentRevenue, ArrayList<T> revenues) {
        ArrayList<BudgetRevenue> superCategories = new ArrayList<>();
        BudgetRevenue superCategory = getAboveLevelSuperCategory(currentRevenue, revenues);
        while (superCategory != null) {
            superCategories.add(superCategory);
            superCategory = (T) getAboveLevelSuperCategory((T)superCategory, revenues);
        }
        return superCategories;
    }

    //Sub Categories

    //Finds only the next level subcategories, by knowing how many digits it's supposed to have
    public static <T extends BudgetRevenue> ArrayList<BudgetRevenue> getNextLevelSubCategories(T parent, ArrayList<T> revenues) {
        int level = parent.getLevelOfHierarchy();
        int subCategoryCodeLength;
        switch (level) {
            case 1 -> subCategoryCodeLength = 3;
            case 2 -> subCategoryCodeLength = 5;
            case 3 -> subCategoryCodeLength = 7;
            case 4 -> subCategoryCodeLength = 10;
            default -> subCategoryCodeLength = 0;
        }
        ArrayList<BudgetRevenue> subCategories = new ArrayList<>();
        for (T budgetRevenue : revenues) {
            if (budgetRevenue.getCode().startsWith(parent.getCode()) && (budgetRevenue.getCode().length() == subCategoryCodeLength)) {
                subCategories.add(budgetRevenue);
            }
        }
        return subCategories;
    }

    /*If the parent has its code starting with the same digits as its child,
    and it's not the same object, then it must be his child
    e.g. 11 (parent), 113 child, 11311 child*/

    public static <T extends BudgetRevenue> ArrayList<BudgetRevenue> getAllSubCategories(T parent, ArrayList<T> revenues) {
        ArrayList<BudgetRevenue> subCategories = new ArrayList<>();
        for (T budgetRevenue : revenues) {
            if (budgetRevenue.getCode().startsWith(parent.getCode()) && !(budgetRevenue.equals(parent))) {
                subCategories.add(budgetRevenue);
            }
        }
        return subCategories;
    }
}