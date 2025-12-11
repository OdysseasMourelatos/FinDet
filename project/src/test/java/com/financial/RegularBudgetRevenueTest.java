package com.financial;

import com.financial.entries.BudgetRevenue;
import com.financial.entries.RegularBudgetRevenue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class RegularBudgetRevenueTest {

    private RegularBudgetRevenue revenue11;
    private RegularBudgetRevenue revenue111;
    private RegularBudgetRevenue revenue11101;
    private RegularBudgetRevenue revenue13;
    private RegularBudgetRevenue revenue131;
    private RegularBudgetRevenue revenue13108;
    private RegularBudgetRevenue revenue132;

    @BeforeEach
    void setUp() {
        RegularBudgetRevenue.getAllRegularBudgetRevenues().clear();
        revenue11 = new RegularBudgetRevenue("11", "Φόροι", "ΕΣΟΔΑ", 62055000000L);
        revenue111 = new RegularBudgetRevenue("111", "Φόροι επί αγαθών και υπηρεσιών", "ΕΣΟΔΑ", 33667000000L);
        revenue11101 = new RegularBudgetRevenue("11101", "Φόροι προστιθέμενης αξίας που εισπράττονται μέσω Δ.Ο.Υ", "ΕΣΟΔΑ", 14635000000L);
        revenue13 = new RegularBudgetRevenue("13", "Μεταβιβάσεις", "ΕΣΟΔΑ", 3906000000L);
        revenue131 = new RegularBudgetRevenue("131", "Τρέχουσες εγχώριες μεταβιβάσεις", "ΕΣΟΔΑ", 322000000L);
        revenue13108 = new RegularBudgetRevenue("13108", "Μεταβιβάσεις από λοιπά νομικά πρόσωπα", "ΕΣΟΔΑ", 322000000L);
        revenue132 = new RegularBudgetRevenue("132", "Τρέχουσες μεταβιβάσεις από οργανισμούς και κράτη-μέλη της Ε.Ε.", "ΕΣΟΔΑ", 15000000L);
    }

    @Test
    void getAllRegularBudgetRevenuesTest() {
        ArrayList<RegularBudgetRevenue> allRevenues = RegularBudgetRevenue.getAllRegularBudgetRevenues();
        assertEquals(7, allRevenues.size());
        assertTrue(allRevenues.get(0) instanceof RegularBudgetRevenue);
    }

    @Test
    void findRegularBudgetRevenueWithCodeTest() {

        //For object that exists
        RegularBudgetRevenue found = RegularBudgetRevenue.findRegularBudgetRevenueWithCode("13");
        assertNotNull(found);
        assertEquals(revenue13.getAmount(), found.getAmount());

        // For object that does not exist
        assertNull(RegularBudgetRevenue.findRegularBudgetRevenueWithCode("999"));
    }

    @Test
    void getMainRegularBudgetRevenuesTest() {

        ArrayList<RegularBudgetRevenue> mainRevenues = RegularBudgetRevenue.getMainRegularBudgetRevenues();

        //Only objects with 2-digit codes (11 & 13)
        assertEquals(2, mainRevenues.size());

        // Checking the codes
        assertEquals("11", mainRevenues.get(0).getCode());
        assertEquals("13", mainRevenues.get(1).getCode());
    }

    @Test
    void calculateSumTest() {
        // 62,055,000,000 (11) + 3,906,000,000 (13)
        long expectedSum = 65961000000L;
        assertEquals(expectedSum, revenue11.calculateSum());
    }

    @Test
    void findSuperCategoryTest() {
        RegularBudgetRevenue parent = revenue11101.findSuperCategory();
        assertNotNull(parent);
        assertEquals("111", parent.getCode());
        assertEquals(33667000000L, parent.getAmount());
    }

    @Test
    void findNextLevelSubCategoriesTest() {

        ArrayList<BudgetRevenue> subCategories = revenue13.findNextLevelSubCategories();

        //2 children in the next level - 131 & 132
        assertEquals(2, subCategories.size());
        assertTrue(subCategories.contains(revenue131));
        assertTrue(subCategories.contains(revenue132));
    }
}