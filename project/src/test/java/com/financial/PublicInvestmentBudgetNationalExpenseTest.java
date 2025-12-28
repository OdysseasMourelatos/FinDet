package com.financial;


import com.financial.entries.PublicInvestmentBudgetCoFundedExpense;
import com.financial.entries.PublicInvestmentBudgetNationalExpense;
import org.junit.jupiter.api.BeforeEach;

public class PublicInvestmentBudgetNationalExpenseTest {

    @BeforeEach
    void setup() {
        // Καθαρισμός πριν από κάθε test για αποφυγή διπλότυπων
        PublicInvestmentBudgetNationalExpense.getAllPublicInvestmentBudgetNationalExpenses().clear();

        new PublicInvestmentBudgetNationalExpense("1004", "Υπουργείο Επικρατείας", "201", "Γραμματεία Πρωθυπουργού", "10042010", "Πιστώσεις υπό κατανομή", "ΕΘΝΙΚΟ", "ΕΞΟΔΑ", 1500000L);
        new PublicInvestmentBudgetNationalExpense("1004", "Υπουργείο Επικρατείας", "202", "Νομικών & Κοινοβουλευτικών Θεμάτων", "10042020", "Πιστώσεις υπό κατανομή", "ΕΘΝΙΚΟ", "ΕΞΟΔΑ", 1500000L);
        new PublicInvestmentBudgetNationalExpense("1007", "Υπουργείο Εσωτερικών", "209", "Αυτοδιοίκησης και Αποκέντρωσης", "10072090", "Πιστώσεις υπό κατανομή", "ΕΘΝΙΚΟ", "ΕΞΟΔΑ", 149000000L);
        new PublicInvestmentBudgetNationalExpense("1007", "Υπουργείο Εσωτερικών", "702", "Ταμείο Ανάκαμψης", "10077020", "Πιστώσεις υπό κατανομή", "ΕΘΝΙΚΟ", "ΕΞΟΔΑ", 121000000L);
        new PublicInvestmentBudgetNationalExpense("1007", "Υπουργείο Εσωτερικών", "705", "Ταμείο Ανάκαμψης (Ανθρ. Δυναμικό)", "10077050", "Πιστώσεις υπό κατανομή", "ΕΘΝΙΚΟ", "ΕΞΟΔΑ", 30000000L);
        new PublicInvestmentBudgetNationalExpense("1007", "Υπουργείο Εσωτερικών", "999", "Μακεδονίας και Θράκης", "10079990", "Πιστώσεις υπό κατανομή", "ΕΘΝΙΚΟ", "ΕΞΟΔΑ", 1000000L);

    }
}
