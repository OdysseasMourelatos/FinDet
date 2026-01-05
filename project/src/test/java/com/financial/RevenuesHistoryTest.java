package com.financial;

import com.financial.entries.*;
import com.financial.services.BudgetType;
import com.financial.services.revenues.RevenuesHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RevenuesHistoryTest {

    @BeforeEach
    void setup() {
        // Καθαρισμός των static Deques πριν από κάθε test
        RevenuesHistory.getHistoryDeque().clear();
        RevenuesHistory.getTypeDeque().clear();

        // Καθαρισμός των λιστών εσόδων για να ξεκινάμε από μηδενική βάση
        RegularBudgetRevenue.getAllRegularBudgetRevenues().clear();
        PublicInvestmentBudgetNationalRevenue.getAllPublicInvestmentBudgetNationalRevenues().clear();
        PublicInvestmentBudgetCoFundedRevenue.getAllPublicInvestmentBudgetCoFundedRevenues().clear();
    }

    @Test
    void testKeepHistoryAndGetters() {
        // Setup: Δημιουργία ενός εσόδου
        RegularBudgetRevenue r1 = new RegularBudgetRevenue("111", "Φόρος Εισοδήματος", "ΕΣΟΔΑ", 500000L);
        ArrayList<RegularBudgetRevenue> list = new ArrayList<>();
        list.add(r1);

        // Action: Αποθήκευση στο ιστορικό
        RevenuesHistory.keepHistory(list, BudgetType.REGULAR_BUDGET);

        // Verification
        assertEquals(1, RevenuesHistory.getHistoryDeque().size());
        assertEquals(BudgetType.REGULAR_BUDGET, RevenuesHistory.getMostRecentBudgetType());

        Map<String, Long> snapshot = RevenuesHistory.getMostRecentRevenuesHistory();
        assertNotNull(snapshot);
        assertEquals(500000L, snapshot.get("111"));
    }

    @Test
    void testReturnToPreviousStateMultipleTypes() {
        // 1. Setup Τακτικού Εσόδου
        RegularBudgetRevenue reg = new RegularBudgetRevenue("1", "Tax", "Category", 1000L);
        ArrayList<RegularBudgetRevenue> regList = new ArrayList<>();
        regList.add(reg);

        // 2. Setup Εθνικού ΠΔΕ Εσόδου
        PublicInvestmentBudgetNationalRevenue nat = new PublicInvestmentBudgetNationalRevenue("2", "National Fund", "Category", "Type", 2000L);
        ArrayList<PublicInvestmentBudgetNationalRevenue> natList = new ArrayList<>();
        natList.add(nat);

        // Snapshot 1: Regular Budget (1000)
        RevenuesHistory.keepHistory(regList, BudgetType.REGULAR_BUDGET);

        // Snapshot 2: National PIB (2000)
        RevenuesHistory.keepHistory(natList, BudgetType.PUBLIC_INVESTMENT_BUDGET_NATIONAL);

        // Αλλαγές στα ποσά
        reg.setAmount(5000L);
        nat.setAmount(8000L);

        // 1ο Undo -> Επιστροφή του Εθνικού ΠΔΕ στο 2000
        RevenuesHistory.returnToPreviousState();
        assertEquals(2000L, nat.getAmount());
        assertEquals(5000L, reg.getAmount()); // Το Regular δεν έπρεπε να επηρεαστεί ακόμα

        // 2ο Undo -> Επιστροφή του Τακτικού στο 1000
        RevenuesHistory.returnToPreviousState();
        assertEquals(1000L, reg.getAmount());

        assertTrue(RevenuesHistory.getHistoryDeque().isEmpty());
    }

    @Test
    void testReturnToPreviousStateEmptyHistory() {
        // Έλεγχος οριακής περίπτωσης (Branch Coverage για άδειο Deque)
        assertNull(RevenuesHistory.getMostRecentBudgetType());
        assertNull(RevenuesHistory.getMostRecentRevenuesHistory());

        // Δεν πρέπει να πετάξει Exception
        assertDoesNotThrow(RevenuesHistory::returnToPreviousState);
    }

    @Test
    void testReturnToPreviousStateBulkChanges() {
        // 1. Δημιουργία πολλαπλών εσόδων ΠΔΕ (Συγχρηματοδοτούμενο)
        PublicInvestmentBudgetCoFundedRevenue r1 = new PublicInvestmentBudgetCoFundedRevenue("C1", "Project A", "CAT", "COFUND", 10000L);
        PublicInvestmentBudgetCoFundedRevenue r2 = new PublicInvestmentBudgetCoFundedRevenue("C2", "Project B", "CAT", "COFUND", 20000L);

        ArrayList<PublicInvestmentBudgetCoFundedRevenue> coFundedList = PublicInvestmentBudgetCoFundedRevenue.getAllPublicInvestmentBudgetCoFundedRevenues();

        // 2. Snapshot ΠΡΙΝ την αλλαγή
        RevenuesHistory.keepHistory(coFundedList, BudgetType.PUBLIC_INVESTMENT_BUDGET_COFUNDED);

        // 3. Μαζική αύξηση
        for (PublicInvestmentBudgetCoFundedRevenue r : coFundedList) {
            r.setAmount(r.getAmount() + 10000L);
        }
        assertEquals(20000L, r1.getAmount());
        assertEquals(30000L, r2.getAmount());

        // 4. Action: Undo
        RevenuesHistory.returnToPreviousState();

        // 5. Verification: Επαναφορά στις αρχικές τιμές
        assertEquals(10000L, r1.getAmount());
        assertEquals(20000L, r2.getAmount());
    }
}
