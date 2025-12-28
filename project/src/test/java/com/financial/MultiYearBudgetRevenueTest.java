package com.financial;

import com.financial.multi_year_analysis.entries.MultiYearBudgetRevenue;
import org.junit.jupiter.api.BeforeEach;

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
}
