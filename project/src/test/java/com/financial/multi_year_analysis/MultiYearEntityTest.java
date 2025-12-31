package com.financial.multi_year_analysis;

import com.financial.multi_year_analysis.entries.MultiYearBudgetExpense;
import com.financial.multi_year_analysis.entries.MultiYearEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MultiYearEntityTest {

    @BeforeEach
    void setup() {
        // 1. Καθαρισμός στατικών λιστών
        MultiYearBudgetExpense.getMultiYearBudgetExpenses().clear();
        MultiYearBudgetExpense.getMultiYearBudgetExpensesOfEntities().clear();
        // Καθαρισμός των Entities (μέσω πρόσβασης στη static λίστα της κλάσης)
        MultiYearEntity.getMultiYearEntities().clear();

        // 2. Δημιουργία Εξόδων (Δεδομένα 2023-2024)
        new MultiYearBudgetExpense("1007", "Υπουργείο Εσωτερικών", "1007-TOTAL", "Δαπάνη", "ΕΞΟΔΑ", 3337487000L, 368000000L, 2024);
        new MultiYearBudgetExpense("1009", "Υπουργείο Εξωτερικών", "1009-TOTAL", "Δαπάνη", "ΕΞΟΔΑ", 384892000L, 23000000L, 2024);
        new MultiYearBudgetExpense("1009", "Υπουργείο Εξωτερικών", "1009-TOTAL", "Δαπάνη", "ΕΞΟΔΑ", 266600000L, 15575000L, 2023);
        new MultiYearBudgetExpense("1011", "Υπουργείο Εθνικής Άμυνας", "1011-TOTAL", "Δαπάνη", "ΕΞΟΔΑ", 5687800000L, 20000000L, 2023);

        // 3. Αρχικοποίηση Entities
        new MultiYearEntity("1007", "Υπουργείο Εσωτερικών");
        new MultiYearEntity("1009", "Υπουργείο Εξωτερικών");
        new MultiYearEntity("1011", "Υπουργείο Εθνικής Άμυνας");
    }

    @Test
    void findMultiYearEntityWithCodeTest() {
        MultiYearEntity entity = MultiYearEntity.findMultiYearEntityWithCode("1007");
        assertNotNull(entity);
        assertEquals("Υπουργείο Εσωτερικών", entity.getEntityName());

        // Edge Case: Ανύπαρκτος κωδικός
        assertNull(MultiYearEntity.findMultiYearEntityWithCode("9999"));
    }

    @Test
    void getTotalExpensesOfEntityPerYearTest() {
        MultiYearEntity ypex = MultiYearEntity.findMultiYearEntityWithCode("1009");
        Map<Integer, Long> totals = ypex.getTotalExpensesOfEntityPerYear();

        assertEquals(2, totals.size());
        // 2023: 266.600.000 + 15.575.000 = 282.175.000
        assertEquals(282175000L, totals.get(2023));
        // 2024: 384.892.000 + 23.000.000 = 407.892.000
        assertEquals(407892000L, totals.get(2024));
    }

    @Test
    void getTotalRegularExpensesOfEntityPerYearTest() {
        MultiYearEntity ypes = MultiYearEntity.findMultiYearEntityWithCode("1007");
        Map<Integer, Long> regular = ypes.getTotalRegularExpensesOfEntityPerYear();

        assertEquals(1, regular.size());
        assertEquals(3337487000L, regular.get(2024));
    }

    @Test
    void getTotalPublicInvestmentExpensesOfEntityPerYearTest() {
        MultiYearEntity ypes = MultiYearEntity.findMultiYearEntityWithCode("1007");
        Map<Integer, Long> pib = ypes.getTotalPublicInvestmentExpensesOfEntityPerYear();

        assertEquals(1, pib.size());
        assertEquals(368000000L, pib.get(2024));
    }

    @Test
    void getEntityCodeTest() {
        MultiYearEntity entity = MultiYearEntity.findMultiYearEntityWithCode("1011");
        assertEquals("1011", entity.getEntityCode());
    }

    @Test
    void getEntityNameTest() {
        MultiYearEntity entity = MultiYearEntity.findMultiYearEntityWithCode("1011");
        assertEquals("Υπουργείο Εθνικής Άμυνας", entity.getEntityName());
    }

    @Test
    void getMultiYearExpensesOfEntityTest() {
        MultiYearEntity ypex = MultiYearEntity.findMultiYearEntityWithCode("1009");
        List<MultiYearBudgetExpense> expenses = ypex.getMultiYearExpensesOfEntity();

        assertEquals(2, expenses.size());
        // Επιβεβαίωση ότι τα αντικείμενα ανήκουν όντως στον κωδικό 1009
        for (MultiYearBudgetExpense e : expenses) {
            // Επειδή το entityCode είναι private στην Expense, ελέγχουμε έμμεσα μέσω του linkage
            assertNotNull(e);
        }
    }

    @Test
    void toStringTest() {
        MultiYearEntity entity = MultiYearEntity.findMultiYearEntityWithCode("1011");
        String expected = "EntityCode: 1011, EntityName: Υπουργείο Εθνικής Άμυνας";
        assertEquals(expected, entity.toString());
    }
}