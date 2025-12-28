package com.financial.entries;

import com.financial.services.BudgetType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EntityChangesTest {

    @BeforeEach
    void setup() {
        // Καθαρισμός όλων των στατικών λιστών για αποφυγή παρεμβολών μεταξύ των tests
        Entity.getEntities().clear();
        RegularBudgetExpense.getAllRegularBudgetExpenses().clear();
        PublicInvestmentBudgetNationalExpense.getAllPublicInvestmentBudgetNationalExpenses().clear();
        PublicInvestmentBudgetCoFundedExpense.getAllPublicInvestmentBudgetCoFundedExpenses().clear();

        // -----------------------------------------------------------------------------------
        // 1. ΠΡΟΕΔΡΙΑ ΤΗΣ ΔΗΜΟΚΡΑΤΙΑΣ (1001) - ΤΑΚΤΙΚΟΣ ΠΡΟΫΠΟΛΟΓΙΣΜΟΣ
        // -----------------------------------------------------------------------------------
        new RegularBudgetExpense("1001", "Προεδρία της Δημοκρατίας", "1001-101-000000", "Προεδρία της Δημοκρατίας", "21", "Παροχές σε εργαζομένους", "ΕΞΟΔΑ", 3532000L);
        new RegularBudgetExpense("1001", "Προεδρία της Δημοκρατίας", "1001-101-000000", "Προεδρία της Δημοκρατίας", "23", "Μεταβιβάσεις", "ΕΞΟΔΑ", 203000L);
        new RegularBudgetExpense("1001", "Προεδρία της Δημοκρατίας", "1001-101-000000", "Προεδρία της Δημοκρατίας", "24", "Αγορές αγαθών και υπηρεσιών", "ΕΞΟΔΑ", 850000L);
        new RegularBudgetExpense("1001", "Προεδρία της Δημοκρατίας", "1001-101-000000", "Προεδρία της Δημοκρατίας", "31", "Πάγια περιουσιακά στοιχεία", "ΕΞΟΔΑ", 53000L);

        // -----------------------------------------------------------------------------------
        // 2. ΒΟΥΛΗ ΤΩΝ ΕΛΛΗΝΩΝ (1003) - ΤΑΚΤΙΚΟΣ & ΠΔΕ
        // -----------------------------------------------------------------------------------
        // Τακτικός: Γραφεία Προέδρου (101)
        new RegularBudgetExpense("1003", "Βουλή των Ελλήνων", "1003-101-000000", "Γραφεία Προέδρου και Αντιπροέδρων της Βουλής", "21", "Παροχές σε εργαζομένους", "ΕΞΟΔΑ", 6423000L);
        new RegularBudgetExpense("1003", "Βουλή των Ελλήνων", "1003-101-000000", "Γραφεία Προέδρου και Αντιπροέδρων της Βουλής", "22", "Κοινωνικές παροχές", "ΕΞΟΔΑ", 12000L);

        // Τακτικός: Γενική Γραμματεία Βουλής (201)
        new RegularBudgetExpense("1003", "Βουλή των Ελλήνων", "1003-201-000000", "Γενική Γραμματεία της Βουλής των Ελλήνων", "21", "Παροχές σε εργαζομένους", "ΕΞΟΔΑ", 44609000L);
        new RegularBudgetExpense("1003", "Βουλή των Ελλήνων", "1003-201-000000", "Γενική Γραμματεία της Βουλής των Ελλήνων", "22", "Κοινωνικές παροχές", "ΕΞΟΔΑ", 716000L);
        new RegularBudgetExpense("1003", "Βουλή των Ελλήνων", "1003-201-000000", "Γενική Γραμματεία της Βουλής των Ελλήνων", "23", "Μεταβιβάσεις", "ΕΞΟΔΑ", 6318000L);
        new RegularBudgetExpense("1003", "Βουλή των Ελλήνων", "1003-201-000000", "Γενική Γραμματεία της Βουλής των Ελλήνων", "24", "Αγορές αγαθών και υπηρεσιών", "ΕΞΟΔΑ", 15605000L);
        new RegularBudgetExpense("1003", "Βουλή των Ελλήνων", "1003-201-000000", "Γενική Γραμματεία της Βουλής των Ελλήνων", "29", "Πιστώσεις υπό κατανομή", "ΕΞΟΔΑ", 100000L);
        new RegularBudgetExpense("1003", "Βουλή των Ελλήνων", "1003-201-000000", "Γενική Γραμματεία της Βουλής των Ελλήνων", "31", "Πάγια περιουσιακά στοιχεία", "ΕΞΟΔΑ", 3208000L);
        new RegularBudgetExpense("1003", "Βουλή των Ελλήνων", "1003-201-000000", "Γενική Γραμματεία της Βουλής των Ελλήνων", "33", "Τιμαλφή", "ΕΞΟΔΑ", 80000L);

        // ΠΔΕ: Λοιπές αυτοτελείς Υπηρεσίες (501) - Συγχρηματοδοτούμενο
        new PublicInvestmentBudgetCoFundedExpense("1003", "Βουλή των Ελλήνων", "1003-501-000000", "Λοιπές αυτοτελείς Υπηρεσίες και μονάδες", "29", "Πιστώσεις υπό κατανομή", "ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ", "ΕΞΟΔΑ", 2000000L);

        // -----------------------------------------------------------------------------------
        // 3. ΠΡΟΕΔΡΙΑ ΤΗΣ ΚΥΒΕΡΝΗΣΗΣ (1004) - ΠΔΕ (ΕΘΝΙΚΟ & ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ)
        // -----------------------------------------------------------------------------------
        // Εθνικό Σκέλος
        new PublicInvestmentBudgetNationalExpense("1004", "Προεδρία της Κυβέρνησης", "1004-201-000000", "Γενική Γραμματεία του Πρωθυπουργού", "29", "Πιστώσεις υπό κατανομή", "ΕΘΝΙΚΟ", "ΕΞΟΔΑ", 1500000L);
        new PublicInvestmentBudgetNationalExpense("1004", "Προεδρία της Κυβέρνησης", "1004-202-000000", "Γενική Γραμματεία Νομικών και Κοινοβουλευτικών Θεμάτων", "29", "Πιστώσεις υπό κατανομή", "ΕΘΝΙΚΟ", "ΕΞΟΔΑ", 1500000L);

        // Συγχρηματοδοτούμενο Σκέλος
        new PublicInvestmentBudgetCoFundedExpense("1004", "Προεδρία της Κυβέρνησης", "1004-201-000000", "Γενική Γραμματεία του Πρωθυπουργού", "29", "Πιστώσεις υπό κατανομή", "ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ", "ΕΞΟΔΑ", 1000000L);

        // -----------------------------------------------------------------------------------
        // ΑΡΧΙΚΟΠΟΙΗΣΗ ENTITIES (Σύνδεση με τα παραπάνω δεδομένα)
        // -----------------------------------------------------------------------------------
        new Entity("1001", "Προεδρία της Δημοκρατίας");
        new Entity("1003", "Βουλή των Ελλήνων");
        new Entity("1004", "Προεδρία της Κυβέρνησης");
    }

    @Test
    void implementChangesInAllRegularExpensesWithFixedAmountTest() {
        Entity entity = Entity.findEntityWithEntityCode("1001");
        // Πριν: 3.532.000 (21), 203.000 (23), 850.000 (24), 53.000 (31)

        entity.implementChangesInAllExpenseCategoriesOfAllServices(0.0, 10000, BudgetType.REGULAR_BUDGET);

        // Έλεγχος συνολικού αθροίσματος
        assertEquals(4678000L, entity.calculateRegularSum());

        // Έλεγχος συγκεκριμένων λογαριασμών
        ArrayList<RegularBudgetExpense> expenses = entity.getRegularBudgetExpenses();
        assertEquals(3542000L, expenses.get(0).getAmount()); // 21: 3.532.000 + 10.000
        assertEquals(63000L, expenses.get(3).getAmount());   // 31: 53.000 + 10.000
    }

    @Test
    void implementChangesInAllNationalPIBExpensesWithPercentageTest() {
        Entity entity = Entity.findEntityWithEntityCode("1004");
        // Πριν: 1.500.000 (201), 1.500.000 (202)

        entity.implementChangesInAllExpenseCategoriesOfAllServices(0.20, 0, BudgetType.PUBLIC_INVESTMENT_BUDGET_NATIONAL);

        assertEquals(3600000L, entity.calculatePublicInvestmentNationalSum());

        // Έλεγχος δαπάνης συγκεκριμένης υπηρεσίας
        // 1.500.000 * 1.20 = 1.800.000
        long service201Amount = entity.getPublicInvestmentNationalSumOfServiceWithCode("1004-201-000000");
        assertEquals(1800000L, service201Amount);
    }

    @Test
    void implementChangesInAllCoFundedPIBExpensesWithPercentageTest() {
        Entity entity = Entity.findEntityWithEntityCode("1003");
        // Πριν: 2.000.000 (501)

        entity.implementChangesInAllExpenseCategoriesOfAllServices(0.05, 0, BudgetType.PUBLIC_INVESTMENT_BUDGET_COFUNDED);

        assertEquals(2100000L, entity.calculatePublicInvestmentCoFundedSum());

        // Έλεγχος μοναδικής δαπάνης
        // 2.000.000 * 1.05 = 2.100.000
        ArrayList<BudgetExpense> expenses = entity.getPublicInvestmentCoFundedExpensesOfServiceWithCode("1003-501-000000");
        assertEquals(2100000L, expenses.get(0).getAmount());
    }

}
