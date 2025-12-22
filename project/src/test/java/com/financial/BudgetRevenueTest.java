package com.financial;

import com.financial.entries.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class BudgetRevenueTest {

    private RegularBudgetRevenue reg12, reg13, reg131, reg13108;
    private PublicInvestmentBudgetRevenue pib13, pib134;

    @BeforeEach
    void setUp() {
        // Καθαρισμός λιστών
        BudgetRevenue.getAllBudgetRevenues().clear();
        PublicInvestmentBudgetRevenue.getAllPublicInvestmentBudgetRevenues().clear();

        // --- REGULAR BUDGET REVENUE (Τακτικός) ---
        // ΝΕΟ: Κωδικός 12 (Νέα Ρίζα)
        reg12 = new RegularBudgetRevenue("12", "Κοινωνικές εισφορές", "ΕΣΟΔΑ", 60000000L);
        // Κωδικός 13 & 131
        reg13 = new RegularBudgetRevenue("13", "Μεταβιβάσεις", "ΕΣΟΔΑ", 3906000000L);
        reg131 = new RegularBudgetRevenue("131", "Τρέχουσες εγχώριες μεταβιβάσεις", "ΕΣΟΔΑ", 322000000L);
        // ΝΕΟ: Κωδικός 13108 (Βάθος ιεραρχίας)
        reg13108 = new RegularBudgetRevenue("13108", "Μεταβιβάσεις από λοιπά ΝΠ", "ΕΣΟΔΑ", 322000000L);

        // --- PUBLIC INVESTMENT BUDGET REVENUE (ΠΔΕ) ---
        pib13 = new PublicInvestmentBudgetRevenue("13", "Μεταβιβάσεις", "ΕΣΟΔΑ", "ΠΔΕ", 35000000L, 4190000000L, 4225000000L);
        pib134 = new PublicInvestmentBudgetRevenue("134", "Επιχορηγήσεις επενδύσεων εσωτερικού", "ΕΣΟΔΑ", "ΠΔΕ", 35000000L, 0L, 35000000L);

        // Διαδικασία Merge στην Υπερκλάση BudgetRevenue
        BudgetRevenue.sortBudgetRevenuesByCode();
        BudgetRevenue.filterBudgetRevenues();
    }

    @Test
    void mergeAndHierarchicalLogicTest() {
        ArrayList<BudgetRevenue> filtered = BudgetRevenue.getAllBudgetRevenues();

        // Έλεγχος Πλήθους: 12, 13(merged), 131, 13108, 134 -> Σύνολο 5
        assertEquals(5, filtered.size());

        // ΕΛΕΓΧΟΣ MERGE ΣΤΟ 13
        BudgetRevenue merged13 = BudgetRevenue.findBudgetRevenueWithCode("13");
        assertNotNull(merged13);
        assertEquals(3906000000L, merged13.getRegularAmount());
        assertEquals(4225000000L, merged13.getPublicInvestmentAmount());
        assertEquals(8131000000L, merged13.getAmount());
    }

    @Test
    void getAllBudgetRevenuesTest() {
        ArrayList<BudgetRevenue> all = BudgetRevenue.getAllBudgetRevenues();
        assertEquals(5, all.size());
    }

    @Test
    void getMainBudgetRevenuesTest() {
        // Ρίζες: 12 και 13
        ArrayList<BudgetRevenue> main = BudgetRevenue.getMainBudgetRevenues();
        assertEquals(2, main.size());
        assertTrue(main.stream().anyMatch(r -> r.getCode().equals("12")));
        assertTrue(main.stream().anyMatch(r -> r.getCode().equals("13")));
    }

    @Test
    void findBudgetRevenueWithCodeTest() {
        BudgetRevenue found = BudgetRevenue.findBudgetRevenueWithCode("13108");
        assertNotNull(found);
        assertEquals("13108", found.getCode());

        assertNull(BudgetRevenue.findBudgetRevenueWithCode("999"));
    }

    @Test
    void getBudgetRevenuesStartingWithCodeTest() {
        // Ξεκινούν από "13": 13, 131, 13108, 134 -> Σύνολο 4
        ArrayList<BudgetRevenue> results = BudgetRevenue.getBudgetRevenuesStartingWithCode("13");
        assertEquals(4, results.size());
    }

    @Test
    void calculateSumTest() {
        // 12 (60.000.000) + 13 (8.131.000.000) = 8.191.000.000
        long expectedSum = 8191000000L;
        assertEquals(expectedSum, BudgetRevenue.calculateSum());
    }

    @Test
    void getLevelOfHierarchyTest() {
        BudgetRevenue br12 = BudgetRevenue.findBudgetRevenueWithCode("12");
        BudgetRevenue br131 = BudgetRevenue.findBudgetRevenueWithCode("131");
        BudgetRevenue br13108 = BudgetRevenue.findBudgetRevenueWithCode("13108");

        assertEquals(1, br12.getLevelOfHierarchy());    // 2 ψηφία
        assertEquals(2, br131.getLevelOfHierarchy());   // 3 ψηφία
        assertEquals(3, br13108.getLevelOfHierarchy()); // 5 ψηφία
    }

    @Test
    void getAboveLevelSuperCategoryTest() {
        BudgetRevenue child = BudgetRevenue.findBudgetRevenueWithCode("13108");
        BudgetRevenue parent = child.getAboveLevelSuperCategory();

        assertNotNull(parent);
        assertEquals("131", parent.getCode());
    }
}