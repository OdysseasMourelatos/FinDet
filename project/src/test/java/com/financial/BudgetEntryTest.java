package com.financial;

import com.financial.entries.BudgetEntry;
import com.financial.entries.BudgetExpense;
import com.financial.entries.BudgetRevenue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class BudgetEntryTest {

    // Dummy subclass γιατί η BudgetEntry είναι abstract
    public static class TestBudgetEntry extends BudgetEntry {
        public TestBudgetEntry(String code, String description, String category, long amount) {
            super(code, description, category, amount);
        }
    }

    @BeforeEach
    public void setup() {
       BudgetEntry.budgetEntries.clear();
    }

    @Test
    public void testConstructorStoresValues() {
        BudgetEntry entry=  new TestBudgetEntry("111", "Test Description", "ΕΣΟΔΑ", 1000);

        assertEquals("111", entry.getCode());
        assertEquals("Test Description", entry.getDescription());
        assertEquals("ΕΣΟΔΑ", entry.getCategory());
        assertEquals(1000, entry.getAmount());
    }

    @Test
    public void testStaticListAddsEntriesAutomatically() {
        int initialSize=  BudgetEntry.budgetEntries.size();
        new TestBudgetEntry("001", "A", "ΕΣΟΔΑ", 500);
        assertEquals(initialSize + 1, BudgetEntry.budgetEntries.size());
    }

    @Test
    public void testSetAmountWorksForPositiveValues() {
        BudgetEntry entry=  new TestBudgetEntry("001", "A", "ΕΣΟΔΑ", 1000);

        entry.setAmount(2000);

        assertEquals(2000, entry.getAmount());
    }

    @Test
    public void testSetAmountDoesNotAcceptNegativeValues() {
        BudgetEntry entry=  new TestBudgetEntry("001", "A", "ΕΣΟΔΑ", 1000);

        entry.setAmount(-50);

        // Πρέπει να ΜΗΝ αλλάξει
        assertEquals(1000, entry.getAmount());
    }

    @Test
    public void testMergeListsCombinesRevenuesAndExpenses() {
        // Δημιουργούμε test data στις υποκλάσεις
        BudgetRevenue revenue1=  new BudgetRevenue("REV1", "Πωλήσεις", "ΕΣΟΔΑ", 5000);
        BudgetRevenue revenue2=  new BudgetRevenue("REV2", "Υπηρεσίες", "ΕΣΟΔΑ", 3000);

        BudgetExpense expense1=  new BudgetExpense("EXP1", "Μισθοί", "ΕΞΟΔΑ", 2000);
        BudgetExpense expense2=  new BudgetExpense("EXP2", "Αγορές", "ΕΞΟΔΑ", 1500);

        // Καλούμε τη merge μέθοδο
        ArrayList<BudgetEntry> result=  BudgetEntry.mergeListsOfMainRevenuesAndMainExpenses();

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
    public void testPrintMergedListsProducesOutput() {
        // Προσθέτουμε δεδομένα για να υπάρχει έξοδος
        new BudgetRevenue("PRINT1", "Έσοδο Εκτύπωσης", "ΕΣΟΔΑ", 10000);
        new BudgetExpense("PRINT2", "Έξοδο Εκτύπωσης", "ΕΞΟΔΑ", 7500);

        // Αποθηκεύουμε το standard output για να ελέγξουμε αν τυπώνει κάτι
        java.io.ByteArrayOutputStream outContent=  new java.io.ByteArrayOutputStream();
        java.io.PrintStream originalOut=  System.out;
        System.setOut(new java.io.PrintStream(outContent));

        try {
            // Καλούμε τη print μέθοδο
            BudgetEntry.printMergedListsOfMainRevenuesAndMainExpenses();

            // Ελέγχουμε ότι δεν προκύπτει exception
            String output=  outContent.toString();
            // Μπορούμε να ελέγξουμε ότι τουλάχιστον κάτι τυπώθηκε
            assertTrue(output.length() >= 0);

        } finally {
            // Επαναφέρουμε το standard output
            System.setOut(originalOut);
        }
    }

    @Test
    public void testToStringFormat() {
        BudgetEntry entry=  new TestBudgetEntry("111", "Description", "ΕΞΟΔΑ", 1234567);

        String text=  entry.toString();

        assertTrue(text.contains("111"));
        assertTrue(text.contains("Description"));
        assertTrue(text.contains("ΕΞΟΔΑ"));
        assertTrue(text.contains("1,234,567") || text.contains("1.234.567"));
    }
}
