package com.financial;

import org.junit.jupiter.api.*;

public class BudgetExpenseTest {

    @BeforeEach
    public void reset() {
        BudgetExpense.expenses.clear();
    }

    @Test
    public void testPlaceholder() {
        Assertions.assertTrue(true);
    }

    @Test
    public void testAddExpense() {
        new BudgetExpense("001", "Food", "ΕΞΟΔΑ", 100);
        Assertions.assertEquals(1, BudgetExpense.expenses.size());
    }

    @Test
    public void testCalculateSum() {
        new BudgetExpense("001", "Food", "ΕΞΟΔΑ", 100);
        new BudgetExpense("002", "Transport", "ΕΞΟΔΑ", 50);

        long sum = BudgetExpense.calculateSum();
        Assertions.assertEquals(150, sum);
    }
}
