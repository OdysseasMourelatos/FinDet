package com.financial;

import com.financial.entries.*;
import com.financial.services.BudgetType;
import com.financial.services.data.DataInput;
import com.financial.services.revenues.RevenuesHistory;
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
        RegularBudgetRevenue.getAllRegularBudgetRevenues().clear();
        PublicInvestmentBudgetRevenue.getAllPublicInvestmentBudgetRevenues().clear();
        RevenuesHistory.getHistoryDeque().clear();
        RevenuesHistory.getTypeDeque().clear();

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
        DataInput.createBudgetRevenueFilteredFromPublicInvestmentBudgetRevenue();
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
        BudgetRevenue temp1 = new BudgetRevenue("1110103", "Level 4", "ΕΣΟΔΑ", 200L);
        BudgetRevenue temp2 = new BudgetRevenue("1110103001", "Level 5", "ΕΣΟΔΑ", 100L);
        RegularBudgetRevenue unknown = new RegularBudgetRevenue("1", "Unknown", "ΕΣΟΔΑ", 100L);

        assertEquals(1, br12.getLevelOfHierarchy());    // 2 ψηφία
        assertEquals(2, br131.getLevelOfHierarchy());   // 3 ψηφία
        assertEquals(3, br13108.getLevelOfHierarchy()); // 5 ψηφία
        assertEquals(4, temp1.getLevelOfHierarchy());   // 7 ψηφία
        assertEquals(5, temp2.getLevelOfHierarchy());   // 10 ψηφία
        assertEquals(0, unknown.getLevelOfHierarchy()); // Unknown
    }

    @Test
    void getNextLevelSubCategoriesTest() {
        // Ο 13 (Μεταβιβάσεις) έχει παιδιά τους 131 και 134
        BudgetRevenue merged13 = BudgetRevenue.findBudgetRevenueWithCode("13");
        ArrayList<BudgetRevenue> subs = merged13.getNextLevelSubCategories();

        assertEquals(2, subs.size());
        assertTrue(subs.stream().anyMatch(r -> r.getCode().equals("131")));
        assertTrue(subs.stream().anyMatch(r -> r.getCode().equals("134")));
    }

    @Test
    void getAllSubCategoriesTest() {
        // Ο 13 έχει ως συνολικούς απογόνους τους: 131, 13108, 134
        BudgetRevenue merged13 = BudgetRevenue.findBudgetRevenueWithCode("13");
        ArrayList<BudgetRevenue> allSubs = merged13.getAllSubCategories();

        assertEquals(3, allSubs.size());
    }

    @Test
    void getAboveLevelSuperCategoryTest() {
        BudgetRevenue child = BudgetRevenue.findBudgetRevenueWithCode("13108");
        BudgetRevenue parent = child.getAboveLevelSuperCategory();

        assertNotNull(parent);
        assertEquals("131", parent.getCode());
    }

    @Test
    void getAllSuperCategoriesTest() {
        // Για τον 13108, οι γονείς είναι [131, 13]
        BudgetRevenue rev13108 = BudgetRevenue.findBudgetRevenueWithCode("13108");
        ArrayList<BudgetRevenue> supers = rev13108.getAllSuperCategories();

        assertEquals(2, supers.size());
        assertTrue(supers.stream().anyMatch(r -> r.getCode().equals("131")));
        assertTrue(supers.stream().anyMatch(r -> r.getCode().equals("13")));
    }

    @Test
    void gettersTest() {
        BudgetRevenue merged13 = BudgetRevenue.findBudgetRevenueWithCode("13");

        // Έλεγχος αν οι getters επιστρέφουν τα αρχικά ποσά από το setUp
        assertEquals(3906000000L, merged13.getRegularAmount());
        assertEquals(4225000000L, merged13.getPublicInvestmentAmount());
    }

    @Test
    void setPublicInvestmentAmountTest() {
        BudgetRevenue br12 = BudgetRevenue.findBudgetRevenueWithCode("12");
        long initialRegular = br12.getRegularAmount(); // 60.000.000
        long newPIB = 15000000L; // 15 εκ.

        // Ενημέρωση με update = true: Πρέπει να αλλάξει και το συνολικό amount
        br12.setPublicInvestmentAmount(newPIB, true);

        assertEquals(newPIB, br12.getPublicInvestmentAmount());
        assertEquals(initialRegular + newPIB, br12.getAmount(), "Το συνολικό ποσό (amount) δεν ενημερώθηκε σωστά.");
    }

    @Test
    void setRegularAmountTest() {
        BudgetRevenue br12 = BudgetRevenue.findBudgetRevenueWithCode("12");
        long initialPIB = br12.getPublicInvestmentAmount(); // 0
        long newRegular = 70000000L;

        // Ενημέρωση Regular ποσού με update = true
        br12.setRegularAmount(newRegular, true);

        assertEquals(newRegular, br12.getRegularAmount());
        assertEquals(newRegular + initialPIB, br12.getAmount());
    }


    @Test
    void setRegularAmountNegativeTest() {
        BudgetRevenue br12 = BudgetRevenue.findBudgetRevenueWithCode("12");

        br12.setRegularAmount(-1L, false);
        //Δεν άλλαξε κάτι
        assertEquals(60000000L, br12.getRegularAmount());
    }

    @Test
    void setPublicInvestmentAmountTryCoverageTest13() {
        BudgetRevenue br13 = BudgetRevenue.findBudgetRevenueWithCode("13");

        br13.setPublicInvestmentAmount(-5L, false);
        //Δεν άλλαξε κάτι
        assertEquals(4225000000L, br13.getPublicInvestmentAmount());
    }

    @Test
    void toStringFormattingTest() {
        BudgetRevenue br13 = BudgetRevenue.findBudgetRevenueWithCode("13");
        String output = br13.toString();

        // Έλεγχος αν η toString περιέχει τα πεδία της super (BudgetEntry)
        assertTrue(output.contains("Code: 13"));
        assertTrue(output.contains("Description: Μεταβιβάσεις"));
    }
}