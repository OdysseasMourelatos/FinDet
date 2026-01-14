package com.financial.multi_year_analysis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MultiYearBudgetRevenueTest {

    @BeforeEach
    void setup() {
        MultiYearBudgetRevenue.getMultiYearBudgetRevenues().clear();

        new MultiYearBudgetRevenue("11", "Φόροι", "ΕΣΟΔΑ", 56597000000L, 2024);
        new MultiYearBudgetRevenue("12", "Κοινωνικές εισφορές", "ΕΣΟΔΑ", 56000000L, 2024);
        new MultiYearBudgetRevenue("13", "Μεταβιβάσεις", "ΕΣΟΔΑ", 7960000000L, 2024);
        new MultiYearBudgetRevenue("11", "Φόροι", "ΕΣΟΔΑ", 51579000000L, 2023);
        new MultiYearBudgetRevenue("12", "Κοινωνικές εισφορές", "ΕΣΟΔΑ", 55000000L, 2023);
        new MultiYearBudgetRevenue("13", "Μεταβιβάσεις", "ΕΣΟΔΑ", 7953000000L, 2023);
        new MultiYearBudgetRevenue("14", "Πωλήσεις αγαθών και υπηρεσιών", "ΕΣΟΔΑ", 2405000000L, 2023);
        new MultiYearBudgetRevenue("11", "Φόροι", "ΕΣΟΔΑ", 45654498000L, 2022);
        new MultiYearBudgetRevenue("12", "Κοινωνικές εισφορές", "ΕΣΟΔΑ", 55000000L, 2022);
    }

    @Test
    void testGetRevenuesByYear() {
        List<MultiYearBudgetRevenue> rev2023 = MultiYearBudgetRevenue.getMultiYearBudgetRevenuesOfSpecificYear(2023);
        assertEquals(4, rev2023.size());

        List<MultiYearBudgetRevenue> rev2022 = MultiYearBudgetRevenue.getMultiYearBudgetRevenuesOfSpecificYear(2022);
        assertEquals(2, rev2022.size());
    }

    @Test
    void testGetRevenuesByCode() {
        // Ο κωδικός "11" (Φόροι) υπάρχει και στα 3 έτη
        List<MultiYearBudgetRevenue> taxTimeline = MultiYearBudgetRevenue.getMultiYearBudgetRevenuesOfSpecificCode("11");
        assertEquals(3, taxTimeline.size());

        // Ο κωδικός "14" υπάρχει μόνο μία φορά (το 2023)
        List<MultiYearBudgetRevenue> sales = MultiYearBudgetRevenue.getMultiYearBudgetRevenuesOfSpecificCode("14");
        assertEquals(1, sales.size());
        assertEquals(2023, sales.get(0).getYear());
    }

    @Test
    void testSumOfSpecificYear() {
        // Για το 2024: 56.597.000.000 + 56.000.000 + 7.960.000.000
        long expectedSum2024 = 64613000000L;
        assertEquals(expectedSum2024, MultiYearBudgetRevenue.getSumOfSpecificYear(2024));
    }

    @Test
    void testSumOfAllYears() {
        Map<Integer, Long> summary = MultiYearBudgetRevenue.getSumOfAllYears();

        assertNotNull(summary);
        assertEquals(3, summary.size());
        assertTrue(summary.containsKey(2022));
        assertTrue(summary.containsKey(2023));
        assertTrue(summary.containsKey(2024));

        long expectedSum2022 = 45654498000L + 55000000L;
        assertEquals(expectedSum2022, summary.get(2022));
    }

    @Test
    void testEmptyResults() {
        assertEquals(0, MultiYearBudgetRevenue.getSumOfSpecificYear(2010));
        assertTrue(MultiYearBudgetRevenue.getMultiYearBudgetRevenuesOfSpecificYear(2010).isEmpty());
        assertTrue(MultiYearBudgetRevenue.getMultiYearBudgetRevenuesOfSpecificCode("99").isEmpty());
    }

    @Test
    void toStringTest() {
        MultiYearBudgetRevenue revenue = MultiYearBudgetRevenue.getMultiYearBudgetRevenues().get(0);

        // Έλεγχος αν η toString επιστρέφει μη κενό String (καλύπτει την @Override toString)
        assertNotNull(revenue.toString());
        assertFalse(revenue.toString().isEmpty());
    }
}
