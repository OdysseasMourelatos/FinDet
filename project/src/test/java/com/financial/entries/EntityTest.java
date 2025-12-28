package com.financial.entries;

import org.junit.jupiter.api.BeforeEach;

public class EntityTest {

    @BeforeEach
    void setup() {
        // Καθαρισμός όλων των στατικών λιστών
        Entity.getEntities().clear();
        RegularBudgetExpense.getAllRegularBudgetExpenses().clear();
        PublicInvestmentBudgetNationalExpense.getAllPublicInvestmentBudgetNationalExpenses().clear();
        PublicInvestmentBudgetCoFundedExpense.getAllPublicInvestmentBudgetCoFundedExpenses().clear();

        // 1. Δεδομένα ΠΡΟΕΔΡΙΑΣ ΤΗΣ ΔΗΜΟΚΡΑΤΙΑΣ (1001) - Τακτικός
        new RegularBudgetExpense("1001", "Προεδρία της Δημοκρατίας", "1001-101-000000", "Προεδρία της Δημοκρατίας", "21", "Παροχές σε εργαζομένους","ΕΞΟΔΑ", 3532000L);
        new RegularBudgetExpense("1001", "Προεδρία της Δημοκρατίας", "1001-101-000000", "Προεδρία της Δημοκρατίας", "23", "Μεταβιβάσεις", "ΕΞΟΔΑ",203000L);
        new RegularBudgetExpense("1001", "Προεδρία της Δημοκρατίας", "1001-101-000000", "Προεδρία της Δημοκρατίας", "24", "Αγορές αγαθών", "ΕΞΟΔΑ",850000L);
        new RegularBudgetExpense("1001", "Προεδρία της Δημοκρατίας", "1001-101-000000", "Προεδρία της Δημοκρατίας", "31", "Πάγια", "ΕΞΟΔΑ",53000L);

        // 2. Δεδομένα ΒΟΥΛΗΣ (1003) - Τακτικός & ΠΔΕ
        new RegularBudgetExpense("1003", "Βουλή των Ελλήνων", "1003-101-000000", "Γραφεία Προέδρου", "21", "Παροχές", "ΕΞΟΔΑ",6423000L);
        new PublicInvestmentBudgetCoFundedExpense("1003", "Βουλή των Ελλήνων", "1003-501-000000", "Λοιπές αυτοτελείς μονάδες", "29", "Πιστώσεις υπό κατανομή", "ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ", "ΕΞΟΔΑ", 2000000L);

        // 3. Δεδομένα ΠΡΟΕΔΡΙΑΣ ΚΥΒΕΡΝΗΣΗΣ (1004) - ΠΔΕ
        new PublicInvestmentBudgetNationalExpense("1004", "Προεδρία της Κυβέρνησης", "1004-201-000000", "ΓΓ Πρωθυπουργού", "29", "Πιστώσεις", "ΕΘΝΙΚΟ", "ΕΞΟΔΑ", 1500000L);
        new PublicInvestmentBudgetNationalExpense("1004", "Προεδρία της Κυβέρνησης", "1004-202-000000", "ΓΓ Νομικών Θεμάτων", "29", "Πιστώσεις", "ΕΘΝΙΚΟ", "ΕΞΟΔΑ", 1500000L);
        new PublicInvestmentBudgetCoFundedExpense("1004", "Προεδρία της Κυβέρνησης", "1004-201-000000", "ΓΓ Πρωθυπουργού", "29", "Πιστώσεις", "ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ", "ΕΞΟΔΑ", 1000000L);

        // Αρχικοποίηση Entities
        new Entity("1001", "Προεδρία της Δημοκρατίας");
        new Entity("1003", "Βουλή των Ελλήνων");
        new Entity("1004", "Προεδρία της Κυβέρνησης");
    }
}