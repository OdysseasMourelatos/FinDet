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

}
