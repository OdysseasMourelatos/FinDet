package com.financial.services.revenues;

import com.financial.entries.BudgetRevenue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.financial.services.revenues.BudgetRevenueLogicService.getNextLevelSubCategories;

/**
 * Utility service class that handles modifications to budget revenue amounts.
 * <p>
 * This class provides methods for propagating amount changes upward to supercategories
 * or downward to subcategories using different distribution strategies (Equal Distribution
 * or Percentage Adjustment).
 */
public class BudgetRevenueChangesService {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private BudgetRevenueChangesService() {
        // utility class – no instances
    }

    /**
     * Updates the amounts of all provided supercategories by adding a fixed change value.
     * This is typically used for upward propagation of financial changes.
     *
     * @param <T>             the type of BudgetRevenue
     * @param superCategories the list of ancestor categories to update
     * @param change          the amount to add (can be negative for decreases)
     */
    public static <T extends BudgetRevenue> void setAmountOfSuperCategories(ArrayList<T> superCategories, long change) {
        for (BudgetRevenue superCategory : superCategories) {
            superCategory.setAmount(superCategory.getAmount() + change);
        }
    }

    /**
     * Distributes a fixed amount equally among the immediate (next-level) subcategories of a parent.
     *
     * @param <T>      the type of BudgetRevenue
     * @param parent   the parent category whose children will be updated
     * @param revenues the list of all available revenues
     * @param change   the total amount to be divided among the children
     */
    public static <T extends BudgetRevenue> void setAmountOfNextLevelSubCategoriesWithEqualDistribution(T parent, ArrayList<T> revenues, long change) {
        ArrayList<BudgetRevenue> nextLevelSubCategories = getNextLevelSubCategories(parent, revenues);
        if (!nextLevelSubCategories.isEmpty()) {
            long changeOfCategory = change / nextLevelSubCategories.size();
            for (BudgetRevenue subCategory : nextLevelSubCategories) {
                subCategory.setAmount(subCategory.getAmount() + changeOfCategory);
            }
        }
    }

    /**
     * Recursively distributes a fixed amount across all descendant levels of a parent category.
     * The change is divided equally among immediate children at each level.
     *
     * @param <T>      the type of BudgetRevenue
     * @param parent   the ancestor category starting the distribution
     * @param revenues the list of all available revenues
     * @param change   the total amount to distribute downwards
     */
    public static <T extends BudgetRevenue> void setAmountOfAllSubCategoriesWithEqualDistribution(T parent, ArrayList<T> revenues, long change) {
        ArrayList<BudgetRevenue> nextLevelSubCategories = getNextLevelSubCategories(parent, revenues);
        long changeOfSubCategory = change;

        try {
            // Map to store initial amounts to track the actual change applied per child
            Map<String, Long> map = new HashMap<>();
            for (BudgetRevenue subCategory : nextLevelSubCategories) {
                map.put(subCategory.getCode(), subCategory.getAmount());
            }

            // Apply change to the immediate next level
            setAmountOfNextLevelSubCategoriesWithEqualDistribution(parent, revenues, changeOfSubCategory);

            // Termination condition: leaf level reached (code length 10)
            if (parent.getCode().length() == 10) {
                return;
            }

            // Recursive call for each subcategory
            for (BudgetRevenue subCategory : nextLevelSubCategories) {
                // Calculate the specific change that was applied to this child
                changeOfSubCategory = (subCategory.getAmount() - map.get(subCategory.getCode()));

                // Recursively apply to deeper levels
                setAmountOfAllSubCategoriesWithEqualDistribution((T) subCategory, revenues, changeOfSubCategory);
            }
        } catch (RuntimeException e) {
            return;
        }
    }

    /**
     * Adjusts the amounts of immediate (next-level) subcategories by a specific percentage.
     *
     * @param <T>        the type of BudgetRevenue
     * @param parent     the parent category
     * @param revenues   the list of all available revenues
     * @param percentage the percentage to apply (e.g., 0.1 for +10%)
     */
    public static <T extends BudgetRevenue> void setAmountOfNextLevelSubCategoriesWithPercentageAdjustment(T parent, ArrayList<T> revenues, double percentage) {
        ArrayList<BudgetRevenue> nextLevelSubCategories = getNextLevelSubCategories(parent, revenues);
        if (!nextLevelSubCategories.isEmpty()) {
            for (BudgetRevenue subCategory : nextLevelSubCategories) {
                subCategory.setAmount((long) (subCategory.getAmount() * (1 + percentage)));
            }
        }
    }

    /**
     * Recursively adjusts the amounts of all descendant categories by a specific percentage.
     *
     * @param <T>        the type of BudgetRevenue
     * @param parent     the ancestor category
     * @param revenues   the list of all available revenues
     * @param percentage the percentage to apply across the sub-tree
     */
    public static <T extends BudgetRevenue> void setAmountOfAllSubCategoriesWithPercentageAdjustment(T parent, ArrayList<T> revenues, double percentage) {
        try {
            // Apply percentage adjustment to the immediate next level
            setAmountOfNextLevelSubCategoriesWithPercentageAdjustment(parent, revenues, percentage);

            // Termination condition: leaf level reached (code length 10)
            if (parent.getCode().length() == 10) {
                throw new RuntimeException();
            }

            // Recursive call for each subcategory
            ArrayList<BudgetRevenue> nextLevelSubCategories = getNextLevelSubCategories(parent, revenues);
            for (BudgetRevenue subCategory : nextLevelSubCategories) {
                setAmountOfAllSubCategoriesWithPercentageAdjustment((T) subCategory, revenues, percentage);
            }
        } catch (RuntimeException e) {
            return;
        }
    }

    /**
     * Helper method to round a financial amount to the nearest hundred.
     *
     * @param amount the raw amount to round
     * @return the rounded amount as a long
     */
    public static long roundToNearestHundred(long amount) {
        return Math.round(amount / 100.0) * 100;
    }
}