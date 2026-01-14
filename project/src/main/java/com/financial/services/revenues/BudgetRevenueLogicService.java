package com.financial.services.revenues;

import com.financial.entries.BudgetRevenue;
import java.util.ArrayList;

/**
 * Utility service class that provides logic for managing and navigating
 * the hierarchical structure of budget revenues.
 * <p>
 * This class includes methods for filtering main categories, searching by code,
 * and traversing parent-child relationships within revenue lists.
 */
public class BudgetRevenueLogicService {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private BudgetRevenueLogicService() {
        // utility class – no instances
    }

    /**
     * Filters and returns only the top-level (main) budget revenues.
     * Main revenues are identified by having a code length of exactly 2 digits.
     *
     * @param <T>            the type of BudgetRevenue
     * @param budgetRevenues the list of revenues to filter
     * @return an ArrayList containing only the top-level revenues
     */
    public static <T extends BudgetRevenue> ArrayList<T> getMainBudgetRevenues(ArrayList<T> budgetRevenues) {
        ArrayList<T> mainBudgetRevenues = new ArrayList<>();
        for (T budgetRevenue : budgetRevenues) {
            if (budgetRevenue.getCode().length() == 2) {
                mainBudgetRevenues.add(budgetRevenue);
            }
        }
        return mainBudgetRevenues;
    }

    /**
     * Searches for a specific revenue object within a list based on its unique code.
     *
     * @param <T>      the type of BudgetRevenue
     * @param code     the unique string code to search for
     * @param revenues the list of revenues to search in
     * @return the matching revenue object, or {@code null} if no match is found
     */
    public static <T extends BudgetRevenue> T findRevenueWithCode(String code, ArrayList<T> revenues) {
        for (T revenue : revenues) {
            if (revenue.getCode().equals(code)) {
                return revenue;
            }
        }
        return null;
    }

    /**
     * Retrieves all revenues whose codes start with the specified prefix.
     *
     * @param code     the prefix to match
     * @param revenues the list of revenues to filter
     * @return an ArrayList of revenues that start with the given code
     */
    public static ArrayList<BudgetRevenue> getRevenuesStartingWithCode(String code, ArrayList<? extends BudgetRevenue> revenues) {
        ArrayList<BudgetRevenue> mainRevenues = new ArrayList<>();
        for (BudgetRevenue revenue : revenues) {
            if (revenue.getCode().startsWith(code)) {
                mainRevenues.add(revenue);
            }
        }
        return mainRevenues;
    }

    /**
     * Calculates the total sum of all top-level (2-digit code) budget revenues.
     *
     * @param budgetRevenues the list of revenues to summarize
     * @return the total sum of amounts for main budget categories
     */
    public static long calculateSum(ArrayList<? extends BudgetRevenue> budgetRevenues) {
        long sum = 0;
        for (BudgetRevenue budgetRevenue : budgetRevenues) {
            if (budgetRevenue.getCode().length() == 2) {
                sum += budgetRevenue.getAmount();
            }
        }
        return sum;
    }

    /**
     * Identifies the immediate parent (supercategory) of the given revenue.
     * The parent is found by truncating the current code based on the hierarchy level.
     *
     * @param <T>            the type of BudgetRevenue
     * @param currentRevenue the revenue whose parent is sought
     * @param revenues       the list of available revenues to search within
     * @return the immediate parent revenue object, or {@code null} if none exists
     */
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

    /**
     * Retrieves the full chain of ancestors (supercategories) for a given revenue.
     *
     * @param <T>            the type of BudgetRevenue
     * @param currentRevenue the revenue to find ancestors for
     * @param revenues       the list of available revenues
     * @return an ArrayList of all parent revenues, from immediate parent up to the root
     */
    public static <T extends BudgetRevenue> ArrayList<BudgetRevenue> getAllSuperCategories(T currentRevenue, ArrayList<T> revenues) {
        ArrayList<BudgetRevenue> superCategories = new ArrayList<>();
        T superCategory = getAboveLevelSuperCategory(currentRevenue, revenues);
        while (superCategory != null) {
            superCategories.add(superCategory);
            superCategory = getAboveLevelSuperCategory(superCategory, revenues);
        }
        return superCategories;
    }

    /**
     * Finds the immediate children (subcategories) of a given parent revenue.
     * Immediate children are identified by their specific code length relative to the parent.
     *
     * @param <T>      the type of BudgetRevenue
     * @param parent   the parent revenue
     * @param revenues the list of revenues to search in
     * @return an ArrayList of immediate subcategories
     */
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

    /**
     * Retrieves all descendants (subcategories) of a parent revenue across all levels.
     *
     * @param <T>      the type of BudgetRevenue
     * @param parent   the ancestor revenue
     * @param revenues the list of revenues to search in
     * @return an ArrayList containing all subcategories that belong to the parent's hierarchy
     */
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