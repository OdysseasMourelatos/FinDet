package com.financial.entries;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EntityTest {

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
    void findEntityWithEntityCodeTest() {
        Entity found = Entity.findEntityWithEntityCode("1001");
        assertNotNull(found);
        assertEquals("Προεδρία της Δημοκρατίας", found.getEntityName());

        Entity notFound = Entity.findEntityWithEntityCode("9999");
        assertNull(notFound);
    }

    @Test
    void calculateRegularSumTest() {
        // Φορέας 1003 (Βουλή των Ελλήνων)
        Entity entity = Entity.findEntityWithEntityCode("1003");
        // Setup 1003: (6.423.000 + 12.000) + (44.609.000 + 716.000 + 6.318.000 + 15.605.000 + 100.000 + 3.208.000 + 80.000)
        // Σύνολο: 6.435.000 + 70.636.000 = 77.071.000
        assertEquals(77071000L, entity.calculateRegularSum());
    }

    @Test
    void calculatePublicInvestmentNationalSumTest() {
        Entity entity = Entity.findEntityWithEntityCode("1004");
        // Setup 1004 National: 1.500.000 (ΓΓ Πρωθυπουργού) + 1.500.000 (ΓΓ Νομικών Θεμάτων)
        assertEquals(3000000L, entity.calculatePublicInvestmentNationalSum());
    }

    @Test
    void calculatePublicInvestmentCoFundedSumTest() {
        Entity entity = Entity.findEntityWithEntityCode("1004");
        // Setup 1004 CoFunded: 1.000.000 (ΓΓ Πρωθυπουργού)
        assertEquals(1000000L, entity.calculatePublicInvestmentCoFundedSum());
    }

    @Test
    void calculatePublicInvestmentSumTest() {
        Entity entity = Entity.findEntityWithEntityCode("1004");
        // 3.000.000 (National) + 1.000.000 (CoFunded) = 4.000.000
        assertEquals(4000000L, entity.calculatePublicInvestmentSum());
    }

    @Test
    void calculateTotalSumTest() {
        Entity entity = Entity.findEntityWithEntityCode("1003");
        // Regular: 77.071.000 + PIB (CoFunded): 2.000.000
        assertEquals(79071000L, entity.calculateTotalSum());
    }

    @Test
    void getRegularServiceNameWithCodeTest() {
        Entity entity = Entity.findEntityWithEntityCode("1003");
        
        String serviceName = entity.getRegularServiceNameWithCode("1003-201-000000");
        assertEquals("Γενική Γραμματεία της Βουλής των Ελλήνων", serviceName);
    }

    @Test
    void getPublicInvestmentNationalServiceNameWithCodeTest() {
        Entity entity = Entity.findEntityWithEntityCode("1004");

        String serviceName = entity.getPublicInvestmentNationalServiceNameWithCode("1004-202-000000");
        assertEquals("Γενική Γραμματεία Νομικών και Κοινοβουλευτικών Θεμάτων", serviceName);
    }

    @Test
    void getPublicInvestmentCoFundedServiceNameWithCodeTest() {
        Entity entity = Entity.findEntityWithEntityCode("1003");

        String serviceName = entity.getPublicInvestmentCoFundedServiceNameWithCode("1003-501-000000");
        assertEquals("Λοιπές αυτοτελείς Υπηρεσίες και μονάδες", serviceName);
    }
}