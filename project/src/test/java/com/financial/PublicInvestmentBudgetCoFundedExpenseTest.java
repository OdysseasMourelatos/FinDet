package com.financial;


import com.financial.entries.PublicInvestmentBudgetCoFundedExpense;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PublicInvestmentBudgetCoFundedExpenseTest {

    @BeforeEach
    void setup() {
        // Καθαρισμός πριν από κάθε test για αποφυγή διπλότυπων
        PublicInvestmentBudgetCoFundedExpense.getAllPublicInvestmentBudgetCoFundedExpenses().clear();

        new PublicInvestmentBudgetCoFundedExpense("1007", "Υπουργείο Εσωτερικών", "1007-207-0000000", "Γενική Γραμματεία Δημόσιας Διοίκησης", "29", "Πιστώσεις υπό κατανομή", "ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ", "ΕΞΟΔΑ", 6000000L);
        new PublicInvestmentBudgetCoFundedExpense("1007", "Υπουργείο Εσωτερικών", "1007-209-0000000", "Γενική Γραμματεία Αυτοδιοίκησης και Αποκέντρωσης", "29", "Πιστώσεις υπό κατανομή", "ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ", "ΕΞΟΔΑ", 73000000L);
        new PublicInvestmentBudgetCoFundedExpense("1007", "Υπουργείο Εσωτερικών", "1007-999-0100000", "Γενική Διεύθυνση Μακεδονίας και Θράκης", "29", "Πιστώσεις υπό κατανομή", "ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ", "ΕΞΟΔΑ", 1000000L);

        // Υπουργείο Εξωτερικών (1009) - Πλήρεις Κωδικοί Ειδικού Φορέα
        new PublicInvestmentBudgetCoFundedExpense("1009", "Υπουργείο Εξωτερικών", "1009-201-0000000", "Υπηρεσιακή Γενική Γραμματεία", "29", "Πιστώσεις υπό κατανομή", "ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ", "ΕΞΟΔΑ", 672000L);
        new PublicInvestmentBudgetCoFundedExpense("1009", "Υπουργείο Εξωτερικών", "1009-206-0000000", "Γενική Γραμματεία Διεθνών Οικονομικών Σχέσεων", "29", "Πιστώσεις υπό κατανομή", "ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ", "ΕΞΟΔΑ", 512000L);
        new PublicInvestmentBudgetCoFundedExpense("1009", "Υπουργείο Εξωτερικών", "1009-501-0000000", "Λοιπές μονάδες/αυτοτελείς μονάδες", "29", "Πιστώσεις υπό κατανομή", "ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ", "ΕΞΟΔΑ", 16000L);
        new PublicInvestmentBudgetCoFundedExpense("1009", "Υπουργείο Εξωτερικών", "1009-701-0000000", "Δαπάνες μεταναστευτικών ροών", "29", "Πιστώσεις υπό κατανομή", "ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ", "ΕΞΟΔΑ", 10800000L);

    }

    @Test
    void testGetAllPublicInvestmentBudgetCoFundedExpenses() {
        ArrayList<PublicInvestmentBudgetCoFundedExpense> all = PublicInvestmentBudgetCoFundedExpense.getAllPublicInvestmentBudgetCoFundedExpenses();
        assertEquals(7, all.size());
    }

    @Test
    void testGetPublicInvestmentBudgetCoFundedExpensesOfEntityWithCode() {
        // Entity 1007: Έχει 3 εγγραφές
        ArrayList<PublicInvestmentBudgetCoFundedExpense> list1007 = PublicInvestmentBudgetCoFundedExpense.getPublicInvestmentBudgetCoFundedExpensesOfEntityWithCode("1007");
        assertEquals(3, list1007.size());

        // Entity 1009: Έχει 4 εγγραφές
        ArrayList<PublicInvestmentBudgetCoFundedExpense> list1009 = PublicInvestmentBudgetCoFundedExpense.getPublicInvestmentBudgetCoFundedExpensesOfEntityWithCode("1009");
        assertEquals(4, list1009.size());
    }

    @Test
    void testGetPublicInvestmentBudgetCoFundedExpensesOfCategoryWithCode() {
        // Εφόσον όλες οι εγγραφές στο setup έχουν code "29", το targetCode πρέπει να είναι "29"
        String targetCode = "29";
        ArrayList<PublicInvestmentBudgetCoFundedExpense> result = PublicInvestmentBudgetCoFundedExpense.getPublicInvestmentBudgetCoFundedExpensesOfCategoryWithCode(targetCode);

        assertEquals(7, result.size());
        for (PublicInvestmentBudgetCoFundedExpense e : result) {
            assertEquals("29", e.getCode());
        }
    }

    @Test
    void testFindPublicInvestmentBudgetCoFundedExpenseWithCodes() {
        // Αναζήτηση συγκεκριμένου: Entity 1009, Service 701, Code 10097010
        PublicInvestmentBudgetCoFundedExpense found = PublicInvestmentBudgetCoFundedExpense.findPublicInvestmentBudgetCoFundedExpenseWithCodes(
                "1009", "1009-701-0000000", "29");

        assertNotNull(found);
        assertEquals(10800000L, found.getAmount());
        assertEquals("Πιστώσεις υπό κατανομή", found.getDescription());

        //Αναζήτηση για έξοδο που δεν υπάρχει
        PublicInvestmentBudgetCoFundedExpense found2 = PublicInvestmentBudgetCoFundedExpense.findPublicInvestmentBudgetCoFundedExpenseWithCodes("999", "999", "99999999");
        assertNull(found2);
    }

    @Test
    void testCalculateSum() {
        // Συνολικό άθροισμα όλων των ποσών:
        // 1007: 6.000.000 + 73.000.000 + 1.000.000 = 80.000.000
        // 1009: 672.000 + 512.000 + 16.000 + 10.800.000 = 12.000.000
        // Σύνολο: 92.000.000
        assertEquals(92000000L, PublicInvestmentBudgetCoFundedExpense.calculateSum());
    }

    @Test
    void testGetSumOfEveryExpenseCategory() {
        Map<String, Long> categorySums = PublicInvestmentBudgetCoFundedExpense.getSumOfEveryExpenseCategory();
        // Εφόσον όλες οι εγγραφές έχουν code "29", το Map πρέπει να έχει 1 entry με το συνολικό ποσό
        assertEquals(92000000L, categorySums.get("29"));
        assertEquals(1, categorySums.size());
    }

    @Test
    void testGetDescriptionWithCode() {
        // Έλεγχος για διάφορες κατηγορίες
        assertEquals("Πιστώσεις υπό κατανομή",
                PublicInvestmentBudgetCoFundedExpense.getDescriptionWithCode("29"));

        // Έλεγχος οριακής περίπτωσης: Κωδικός που δεν υπάρχει στα δεδομένα του setup
        assertEquals("Περιγραφή μη διαθέσιμη",
                PublicInvestmentBudgetCoFundedExpense.getDescriptionWithCode("99999999"));
    }

    @Test
    void implementGlobalIncreaseInCertainPublicInvestmentBudgetCoFundedCategoryTest1() {
        // Εφαρμογή αύξησης 10% σε ΟΛΕΣ τις πιστώσεις υπό κατανομή (Code 29)
        double percentage = 0.10;
        PublicInvestmentBudgetCoFundedExpense.implementGlobalChangesInCertainPublicInvestmentBudgetCoFundedCategory("29", percentage, 0);

        for (PublicInvestmentBudgetCoFundedExpense expense : PublicInvestmentBudgetCoFundedExpense.getAllPublicInvestmentBudgetCoFundedExpenses()) {
            // Έλεγχος ενδεικτικά για την Αυτοδιοίκηση: 73.000.000 * 1.1 = 80.300.000
            if (expense.getServiceCode().equals("1007-209-0000000")) {
                assertEquals(80300000L, expense.getAmount());
            }
            // Έλεγχος ενδεικτικά για τη Δημόσια Διοίκηση: 6.000.000 * 1.1 = 6.600.000
            if (expense.getServiceCode().equals("1007-207-0000000")) {
                assertEquals(6600000L, expense.getAmount());
            }
        }
    }

    @Test
    void implementGlobalIncreaseInCertainPublicInvestmentBudgetCoFundedCategoryTest2() {
        // Σενάριο: Προσθήκη 500.000 ευρώ fixed σε όλες τις εγγραφές της κατηγορίας "29"
        // Σημείωση: Αλλάζουμε το "10097010" σε "29" γιατί αυτός είναι ο κωδικός στο setup σου
        long fixedAmount = 500000L;
        PublicInvestmentBudgetCoFundedExpense.implementGlobalChangesInCertainPublicInvestmentBudgetCoFundedCategory("29", 0, fixedAmount);

        // Έλεγχος όλων των εγγραφών στη βασική λίστα
        for (PublicInvestmentBudgetCoFundedExpense expense : PublicInvestmentBudgetCoFundedExpense.getAllPublicInvestmentBudgetCoFundedExpenses()) {

            // Έλεγχος συγκεκριμένα για την υπηρεσία 1009-701
            if (expense.getServiceCode().equals("1009-701-0000000")) {
                // Αρχικό 10.800.000 + 500.000 = 11.300.000
                assertEquals(11300000L, expense.getAmount());
            }

            // Έλεγχος για την υπηρεσία 1009-201 (επηρεάζεται και αυτή γιατί έχει κωδικό "29")
            if (expense.getServiceCode().equals("1009-201-0000000")) {
                // Αρχικό 672.000 + 500.000 = 1.172.000
                assertEquals(1172000L, expense.getAmount());
            }
        }
    }

    @Test
    void implementGlobalIncreaseInAllPublicInvestmentBudgetCoFundedCategoriesTest1() {
        // Εφαρμογή 10% σε ΟΛΑ τα έξοδα του συστήματος
        double percentage = 0.10;
        PublicInvestmentBudgetCoFundedExpense.implementGlobalChangesInAllPublicInvestmentBudgetCoFundedCategories(percentage, 0);

        // Σάρωση όλης της λίστας για επαλήθευση
        for (PublicInvestmentBudgetCoFundedExpense expense : PublicInvestmentBudgetCoFundedExpense.getAllPublicInvestmentBudgetCoFundedExpenses()) {
            // Παράδειγμα ελέγχου για την εγγραφή 1007-207-0000000 (Αρχικό: 6.000.000)
            if (expense.getEntityCode().equals("1007") && expense.getServiceCode().equals("1007-207-0000000")) {
                assertEquals(6600000L, expense.getAmount()); // 6.000.000 * 1.1
            }

            // Παράδειγμα ελέγχου για την εγγραφή 1009-501-0000000 (Αρχικό: 16.000)
            if (expense.getEntityCode().equals("1009") && expense.getServiceCode().equals("1009-501-0000000")) {
                assertEquals(17600L, expense.getAmount()); // 16.000 * 1.1
            }
        }
    }

    @Test
    void implementGlobalIncreaseInAllPublicInvestmentBudgetCoFundedCategoriesTest2() {
        // Προσθήκη 50.000 ευρώ σε ΚΑΘΕ εγγραφή στη λίστα
        long fixed = 50000L;
        PublicInvestmentBudgetCoFundedExpense.implementGlobalChangesInAllPublicInvestmentBudgetCoFundedCategories(0, fixed);

        ArrayList<PublicInvestmentBudgetCoFundedExpense> allExpenses = PublicInvestmentBudgetCoFundedExpense.getAllPublicInvestmentBudgetCoFundedExpenses();

        // Επαλήθευση ότι και οι 7 εγγραφές αυξήθηκαν
        for (PublicInvestmentBudgetCoFundedExpense expense : allExpenses) {
            // Ελέγχουμε με βάση τις γνωστές αρχικές τιμές από το setup
            if (expense.getEntityCode().equals("1009") && expense.getServiceCode().equals("1009-501-0000000")) {
                assertEquals(66000L, expense.getAmount()); // 16.000 + 50.000
            }

            if (expense.getEntityCode().equals("1007") && expense.getServiceCode().equals("1007-207-0000000")) {
                assertEquals(6050000L, expense.getAmount()); // 6.000.000 + 50.000
            }
        }
    }

    @Test
    void testToStringFormatting() {
        PublicInvestmentBudgetCoFundedExpense e = new PublicInvestmentBudgetCoFundedExpense("1007", "ΤΕΣΤ", "207", "ΥΠ", "10072070", "Μισθοί", "ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ", "ΕΞΟΔΑ", 1000000L);
        String output = e.toString();

        assertTrue(output.contains("1007"));
        assertTrue(output.contains("ΤΕΣΤ"));
        assertTrue(output.contains("ΥΠ"));
    }
}
