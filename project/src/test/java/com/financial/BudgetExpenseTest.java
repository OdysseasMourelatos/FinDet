package com.financial;

import com.financial.entries.BudgetExpense;
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

    @Test
    public void testGetSumOfEveryCategory() {
        new BudgetExpense("001", "Food", "ΕΞΟΔΑ", 100);
        new BudgetExpense("001", "Food", "ΕΞΟΔΑ", 50);

        var result = BudgetExpense.getSumOfEveryCategory();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(150, result.get(0).getAmount());
    }

    @Test
    public void testFindExpenseWithCode() {
        BudgetExpense e = new BudgetExpense("003", "Coffee", "ΕΞΟΔΑ", 20);

        BudgetExpense found = BudgetExpense.findExpenseWithCode("003");
        Assertions.assertEquals(e, found);
    }

    @Test
    public void testNoExpenseFound() {
        BudgetExpense found = BudgetExpense.findExpenseWithCode("999");
        Assertions.assertNull(found);
    }


}
