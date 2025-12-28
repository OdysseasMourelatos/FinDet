package com.financial.multi_year_analysis;

import com.financial.multi_year_analysis.entries.MultiYearBudgetExpense;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MultiYearBudgetExpenseTest {

    @BeforeEach
    void setupMultiYearExpenses() {
        // Καθαρισμός των στατικών λιστών για απομονωμένα tests
        MultiYearBudgetExpense.getMultiYearBudgetExpenses().clear();
        MultiYearBudgetExpense.getMultiYearBudgetExpensesOfEntities().clear();

        // Γενικές Κατηγορίες (Constructor 1)
        new MultiYearBudgetExpense("21", "Παροχές σε εργαζομένους", "ΕΞΟΔΑ", 14849625000L, 2024);
        new MultiYearBudgetExpense("22", "Κοινωνικές παροχές", "ΕΞΟΔΑ", 410540000L, 2024);
        new MultiYearBudgetExpense("21", "Παροχές σε εργαζομένους", "ΕΞΟΔΑ", 13795795000L, 2023);
        new MultiYearBudgetExpense("22", "Κοινωνικές παροχές", "ΕΞΟΔΑ", 397439000L, 2023);
        new MultiYearBudgetExpense("23", "Μεταβιβάσεις", "ΕΞΟΔΑ", 32475557000L, 2023);

        // Υπουργεία (Constructor 2)
        new MultiYearBudgetExpense("1007", "Υπουργείο Εσωτερικών", "1007-TOTAL", "Συνολική Δαπάνη ΥΠΕΣ", "ΕΞΟΔΑ", 3337487000L, 368000000L, 2024);
        new MultiYearBudgetExpense("1009", "Υπουργείο Εξωτερικών", "1009-TOTAL", "Συνολική Δαπάνη ΥΠΕΞ", "ΕΞΟΔΑ", 384892000L, 23000000L, 2024);
        new MultiYearBudgetExpense("1009", "Υπουργείο Εξωτερικών", "1009-TOTAL", "Συνολική Δαπάνη ΥΠΕΞ", "ΕΞΟΔΑ", 266600000L, 15575000L, 2023);
        new MultiYearBudgetExpense("1011", "Υπουργείο Εθνικής Άμυνας", "1011-TOTAL", "Συνολική Δαπάνη ΥΠΕΘΑ", "ΕΞΟΔΑ", 5687800000L, 20000000L, 2023);
        new MultiYearBudgetExpense("1015", "Υπουργείο Υγείας", "1015-TOTAL", "Συνολική Δαπάνη ΥΠ.ΥΓ.", "ΕΞΟΔΑ", 4776460000L, 425928000L, 2023);
    }

    @Test
    void getMultiYearBudgetExpensesTest() {
        // Περιμένουμε 5 γενικές κατηγορίες από τον 1ο constructor
        List<MultiYearBudgetExpense> allExpenses = MultiYearBudgetExpense.getMultiYearBudgetExpenses();
        assertEquals(5, allExpenses.size());
    }

    @Test
    void getMultiYearBudgetExpensesOfEntitiesTest() {
        // Περιμένουμε 5 γενικές κατηγορίες από τον 2ο constructor
        List<MultiYearBudgetExpense> allExpenses = MultiYearBudgetExpense.getMultiYearBudgetExpensesOfEntities();
        assertEquals(5, allExpenses.size());
    }

    @Test
    void constructorAndTotalAmountCalculationTest() {
        // Έλεγχος του ΥΠΕΣ 2024 (1007)
        List<MultiYearBudgetExpense> expenses = MultiYearBudgetExpense.getMultiYearExpensesOfEntityWithEntityCode("1007");
        MultiYearBudgetExpense ypes = expenses.get(0);

        // 3.337.487.000 + 368.000.000 = 3.705.487.000
        assertEquals(3705487000L, ypes.getAmount());
        assertEquals(3337487000L, ypes.getRegularAmount());
        assertEquals(368000000L, ypes.getPublicInvestmentAmount());
    }

    @Test
    void getMultiYearBudgetExpensesOfSpecificYearTest() {
        // Ζητάμε γενικές δαπάνες για το 2024
        List<MultiYearBudgetExpense> expenses2024 = MultiYearBudgetExpense.getMultiYearBudgetExpensesOfSpecificYear(2024);

        // Περιμένουμε 2 (Παροχές εργαζομένων και Κοινωνικές παροχές)
        assertEquals(2, expenses2024.size());
    }

    @Test
    void getMultiYearBudgetExpensesOfSpecificCodeTest() {
        // Ζητάμε έξοδα με κωδικό "21" (Παροχές σε εργαζομένους - υπάρχει το 2023 και το 2024)
        List<MultiYearBudgetExpense> code21Expenses = MultiYearBudgetExpense.getMultiYearBudgetExpensesOfSpecificCode("21");
        assertEquals(2, code21Expenses.size());
    }

    @Test
    void getMultiYearExpensesOfEntityWithEntityCodeTest() {
        // Αναζήτηση για το Υπουργείο Εξωτερικών (1009) που υπάρχει και τα δύο έτη
        List<MultiYearBudgetExpense> ypexExpenses = MultiYearBudgetExpense.getMultiYearExpensesOfEntityWithEntityCode("1009");

        assertEquals(2, ypexExpenses.size());
    }

    @Test
    void getSumOfSpecificYearTest() {
        // Υπολογισμός αθροίσματος γενικών κατηγοριών για το 2024
        // 14.849.625.000 + 410.540.000 = 15.260.165.000
        long sum2024 = MultiYearBudgetExpense.getSumOfSpecificYear(2024);
        assertEquals(15260165000L, sum2024);

        // Υπολογισμός αθροίσματος γενικών κατηγοριών για το 2023
        // 13.795.795.000 + 397.439.000 + 32.475.557.000 = 46.668.791.000
        long sum2023 = MultiYearBudgetExpense.getSumOfSpecificYear(2023);
        assertEquals(46668791000L, sum2023);
    }

    @Test
    void getSumOfAllYearsTest() {
        Map<Integer, Long> sums = MultiYearBudgetExpense.getSumOfAllYears();

        assertEquals(2, sums.size());
        assertTrue(sums.containsKey(2023));
        assertTrue(sums.containsKey(2024));

        assertEquals(15260165000L, sums.get(2024));
        assertEquals(46668791000L, sums.get(2023));
    }

    @Test
    void getDescriptionAndCategoryBranchCoverageTest() {
        MultiYearBudgetExpense multiYearBudgetExpense = new MultiYearBudgetExpense("99999", "TEST", "99", "ΤΕΣΤ ΔΑΠΑΝΕΣ", "ΕΞΟΔΑ", 0L, 0L, 2025);
        assertEquals(multiYearBudgetExpense.getDescription(), "ΤΕΣΤ ΔΑΠΑΝΕΣ");
        assertEquals(multiYearBudgetExpense.getCategory(), "ΕΞΟΔΑ");

    }

    @Test
    void toStringTest() {
        // Επιλέγουμε ένα αντικείμενο από τη λίστα
        MultiYearBudgetExpense expense = MultiYearBudgetExpense.getMultiYearBudgetExpenses().get(0);

        // Έλεγχος αν η toString επιστρέφει μη κενό String (καλύπτει την @Override toString)
        assertNotNull(expense.toString());
        assertFalse(expense.toString().isEmpty());
    }
}