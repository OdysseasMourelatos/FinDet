package com.financial.entries;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PublicInvestmentBudgetExpenseTest {

    @BeforeEach
    void setup() {
        // Καθαρισμός όλων των στατικών λιστών για αποφυγή παρεμβολών
        PublicInvestmentBudgetExpense.getAllPublicInvestmentBudgetExpenses().clear();
        PublicInvestmentBudgetNationalExpense.getAllPublicInvestmentBudgetNationalExpenses().clear();
        PublicInvestmentBudgetCoFundedExpense.getAllPublicInvestmentBudgetCoFundedExpenses().clear();

        // Εθνικό Σκέλος
        new PublicInvestmentBudgetNationalExpense("1004", "Υπουργείο Επικρατείας", "1004-201-0000000", "Γραμματεία Πρωθυπουργού", "29", "Πιστώσεις υπό κατανομή", "ΕΘΝΙΚΟ", "ΕΞΟΔΑ", 1500000L);
        new PublicInvestmentBudgetNationalExpense("1004", "Υπουργείο Επικρατείας", "1004-202-0000000", "Νομικών & Κοινοβουλευτικών Θεμάτων", "29", "Πιστώσεις υπό κατανομή", "ΕΘΝΙΚΟ", "ΕΞΟΔΑ", 1500000L);
        new PublicInvestmentBudgetNationalExpense("1007", "Υπουργείο Εσωτερικών", "1007-209-0000000", "Αυτοδιοίκησης και Αποκέντρωσης", "29", "Πιστώσεις υπό κατανομή", "ΕΘΝΙΚΟ", "ΕΞΟΔΑ", 149000000L);
        new PublicInvestmentBudgetNationalExpense("1007", "Υπουργείο Εσωτερικών", "1007-702-0000000", "Ταμείο Ανάκαμψης", "29", "Πιστώσεις υπό κατανομή", "ΕΘΝΙΚΟ", "ΕΞΟΔΑ", 121000000L);
        new PublicInvestmentBudgetNationalExpense("1007", "Υπουργείο Εσωτερικών", "1007-705-0000000", "Ταμείο Ανάκαμψης (Ανθρ. Δυναμικό)", "29", "Πιστώσεις υπό κατανομή", "ΕΘΝΙΚΟ", "ΕΞΟΔΑ", 30000000L);
        new PublicInvestmentBudgetNationalExpense("1007", "Υπουργείο Εσωτερικών", "1007-999-0100000", "Γενική Διεύθυνση Μακεδονίας και Θράκης", "29", "Πιστώσεις υπό κατανομή", "ΕΘΝΙΚΟ", "ΕΞΟΔΑ", 1000000L);

        // Συγχρηματοδοτούμενο Σκέλος
        new PublicInvestmentBudgetCoFundedExpense("1007", "Υπουργείο Εσωτερικών", "1007-207-0000000", "Γενική Γραμματεία Δημόσιας Διοίκησης", "29", "Πιστώσεις υπό κατανομή", "ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ", "ΕΞΟΔΑ", 6000000L);
        new PublicInvestmentBudgetCoFundedExpense("1007", "Υπουργείο Εσωτερικών", "1007-209-0000000", "Γενική Γραμματεία Αυτοδιοίκησης και Αποκέντρωσης", "29", "Πιστώσεις υπό κατανομή", "ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ", "ΕΞΟΔΑ", 73000000L);
        new PublicInvestmentBudgetCoFundedExpense("1007", "Υπουργείο Εσωτερικών", "1007-999-0100000", "Γενική Διεύθυνση Μακεδονίας και Θράκης", "29", "Πιστώσεις υπό κατανομή", "ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ", "ΕΞΟΔΑ", 1000000L);

        new PublicInvestmentBudgetCoFundedExpense("1009", "Υπουργείο Εξωτερικών", "1009-201-0000000", "Υπηρεσιακή Γενική Γραμματεία", "29", "Πιστώσεις υπό κατανομή", "ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ", "ΕΞΟΔΑ", 672000L);
        new PublicInvestmentBudgetCoFundedExpense("1009", "Υπουργείο Εξωτερικών", "1009-206-0000000", "Γενική Γραμματεία Διεθνών Οικονομικών Σχέσεων", "29", "Πιστώσεις υπό κατανομή", "ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ", "ΕΞΟΔΑ", 512000L);
        new PublicInvestmentBudgetCoFundedExpense("1009", "Υπουργείο Εξωτερικών", "1009-501-0000000", "Λοιπές μονάδες/αυτοτελείς μονάδες", "29", "Πιστώσεις υπό κατανομή", "ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ", "ΕΞΟΔΑ", 16000L);
        new PublicInvestmentBudgetCoFundedExpense("1009", "Υπουργείο Εξωτερικών", "1009-701-0000000", "Δαπάνες μεταναστευτικών ροών", "29", "Πιστώσεις υπό κατανομή", "ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ", "ΕΞΟΔΑ", 10800000L);
    }

    @Test
    void calculateSumTest() {
        // Αναμενόμενο σύνολο: Εθνικό (304.000.000) + Συγχρηματοδοτούμενο (92.000.000)
        long expectedSum = 396000000L;
        assertEquals(expectedSum, PublicInvestmentBudgetExpense.calculateSum());
    }

    @Test
    void getSumOfEveryPublicInvestmentExpenseCategoryTest() {
        Map<String, Long> summary = PublicInvestmentBudgetExpense.getSumOfEveryPublicInvestmentExpenseCategory();

        assertTrue(summary.containsKey("29"));
        assertEquals(396000000L, summary.get("29"));
    }

    @Test
    void getAllPublicInvestmentBudgetExpensesTest() {
        ArrayList<PublicInvestmentBudgetExpense> allExpenses = PublicInvestmentBudgetExpense.getAllPublicInvestmentBudgetExpenses();

        // Έχουμε 6 εθνικές και 7 συγχρηματοδοτούμενες εγγραφές
        assertEquals(13, allExpenses.size());
    }

    @Test
    void getDescriptionWithCodeTest() {
        String description = PublicInvestmentBudgetExpense.getDescriptionWithCode("29");
        assertEquals("Πιστώσεις υπό κατανομή", description);
    }

    @Test
    void getTypeTest() {
        PublicInvestmentBudgetExpense expense = PublicInvestmentBudgetExpense.getAllPublicInvestmentBudgetExpenses().get(0);
        assertNotNull(expense.getType());
        assertTrue(expense.getType().equals("ΕΘΝΙΚΟ") || expense.getType().equals("ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ"));
    }
}