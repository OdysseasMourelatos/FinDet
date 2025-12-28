package com.financial.entries;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EntityTest {

    @BeforeEach
    void setup() {
        // Καθαρισμός όλων των στατικών λιστών
        Entity.getEntities().clear();
        RegularBudgetExpense.getAllRegularBudgetExpenses().clear();
        PublicInvestmentBudgetNationalExpense.getAllPublicInvestmentBudgetNationalExpenses().clear();
        PublicInvestmentBudgetCoFundedExpense.getAllPublicInvestmentBudgetCoFundedExpenses().clear();

        // 1. Δεδομένα ΠΡΟΕΔΡΙΑΣ ΤΗΣ ΔΗΜΟΚΡΑΤΙΑΣ (1001) - Τακτικός
        new RegularBudgetExpense("1001", "Προεδρία της Δημοκρατίας", "1001-101-000000", "Προεδρία της Δημοκρατίας", "21", "Παροχές σε εργαζομένους", "ΕΞΟΔΑ", 3532000L);
        new RegularBudgetExpense("1001", "Προεδρία της Δημοκρατίας", "1001-101-000000", "Προεδρία της Δημοκρατίας", "23", "Μεταβιβάσεις", "ΕΞΟΔΑ", 203000L);
        new RegularBudgetExpense("1001", "Προεδρία της Δημοκρατίας", "1001-101-000000", "Προεδρία της Δημοκρατίας", "24", "Αγορές αγαθών", "ΕΞΟΔΑ", 850000L);
        new RegularBudgetExpense("1001", "Προεδρία της Δημοκρατίας", "1001-101-000000", "Προεδρία της Δημοκρατίας", "31", "Πάγια", "ΕΞΟΔΑ", 53000L);

        // 2. Δεδομένα ΒΟΥΛΗΣ (1003) - Τακτικός & ΠΔΕ
        new RegularBudgetExpense("1003", "Βουλή των Ελλήνων", "1003-101-000000", "Γραφεία Προέδρου", "21", "Παροχές", "ΕΞΟΔΑ", 6423000L);
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

    @Test
    void findEntityWithEntityCodeTest() {
        // 1. Έλεγχος Επιτυχούς Αναζήτησης
        // Αναζητούμε την Προεδρία της Δημοκρατίας που δημιουργήσαμε στο setup
        Entity found = Entity.findEntityWithEntityCode("1001");

        assertNotNull(found, "Η μέθοδος θα έπρεπε να βρει το Entity με κωδικό 1001");
        assertEquals("Προεδρία της Δημοκρατίας", found.getEntityName());
        assertEquals("1001", found.getEntityCode());

        // 2. Έλεγχος Αποτυχημένης Αναζήτησης (Edge Case)
        // Αναζητούμε έναν κωδικό που δεν υπάρχει στη λίστα entities
        Entity notFound = Entity.findEntityWithEntityCode("9999");

        assertNull(notFound, "Η μέθοδος θα έπρεπε να επιστρέψει null για ανύπαρκτο κωδικό");
    }

    @Test
    void calculateRegularSumTest() {
        // Φορέας 1001 (Προεδρία Δημοκρατίας)
        Entity entity = Entity.findEntityWithEntityCode("1001");
        // Setup: 3.532.000 + 203.000 + 850.000 + 53.000
        assertEquals(4638000L, entity.calculateRegularSum());
    }

    @Test
    void calculatePublicInvestmentNationalSumTest() {
        // Φορέας 1004 (Προεδρία Κυβέρνησης)
        Entity entity = Entity.findEntityWithEntityCode("1004");
        // Setup: 1.500.000 + 1.500.000
        assertEquals(3000000L, entity.calculatePublicInvestmentNationalSum());
    }

    @Test
    void calculatePublicInvestmentCoFundedSumTest() {
        // Φορέας 1004 (Προεδρία Κυβέρνησης)
        Entity entity = Entity.findEntityWithEntityCode("1004");
        // Setup: 1.000.000
        assertEquals(1000000L, entity.calculatePublicInvestmentCoFundedSum());
    }

    @Test
    void calculatePublicInvestmentSumTest() {
        // Default μέθοδος στο Interface (National + CoFunded)
        Entity entity = Entity.findEntityWithEntityCode("1004");
        // 3.000.000 + 1.000.000
        assertEquals(4000000L, entity.calculatePublicInvestmentSum());
    }

    @Test
    void calculateTotalSumTest() {
        // Default μέθοδος στο Interface (Regular + PIB)
        Entity entity = Entity.findEntityWithEntityCode("1003");
        // Regular: 6.423.000 + PIB (CoFunded): 2.000.000
        assertEquals(8423000L, entity.calculateTotalSum());
    }

    @Test
    void getRegularServiceNameWithCodeTest() {
        Entity entity = Entity.findEntityWithEntityCode("1001");
        // Αναζήτηση ονόματος για τον κωδικό που βάλαμε στο setup
        String serviceName = entity.getRegularServiceNameWithCode("1001-101-000000");

        assertEquals("Προεδρία της Δημοκρατίας", serviceName );
    }

    @Test
    void getPublicInvestmentNationalServiceNameWithCodeTest() {
        Entity entity = Entity.findEntityWithEntityCode("1004");
        // Αναζήτηση ονόματος στο Εθνικό ΠΔΕ
        String serviceName = entity.getPublicInvestmentNationalServiceNameWithCode("1004-201-000000");

        assertEquals("ΓΓ Πρωθυπουργού", serviceName);
    }

    @Test
    void getPublicInvestmentCoFundedServiceNameWithCodeTest() {
        Entity entity = Entity.findEntityWithEntityCode("1003");
        // Αναζήτηση ονόματος στο Συγχρηματοδοτούμενο ΠΔΕ
        String serviceName = entity.getPublicInvestmentCoFundedServiceNameWithCode("1003-501-000000");

        assertEquals("Λοιπές αυτοτελείς μονάδες", serviceName);
    }
}