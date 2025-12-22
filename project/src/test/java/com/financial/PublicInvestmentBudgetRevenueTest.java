package com.financial;

import com.financial.entries.BudgetRevenue;
import com.financial.entries.PublicInvestmentBudgetRevenue;
import com.financial.entries.PublicInvestmentBudgetNationalRevenue;
import com.financial.entries.PublicInvestmentBudgetCoFundedRevenue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PublicInvestmentBudgetRevenueTest {

    // Αντικείμενα υποκλάσεων
    private PublicInvestmentBudgetNationalRevenue nat13, nat134;
    private PublicInvestmentBudgetCoFundedRevenue co13, co135;
    private PublicInvestmentBudgetNationalRevenue nat15, nat156, nat15609;

    @BeforeEach
    void setUp() {
        PublicInvestmentBudgetRevenue.getAllPublicInvestmentBudgetRevenues().clear();
        BudgetRevenue.getAllBudgetRevenues().clear();
        PublicInvestmentBudgetNationalRevenue.getAllPublicInvestmentBudgetNationalRevenues().clear();
        PublicInvestmentBudgetCoFundedRevenue.getAllPublicInvestmentBudgetCoFundedRevenues().clear();

        //Μίξη National & CoFunded
        nat13 = new PublicInvestmentBudgetNationalRevenue("13", "Κατηγορία 13", "ΕΣΟΔΑ", "ΕΘΝΙΚΟ", 35000000L);
        nat134 = new PublicInvestmentBudgetNationalRevenue("134", "Υποκατηγορία 134", "ΕΣΟΔΑ", "ΕΘΝΙΚΟ", 35000000L);

        co13 = new PublicInvestmentBudgetCoFundedRevenue("13", "Κατηγορία 13", "ΕΣΟΔΑ", "ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ", 4190000000L);
        co135 = new PublicInvestmentBudgetCoFundedRevenue("135", "Υποκατηγορία 135", "ΕΣΟΔΑ", "ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ", 4190000000L);

        //Μόνο National
        nat15 = new PublicInvestmentBudgetNationalRevenue("15", "Κατηγορία 15", "ΕΣΟΔΑ", "ΕΘΝΙΚΟ", 265000000L);
        nat156 = new PublicInvestmentBudgetNationalRevenue("156", "Υποκατηγορία 156", "ΕΣΟΔΑ", "ΕΘΝΙΚΟ", 1000000L);
        nat15609 = new PublicInvestmentBudgetNationalRevenue("15609", "Υποκατηγορία 15609", "ΕΣΟΔΑ", "ΕΘΝΙΚΟ", 1000000L);

        PublicInvestmentBudgetRevenue.sortPublicInvestmentBudgetRevenuesByCode();
        PublicInvestmentBudgetRevenue.filterPublicInvestmentBudgetRevenues();
    }

    @Test
    void mergeAndHierarchicalLogicTest() {
        ArrayList<PublicInvestmentBudgetRevenue> filtered = PublicInvestmentBudgetRevenue.getAllPublicInvestmentBudgetRevenues();

        // Έλεγχος Πλήθους:
        // 13(merged), 134(nat), 135(co), 15(nat), 156(nat), 15609(nat) -> Σύνολο 6 μοναδικοί κωδικοί
        assertEquals(6, filtered.size());

        // --- ΕΛΕΓΧΟΣ MERGE ΣΤΟ 13 ---
        PublicInvestmentBudgetRevenue merged13 = PublicInvestmentBudgetRevenue.findPublicInvestmentBudgetRevenueWithCode("13");
        assertNotNull(merged13);
        assertEquals(35000000L, merged13.getNationalAmount());
        assertEquals(4190000000L, merged13.getCoFundedAmount());
        long expectedTotal13 = 35000000L + 4190000000L;
        assertEquals(expectedTotal13, merged13.getAmount());

        // --- ΕΛΕΓΧΟΣ ΚΛΑΔΟΥ 15 ---
        PublicInvestmentBudgetRevenue rev15 = PublicInvestmentBudgetRevenue.findPublicInvestmentBudgetRevenueWithCode("15");
        assertEquals(265000000L, rev15.getAmount());
        assertEquals(265000000L, rev15.getNationalAmount());
        assertEquals(0L, rev15.getCoFundedAmount());

    }

    @Test
    void testGetAllPublicInvestmentBudgetRevenues() {
        // Επιβεβαιώνουμε ότι η λίστα περιέχει τα φιλτραρισμένα αντικείμενα (6 μοναδικοί κωδικοί)
        ArrayList<PublicInvestmentBudgetRevenue> all = PublicInvestmentBudgetRevenue.getAllPublicInvestmentBudgetRevenues();
        assertEquals(6, all.size(), "Η getAll πρέπει να επιστρέφει τη λίστα μετά το merge.");
    }

    @Test
    void testGetMainPublicInvestmentBudgetRevenues() {
        // "Main" θεωρούνται οι ρίζες (π.χ. κωδικοί με 2 ψηφία όπως 13 και 15)
        ArrayList<PublicInvestmentBudgetRevenue> main = PublicInvestmentBudgetRevenue.getMainPublicInvestmentBudgetRevenues();


        assertEquals(2, main.size());
        assertTrue(main.stream().anyMatch(r -> r.getCode().equals("13")));
        assertTrue(main.stream().anyMatch(r -> r.getCode().equals("15")));
    }

    @Test
    void testFindPublicInvestmentBudgetRevenueWithCode() {
        // Έλεγχος για υπάρχοντα κωδικό
        PublicInvestmentBudgetRevenue found = PublicInvestmentBudgetRevenue.findPublicInvestmentBudgetRevenueWithCode("134");
        assertNotNull(found);
        assertEquals(found.getCode(), "134");

        // Έλεγχος για κωδικό που δεν υπάρχει
        PublicInvestmentBudgetRevenue notFound = PublicInvestmentBudgetRevenue.findPublicInvestmentBudgetRevenueWithCode("999");
        assertSame(notFound, null);
    }

    @Test
    void testGetPublicInvestmentBudgetRevenuesStartingWithCode() {
        // Αναζήτηση όλων των εσόδων που ξεκινούν από "15"
        // Πρέπει να βρει: 15, 156, 15609
        ArrayList<BudgetRevenue> results = PublicInvestmentBudgetRevenue.getPublicInvestmentBudgetRevenuesStartingWithCode("15");

        assertEquals(3, results.size());

        // Αναζήτηση που δεν επιστρέφει τίποτα
        ArrayList<BudgetRevenue> emptyResults = PublicInvestmentBudgetRevenue.getPublicInvestmentBudgetRevenuesStartingWithCode("9");
        assertEquals(0, emptyResults.size());
    }

    @Test
    void calculateSumTest() {
        // Το calculateSum() πρέπει να αθροίζει μόνο τις "Main" κατηγορίες (13 και 15)
        // 4,225,000,000 (13) + 265,000,000 (15) = 4,490,000,000
        long expectedSum = 4490000000L;
        assertEquals(expectedSum, PublicInvestmentBudgetRevenue.calculateSum());
    }

    @Test
    void getNextLevelSubCategoriesTest() {
        PublicInvestmentBudgetRevenue merged13 = PublicInvestmentBudgetRevenue.findPublicInvestmentBudgetRevenueWithCode("13");
        ArrayList<BudgetRevenue> subs = merged13.getNextLevelSubCategories();

        // Πρέπει να έχει το 134 και το 135
        assertEquals(2, subs.size());
        assertTrue(subs.stream().anyMatch(r -> r.getCode().equals("134")));
        assertTrue(subs.stream().anyMatch(r -> r.getCode().equals("135")));
    }

    @Test
    void getAllSubCategoriesTest() {
        PublicInvestmentBudgetRevenue rev15 = PublicInvestmentBudgetRevenue.findPublicInvestmentBudgetRevenueWithCode("15");
        ArrayList<BudgetRevenue> allSubs = rev15.getAllSubCategories();

        // Για τον 15, τα subs είναι 156 και 15609
        assertEquals(2, allSubs.size());
    }

    @Test
    void getAboveLevelSuperCategoryTest() {
        PublicInvestmentBudgetRevenue rev15609 = PublicInvestmentBudgetRevenue.findPublicInvestmentBudgetRevenueWithCode("15609");
        BudgetRevenue parent = rev15609.getAboveLevelSuperCategory();

        assertNotNull(parent);
        assertEquals("156", parent.getCode());
    }

    @Test
    void getAllSuperCategoriesTest() {
        PublicInvestmentBudgetRevenue rev15609 = PublicInvestmentBudgetRevenue.findPublicInvestmentBudgetRevenueWithCode("15609");
        ArrayList<BudgetRevenue> supers = rev15609.getAllSuperCategories();

        // Πρέπει να επιστρέφει [156, 15]
        assertEquals(2, supers.size());
        assertEquals("156", supers.get(0).getCode());
        assertEquals("15", supers.get(1).getCode());
    }
}