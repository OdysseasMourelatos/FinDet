package com.financial.entries;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Test
    void getAllRegularServiceCodesTest() {
        // Φορέας 1003 (Βουλή των Ελλήνων)
        Entity entity = Entity.findEntityWithEntityCode("1003");
        List<String> codes = entity.getAllRegularServiceCodes();

        // Έχουμε δύο μοναδικούς κωδικούς: 1003-101-000000 και 1003-201-000000
        assertEquals(2, codes.size());
        assertTrue(codes.contains("1003-101-000000"));
        assertTrue(codes.contains("1003-201-000000"));
    }

    @Test
    void getAllPublicInvestmentNationalServiceCodesTest() {
        // Φορέας 1004 (Προεδρία Κυβέρνησης)
        Entity entity = Entity.findEntityWithEntityCode("1004");
        List<String> codes = entity.getAllPublicInvestmentNationalServiceCodes();

        // Έχουμε δύο μοναδικούς κωδικούς στο Εθνικό ΠΔΕ: 201 και 202
        assertEquals(2, codes.size());
        assertTrue(codes.contains("1004-201-000000"));
        assertTrue(codes.contains("1004-202-000000"));
    }

    @Test
    void getAllPublicInvestmentCoFundedServiceCodesTest() {
        // Φορέας 1004 (Προεδρία Κυβέρνησης)
        Entity entity = Entity.findEntityWithEntityCode("1004");
        List<String> codes = entity.getAllPublicInvestmentCoFundedServiceCodes();

        // Έχουμε μόνο έναν κωδικό στο Συγχρηματοδοτούμενο: 201
        assertEquals(1, codes.size());
        assertEquals("1004-201-000000", codes.get(0));
    }

    @Test
    void getAllPublicInvestmentServiceCodesTest() {
        // Default μέθοδος (National + CoFunded)
        Entity entity = Entity.findEntityWithEntityCode("1004");
        List<String> combinedPibCodes = entity.getAllPublicInvestmentServiceCodes();

        // National: [201, 202], CoFunded: [201]
        // Το Stream.distinct() πρέπει να επιστρέψει μοναδικούς: [201, 202]
        assertEquals(2, combinedPibCodes.size());
        assertTrue(combinedPibCodes.contains("1004-201-000000"));
        assertTrue(combinedPibCodes.contains("1004-202-000000"));
    }

    @Test
    void getAllServiceCodesTest() {
        // Default μέθοδος (Regular + All Public Investment)
        Entity entity = Entity.findEntityWithEntityCode("1003");
        List<String> allCodes = entity.getAllServiceCodes();

        // Regular: [101, 201], CoFunded: [501]
        // Σύνολο: 3 μοναδικοί κωδικοί
        assertEquals(3, allCodes.size());
        assertTrue(allCodes.contains("1003-101-000000"));
        assertTrue(allCodes.contains("1003-201-000000"));
        assertTrue(allCodes.contains("1003-501-000000"));
    }

    @Test
    void getRegularSumOfServiceWithCodeTest() {
        // Φορέας 1003 (Βουλή των Ελλήνων) - Υπηρεσία 101 (Γραφεία Προέδρου)
        Entity entity = Entity.findEntityWithEntityCode("1003");

        // Setup 101: 6.423.000 (Παροχές) + 12.000 (Κοινωνικές) = 6.435.000
        long sum = entity.getRegularSumOfServiceWithCode("1003-101-000000");
        assertEquals(6435000L, sum);
    }

    @Test
    void getPublicInvestmentNationalSumOfServiceWithCodeTest() {
        // Φορέας 1004 (Προεδρία Κυβέρνησης) - Υπηρεσία 201 (ΓΓ Πρωθυπουργού)
        Entity entity = Entity.findEntityWithEntityCode("1004");

        // Setup 201 National: 1.500.000
        long sum = entity.getPublicInvestmentNationalSumOfServiceWithCode("1004-201-000000");
        assertEquals(1500000L, sum);
    }

    @Test
    void getPublicInvestmentCoFundedSumOfServiceWithCodeTest() {
        // Φορέας 1004 (Προεδρία Κυβέρνησης) - Υπηρεσία 201 (ΓΓ Πρωθυπουργού)
        Entity entity = Entity.findEntityWithEntityCode("1004");

        // Setup 201 CoFunded: 1.000.000
        long sum = entity.getPublicInvestmentCoFundedSumOfServiceWithCode("1004-201-000000");
        assertEquals(1000000L, sum);
    }

    @Test
    void getPublicInvestmentSumOfServiceWithCodeTest() {
        // Default μέθοδος (National + CoFunded για την ίδια υπηρεσία)
        Entity entity = Entity.findEntityWithEntityCode("1004");
        String serviceCode = "1004-201-000000";

        // 1.500.000 (National) + 1.000.000 (CoFunded) = 2.500.000
        assertEquals(2500000L, entity.getPublicInvestmentSumOfServiceWithCode(serviceCode));
    }

    @Test
    void getTotalSumOfServiceWithCodeTest() {
        // Default μέθοδος (Regular + PIB) για τη Βουλή
        Entity entity = Entity.findEntityWithEntityCode("1003");

        // Για την υπηρεσία 101:
        // Regular: 6.435.000 | PIB: 0
        assertEquals(6435000L, entity.getTotalSumOfServiceWithCode("1003-101-000000"));
    }

    @Test
    void getRegularExpensesOfServiceWithCodeTest() {
        // Φορέας 1001 (Προεδρία Δημοκρατίας) - Υπηρεσία 101
        Entity entity = Entity.findEntityWithEntityCode("1001");
        ArrayList<BudgetExpense> expenses = entity.getRegularExpensesOfServiceWithCode("1001-101-000000");

        // Στο setup για τον 101 βάλαμε 4 εγγραφές (21, 23, 24, 31)
        assertEquals(4, expenses.size(), "Θα έπρεπε να επιστραφούν 4 εγγραφές για τον κωδικό 101");
        assertEquals("Παροχές σε εργαζομένους", expenses.get(0).getExpenseName());
    }

    @Test
    void getPublicInvestmentNationalExpensesOfServiceWithCodeTest() {
        // Φορέας 1004 (Προεδρία Κυβέρνησης) - Υπηρεσία 202 (ΓΓ Νομικών Θεμάτων)
        Entity entity = Entity.findEntityWithEntityCode("1004");
        ArrayList<BudgetExpense> expenses = entity.getPublicInvestmentNationalExpensesOfServiceWithCode("1004-202-000000");

        // Setup 202 National: 1 εγγραφή
        assertEquals(1, expenses.size());
        assertEquals(1500000L, expenses.get(0).getAmount());
    }

    @Test
    void getPublicInvestmentCoFundedExpensesOfServiceWithCodeTest() {
        // Φορέας 1003 (Βουλή των Ελλήνων) - Υπηρεσία 501
        Entity entity = Entity.findEntityWithEntityCode("1003");
        ArrayList<BudgetExpense> expenses = entity.getPublicInvestmentCoFundedExpensesOfServiceWithCode("1003-501-000000");

        // Setup 501 CoFunded: 1 εγγραφή
        assertEquals(1, expenses.size());

    }

    @Test
    void getRegularSumOfEveryServiceTest() {
        // Φορέας 1003 (Βουλή)
        Entity entity = Entity.findEntityWithEntityCode("1003");
        Map<String, Long> serviceMap = entity.getRegularSumOfEveryService();

        // 101: 6.423.000 + 12.000 = 6.435.000
        // 201: 44.609.000 + 716.000 + 6.318.000 + 15.605.000 + 100.000 + 3.208.000 + 80.000 = 70.636.000
        assertEquals(2, serviceMap.size());
        assertEquals(6435000L, serviceMap.get("1003-101-000000"));
        assertEquals(70636000L, serviceMap.get("1003-201-000000"));
    }

    @Test
    void getPublicInvestmentNationalSumOfEveryServiceTest() {
        // Φορέας 1004 (Προεδρία Κυβέρνησης)
        Entity entity = Entity.findEntityWithEntityCode("1004");
        Map<String, Long> serviceMap = entity.getPublicInvestmentNationalSumOfEveryService();

        // 201: 1.500.000 | 202: 1.500.000
        assertEquals(2, serviceMap.size());
        assertEquals(1500000L, serviceMap.get("1004-201-000000"));
        assertEquals(1500000L, serviceMap.get("1004-202-000000"));
    }

    @Test
    void getPublicInvestmentCoFundedSumOfEveryServiceTest() {
        // Φορέας 1004 (Προεδρία Κυβέρνησης)
        Entity entity = Entity.findEntityWithEntityCode("1004");
        Map<String, Long> serviceMap = entity.getPublicInvestmentCoFundedSumOfEveryService();

        // 201: 1.000.000
        assertEquals(1, serviceMap.size());
        assertEquals(1000000L, serviceMap.get("1004-201-000000"));
    }

    @Test
    void getPublicInvestmentSumOfEveryServiceTest() {
        // Default μέθοδος: Ενοποίηση Εθνικού + Συγχρηματοδοτούμενου ΠΔΕ
        Entity entity = Entity.findEntityWithEntityCode("1004");
        Map<String, Long> pibMap = entity.getPublicInvestmentSumOfEveryService();

        // 201: 1.500.000 (National) + 1.000.000 (CoFunded) = 2.500.000
        // 202: 1.500.000 (National) + 0 (CoFunded) = 1.500.000
        assertEquals(2, pibMap.size());
        assertEquals(2500000L, pibMap.get("1004-201-000000"));
        assertEquals(1500000L, pibMap.get("1004-202-000000"));
    }

    @Test
    void getTotalSumOfEveryServiceTest() {
        // Default μέθοδος: Γενικό σύνολο (Regular + PIB) για τη Βουλή (1003)
        Entity entity = Entity.findEntityWithEntityCode("1003");
        Map<String, Long> totalMap = entity.getTotalSumOfEveryService();

        // 101: 6.435.000 (Regular)
        // 201: 70.636.000 (Regular)
        // 501: 2.000.000 (PIB)
        assertEquals(3, totalMap.size());
        assertEquals(6435000L, totalMap.get("1003-101-000000"));
        assertEquals(70636000L, totalMap.get("1003-201-000000"));
        assertEquals(2000000L, totalMap.get("1003-501-000000"));
    }
}