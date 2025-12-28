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
        // 1. Δεδομένα: Έχουμε 7 εγγραφές με διαφορετικούς κωδικούς
        String targetCode = "10072090";

        // 2. Εκτέλεση φιλτραρίσματος
        ArrayList<PublicInvestmentBudgetCoFundedExpense> result = PublicInvestmentBudgetCoFundedExpense.getPublicInvestmentBudgetCoFundedExpensesOfCategoryWithCode(targetCode);

        // 3. Επαλήθευση
        // α) Πρέπει να βρει ακριβώς 1 εγγραφή
        assertEquals(1, result.size());

        // β) Η εγγραφή στη λίστα πρέπει να έχει κωδικό "10072090"
        for (PublicInvestmentBudgetCoFundedExpense e : result) {
            assertEquals(targetCode, e.getCode());
        }

        // γ) Έλεγχος για κωδικό που δεν υπάρχει
        ArrayList<PublicInvestmentBudgetCoFundedExpense> emptyResult = PublicInvestmentBudgetCoFundedExpense.getPublicInvestmentBudgetCoFundedExpensesOfCategoryWithCode("99999999");
        assertTrue(emptyResult.isEmpty());
    }

    @Test
    void testFindPublicInvestmentBudgetCoFundedExpenseWithCodes() {
        // Αναζήτηση συγκεκριμένου: Entity 1009, Service 701, Code 10097010
        PublicInvestmentBudgetCoFundedExpense found = PublicInvestmentBudgetCoFundedExpense.findPublicInvestmentBudgetCoFundedExpenseWithCodes(
                "1009", "701", "10097010");

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

        // Έλεγχος κάθε κατηγορίας
        assertEquals(6000000L, categorySums.get("10072070"));
        assertEquals(73000000L, categorySums.get("10072090"));
        assertEquals(1000000L, categorySums.get("10079990"));
        assertEquals(672000L, categorySums.get("10092010"));
        assertEquals(512000L, categorySums.get("10092060"));
        assertEquals(16000L, categorySums.get("10095010"));
        assertEquals(10800000L, categorySums.get("10097010"));

        // Έλεγχος πλήθους κατηγοριών: 7 κατηγορίες
        assertEquals(7, categorySums.size());
    }

    @Test
    void testGetDescriptionWithCode() {
        // Έλεγχος για διάφορες κατηγορίες
        assertEquals("Πιστώσεις υπό κατανομή",
                PublicInvestmentBudgetCoFundedExpense.getDescriptionWithCode("10072070"));

        assertEquals("Πιστώσεις υπό κατανομή",
                PublicInvestmentBudgetCoFundedExpense.getDescriptionWithCode("10097010"));

        // Έλεγχος οριακής περίπτωσης: Κωδικός που δεν υπάρχει στα δεδομένα του setup
        assertEquals("Περιγραφή μη διαθέσιμη",
                PublicInvestmentBudgetCoFundedExpense.getDescriptionWithCode("99999999"));
    }

    @Test
    void implementGlobalIncreaseInCertainPublicInvestmentBudgetCoFundedCategoryTest1() {
        // Εφαρμογή αύξησης 10% στην κατηγορία 10072090
        double percentage = 0.10;
        PublicInvestmentBudgetCoFundedExpense.implementGlobalChangesInCertainPublicInvestmentBudgetCoFundedCategory("10072090", percentage, 0);

        // Έλεγχος όλων των εγγραφών στη βασική λίστα
        for (PublicInvestmentBudgetCoFundedExpense expense : PublicInvestmentBudgetCoFundedExpense.getAllPublicInvestmentBudgetCoFundedExpenses()) {
            if (expense.getCode().equals("10072090")) {
                assertEquals(80300000L, expense.getAmount()); // 73.000.000 * 1.1
            } else {
                // Έλεγχος ότι καμία άλλη κατηγορία δεν επηρεάστηκε
                if (expense.getCode().equals("10072070")) {
                    assertEquals(6000000L, expense.getAmount());
                }
            }
        }
    }

    @Test
    void implementGlobalIncreaseInCertainPublicInvestmentBudgetCoFundedCategoryTest2() {
        // Σενάριο: Προσθήκη 500.000 ευρώ fixed σε κάθε εγγραφή της κατηγορίας 10097010
        long fixedAmount = 500000L;
        PublicInvestmentBudgetCoFundedExpense.implementGlobalChangesInCertainPublicInvestmentBudgetCoFundedCategory("10097010", 0, fixedAmount);

        // Έλεγχος όλων των εγγραφών στη βασική λίστα για την κατηγορία 10097010
        for (PublicInvestmentBudgetCoFundedExpense expense : PublicInvestmentBudgetCoFundedExpense.getAllPublicInvestmentBudgetCoFundedExpenses()) {
            if (expense.getCode().equals("10097010")) {
                // Αρχικό 10.800.000 + 500.000 = 11.300.000
                assertEquals(11300000L, expense.getAmount());
            } else {
                // Έλεγχος ότι καμία άλλη κατηγορία δεν επηρεάστηκε
                if (expense.getCode().equals("10092010")) {
                    assertEquals(672000L, expense.getAmount());
                }
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
            // Παράδειγμα ελέγχου για την εγγραφή 1007-10072070 (Αρχικό: 6.000.000)
            if (expense.getEntityCode().equals("1007") && expense.getCode().equals("10072070")) {
                assertEquals(6600000L, expense.getAmount());
            }

            // Παράδειγμα ελέγχου για την εγγραφή 1009-10095010 (Αρχικό: 16.000)
            if (expense.getEntityCode().equals("1009") && expense.getCode().equals("10095010")) {
                assertEquals(17600L, expense.getAmount());
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
            if (expense.getEntityCode().equals("1009") && expense.getCode().equals("10095010")) {
                assertEquals(66000L, expense.getAmount()); // 16.000 + 50.000
            }

            if (expense.getEntityCode().equals("1007") && expense.getCode().equals("10072070")) {
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
