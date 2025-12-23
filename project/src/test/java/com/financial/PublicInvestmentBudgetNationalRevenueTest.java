package com.financial;

import com.financial.entries.BudgetRevenue;
import com.financial.entries.PublicInvestmentBudgetNationalRevenue;
import com.financial.entries.PublicInvestmentBudgetRevenue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static com.financial.entries.PublicInvestmentBudgetNationalRevenue.calculateSum;
import static org.junit.jupiter.api.Assertions.*;

public class PublicInvestmentBudgetNationalRevenueTest {

    private PublicInvestmentBudgetNationalRevenue revenue13;
    private PublicInvestmentBudgetNationalRevenue revenue134;
    private PublicInvestmentBudgetNationalRevenue revenue13409;
    private PublicInvestmentBudgetNationalRevenue revenue15;
    private PublicInvestmentBudgetNationalRevenue revenue156;
    private PublicInvestmentBudgetNationalRevenue revenue15609;

    private PublicInvestmentBudgetRevenue pib13;
    private PublicInvestmentBudgetRevenue pib134;
    private PublicInvestmentBudgetRevenue pib13409;

    @BeforeEach
    void setUp() {
        PublicInvestmentBudgetRevenue.getAllPublicInvestmentBudgetRevenues().clear();
        PublicInvestmentBudgetNationalRevenue.getAllPublicInvestmentBudgetNationalRevenues().clear();

        revenue13 = new PublicInvestmentBudgetNationalRevenue("13", "Κατηγορία 13", "ΕΣΟΔΑ", "ΕΘΝΙΚΟ", 35000000L);
        revenue134 = new PublicInvestmentBudgetNationalRevenue("134", "Υποκατηγορία 134", "ΕΣΟΔΑ", "ΕΘΝΙΚΟ", 35000000L);
        revenue13409 = new PublicInvestmentBudgetNationalRevenue("13409", "Υποκατηγορία 13409", "ΕΣΟΔΑ", "ΕΘΝΙΚΟ", 35000000L);

        revenue15 = new PublicInvestmentBudgetNationalRevenue("15", "Κατηγορία 15", "ΕΣΟΔΑ", "ΕΘΝΙΚΟ", 265000000L);
        revenue156 = new PublicInvestmentBudgetNationalRevenue("156", "Υποκατηγορία 156", "ΕΣΟΔΑ", "ΕΘΝΙΚΟ", 1000000L);
        revenue15609 = new PublicInvestmentBudgetNationalRevenue("15609", "Υποκατηγορία 15609", "ΕΣΟΔΑ", "ΕΘΝΙΚΟ", 1000000L);

        pib13 = PublicInvestmentBudgetRevenue.findPublicInvestmentBudgetRevenueWithCode("13");
        pib134 = PublicInvestmentBudgetRevenue.findPublicInvestmentBudgetRevenueWithCode("134");
        pib13409 = PublicInvestmentBudgetRevenue.findPublicInvestmentBudgetRevenueWithCode("13409");
    }

    @Test
    void getAllPublicInvestmentBudgetNationalRevenuesTest() {
        ArrayList<PublicInvestmentBudgetNationalRevenue> allRevenues = PublicInvestmentBudgetNationalRevenue.getAllPublicInvestmentBudgetNationalRevenues();
        assertEquals(6, allRevenues.size());
        assertTrue(allRevenues.get(0) instanceof PublicInvestmentBudgetNationalRevenue);
    }

    @Test
    void getMainPublicInvestmentBudgetNationalRevenuesTest() {
        ArrayList<PublicInvestmentBudgetNationalRevenue> mainRevenues = PublicInvestmentBudgetNationalRevenue.getMainPublicInvestmentBudgetNationalRevenues();
        // Εδώ έχουμε 2 roots (13 και 15)
        assertEquals(2, mainRevenues.size());
        assertTrue(mainRevenues.contains(revenue13));
        assertTrue(mainRevenues.contains(revenue15));
    }

    @Test
    void findPublicInvestmentBudgetNationalRevenueWithCodeTest() {
        // Έλεγχος για υπάρχοντα κωδικό
        PublicInvestmentBudgetNationalRevenue found = PublicInvestmentBudgetNationalRevenue.findPublicInvestmentBudgetNationalRevenueWithCode("13");
        assertNotNull(found);
        assertEquals(revenue13.getAmount(), found.getAmount());

        // Έλεγχος για κωδικό που δεν υπάρχει
        assertNull(PublicInvestmentBudgetNationalRevenue.findPublicInvestmentBudgetNationalRevenueWithCode("999"));
    }

    @Test
    void getPublicInvestmentBudgetNationalRevenuesStartingWithCodeTest() {
        // Αναζήτηση όλων των εσόδων που ξεκινούν από "13" (13, 134, 13409)
        ArrayList<BudgetRevenue> results = PublicInvestmentBudgetNationalRevenue.getPublicInvestmentBudgetNationalRevenuesStartingWithCode("13");
        assertEquals(3, results.size());
        assertTrue(results.contains(revenue13));
        assertTrue(results.contains(revenue134));
        assertTrue(results.contains(revenue13409));

        // Αναζήτηση που δεν επιστρέφει τίποτα
        ArrayList<BudgetRevenue> emptyResults = PublicInvestmentBudgetNationalRevenue.getPublicInvestmentBudgetNationalRevenuesStartingWithCode("9");
        assertEquals(0, emptyResults.size());
    }

    @Test
    void calculateSumTest() {
        // 35,000,000 (13) + 265,000,000 (15)
        long expectedSum = 300000000L;
        assertEquals(expectedSum, calculateSum());
    }

    @Test
    void getAboveLevelSuperCategoryTest() {
        BudgetRevenue parent = revenue13409.getAboveLevelSuperCategory();
        assertNotNull(parent);
        assertEquals("134", parent.getCode());
        assertEquals(35000000L, parent.getAmount());
    }


    @Test
    void getAllSuperCategoriesTest() {
        ArrayList<BudgetRevenue> superCategories = revenue13409.getAllSuperCategories();
        assertEquals(2, superCategories.size());
        //Checking if the list contains all super categories
        assertTrue(superCategories.contains(revenue13));
        assertTrue(superCategories.contains(revenue134));
    }

    @Test
    void getNextLevelSubCategoriesTest() {

        ArrayList<BudgetRevenue> subCategories = revenue13.getNextLevelSubCategories();

        //One children in the next level - 134
        assertEquals(1, subCategories.size());
        assertTrue(subCategories.contains(revenue134));
    }


    @Test
    void getllSubCategoriesTest() {
        ArrayList<BudgetRevenue> allSubCategories = revenue13.getAllSubCategories();
        //2 children total - 134, 13409
        assertEquals(2, allSubCategories.size());
        assertTrue(allSubCategories.contains(revenue134));
        assertTrue(allSubCategories.contains(revenue13409));
    }

    @Test
    void implementChangesOfPercentageAdjustmentTest() {
        double percentage = 0.2; // +20% applied to 13 (Top Level)

        long initial13 = revenue13.getAmount();
        long initial134 = revenue134.getAmount();
        long initial13409 = revenue13409.getAmount();

        long changeOnSelf = (long) (initial13 * percentage);

        // Εφαρμογή μόνο στο 13
        revenue13.implementChangesOfPercentageAdjustment(percentage);

        assertEquals(initial13 + changeOnSelf, revenue13.getAmount());

        // 2 - Checking the subcategories
        long expected134 = (long) (initial134 * (1 + percentage));
        long expected13409 = (long) (initial13409 * (1 + percentage));
        assertEquals(expected134, revenue134.getAmount());
        assertEquals(expected13409, revenue13409.getAmount());

        // 2. Το 15 μένει ανεπηρέαστο
        assertEquals(265000000L, revenue15.getAmount());
    }

    @Test
    void implementChangesOfPercentageAdjustmentTest2() {
        double percentage = 0.1; // +10% applied to 134 (Middle Level)

        long initial13 = revenue13.getAmount();
        long initial134 = revenue134.getAmount();
        long initial13409 = revenue13409.getAmount();

        long changeOnSelf = (long) (initial134 * percentage);

        // Εφαρμογή μόνο στο 13
        revenue134.implementChangesOfPercentageAdjustment(percentage);

        assertEquals(initial134 + changeOnSelf, revenue134.getAmount());

        assertEquals(initial13 + changeOnSelf, revenue13.getAmount());

        // 2 - Checking the subcategories
        long expected13409 = (long) (initial13409 * (1 + percentage));
        assertEquals(expected13409, revenue13409.getAmount());

        // 2. Το 15 μένει ανεπηρέαστο
        assertEquals(265000000L, revenue15.getAmount());
    }
    @Test
    void implementChangesOfEqualDistributionTest() {
        double percentage = 0.2; // +20% applied to 13 (Top Level)

        long initial13 = revenue13.getAmount();
        long initial134 = revenue134.getAmount();
        long initial13409 = revenue13409.getAmount();

        long changeOnSelf = (long) (initial13 * percentage);

        // Εφαρμογή μόνο στο 13
        revenue13.implementChangesOfPercentageAdjustment(percentage);

        assertEquals(initial13 + changeOnSelf, revenue13.getAmount());

        // 2 - Checking the subcategories
        long expected134 = (long) (initial134 * (1 + percentage));
        long expected13409 = (long) (initial13409 * (1 + percentage));
        assertEquals(expected134, revenue134.getAmount());
        assertEquals(expected13409, revenue13409.getAmount());

        // 2. Το 15 μένει ανεπηρέαστο
        assertEquals(265000000L, revenue15.getAmount());
    }

    @Test
    void implementChangesOfEqualDistributionTest2() {
        double percentage = 0.1; // +10% applied to 134 (Middle Level)

        long initial13 = revenue13.getAmount();
        long initial134 = revenue134.getAmount();
        long initial13409 = revenue13409.getAmount();

        long changeOnSelf = (long) (initial134 * percentage);

        // Εφαρμογή μόνο στο 13
        revenue134.implementChangesOfPercentageAdjustment(percentage);

        assertEquals(initial134 + changeOnSelf, revenue134.getAmount());

        assertEquals(initial13 + changeOnSelf, revenue13.getAmount());

        // 2 - Checking the subcategories
        long expected13409 = (long) (initial13409 * (1 + percentage));
        assertEquals(expected13409, revenue13409.getAmount());

        // 2. Το 15 μένει ανεπηρέαστο
        assertEquals(265000000L, revenue15.getAmount());
    }

    @Test
    void updateAmountOfSuperClassFilteredObjectTest1() {
        long change = 5000000L; // +5 Million change

        // Αρχικά ποσά των filtered αντικειμένων (PIB)
        long initialPIB13Amount = pib13.getNationalAmount();
        long initialPIB134Amount = pib134.getNationalAmount();
        long initialPIB13409Amount = pib13409.getNationalAmount();

        // Implementation (Equal Distribution στο 134)
        revenue134.implementChangesOfEqualDistribution(change);

        // Έλεγχος στην PublicInvestmentBudgetRevenue (SuperClass)

        // PIB 13 (Upward - γονέας)
        assertEquals(initialPIB13Amount + change, pib13.getNationalAmount());
        assertEquals(pib13.getNationalAmount() + pib13.getCoFundedAmount(), pib13.getAmount());

        // PIB 134 (Self)
        assertEquals(initialPIB134Amount + change, pib134.getNationalAmount());

        // PIB 13409 (Downward - παιδί)
        assertEquals(initialPIB13409Amount + change, pib13409.getNationalAmount());
    }

    @Test
    void updateAmountOfSuperClassFilteredObjectTest2() {
        double percentage = 0.1; // +10% change

        // Αρχικά ποσά των filtered αντικειμένων (PIB)
        long initialPIB13Amount = pib13.getNationalAmount();
        long initialPIB134Amount = pib134.getNationalAmount();

        // Implementation (Percentage Adjustment στο 13)
        revenue13.implementChangesOfPercentageAdjustment(percentage);

        // Αναμενόμενες τιμές
        long expectedPIB13Amount = (long) (initialPIB13Amount * (1 + percentage));
        long expectedPIB134Amount = (long) (initialPIB134Amount * (1 + percentage));

        // Έλεγχος στην PublicInvestmentBudgetRevenue (SuperClass)
        assertEquals(expectedPIB13Amount, pib13.getNationalAmount());
        assertEquals(expectedPIB134Amount, pib134.getNationalAmount());
    }

    @Test
    void updateAmountOfSuperClassFilteredObjectNullBranchTest() {
        // Δημιουργούμε ένα προσωρινό αντικείμενο
        PublicInvestmentBudgetNationalRevenue tempRevenue = new PublicInvestmentBudgetNationalRevenue("99", "Temp", "ΕΣΟΔΑ", "ΕΘΝΙΚΟ", 100L);

        // Αφαιρούμε το αντίστοιχο αντικείμενο από την υπερκλάση για να αναγκάσουμε το lookup να αποτύχει (null)
        // Αυτό θα καλύψει το "κίτρινο" branch στην updateAmountOfSuperClassFilteredObjects
        PublicInvestmentBudgetRevenue pib99 = PublicInvestmentBudgetRevenue.findPublicInvestmentBudgetRevenueWithCode("99");
        PublicInvestmentBudgetRevenue.getAllPublicInvestmentBudgetRevenues().remove(pib99);

        // Καλούμε την ενημέρωση - η μέθοδος θα μπει στο branch όπου η find επιστρέφει null
        tempRevenue.implementChangesOfEqualDistribution(50L);

        // Δεν περιμένουμε crash, απλώς επιβεβαιώνουμε ότι το τοπικό amount άλλαξε
        assertEquals(150L, tempRevenue.getAmount());
    }

    @Test
    void toStringFormatTest() {
        String output = revenue15.toString();
        // Έλεγχος αν η toString περιέχει τα βασικά στοιχεία
        assertTrue(output.contains("Code: 15"));
        assertTrue(output.contains("ΕΘΝΙΚΟ"));
        // Έλεγχος για το σωστό formatting του ποσού (265.000.000)
        assertTrue(output.contains("265.000.000") || output.contains("265,000,000"));
    }
}