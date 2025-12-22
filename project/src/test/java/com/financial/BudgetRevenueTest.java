package com.financial;

import com.financial.entries.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class BudgetRevenueTest {

    private RegularBudgetRevenue reg13, reg131;
    private PublicInvestmentBudgetRevenue pib13, pib134;

    @BeforeEach
    void setUp() {
        // Καθαρισμός λιστών
        BudgetRevenue.getAllBudgetRevenues().clear();
        PublicInvestmentBudgetRevenue.getAllPublicInvestmentBudgetRevenues().clear();

        // Δημιουργία RegularBudgetRevenue (Τακτικός Προϋπολογισμός)
        reg13 = new RegularBudgetRevenue("13", "Μεταβιβάσεις", "ΕΣΟΔΑ", 3906000000L);
        reg131 = new RegularBudgetRevenue("131", "Τρέχουσες εγχώριες μεταβιβάσεις", "ΕΣΟΔΑ", 322000000L);

        // Δημιουργία PublicInvestmentBudgetRevenue
        pib13 = new PublicInvestmentBudgetRevenue("13", "Μεταβιβάσεις", "ΕΣΟΔΑ", "ΠΔΕ", 35000000L, 4190000000L, 4225000000L);
        pib134 = new PublicInvestmentBudgetRevenue("134", "Επιχορηγήσεις επενδύσεων εσωτερικού", "ΕΣΟΔΑ", "ΠΔΕ", 35000000L, 0L, 35000000L);

        //Διαδικασία Merge στην Υπερκλάση BudgetRevenue
        BudgetRevenue.sortBudgetRevenuesByCode();
        BudgetRevenue.filterBudgetRevenues();
    }

    @Test
    void mergeAndHierarchicalLogicTest() {
        ArrayList<BudgetRevenue> filtered = BudgetRevenue.getAllBudgetRevenues();

        // Έλεγχος Πλήθους:
        // 13(merged), 131(regular), 134(pib) -> Σύνολο 3 μοναδικοί κωδικοί
        assertEquals(3, filtered.size());

        // --- ΕΛΕΓΧΟΣ MERGE ΣΤΟ 13 ---
        BudgetRevenue merged13 = BudgetRevenue.findBudgetRevenueWithCode("13");
        assertNotNull(merged13);
        assertEquals(3906000000L, merged13.getRegularAmount());
        assertEquals(4225000000L, merged13.getPublicInvestmentAmount());
        long expectedTotal13 = 3906000000L + 4225000000L;
        assertEquals(expectedTotal13, merged13.getAmount());

        // --- ΕΛΕΓΧΟΣ ΓΙΑ ΚΩΔΙΚΟ 131 (Μόνο Τακτικός) ---
        BudgetRevenue rev131 = BudgetRevenue.findBudgetRevenueWithCode("131");
        assertEquals(322000000L, rev131.getAmount());
        assertEquals(322000000L, rev131.getRegularAmount());
        assertEquals(0L, rev131.getPublicInvestmentAmount());

        // --- ΕΛΕΓΧΟΣ ΓΙΑ ΚΩΔΙΚΟ 134 (Μόνο ΠΔΕ) ---
        BudgetRevenue rev134 = BudgetRevenue.findBudgetRevenueWithCode("134");
        assertEquals(35000000L, rev134.getAmount());
        assertEquals(0L, rev134.getRegularAmount());
        assertEquals(35000000L, rev134.getPublicInvestmentAmount());
    }

    @Test
    void getAllBudgetRevenuesTest() {
        // Επιβεβαιώνουμε ότι η λίστα περιέχει τους 3 μοναδικούς κωδικούς (13, 131, 134)
        ArrayList<BudgetRevenue> all = BudgetRevenue.getAllBudgetRevenues();
        assertEquals(3, all.size());
    }

    @Test
    void getMainBudgetRevenuesTest() {
        // Μόνο ο κωδικός 13 είναι ρίζα (Level 1) στα τρέχοντα δεδομένα του setUp
        ArrayList<BudgetRevenue> main = BudgetRevenue.getMainBudgetRevenues();
        assertEquals(1, main.size());
        assertTrue(main.stream().anyMatch(r -> r.getCode().equals("13")));
    }

    @Test
    void findBudgetRevenueWithCodeTest() {
        // Έλεγχος για τον merged κωδικό 13
        BudgetRevenue found = BudgetRevenue.findBudgetRevenueWithCode("13");
        assertNotNull(found);
        assertEquals("13", found.getCode());

        // Έλεγχος για κωδικό που δεν υπάρχει
        BudgetRevenue notFound = BudgetRevenue.findBudgetRevenueWithCode("999");
        assertNull(notFound);
    }

    @Test
    void getBudgetRevenuesStartingWithCodeTest() {
        // Αναζήτηση εσόδων που ξεκινούν από "13" (13, 131, 134)
        ArrayList<BudgetRevenue> results = BudgetRevenue.getBudgetRevenuesStartingWithCode("13");
        assertEquals(3, results.size());
    }

    @Test
    void calculateSumTest() {
        // Το calculateSum() αθροίζει τις Main κατηγορίες (μόνο το 13 στα δεδομένα μας)
        // 3.906.000.000 (Regular) + 4.225.000.000 (PIB) = 8.131.000.000
        long expectedSum = 8131000000L;
        assertEquals(expectedSum, BudgetRevenue.calculateSum());
    }
}