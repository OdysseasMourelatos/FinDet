package com.financial.multi_year_analysis;

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
     @Test 
    void getMultiYearBudgetRevenuesOfSpecificYearTest() {
        var instanceList = new ArrayList<MultiYearBudgetRevenue>("11", "Φόροι", "ΕΣΟΔΑ", 45654498000L, 2022);
        instanceList.add("12", "Κοινωνικές εισφορές", "ΕΣΟΔΑ", 55000000L, 2022);
        int year = 2022;
        var checkList = getMultiYearBudgetRevenuesOfSpecificYear(2022);
        assertEquals(instanceList,checkList);
    }

    @Test 
    void getMultiYearBudgetRevenuesOfSpecificCodeTest() {
        var code = "13" ;
        var instanceList = new ArrayList<MultiYearBudgetRevenue>("13", "Μεταβιβάσεις", "ΕΣΟΔΑ", 7960000000L, 2024);
        instanceList.add("13", "Μεταβιβάσεις", "ΕΣΟΔΑ", 7953000000L, 2023);
        var checkList = MultiYearBudgetRevenue.getMultiYearBudgetRevenuesOfSpecificCode(code);
        assertEquals(instanceList,checkList);
    }

    @Test
    void getSumCheck() {
        int yr = 2022;
        int yrCheck = MultiYearBudgetRevenue.getSumOfSpecificYear(yr);
        assertEquals(45709498000L,yrCheck);
    }

    @Test
    void sumOfAllyrsCheck() {
        var result = MultiYearBudgetRevenue.getSumOfAllYears();
        assertEquals(3,result.size());
        assertEquals(64613000000L, result.get(2024));
        assertEquals(61992000000L, result.get(2023));
        assertEquals(45709498000L, result.get(2022));
    }

    @Test 
    void toStringCheck() {
        var mybr13 = MultiYearBudgetRevenue.getMultiYearBudgetRevenuesOfSpecificCode("13");
        String expectedString = mybr13.toString();
        assertTrue(output.contains("Code: 13"));
        assertTrue(output.contains("Description: Μεταβιβάσεις"));
    }
}

