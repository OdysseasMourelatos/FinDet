package com.financial.entries;

import com.financial.services.BudgetType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    void implementChangesInAllRegularExpensesIncreaseFixedTest() {
        Entity entity = Entity.findEntityWithEntityCode("1001");
        // Πριν: 3.532.000 (21), 203.000 (23), 850.000 (24), 53.000 (31)
        entity.implementChangesInAllExpenseCategoriesOfAllServices(0.0, 10000, BudgetType.REGULAR_BUDGET);

        assertEquals(4678000L, entity.calculateRegularSum());

        ArrayList<RegularBudgetExpense> expenses = entity.getRegularBudgetExpenses();
        assertEquals(3542000L, expenses.get(0).getAmount()); // 21: +10.000
        assertEquals(213000L,  expenses.get(1).getAmount()); // 23: +10.000
        assertEquals(860000L,  expenses.get(2).getAmount()); // 24: +10.000
        assertEquals(63000L,   expenses.get(3).getAmount()); // 31: +10.000
    }

    @Test
    void implementChangesInAllNationalPIBExpensesIncreasePercentageTest() {
        Entity entity = Entity.findEntityWithEntityCode("1004");
        // Πριν: 1.500.000 (Service 201), 1.500.000 (Service 202)
        entity.implementChangesInAllExpenseCategoriesOfAllServices(0.20, 0, BudgetType.PUBLIC_INVESTMENT_BUDGET_NATIONAL);

        assertEquals(3600000L, entity.calculatePublicInvestmentNationalSum());

        ArrayList<PublicInvestmentBudgetNationalExpense> expenses = entity.getPublicInvestmentBudgetNationalExpenses();
        assertEquals(1800000L, expenses.get(0).getAmount()); // 1.5M * 1.20
        assertEquals(1800000L, expenses.get(1).getAmount()); // 1.5M * 1.20
    }

    @Test
    void implementChangesInAllCoFundedPIBExpensesIncreasePercentageTest() {
        Entity entity = Entity.findEntityWithEntityCode("1003");
        // Πριν: 2.000.000 (Service 501)
        entity.implementChangesInAllExpenseCategoriesOfAllServices(0.05, 0, BudgetType.PUBLIC_INVESTMENT_BUDGET_COFUNDED);

        assertEquals(2100000L, entity.calculatePublicInvestmentCoFundedSum());

        ArrayList<PublicInvestmentBudgetCoFundedExpense> expenses = entity.getPublicInvestmentBudgetCoFundedExpenses();
        assertEquals(2100000L, expenses.get(0).getAmount()); // 2M * 1.05
    }

    @Test
    void implementChangesInAllRegularExpensesDecreasePercentageTest() {
        Entity entity = Entity.findEntityWithEntityCode("1001");
        // Πριν: 3.532.000 (21), 203.000 (23), 850.000 (24), 53.000 (31)
        // Μείωση 10%
        entity.implementChangesInAllExpenseCategoriesOfAllServices(-0.10, 0, BudgetType.REGULAR_BUDGET);

        assertEquals(4174200L, entity.calculateRegularSum());

        ArrayList<RegularBudgetExpense> expenses = entity.getRegularBudgetExpenses();
        assertEquals(3178800L, expenses.get(0).getAmount()); // 3.532.000 * 0.9
        assertEquals(182700L,  expenses.get(1).getAmount()); // 203.000 * 0.9
        assertEquals(765000L,  expenses.get(2).getAmount()); // 850.000 * 0.9
        assertEquals(47700L,   expenses.get(3).getAmount()); // 53.000 * 0.9
    }

    @Test
    void implementChangesInAllNationalPIBExpensesDecreaseFixedTest() {
        Entity entity = Entity.findEntityWithEntityCode("1004");
        // Πριν: 1.500.000 (201), 1.500.000 (202)
        // Μείωση 100.000 ανά λογαριασμό
        entity.implementChangesInAllExpenseCategoriesOfAllServices(0.0, -100000, BudgetType.PUBLIC_INVESTMENT_BUDGET_NATIONAL);

        assertEquals(2800000L, entity.calculatePublicInvestmentNationalSum());

        ArrayList<PublicInvestmentBudgetNationalExpense> expenses = entity.getPublicInvestmentBudgetNationalExpenses();
        assertEquals(1400000L, expenses.get(0).getAmount()); // 1.5M - 100k
        assertEquals(1400000L, expenses.get(1).getAmount()); // 1.5M - 100k
    }

    @Test
    void implementChangesInAllCoFundedPIBExpensesDecreaseFixedTest() {
        Entity entity = Entity.findEntityWithEntityCode("1003");
        // Πριν: 2.000.000 (501). Μείωση 500.000.
        entity.implementChangesInAllExpenseCategoriesOfAllServices(0.0, -500000, BudgetType.PUBLIC_INVESTMENT_BUDGET_COFUNDED);

        assertEquals(1500000L, entity.calculatePublicInvestmentCoFundedSum());

        ArrayList<PublicInvestmentBudgetCoFundedExpense> expenses = entity.getPublicInvestmentBudgetCoFundedExpenses();
        assertEquals(1500000L, expenses.get(0).getAmount()); // 2M - 500k
    }

    @Test
    void implementSpecificCategoryIncreasePercentageRegularTest() {
        Entity entity = Entity.findEntityWithEntityCode("1001");
        // Κατηγορία 21: 3.532.000, Κατηγορία 23: 203.000
        // Αύξηση 10% ΜΟΝΟ στην 21
        entity.implementChangesInSpecificExpenseCategoryOfAllServices("21", 0.10, 0, BudgetType.REGULAR_BUDGET);

        ArrayList<RegularBudgetExpense> expenses = entity.getRegularBudgetExpenses();
        // Έλεγχος λογαριασμού που ΕΠΡΕΠΕ να αλλάξει
        assertEquals(3885200L, expenses.get(0).getAmount()); // 21: 3.532.000 * 1.1
        // Έλεγχος λογαριασμού που ΔΕΝ ΕΠΡΕΠΕ να αλλάξει (Isolation Check)
        assertEquals(203000L, expenses.get(1).getAmount());  // 23: Παραμένει ίδιο
    }

    @Test
    void implementSpecificCategoryIncreaseFixedNationalPIBTest() {
        Entity entity = Entity.findEntityWithEntityCode("1004");
        // Κατηγορία 29 (National): Δύο λογαριασμοί των 1.500.000 (Service 201 & 202)
        // Προσθήκη 50.000 σταθερά
        entity.implementChangesInSpecificExpenseCategoryOfAllServices("29", 0.0, 50000, BudgetType.PUBLIC_INVESTMENT_BUDGET_NATIONAL);

        ArrayList<PublicInvestmentBudgetNationalExpense> expenses = entity.getPublicInvestmentBudgetNationalExpenses();
        assertEquals(1550000L, expenses.get(0).getAmount()); // 201: 1.5M + 50k
        assertEquals(1550000L, expenses.get(1).getAmount()); // 202: 1.5M + 50k
    }

    @Test
    void implementSpecificCategoryIncreasePercentageCoFundedTest() {
        Entity entity = Entity.findEntityWithEntityCode("1003");
        // Κατηγορία 29 (CoFunded): 2.000.000. Αύξηση 5%.
        entity.implementChangesInSpecificExpenseCategoryOfAllServices("29", 0.05, 0, BudgetType.PUBLIC_INVESTMENT_BUDGET_COFUNDED);

        ArrayList<PublicInvestmentBudgetCoFundedExpense> expenses = entity.getPublicInvestmentBudgetCoFundedExpenses();
        assertEquals(2100000L, expenses.get(0).getAmount()); // 2M * 1.05
    }

    @Test
    void implementSpecificCategoryDecreasePercentageRegularTest() {
        Entity entity = Entity.findEntityWithEntityCode("1001");
        // Κατηγορία 24: 850.000, Κατηγορία 31: 53.000. Μείωση 20% στην 24.
        entity.implementChangesInSpecificExpenseCategoryOfAllServices("24", -0.20, 0, BudgetType.REGULAR_BUDGET);

        ArrayList<RegularBudgetExpense> expenses = entity.getRegularBudgetExpenses();
        assertEquals(680000L, expenses.get(2).getAmount()); // 24: 850.000 * 0.8
        assertEquals(53000L,  expenses.get(3).getAmount()); // 31: Παραμένει ίδιο
    }

    @Test
    void implementSpecificCategoryDecreaseFixedNationalPIBTest() {
        Entity entity = Entity.findEntityWithEntityCode("1004");
        // Κατηγορία 29: 1.500.000 ανά υπηρεσία. Μείωση 100.000.
        entity.implementChangesInSpecificExpenseCategoryOfAllServices("29", 0.0, -100000, BudgetType.PUBLIC_INVESTMENT_BUDGET_NATIONAL);

        ArrayList<PublicInvestmentBudgetNationalExpense> expenses = entity.getPublicInvestmentBudgetNationalExpenses();
        assertEquals(1400000L, expenses.get(0).getAmount()); // 1.5M - 100k
    }

    @Test
    void implementSpecificCategoryDecreaseFixedCoFundedTest() {
        Entity entity = Entity.findEntityWithEntityCode("1003");
        // Κατηγορία 29: 2.000.000. Μείωση 1.500.000.
        entity.implementChangesInSpecificExpenseCategoryOfAllServices("29", 0.0, -1500000, BudgetType.PUBLIC_INVESTMENT_BUDGET_COFUNDED);

        ArrayList<PublicInvestmentBudgetCoFundedExpense> expenses = entity.getPublicInvestmentBudgetCoFundedExpenses();
        assertEquals(500000L, expenses.get(0).getAmount()); // 2M - 1.5M
    }

    @Test
    void implementSpecificServiceIncreaseFixedRegularTest() {
        Entity entity = Entity.findEntityWithEntityCode("1003"); // Βουλή
        // Υπηρεσία 101: Λογαριασμοί 21 (6.423.000) και 24 (12.000). Προσθήκη 50.000 ανά λογαριασμό.
        entity.implementChangesInAllExpenseCategoriesOfSpecificService("1003-101-000000", 0.0, 50000, BudgetType.REGULAR_BUDGET);

        ArrayList<BudgetExpense> service101Exps = entity.getRegularExpensesOfServiceWithCode("1003-101-000000");
        // Έλεγχος ότι άλλαξαν ΟΛΟΙ οι λογαριασμοί της υπηρεσίας
        assertEquals(6473000L, service101Exps.get(0).getAmount()); // 21: 6.423.000 + 50.000
        assertEquals(62000L,   service101Exps.get(1).getAmount()); // 24: 12.000 + 50.000

        // Έλεγχος Isolation: Η υπηρεσία 201 (Λογαριασμός 21: 44.609.000) πρέπει να είναι ίδια
        long otherServiceAcc21 = entity.getRegularExpensesOfServiceWithCode("1003-201-000000").get(0).getAmount();
        assertEquals(44609000L, otherServiceAcc21);
    }

    @Test
    void implementSpecificServiceIncreasePercentageNationalPIBTest() {
        Entity entity = Entity.findEntityWithEntityCode("1004");
        // Υπηρεσία 201: 1.500.000. Αύξηση 10%.
        entity.implementChangesInAllExpenseCategoriesOfSpecificService("1004-201-000000", 0.10, 0, BudgetType.PUBLIC_INVESTMENT_BUDGET_NATIONAL);

        ArrayList<PublicInvestmentBudgetNationalExpense> expenses = entity.getPublicInvestmentBudgetNationalExpenses();
        assertEquals(1650000L, expenses.get(0).getAmount()); // Service 201: Άλλαξε
        assertEquals(1500000L, expenses.get(1).getAmount()); // Service 202: Ίδιο
    }

    @Test
    void implementSpecificServiceIncreaseFixedCoFundedTest() {
        Entity entity = Entity.findEntityWithEntityCode("1003");
        // Υπηρεσία 501: 2.000.000. Προσθήκη 1.000.000.
        entity.implementChangesInAllExpenseCategoriesOfSpecificService("1003-501-000000", 0.0, 1000000, BudgetType.PUBLIC_INVESTMENT_BUDGET_COFUNDED);

        assertEquals(3000000L, entity.getPublicInvestmentCoFundedExpensesOfServiceWithCode("1003-501-000000").get(0).getAmount());
    }

    @Test
    void implementSpecificServiceDecreasePercentageRegularTest() {
        Entity entity = Entity.findEntityWithEntityCode("1001");
        // Υπηρεσία 101 (Προεδρία): Λογαριασμός 21: 3.532.000. Μείωση 50%.
        entity.implementChangesInAllExpenseCategoriesOfSpecificService("1001-101-000000", -0.50, 0, BudgetType.REGULAR_BUDGET);

        long amount21 = entity.getRegularExpensesOfServiceWithCode("1001-101-000000").get(0).getAmount();
        assertEquals(1766000L, amount21); // 3.532.000 * 0.5
    }

    @Test
    void implementSpecificServiceDecreaseFixedNationalPIBTest() {
        Entity entity = Entity.findEntityWithEntityCode("1004");
        // Υπηρεσία 202: 1.500.000. Μείωση 200.000.
        entity.implementChangesInAllExpenseCategoriesOfSpecificService("1004-202-000000", 0.0, -200000, BudgetType.PUBLIC_INVESTMENT_BUDGET_NATIONAL);

        assertEquals(1300000L, entity.getPublicInvestmentBudgetNationalExpenses().get(1).getAmount()); // Service 202: Άλλαξε
        assertEquals(1500000L, entity.getPublicInvestmentBudgetNationalExpenses().get(0).getAmount()); // Service 201: Ίδιο
    }

    @Test
    void implementSpecificServiceDecreasePercentageCoFundedTest() {
        Entity entity = Entity.findEntityWithEntityCode("1003");
        // Υπηρεσία 501: 2.000.000. Μείωση 10%.
        entity.implementChangesInAllExpenseCategoriesOfSpecificService("1003-501-000000", -0.10, 0, BudgetType.PUBLIC_INVESTMENT_BUDGET_COFUNDED);

        assertEquals(1800000L, entity.getPublicInvestmentCoFundedExpensesOfServiceWithCode("1003-501-000000").get(0).getAmount());
    }

    @Test
    void applyChangesWithUnsupportedBudgetTypeThrowsExceptionTest() {
        Entity entity = Entity.findEntityWithEntityCode("1001");

        // Ελέγχουμε αν η μέθοδος πετάει IllegalStateException όταν το budgetType είναι Public Investment (General)
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            entity.implementChangesInAllExpenseCategoriesOfAllServices(0.10, 0, BudgetType.PUBLIC_INVESTMENT_BUDGET);
        });
    }
}
