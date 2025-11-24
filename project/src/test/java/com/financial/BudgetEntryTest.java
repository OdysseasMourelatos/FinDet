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
}
