package com.financial;


import com.financial.entries.PublicInvestmentBudgetCoFundedExpense;
import org.junit.jupiter.api.BeforeEach;

public class PublicInvestmentBudgetCoFundedExpenseTest {

    @BeforeEach
    void setup() {
        // Καθαρισμός πριν από κάθε test για αποφυγή διπλότυπων
        PublicInvestmentBudgetCoFundedExpense.getAllPublicInvestmentBudgetCoFundedExpenses().clear();

        new PublicInvestmentBudgetCoFundedExpense("1007", "Υπουργείο Εσωτερικών", "207", "Γενική Γραμματεία Δημόσιας Διοίκησης", "10072070", "Πιστώσεις υπό κατανομή", "ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ", "ΕΞΟΔΑ", 6000000L);
        new PublicInvestmentBudgetCoFundedExpense("1007", "Υπουργείο Εσωτερικών", "209", "Γενική Γραμματεία Αυτοδιοίκησης και Αποκέντρωσης", "10072090", "Πιστώσεις υπό κατανομή", "ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ", "ΕΞΟΔΑ", 73000000L);
        new PublicInvestmentBudgetCoFundedExpense("1007", "Υπουργείο Εσωτερικών", "999", "Γενική Διεύθυνση Μακεδονίας και Θράκης", "10079990", "Πιστώσεις υπό κατανομή", "ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ", "ΕΞΟΔΑ", 1000000L);
        new PublicInvestmentBudgetCoFundedExpense("1009", "Υπουργείο Εξωτερικών", "201", "Υπηρεσιακή Γενική Γραμματεία", "10092010", "Πιστώσεις υπό κατανομή", "ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ", "ΕΞΟΔΑ", 672000L);
        new PublicInvestmentBudgetCoFundedExpense("1009", "Υπουργείο Εξωτερικών", "206", "Διεθνών Οικονομικών Σχέσεων και Εξωστρέφειας", "10092060", "Πιστώσεις υπό κατανομή", "ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ", "ΕΞΟΔΑ", 512000L);
        new PublicInvestmentBudgetCoFundedExpense("1009", "Υπουργείο Εξωτερικών", "501", "Λοιπές αυτοτελείς μονάδες", "10095010", "Πιστώσεις υπό κατανομή", "ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ", "ΕΞΟΔΑ", 16000L);
        new PublicInvestmentBudgetCoFundedExpense("1009", "Υπουργείο Εξωτερικών", "701", "Δαπάνες μεταναστευτικών ροών", "10097010", "Πιστώσεις υπό κατανομή", "ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ", "ΕΞΟΔΑ", 10800000L);
    }
}