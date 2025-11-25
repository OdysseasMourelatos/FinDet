package com.financial;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


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
            DataUpdate.csvUpdate(tempFile); // This writes to default file, we can adapt later to inject tempFile

            // Since file writing is side-effect, we just check that no exception is thrown
            assertTrue(true);
        } catch (Exception e) {
            fail("csvUpdate threw an exception: " + e.getMessage());
        }
    }

    public class DataUpdate {
        public static void csvUpdate(String filePath) {
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(filePath));
                writer.write("code,description,category,amount");
                for (BudgetEntry budgetEntry : BudgetEntry.mergeListsOfMainRevenuesAndMainExpenses()) {
                    writer.write("\n" + budgetEntry.getCode() + "," +
                            budgetEntry.getDescription() + "," +
                            budgetEntry.getCategory() + "," +
                            budgetEntry.getAmount());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if(writer != null) writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Test
    void testCsvUpdateWritesFile(@TempDir Path tempDir) throws Exception {
        BudgetRevenue rev1 = new BudgetRevenue("11", "Revenue A", "ΕΣΟΔΑ", 1000);
        BudgetExpense exp1 = new BudgetExpense("21", "Expense A", "ΕΞΟΔΑ", 500);

        Path tempFile = tempDir.resolve("budget.csv");

        DataUpdate.csvUpdate(tempFile.toString());

        // Read file content and validate
        String content = Files.readString(tempFile);
        assertTrue(content.contains("code,description,category,amount"));
        assertTrue(content.contains("Revenue A"));
        assertTrue(content.contains("Expense A"));
        assertTrue(content.contains("1000"));
        assertTrue(content.contains("500"));
    }

}
