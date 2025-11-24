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
}
