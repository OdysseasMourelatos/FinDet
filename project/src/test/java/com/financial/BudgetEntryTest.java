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

    @Test
    void testMergeListsCombinesRevenuesAndExpenses() {
        // Δημιουργούμε test data στις υποκλάσεις
        BudgetRevenue revenue1 = new BudgetRevenue("REV1", "Πωλήσεις", "ΕΣΟΔΑ", 5000);
        BudgetRevenue revenue2 = new BudgetRevenue("REV2", "Υπηρεσίες", "ΕΣΟΔΑ", 3000);

        BudgetExpense expense1 = new BudgetExpense("EXP1", "Μισθοί", "ΕΞΟΔΑ", 2000);
        BudgetExpense expense2 = new BudgetExpense("EXP2", "Αγορές", "ΕΞΟΔΑ", 1500);

        // Καλούμε τη merge μέθοδο
        ArrayList<BudgetEntry> result = BudgetEntry.mergeListsOfMainRevenuesAndMainExpenses();

        // Ελέγχουμε ότι η λίστα δεν είναι κενή και περιέχει στοιχεία
        assertNotNull(result);
        assertFalse(result.isEmpty());

        // Ελέγχουμε ότι όλα τα στοιχεία είναι BudgetEntry
        for (BudgetEntry entry : result) {
            assertTrue(entry instanceof BudgetEntry);
            assertNotNull(entry.getCode());
            assertNotNull(entry.getDescription());
            assertTrue(entry.getAmount() > 0);
        }
    }

    @Test
    void testToStringFormat() {
        BudgetEntry entry = new TestBudgetEntry("111", "Description", "ΕΞΟΔΑ", 1234567);

        String text = entry.toString();

        assertTrue(text.contains("111"));
        assertTrue(text.contains("Description"));
        assertTrue(text.contains("ΕΞΟΔΑ"));
        assertTrue(text.contains("1,234,567") || text.contains("1.234.567"));
    }
}
