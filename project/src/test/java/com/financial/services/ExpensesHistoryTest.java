package com.financial.services;

import com.financial.entries.PublicInvestmentBudgetCoFundedExpense;
import com.financial.entries.RegularBudgetExpense;
import com.financial.services.expenses.ExpensesHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ExpensesHistoryTest {

    @BeforeEach
    void setup() {
        // Καθαρισμός των static Deques πριν από κάθε test
        ExpensesHistory.getHistoryDeque().clear();
        ExpensesHistory.getTypeDeque().clear();

        // Καθαρισμός και της λίστας εξόδων αν χρειάζεται
        RegularBudgetExpense.getAllRegularBudgetExpenses().clear();
    }

    @Test
    void testKeepHistoryAndGetters() {
        RegularBudgetExpense e1 = new RegularBudgetExpense("1001", "ORG", "S1", "SERV", "E1", "DESC", "CAT", 1000L);
        ArrayList<RegularBudgetExpense> list = new ArrayList<>();
        list.add(e1);

        ExpensesHistory.keepHistory(list, BudgetType.REGULAR_BUDGET);

        // Έλεγχος των Getters
        assertNotNull(ExpensesHistory.getHistoryDeque());
        assertNotNull(ExpensesHistory.getTypeDeque());
        assertEquals(1, ExpensesHistory.getHistoryDeque().size());
        assertEquals(BudgetType.REGULAR_BUDGET, ExpensesHistory.getMostRecentBudgetType());

        // Έλεγχος αν το περιεχόμενο του snapshot είναι σωστό
        Map<String[], Long> snapshot = ExpensesHistory.getMostRecentExpensesHistory();
        assertNotNull(snapshot);
        assertEquals(1000L, snapshot.values().iterator().next());
    }

    @Test
    void testReturnToPreviousStateMultipleSnapshots() {
        // 1. Setup αρχικού αντικειμένου
        RegularBudgetExpense expense = new RegularBudgetExpense("1001", "NAME", "S1", "SNAME", "E1", "DESC", "REGULAR", 5000L);
        ArrayList<RegularBudgetExpense> list = new ArrayList<>();
        list.add(expense);

        // Snapshot 1: Ποσό 5000
        ExpensesHistory.keepHistory(list, BudgetType.REGULAR_BUDGET);

        // Snapshot 2: Ποσό 7000
        expense.setAmount(7000L);
        ExpensesHistory.keepHistory(list, BudgetType.REGULAR_BUDGET);

        // Αλλαγή σε 10000
        expense.setAmount(10000L);

        // 1ο Undo -> Επιστροφή στο 7000
        ExpensesHistory.returnToPreviousState();
        assertEquals(7000L, expense.getAmount());
        assertEquals(1, ExpensesHistory.getHistoryDeque().size());

        // 2ο Undo -> Επιστροφή στο 5000
        ExpensesHistory.returnToPreviousState();
        assertEquals(5000L, expense.getAmount());
        assertTrue(ExpensesHistory.getHistoryDeque().isEmpty());
    }

    @Test
    void testReturnToPreviousStateEmptyHistory() {
        // Έλεγχος NoSuchElementException (Branch Coverage για το catch block)
        // Η λίστα είναι ήδη άδεια λόγω του @BeforeEach
        assertNull(ExpensesHistory.getMostRecentBudgetType());
        assertNull(ExpensesHistory.getMostRecentExpensesHistory());
    }

    @Test
    void testReturnToPreviousStateBulkChanges() {
        // 1. Δημιουργία πολλαπλών εξόδων
        RegularBudgetExpense e1 = new RegularBudgetExpense("1001", "ORG", "S1", "SERV1", "21", "Μισθοί", "ΕΞΟΔΑ", 1000L);
        RegularBudgetExpense e2 = new RegularBudgetExpense("1001", "ORG", "S1", "SERV1", "23", "Ασφάλιση", "ΕΞΟΔΑ", 2000L);
        RegularBudgetExpense e3 = new RegularBudgetExpense("1001", "ORG", "S2", "SERV2", "21", "Μισθοί", "ΕΞΟΔΑ", 3000L);

        ArrayList<RegularBudgetExpense> allExpenses = RegularBudgetExpense.getAllRegularBudgetExpenses();

        // 2. Κρατάμε snapshot ΠΡΙΝ τη μαζική αλλαγή
        ExpensesHistory.keepHistory(allExpenses, BudgetType.REGULAR_BUDGET);

        // 3. Προσομοίωση μαζικής αλλαγής (π.χ. +50% σε όλα)
        for (RegularBudgetExpense e : allExpenses) {
            e.setAmount((long) (e.getAmount() * 1.5));
        }
        assertEquals(1500L, e1.getAmount());
        assertEquals(3000L, e2.getAmount());
        assertEquals(4500L, e3.getAmount());

        // 4. Εκτέλεση Undo
        ExpensesHistory.returnToPreviousState();

        // 5. Έλεγχος αν ΟΛΟΙ οι λογαριασμοί επέστρεψαν στις αρχικές τιμές
        assertEquals(1000L, e1.getAmount());
        assertEquals(2000L, e2.getAmount());
        assertEquals(3000L, e3.getAmount());

        assertTrue(ExpensesHistory.getHistoryDeque().isEmpty());
    }

    @Test
    void returnToPreviousStateTriggerCatchTest() {
        ExpensesHistory.getHistoryDeque().clear();
        ExpensesHistory.getTypeDeque().clear();

        assertDoesNotThrow(() -> {
            ExpensesHistory.returnToPreviousState();
        });
    }

    @Test
    void returnToPreviousStateCoFundedTest() {

        PublicInvestmentBudgetCoFundedExpense coFundedExpense = new PublicInvestmentBudgetCoFundedExpense("1001", "NAME", "S1", "SNAME", "E1", "DESC", "COFUNDED", "ΕΞΟΔΑ", 2000L);
        ArrayList<PublicInvestmentBudgetCoFundedExpense> list = new ArrayList<>();
        list.add(coFundedExpense);

        ExpensesHistory.keepHistory(list, BudgetType.PUBLIC_INVESTMENT_BUDGET_COFUNDED);

        coFundedExpense.setAmount(9999L);

        ExpensesHistory.returnToPreviousState();

        assertEquals(2000L, coFundedExpense.getAmount());
    }

    @Test
    void returnToPreviousStateNullCaseTest() {
        ExpensesHistory.getHistoryDeque().clear();
        ExpensesHistory.getTypeDeque().clear();

        assertDoesNotThrow(() -> {
            ExpensesHistory.returnToPreviousState();
        });

        // 3. Επιβεβαιώνουμε ότι οι στοίβες παρέμειναν άδειες
        assertTrue(ExpensesHistory.getHistoryDeque().isEmpty());
        assertTrue(ExpensesHistory.getTypeDeque().isEmpty());
    }
}
