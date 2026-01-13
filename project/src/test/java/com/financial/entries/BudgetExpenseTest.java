package com.financial.entries;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Map;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class BudgetExpenseTest {

    @BeforeEach
    void setup() {
        // Καθαρισμός όλων των στατικών λιστών για αποφυγή παρεμβολών μεταξύ των tests
        BudgetExpense.getBudgetExpenses().clear();
        RegularBudgetExpense.getAllRegularBudgetExpenses().clear();
        PublicInvestmentBudgetExpense.getAllPublicInvestmentBudgetExpenses().clear();
        PublicInvestmentBudgetNationalExpense.getAllPublicInvestmentBudgetNationalExpenses().clear();
        PublicInvestmentBudgetCoFundedExpense.getAllPublicInvestmentBudgetCoFundedExpenses().clear();

        // Εισαγωγή δεδομένων Τακτικού Προϋπολογισμού
        new RegularBudgetExpense("1001", "ΠΡΟΕΔΡΙΑ ΤΗΣ ΔΗΜΟΚΡΑΤΙΑΣ", "1001-101-0000000", "Προεδρία της Δημοκρατίας", "21", "Παροχές σε εργαζομένους", "ΕΞΟΔΑ", 3532000L);
        new RegularBudgetExpense("1001", "ΠΡΟΕΔΡΙΑ ΤΗΣ ΔΗΜΟΚΡΑΤΙΑΣ", "1001-101-0000000", "Προεδρία της Δημοκρατίας", "23", "Μεταβιβάσεις", "ΕΞΟΔΑ", 203000L);
        new RegularBudgetExpense("1001", "ΠΡΟΕΔΡΙΑ ΤΗΣ ΔΗΜΟΚΡΑΤΙΑΣ", "1001-101-0000000", "Προεδρία της Δημοκρατίας", "24", "Αγορές αγαθών και υπηρεσιών", "ΕΞΟΔΑ", 850000L);
        new RegularBudgetExpense("1001", "ΠΡΟΕΔΡΙΑ ΤΗΣ ΔΗΜΟΚΡΑΤΙΑΣ", "1001-101-0000000", "Προεδρία της Δημοκρατίας", "31", "Πάγια περιουσιακά στοιχεία", "ΕΞΟΔΑ", 53000L);

        // Εθνικό Σκέλος ΠΔΕ
        new PublicInvestmentBudgetNationalExpense("1004", "Υπουργείο Επικρατείας", "1004-201-0000000", "Γραμματεία Πρωθυπουργού", "29", "Πιστώσεις υπό κατανομή", "ΕΘΝΙΚΟ", "ΕΞΟΔΑ", 1500000L);
        new PublicInvestmentBudgetNationalExpense("1004", "Υπουργείο Επικρατείας", "1004-202-0000000", "Νομικών & Κοινοβουλευτικών Θεμάτων", "29", "Πιστώσεις υπό κατανομή", "ΕΘΝΙΚΟ", "ΕΞΟΔΑ", 1500000L);
        new PublicInvestmentBudgetNationalExpense("1007", "Υπουργείο Εσωτερικών", "1007-209-0000000", "Αυτοδιοίκησης και Αποκέντρωσης", "29", "Πιστώσεις υπό κατανομή", "ΕΘΝΙΚΟ", "ΕΞΟΔΑ", 149000000L);

        // Συγχρηματοδοτούμενο Σκέλος ΠΔΕ
        new PublicInvestmentBudgetCoFundedExpense("1007", "Υπουργείο Εσωτερικών", "1007-207-0000000", "Γενική Γραμματεία Δημόσιας Διοίκησης", "29", "Πιστώσεις υπό κατανομή", "ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ", "ΕΞΟΔΑ", 6000000L);
        new PublicInvestmentBudgetCoFundedExpense("1007", "Υπουργείο Εσωτερικών", "1007-209-0000000", "Γενική Γραμματεία Αυτοδιοίκησης και Αποκέντρωσης", "29", "Πιστώσεις υπό κατανομή", "ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ", "ΕΞΟΔΑ", 73000000L);
        new PublicInvestmentBudgetCoFundedExpense("1007", "Υπουργείο Εσωτερικών", "1007-999-0100000", "Γενική Διεύθυνση Μακεδονίας και Θράκης", "29", "Πιστώσεις υπό κατανομή", "ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ", "ΕΞΟΔΑ", 1000000L);
    }

    @Test
    void calculateSumTest() {
        // Τακτικός: 3.532.000 + 203.000 + 850.000 + 53.000 = 4.638.000
        // ΠΔΕ Εθνικό: 1.500.000 + 1.500.000 + 149.000.000 = 152.000.000
        // ΠΔΕ Συγχρηματοδοτούμενο: 6.000.000 + 73.000.000 + 1.000.000 = 80.000.000
        // Σύνολο ΠΔΕ: 232.000.000
        // Γενικό Σύνολο: 4.638.000 + 232.000.000 = 236.638.000

        long expectedTotal = 236638000L;
        assertEquals(expectedTotal, BudgetExpense.calculateSum());
    }

    @Test
    void getSumOfEveryBudgetExpenseCategoryTest() {
        Map<String, Long> summary = BudgetExpense.getSumOfEveryBudgetExpenseCategory();

        // Έλεγχος κατηγορίας από τον Τακτικό
        assertEquals(3532000L, summary.get("21"));
        // Έλεγχος κατηγορίας από το ΠΔΕ (ενοποιημένο Εθνικό + Συγχρηματοδοτούμενο)
        assertEquals(232000000L, summary.get("29"));
        // Έλεγχος μεγέθους Map (Κατηγορίες: 21, 23, 24, 31, 29)
        assertEquals(5, summary.size());
    }

    @Test
    void getDescriptionWithCodeTest() {
        String description = BudgetExpense.getDescriptionWithCode("21");
        assertEquals("Παροχές σε εργαζομένους", description);

        String pibDescription = BudgetExpense.getDescriptionWithCode("29");
        assertEquals("Πιστώσεις υπό κατανομή", pibDescription);
    }

    @Test
    void equalsAndHashCodeTest() {
        BudgetExpense e1 = new BudgetExpense("1001", "ORG", "S1", "NAME", "CODE1", "DESC", "CAT", 100L);

        assertTrue(e1.equals(e1));

        assertFalse(e1.equals(null));

        assertFalse(e1.equals("Not a BudgetExpense object"));

        BudgetExpense e2 = new BudgetExpense("1001", "OTHER_ORG", "S1", "OTHER_NAME", "CODE1", "OTHER_DESC", "OTHER_CAT", 500L);
        assertTrue(e1.equals(e2));
        assertEquals(e1.hashCode(), e2.hashCode());

        assertFalse(e1.equals(new BudgetExpense("1002", "ORG", "S1", "NAME", "CODE1", "DESC", "CAT", 100L)));
        assertFalse(e1.equals(new BudgetExpense("1001", "ORG", "S2", "NAME", "CODE1", "DESC", "CAT", 100L)));
        assertFalse(e1.equals(new BudgetExpense("1001", "ORG", "S1", "NAME", "CODE2", "DESC", "CAT", 100L)));
    }

    @Test
    void getEntityNameTest() {
        RegularBudgetExpense expense = RegularBudgetExpense.getAllRegularBudgetExpenses().get(0);
        assertEquals("ΠΡΟΕΔΡΙΑ ΤΗΣ ΔΗΜΟΚΡΑΤΙΑΣ", expense.getEntityName());
    }

    @Test
    void testStaticListSize() {
        // Στο setup δημιουργούμε 4 (Regular) + 3 (National) + 3 (CoFunded) = 10 εγγραφές
        assertEquals(10, BudgetExpense.getBudgetExpenses().size());
    }
}