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
    void implementChangesOfPercentageAdjustmentIncreaseTest1() {
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
    void implementChangesOfPercentageAdjustmentIncreaseTest2() {
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
    void implementChangesOfEqualDistributionIncreaseTest1() {
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
    void implementChangesOfEqualDistributionIncreaseTest2() {
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
    void implementChangesOfEqualDistributionReductionTest1() {
        // Σενάριο: Μείωση 20.000.000€ στη Ρίζα (13) - Ασφαλές ποσό
        long reduction = -20000000L;

        long initial13 = revenue13.getAmount();      // 3.906.000.000L
        long initial131 = revenue131.getAmount();    // 322.000.000L
        long initial132 = revenue132.getAmount();    // 15.000.000L
        long initial13108 = revenue13108.getAmount(); // 322.000.000L

        // Εκτέλεση
        revenue13.implementChangesOfEqualDistribution(reduction);

        // 1. Έλεγχος Ρίζας
        assertEquals(initial13 + reduction, revenue13.getAmount());

        // 2. Έλεγχος Downward Propagation (Ισόποση διανομή στα 2 παιδιά: 131 & 132)
        // -20M / 2 παιδιά = -10.000.000€ στο καθένα
        long splitReduction = reduction / 2;
        assertEquals(initial131 + splitReduction, revenue131.getAmount()); // 312.000.000
        assertEquals(initial132 + splitReduction, revenue132.getAmount()); // 5.000.000

        // 3. Έλεγχος Βαθύτερου Επιπέδου (Το 13108 παίρνει όλη τη μείωση του γονέα του 131)
        assertEquals(initial13108 + splitReduction, revenue13108.getAmount());
    }

    @Test
    void implementChangesOfEqualDistributionReductionTest2() {
        // Σενάριο: Ισόποση μείωση 1.000.000€ στις Εισφορές Εργαζομένων (12201)
        // Ποσό που δεν μηδενίζει τα παιδιά (1220101: 1M -> 500K)
        long reduction = -1000000L;

        long initial12 = revenue12.getAmount();
        long initial12201 = revenue12201.getAmount();
        long initial1220101 = revenue1220101.getAmount();
        long initial1220102 = revenue1220102.getAmount();

        // Εκτέλεση
        revenue12201.implementChangesOfEqualDistribution(reduction);

        // 1. Upward Propagation (Οι γονείς 122 και 12 μειώνονται κατά 1M)
        assertEquals(initial12 + reduction, revenue12.getAmount());

        // 2. Target (12201)
        assertEquals(initial12201 + reduction, revenue12201.getAmount());

        // 3. Downward Propagation (Η μείωση -1M μοιράζεται στα 2 παιδιά: -500K το καθένα)
        long split = reduction / 2;
        assertEquals(initial1220101 + split, revenue1220101.getAmount());
        assertEquals(initial1220102 + split, revenue1220102.getAmount());
    }

    @Test
    void implementChangesOfPercentageAdjustmentReductionTest1() {
        // Σενάριο: Ποσοστιαία μείωση 10% στο μεσαίο επίπεδο (131)
        double percentage = -0.1;

        long initial13 = revenue13.getAmount();
        long initial131 = revenue131.getAmount();
        long initial13108 = revenue13108.getAmount();
        long initial132 = revenue132.getAmount();

        long changeAmount = (long) (initial131 * percentage); // -32.200.000

        // Εκτέλεση
        revenue131.implementChangesOfPercentageAdjustment(percentage);

        // 1. Έλεγχος Target (131)
        assertEquals(initial131 + changeAmount, revenue131.getAmount());

        // 2. Upward Propagation (Ο γονέας 13 μειώνεται κατά το ποσό της αλλαγής)
        assertEquals(initial13 + changeAmount, revenue13.getAmount());

        // 3. Downward Propagation (Το παιδί 13108 μειώνεται κατά 10%)
        long expected13108 = (long) (initial13108 * (1 + percentage));
        assertEquals(expected13108, revenue13108.getAmount());

        // 4. Isolation Check (Το 132 δεν πρέπει να αλλάξει)
        assertEquals(initial132, revenue132.getAmount());
    }

    @Test
    void implementChangesOfPercentageAdjustmentReductionTest2() {
        // Σενάριο: Ποσοστιαία μείωση 50% στη Ρίζα (12) - Επηρεάζει όλο το δέντρο
        double percentage = -0.5;

        long initial12 = revenue12.getAmount();           // 60M
        long initial122 = revenue122.getAmount();         // 60M
        long initial1220101 = revenue1220101.getAmount(); // 1M
        long initial1220102 = revenue1220102.getAmount(); // 59M

        revenue12.implementChangesOfPercentageAdjustment(percentage);

        // 1. Έλεγχος Ρίζας
        assertEquals((long)(initial12 * 0.5), revenue12.getAmount());

        // 2. Έλεγχος Ενδιάμεσου Επιπέδου (122)
        assertEquals((long)(initial122 * 0.5), revenue122.getAmount());

        // 3. Έλεγχος Βαθύτερων επιπέδων
        assertEquals((long)(initial1220101 * 0.5), revenue1220101.getAmount());
        assertEquals((long)(initial1220102 * 0.5), revenue1220102.getAmount());
    }

    @Test
    void implementChangesOfEqualDistributionRollbackTest() {
        // Σενάριο: Μείωση 4.000.000€ στο 122 (Middle Level)
        // Η μείωση μεταφέρεται στο 12201 και μετά στα παιδιά 1220101 & 1220102.
        // Το 1220101 (1M) θα έπρεπε να πάρει -2M -> Αρνητικό -> Rollback.
        long reduction = -4000000L;

        // Εκτέλεση αλλαγής στο 122
        revenue122.implementChangesOfEqualDistribution(reduction);

        //Filtered Αντικείμενα Υπερκλάσης
        BudgetRevenue budget12 = BudgetRevenue.findBudgetRevenueWithCode("12");
        BudgetRevenue budget122 = BudgetRevenue.findBudgetRevenueWithCode("122");
        long initialBudget12Amount = budget12.getAmount();
        long initialBudget122Amount = budget122.getAmount();

        // --- ΕΛΕΓΧΟΣ ΥΠΟΚΛΑΣΗΣ (RegularBudgetRevenue) ---
        // Αν το rollback δουλεύει, όλα πρέπει να είναι όπως πριν
        assertEquals(60000000L, revenue12.getAmount());
        assertEquals(60000000L, revenue122.getAmount());
        assertEquals(60000000L, revenue12201.getAmount());
        assertEquals(1000000L, revenue1220101.getAmount());
        assertEquals(59000000L, revenue1220102.getAmount());

        // ΕΛΕΓΧΟΣ ΥΠΕΡΚΛΑΣΗΣ
        assertEquals(initialBudget12Amount, budget12.getAmount());
        assertEquals(initialBudget122Amount, budget122.getAmount());
        assertEquals(60000000L, budget12.getRegularAmount());
    }

    @Test
    void updateAmountOfSuperClassFilteredObjectsIncreaseTest1() {
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
    void updateAmountOfSuperClassFilteredObjectsIncreaseTest2() {
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
    void updateAmountOfSuperClassFilteredObjectsReductionTest1() {
        // Σενάριο: Ισόποση μείωση 100.000.000€ στους Φόρους (11)
        long reduction = -100000000L;

        // Αρχικά ποσά από την υπερκλάση BudgetRevenue
        long initialBR11Regular = budget11.getRegularAmount();
        long initialBR111Regular = budget111.getRegularAmount();
        long initialBR11101Regular = budget11101.getRegularAmount();

        // Εκτέλεση της μείωσης μέσω του RegularBudgetRevenue αντικειμένου
        revenue11.implementChangesOfEqualDistribution(reduction);

        // Έλεγχος αν η υπερκλάση BudgetRevenue ενημερώθηκε σωστά

        // Για τον κωδικό 11
        assertEquals(initialBR11Regular + reduction, budget11.getRegularAmount());
        assertEquals(budget11.getRegularAmount() + budget11.getPublicInvestmentAmount(), budget11.getAmount());

        // Για τον κωδικό 111 (Downward Propagation στην υπερκλάση)
        assertEquals(initialBR111Regular + reduction, budget111.getRegularAmount());

        // Για τον κωδικό 11101
        assertEquals(initialBR11101Regular + reduction, budget11101.getRegularAmount());
    }

    @Test
    void updateAmountOfSuperClassFilteredObjectsReductionTest2() {
        // Σενάριο: Ποσοστιαία μείωση 20% στους Φόρους (11)
        double percentage = -0.2;

        // Αρχικά ποσά
        long initialBR11Regular = budget11.getRegularAmount();
        long initialBR111Regular = budget111.getRegularAmount();

        // Εκτέλεση
        revenue11.implementChangesOfPercentageAdjustment(percentage);

        // Υπολογισμός αναμενόμενου ποσού
        long expectedBR11Regular = (long) (initialBR11Regular * (1 + percentage));
        long expectedBR111Regular = (long) (initialBR111Regular * (1 + percentage));

        // Έλεγχος συγχρονισμού στην υπερκλάση
        assertEquals(expectedBR11Regular, budget11.getRegularAmount());
        assertEquals(expectedBR111Regular, budget111.getRegularAmount());

        // Έλεγχος ότι το συνολικό ποσό (Amount) της BudgetRevenue παραμένει συνεπές
        assertEquals(budget11.getRegularAmount() + budget11.getPublicInvestmentAmount(), budget11.getAmount());
    }

    @Test
    void updateAmountOfSuperClassFilteredObjectsNullBranchTest() {
        // 1. Δημιουργούμε ένα νέο αντικείμενο
        RegularBudgetRevenue tempRevenue = new RegularBudgetRevenue("TEMP-99", "Προσωρινό", "ΕΣΟΔΑ", 1000L);

        // 2. Αφαιρούμε το αντίστοιχο αντικείμενο από την υπερκλάση
        // για να αναγκάσουμε το lookup (find) να επιστρέψει null
        BudgetRevenue brTemp = BudgetRevenue.findBudgetRevenueWithCode("TEMP-99");
        BudgetRevenue.getAllBudgetRevenues().remove(brTemp);

        // 3. Εκτελούμε μια αλλαγή
        // Η setAmount θα καλέσει την updateAmountOfSuperClassFilteredObjects,
        // η οποία θα βρει null στο find και θα προσπεράσει το εσωτερικό του if
        tempRevenue.setAmount(1500L);

        // 4. Επιβεβαιώνουμε ότι η υποκλάση ενημερώθηκε κανονικά
        assertEquals(1500L, tempRevenue.getAmount());

        // 5. Επιβεβαιώνουμε ότι στην υπερκλάση όντως δεν υπάρχει πλέον το αντικείμενο
        assertNull(BudgetRevenue.findBudgetRevenueWithCode("TEMP-99"));
    }

    @Test
    void toStringFormatTest() {
        String output = revenue11.toString();
        // Έλεγχος αν η toString περιέχει τα βασικά στοιχεία
        assertTrue(output.contains("Code: 11"));
        assertTrue(output.contains("Φόροι"));
    }
}