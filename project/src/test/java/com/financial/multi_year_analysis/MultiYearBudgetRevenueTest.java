package com.financial.multi_year_analysis;

import com.financial.multi_year_analysis.entries.MultiYearBudgetRevenue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

import static com.financial.multi_year_analysis.entries.MultiYearBudgetRevenue.getMultiYearBudgetRevenuesOfSpecificYear;

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
    void getMultiYearBudgetRevenuesOfSpecificYearTest() {
        int year = 2022;
        List<MultiYearBudgetRevenue> checkList = MultiYearBudgetRevenue.getMultiYearBudgetRevenuesOfSpecificYear(year);

        // Επαλήθευση ότι για το 2022 επέστρεψε 2 αντικείμενα
        assertEquals(2, checkList.size());

        // Έλεγχος αν οι κωδικοί είναι οι αναμενόμενοι (11 και 12)
        assertTrue(checkList.stream().anyMatch(r -> r.getCode().equals("11")));
        assertTrue(checkList.stream().anyMatch(r -> r.getCode().equals("12")));
    }

    @Test
    void getMultiYearBudgetRevenuesOfSpecificCodeTest() {
        String code = "13";
        List<MultiYearBudgetRevenue> checkList = MultiYearBudgetRevenue.getMultiYearBudgetRevenuesOfSpecificCode(code);

        // Επαλήθευση ότι ο κωδικός 13 υπάρχει σε 2 έτη (2024, 2023)
        assertEquals(2, checkList.size());
        assertEquals("13", checkList.get(0).getCode());
        assertEquals("13", checkList.get(1).getCode());
    }

    @Test
    void getSumCheck() {
        int yr = 2022;
        long yrCheck = MultiYearBudgetRevenue.getSumOfSpecificYear(yr);

        // 45.654.498.000 + 55.000.000 = 45.709.498.000
        assertEquals(45709498000L, yrCheck);
    }

    @Test
    void sumOfAllYearsCheck() {
        Map<Integer, Long> result = MultiYearBudgetRevenue.getSumOfAllYears();

        // Πρέπει να έχουμε δεδομένα για 3 έτη
        assertEquals(3, result.size());

        // Έλεγχος αθροισμάτων ανά έτος
        assertEquals(64613000000L, result.get(2024)); // 56.597M + 56M + 7.960M
        assertEquals(61992000000L, result.get(2023)); // 51.579M + 55M + 7.953M + 2.405M
        assertEquals(45709498000L, result.get(2022));
    }

    @Test
    void toStringCheck() {
        List<MultiYearBudgetRevenue> mybr13 = MultiYearBudgetRevenue.getMultiYearBudgetRevenuesOfSpecificCode("13");
        assertFalse(mybr13.isEmpty());

        String output = mybr13.get(0).toString();

        // Έλεγχος αν το toString περιέχει τις βασικές πληροφορίες
        assertTrue(output.contains("13"));
        assertTrue(output.contains("Μεταβιβάσεις"));
    }
}

