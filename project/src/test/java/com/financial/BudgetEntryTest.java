package com.financial;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BudgetEntryTest {

    // Dummy subclass γιατί η BudgetEntry είναι abstract
    static class TestBudgetEntry extends BudgetEntry {
        public TestBudgetEntry(String code, String description, String category, long amount) {
            super(code, description, category, amount);
        }
    }

    @BeforeEach
    void setup() {
        // Καθαρίζουμε τη static λίστα κάθε φορά πριν από test
        BudgetEntry.budgetEntries.clear();
    }

    @Test
    void testConstructorStoresValues() {
        BudgetEntry entry = new TestBudgetEntry("111", "Test Description", "ΕΣΟΔΑ", 1000);

        assertEquals("111", entry.getCode());
        assertEquals("Test Description", entry.getDescription());
        assertEquals("ΕΣΟΔΑ", entry.getCategory());
        assertEquals(1000, entry.getAmount());
    }
}
