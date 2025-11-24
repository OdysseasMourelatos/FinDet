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

    @Test
    void testStaticListAddsEntriesAutomatically() {
        assertEquals(0, BudgetEntry.budgetEntries.size());

        new TestBudgetEntry("001", "A", "ΕΣΟΔΑ", 500);

        assertEquals(1, BudgetEntry.budgetEntries.size());
    }

    @Test
    void testSetAmountWorksForPositiveValues() {
        BudgetEntry entry = new TestBudgetEntry("001", "A", "ΕΣΟΔΑ", 1000);

        entry.setAmount(2000);

        assertEquals(2000, entry.getAmount());
    }

    @Test
    void testSetAmountDoesNotAcceptNegativeValues() {
        BudgetEntry entry = new TestBudgetEntry("001", "A", "ΕΣΟΔΑ", 1000);

        entry.setAmount(-50);

        // Πρέπει να ΜΗΝ αλλάξει
        assertEquals(1000, entry.getAmount());
    }
}
