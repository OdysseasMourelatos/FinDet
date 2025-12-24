package com.financial;

import com.financial.entries.RegularBudgetExpense;
import com.financial.services.BudgetType;
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
}
