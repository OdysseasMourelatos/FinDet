package com.financial;

import com.financial.entries.BudgetRevenue;
import com.financial.entries.RegularBudgetRevenue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static com.financial.entries.RegularBudgetRevenue.calculateSum;
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

    private BudgetRevenue budget11;
    private BudgetRevenue budget111;
    private BudgetRevenue budget11101;

    @BeforeEach
    void setUp() {
        BudgetRevenue.getAllBudgetRevenues().clear();
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

        budget11 = BudgetRevenue.findBudgetRevenueWithCode("11");
        budget111 = BudgetRevenue.findBudgetRevenueWithCode("111");
        budget11101 = BudgetRevenue.findBudgetRevenueWithCode("11101");
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
        assertEquals(expectedSum, calculateSum());
    }

    @Test
    void getAboveLevelSuperCategoryTest() {
        RegularBudgetRevenue parent = revenue11101.getAboveLevelSuperCategory();
        assertNotNull(parent);
        assertEquals("111", parent.getCode());
        assertEquals(33667000000L, parent.getAmount());
    }

    @Test
    void getAllSuperCategoriesTest() {
        ArrayList<BudgetRevenue> superCategories = revenue11101.getAllSuperCategories();
        assertEquals(2, superCategories.size());
        //Checking if the list contains all super categories
        assertTrue(superCategories.contains(revenue11));
        assertTrue(superCategories.contains(revenue111));
    }

    @Test
    void getNextLevelSubCategoriesTest() {

        ArrayList<BudgetRevenue> subCategories = revenue13.getNextLevelSubCategories();

        //2 children in the next level - 131 & 132
        assertEquals(2, subCategories.size());
        assertTrue(subCategories.contains(revenue131));
        assertTrue(subCategories.contains(revenue132));
    }

    @Test
    void getAllSubCategoriesTest() {
        ArrayList<BudgetRevenue> allSubCategories = revenue13.getAllSubCategories();
        //3 children total - 131, 132 & 13108
        assertEquals(3, allSubCategories.size());
        assertTrue(allSubCategories.contains(revenue131));
        assertTrue(allSubCategories.contains(revenue13108));
        assertTrue(allSubCategories.contains(revenue132));
    }

    @Test
    void implementChangesOfPercentageAdjustmentTest1() {
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

    @Test
    void implementChangesOfPercentageAdjustmentTest2() {
        double percentage = 0.1; // +10% applied to 131 (Middle Level)

        // Initial amounts
        long initial13 = revenue13.getAmount();
        long initial131 = revenue131.getAmount();
        long initial132 = revenue132.getAmount();
        long initial13108 = revenue13108.getAmount();

        //Change of itself (13)
        long changeOnSelf = (long) (initial131 * percentage);

        //Implementation
        revenue131.implementChangesOfPercentageAdjustment(percentage);

        // 1- Checking the same account
        assertEquals(initial131 + changeOnSelf, revenue131.getAmount());

        // 2. Checking the supercategories
        assertEquals(initial13 + changeOnSelf, revenue13.getAmount());

        // 3 - Checking the subcategories
        long expected13108 = (long) (initial13108 * (1 + percentage));
        assertEquals(expected13108, revenue13108.getAmount());

        // 4 - Checking if objects with no correlation have indeed not changed
        assertEquals(initial132, revenue132.getAmount());
        assertEquals(62055000000L, revenue11.getAmount());
    }

    @Test
    void implementChangesOfEqualDistributionTest1() {
        long change = 10000000L; // +10 Million applied to 12

        // Initial amounts
        long initial12 = revenue12.getAmount();
        long initial122 = revenue122.getAmount();
        long initial12201 = revenue12201.getAmount();
        long initial1220101 = revenue1220101.getAmount();
        long initial1220102 = revenue1220102.getAmount();

        // Implementation
        revenue12.implementChangesOfEqualDistribution(change);

        // 1- Checking the same account
        assertEquals(initial12 + change, revenue12.getAmount());

        // 2 - Checking the subcategories
        // 12 -> 122 (1 child: 10M / 1 = 10M)
        long changeLevel1 = 10000000L;
        assertEquals(initial122 + changeLevel1, revenue122.getAmount());

        // 122 -> 12201 (1 child: 10M / 1 = 10M)
        long changeLevel2 = 10000000L;
        assertEquals(initial12201 + changeLevel2, revenue12201.getAmount());

        // 12201 -> (1220101 & 1220102). (Change: 10M / 2 children = 5M each)
        long changeLevel3 = 5000000L;
        assertEquals(initial1220101 + changeLevel3, revenue1220101.getAmount());
        assertEquals(initial1220102 + changeLevel3, revenue1220102.getAmount());

        // 3 - Checking if ab object with no correlation has indeed not changed
        assertEquals(62055000000L, revenue11.getAmount());
    }

    @Test
    void implementChangesOfEqualDistributionTest2() {
        long change = 4000000L; // +4 Million applied to 12201

        // Initial amounts (Supercategories for checking upward propagation)
        long initial12 = revenue12.getAmount();
        long initial122 = revenue122.getAmount();
        long initial12201 = revenue12201.getAmount();

        // Initial amounts (Subcategories for checking downward propagation)
        long initial1220101 = revenue1220101.getAmount();
        long initial1220102 = revenue1220102.getAmount();

        // Implementation
        revenue12201.implementChangesOfEqualDistribution(change);

        // 1. ΕΛΕΓΧΟΣ: SuperCategories (12 and 122 must increase by 4M)
        assertEquals(initial12 + change, revenue12.getAmount());
        assertEquals(initial122 + change, revenue122.getAmount());

        // 2 - Checking the same account
        assertEquals(initial12201 + change, revenue12201.getAmount());

        // 3 - Checking the subcategories (Change: 4M / 2 children = 2M each)
        long changeToBranch = 2000000L;

        // 1220101 and 1220102 receive 2M each
        assertEquals(initial1220101 + changeToBranch, revenue1220101.getAmount());
        assertEquals(initial1220102 + changeToBranch, revenue1220102.getAmount());

        // 4 - Checking if an object with no correlation has indeed not changed
        assertEquals(62055000000L, revenue11.getAmount());
    }

    @Test
    void updateAmountOfSuperClassFilteredObjectsTest1() {
        long change = 1000000L; // 1 million change in regular revenue 11

        // Initial amounts of filtered objects
        long initialBR11Amount = budget11.getRegularAmount();
        long initialBR111Amount = budget111.getRegularAmount();
        long initialBR11101Amount = budget11101.getRegularAmount();

        // Implementation
        revenue11.implementChangesOfEqualDistribution(change);

        // Checking Filtered List Of SuperClass

        // BR 11
        assertEquals(initialBR11Amount + change, budget11.getRegularAmount());
        assertEquals(initialBR11Amount + change + budget11.getPublicInvestmentAmount(), budget11.getAmount());

        // BR 111
        assertEquals(initialBR111Amount + change, budget111.getRegularAmount());
        assertEquals(initialBR111Amount + change + budget111.getPublicInvestmentAmount(), budget111.getAmount());

        // BR 11101
        assertEquals(initialBR11101Amount + change, budget11101.getRegularAmount());
        assertEquals(initialBR11101Amount + change + budget11101.getPublicInvestmentAmount(), budget11101.getAmount());
    }

    @Test
    void updateAmountOfSuperClassFilteredObjects2() {
        double percentage = 0.1; // 10% change applied to regular revenue 11

        // Initial amounts of filtered objects
        long initialBR11Amount = budget11.getRegularAmount();
        long initialBR111Amount = budget111.getRegularAmount();
        long initialBR11101Amount = budget11101.getRegularAmount();

        // Implementation
        revenue11.implementChangesOfPercentageAdjustment(percentage);

        // Expected change
        long expectedBR11Amount = (long) (initialBR11Amount * (1 + percentage));
        long expectedBR111Amount = (long) (initialBR111Amount * (1 + percentage));
        long expectedBR11101Amount = (long) (initialBR11101Amount * (1 + percentage));


        // Checking Filtered List Of SuperClass

        // BR 11
        assertEquals(expectedBR11Amount, budget11.getRegularAmount());
        assertEquals(expectedBR11Amount + budget11.getPublicInvestmentAmount(), budget11.getAmount());

        // BR 111
        assertEquals(expectedBR111Amount, budget111.getRegularAmount());
        assertEquals(expectedBR111Amount + budget111.getPublicInvestmentAmount(), budget111.getAmount());

        // BR 11101
        assertEquals(expectedBR11101Amount, budget11101.getRegularAmount());
        assertEquals(expectedBR11101Amount + budget11101.getPublicInvestmentAmount(), budget11101.getAmount());
    }

    @Test
    void toStringFormatTest() {
        String output = revenue11.toString();
        // Έλεγχος αν η toString περιέχει τα βασικά στοιχεία
        assertTrue(output.contains("Code: 11"));
        assertTrue(output.contains("Φόροι"));
    }
}