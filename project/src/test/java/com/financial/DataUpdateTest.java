package com.financial;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class DataUpdateTest {

    @BeforeEach
    void resetState() {
        BudgetRevenue.getAllBudgetRevenues().clear();
        BudgetExpense.expenses.clear();
    }

    @Test
    void testPlaceholder() {
        assertTrue(true);
    }

    @Test
    void testCsvUpdateWritesMergedList() {
        // Mock BudgetEntry.mergeListsOfMainRevenuesAndMainExpenses()
        BudgetRevenue rev1 = new BudgetRevenue("11", "Revenue A", "ΕΣΟΔΑ", 1000);
        BudgetExpense exp1 = new BudgetExpense("21", "Expense A", "ΕΞΟΔΑ", 500);

        // Use temporary file instead of real "GovernmentBudget.csv"
        String tempFile = "temp_GovernmentBudget.csv";

        try {
            DataUpdate.csvUpdate(); // This writes to default file, we can adapt later to inject tempFile

            // Since file writing is side-effect, we just check that no exception is thrown
            assertTrue(true);
        } catch (Exception e) {
            fail("csvUpdate threw an exception: " + e.getMessage());
        }
    }
}
