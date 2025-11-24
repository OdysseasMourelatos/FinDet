package com.financial;

import org.junit.jupiter.api.*;
import java.util.*;

public class BudgetRevenueTest {

    @BeforeEach
    public void reset() {
        BudgetRevenue.budgetRevenues.clear();
    }

    @Test
    public void testPlaceholder() {
        Assertions.assertTrue(true);
    }

    @Test
    public void testAddRevenue() {
        new BudgetRevenue("11", "Main Category", "ΕΣΟΔΑ", 1000);

        Assertions.assertEquals(1, BudgetRevenue.getAllBudgetRevenues().size());
    }

    @Test
    public void testGetAllBudgetRevenues() {
        new BudgetRevenue("11", "Main", "ΕΣΟΔΑ", 1000);
        new BudgetRevenue("111", "Sub", "ΕΣΟΔΑ", 200);

        List<BudgetRevenue> all = BudgetRevenue.getAllBudgetRevenues();
        Assertions.assertEquals(2, all.size());
    }

    @Test
    public void testHierarchyLevel() {
        BudgetRevenue r1 = new BudgetRevenue("11", "Top", "ΕΣΟΔΑ", 1000);
        BudgetRevenue r2 = new BudgetRevenue("111", "Second", "ΕΣΟΔΑ", 500);
        BudgetRevenue r3 = new BudgetRevenue("11101", "Third", "ΕΣΟΔΑ", 300);

        Assertions.assertEquals(1, r1.getLevelOfHierarchy());
        Assertions.assertEquals(2, r2.getLevelOfHierarchy());
        Assertions.assertEquals(3, r3.getLevelOfHierarchy());
    }

    @Test
    public void testFindSuperCategory() {
        BudgetRevenue top = new BudgetRevenue("11", "Top", "ΕΣΟΔΑ", 1000);
        BudgetRevenue sub = new BudgetRevenue("111", "Sub", "ΕΣΟΔΑ", 200);

        Assertions.assertEquals(top, sub.findSuperCategory());
    }

    @Test
    public void testFindAllSubCategories() {
        BudgetRevenue main = new BudgetRevenue("11", "Main", "ΕΣΟΔΑ", 1000);
        BudgetRevenue sub1 = new BudgetRevenue("111", "Sub1", "ΕΣΟΔΑ", 300);
        BudgetRevenue sub2 = new BudgetRevenue("112", "Sub2", "ΕΣΟΔΑ", 200);

        List<BudgetRevenue> subs = main.findAllSubCategories();
        Assertions.assertEquals(2, subs.size());
    }

    @Test
    public void testFindNextLevelSubCategories() {
        BudgetRevenue main = new BudgetRevenue("11", "Main", "ΕΣΟΔΑ", 1000);
        BudgetRevenue sub = new BudgetRevenue("111", "Sub1", "ΕΣΟΔΑ", 300);
        BudgetRevenue deeper = new BudgetRevenue("11101", "Deep", "ΕΣΟΔΑ", 200);

        List<BudgetRevenue> nextLevel = main.findNextLevelSubCategories();
        Assertions.assertEquals(1, nextLevel.size());
        Assertions.assertEquals(sub, nextLevel.get(0));
    }
    @Test
    public void testEqualDistributionNextLevel() {
        BudgetRevenue main = new BudgetRevenue("11", "Main", "ΕΣΟΔΑ", 1000);
        BudgetRevenue sub1 = new BudgetRevenue("111", "A", "ΕΣΟΔΑ", 100);
        BudgetRevenue sub2 = new BudgetRevenue("112", "B", "ΕΣΟΔΑ", 100);

        main.setAmountOfNextLevelSubCategoriesWithEqualDistribution(200);

        Assertions.assertEquals(200, sub1.getAmount());
        Assertions.assertEquals(200, sub2.getAmount());
    }

    @Test
    public void testPercentageAdjustment() {
        BudgetRevenue main = new BudgetRevenue("11", "Main", "ΕΣΟΔΑ", 1000);
        BudgetRevenue sub1 = new BudgetRevenue("111", "A", "ΕΣΟΔΑ", 100);

        main.setAmountOfNextLevelSubCategoriesWithPercentageAdjustment(0.10);

        Assertions.assertEquals(110, sub1.getAmount());
    }

}
