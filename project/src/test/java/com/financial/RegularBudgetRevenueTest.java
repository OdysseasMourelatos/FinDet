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
    private RegularBudgetRevenue revenue12;
    private RegularBudgetRevenue revenue122;
    private RegularBudgetRevenue revenue12201;
    private RegularBudgetRevenue revenue1220101;
    private RegularBudgetRevenue revenue1220102;
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
        revenue12 = new RegularBudgetRevenue("12", "Κοινωνικές εισφορές", "ΕΣΟΔΑ", 60000000L);
        revenue122 = new RegularBudgetRevenue("122", "Λοιπές κοινωνικές εισφορές", "ΕΣΟΔΑ", 60000000L);
        revenue12201 = new RegularBudgetRevenue("12201", "Εισφορές εργαζομένων", "ΕΣΟΔΑ", 60000000L);
        revenue1220101 = new RegularBudgetRevenue("1220101", "Εισφορές εργαζομένων για συνταξιοδότηση από το Δημόσιο", "ΕΣΟΔΑ", 1000000L);
        revenue1220102 = new RegularBudgetRevenue("1220102", "Εισφορές εργαζομένων για παροχές υγείας από το Δημόσιο", "ΕΣΟΔΑ", 59000000L);
        revenue13 = new RegularBudgetRevenue("13", "Μεταβιβάσεις", "ΕΣΟΔΑ", 3906000000L);
        revenue131 = new RegularBudgetRevenue("131", "Τρέχουσες εγχώριες μεταβιβάσεις", "ΕΣΟΔΑ", 322000000L);
        revenue13108 = new RegularBudgetRevenue("13108", "Μεταβιβάσεις από λοιπά νομικά πρόσωπα", "ΕΣΟΔΑ", 322000000L);
        revenue132 = new RegularBudgetRevenue("132", "Τρέχουσες μεταβιβάσεις από οργανισμούς και κράτη-μέλη της Ε.Ε.", "ΕΣΟΔΑ", 15000000L);
    }

    @Test
    void getAllRegularBudgetRevenuesTest() {
        ArrayList<RegularBudgetRevenue> allRevenues = RegularBudgetRevenue.getAllRegularBudgetRevenues();
        assertEquals(12, allRevenues.size());
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

        //Only objects with 2-digit codes (11, 12 & 13)
        assertEquals(3, mainRevenues.size());

        // Checking the codes
        assertTrue(mainRevenues.contains(revenue11));
        assertTrue(mainRevenues.contains(revenue12));
        assertTrue(mainRevenues.contains(revenue13));
    }

    @Test
    void getRegularBudgetRevenuesStartingWithCode() {
        ArrayList<BudgetRevenue> revenues = RegularBudgetRevenue.getRegularBudgetRevenuesStartingWithCode("11");

        //Only objects with codes starting with 13
        assertEquals(3, revenues.size());

        // Checking the codes
        assertTrue(revenues.contains(revenue11));
        assertTrue(revenues.contains(revenue111));
        assertTrue(revenues.contains(revenue11101));
    }

    @Test
    void calculateSumTest() {
        // 62,055,000,000 (11) + 60,000,000 (12) + 3,906,000,000 (13)
        long expectedSum = 66021000000L;
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
    void getSuperCategoriesTest() {
        ArrayList<BudgetRevenue> superCategories = revenue11101.getSuperCategories();
        assertEquals(2, superCategories.size());
        //Checking if the list contains all super categories
        assertTrue(superCategories.contains(revenue11));
        assertTrue(superCategories.contains(revenue111));
    }

    @Test
    void findNextLevelSubCategoriesTest() {

        ArrayList<BudgetRevenue> subCategories = revenue13.findNextLevelSubCategories();

        //2 children in the next level - 131 & 132
        assertEquals(2, subCategories.size());
        assertTrue(subCategories.contains(revenue131));
        assertTrue(subCategories.contains(revenue132));
    }

    @Test
    void findAllSubCategoriesTest() {
        ArrayList<BudgetRevenue> allSubCategories = revenue13.findAllSubCategories();
        //3 children total - 131, 132 & 13108
        assertEquals(3, allSubCategories.size());
        assertTrue(allSubCategories.contains(revenue131));
        assertTrue(allSubCategories.contains(revenue13108));
        assertTrue(allSubCategories.contains(revenue132));
    }

    @Test
    void ImplementChangesOfPercentageAdjustmentTest1() {
        double percentage = 0.1; // +10% applied to 13 (Top Level)

        // Initial amounts
        long initial13 = revenue13.getAmount();         // 3906000000L
        long initial131 = revenue131.getAmount();       // 322000000L
        long initial132 = revenue132.getAmount();       // 15000000L
        long initial13108 = revenue13108.getAmount();   // 322000000L

        //Change of itself (13)
        long changeOnSelf = (long) (initial13 * percentage);

        //Implementation
        revenue13.implementChangesOfPercentageAdjustment(percentage);

        // 1- Checking the same account
        assertEquals(initial13 + changeOnSelf, revenue13.getAmount());

        // 2 - Checking the subcategories
        long expected131 = (long) (initial131 * (1 + percentage));
        long expected132 = (long) (initial132 * (1 + percentage));
        long expected13108 = (long) (initial13108 * (1 + percentage));

        assertEquals(expected131, revenue131.getAmount());
        assertEquals(expected132, revenue132.getAmount());
        assertEquals(expected13108, revenue13108.getAmount());

        // 3 - Checking if an object with no correlation has indeed not changed
        assertEquals(62055000000L, revenue11.getAmount());
    }
}