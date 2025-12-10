package com.financial.services;

import com.financial.entries.BudgetRevenue;

import java.util.ArrayList;

public class BudgetRevenueHandling {

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
    public static <T extends BudgetRevenue> T findSuperCategory(T currentRevenue, ArrayList<T> revenues) {
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

    //Gets all super categories by using findSuperCategory as long as there is one
    public static <T extends BudgetRevenue> ArrayList<BudgetRevenue> getSuperCategories(T currentRevenue, ArrayList<T> revenues) {
        ArrayList<BudgetRevenue> superCategories = new ArrayList<>();
        BudgetRevenue superCategory = findSuperCategory(currentRevenue, revenues);
        while (superCategory != null) {
            superCategories.add(superCategory);
            superCategory = (T) findSuperCategory((T)superCategory, revenues);
        }
        return superCategories;
    }

    //Sub Categories

    /*If the parent has its code starting with the same digits as its child,
    and it's not the same object, then it must be his child
    e.g. 11 (parent), 113 child, 11311 child*/

    public static <T extends BudgetRevenue> ArrayList<T> findAllSubCategories(T parent, ArrayList<T> revenues) {
        ArrayList<T> subCategories = new ArrayList<>();
        for (T budgetRevenue : revenues) {
            if (budgetRevenue.getCode().startsWith(parent.getCode()) && !(budgetRevenue.equals(parent))) {
                subCategories.add(budgetRevenue);
            }
        }
        return subCategories;
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

    //Returns only revenues with 2 digits
    public static ArrayList<BudgetRevenue> getMainBudgetRevenues(ArrayList<? extends BudgetRevenue> budgetRevenues) {
        ArrayList<BudgetRevenue> mainBudgetRevenues = new ArrayList<>();
        for (BudgetRevenue budgetRevenue : budgetRevenues) {
            if (budgetRevenue.getCode().length() == 2) {
                mainBudgetRevenues.add(budgetRevenue);
            }
        }
        return mainBudgetRevenues;
    }

    public static void printMainBudgetRevenues(ArrayList<? extends BudgetRevenue> budgetRevenues) {
        DataOutput.printRevenueWithAsciiTable(getMainBudgetRevenues(budgetRevenues), calculateSum(budgetRevenues));
    }
}