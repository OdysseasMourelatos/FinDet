package com.financial.services.revenues;

import com.financial.entries.BudgetRevenue;
import java.util.ArrayList;

/**
 * Interface defining the essential logic for hierarchical navigation within budget revenues.
 * <p>
 * Classes implementing this interface must provide functionality to traverse
 * both upwards (supercategories) and downwards (subcategories) in the financial structure.
 */
public interface BudgetRevenueLogic {

    /**
     * Retrieves the immediate parent category of the current revenue entry.
     *
     * @return the {@link BudgetRevenue} object representing the direct ancestor,
     * or {@code null} if the entry is at the root level.
     */
    BudgetRevenue getAboveLevelSuperCategory();

    /**
     * Retrieves all ancestor categories of the current revenue entry, moving up to the root.
     *
     * @return an {@link ArrayList} containing all parent categories in hierarchical order.
     */
    ArrayList<BudgetRevenue> getAllSuperCategories();

    /**
     * Retrieves the immediate children categories of the current revenue entry.
     *
     * @return an {@link ArrayList} containing the direct descendants (next level) of this entry.
     */
    ArrayList<BudgetRevenue> getNextLevelSubCategories();

    /**
     * Retrieves all descendant categories of the current revenue entry across all sub-levels.
     *
     * @return an {@link ArrayList} containing all subcategories within this entry's sub-tree.
     */
    ArrayList<BudgetRevenue> getAllSubCategories();
}