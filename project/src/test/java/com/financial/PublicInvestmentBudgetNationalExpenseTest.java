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

    @Test
    void testGetAllPublicInvestmentBudgetNationalExpenses() {
        ArrayList<PublicInvestmentBudgetNationalExpense> all = PublicInvestmentBudgetNationalExpense.getAllPublicInvestmentBudgetNationalExpenses();
        assertEquals(6, all.size());
    }

    @Test
    void testGetPublicInvestmentBudgetNationalExpensesOfEntityWithCode() {
        // Entity 1004: Έχει 2 εγγραφές
        ArrayList<PublicInvestmentBudgetNationalExpense> list1004 = PublicInvestmentBudgetNationalExpense.getPublicInvestmentBudgetNationalExpensesOfEntityWithCode("1004");
        assertEquals(2, list1004.size());

        // Entity 1007: Έχει 4 εγγραφές
        ArrayList<PublicInvestmentBudgetNationalExpense> list1007 = PublicInvestmentBudgetNationalExpense.getPublicInvestmentBudgetNationalExpensesOfEntityWithCode("1007");
        assertEquals(4, list1007.size());
    }

    @Test
    void testGetPublicInvestmentBudgetNationalExpensesOfCategoryWithCode() {
        // 1. Δεδομένα: Έχουμε 6 εγγραφές με κωδικό "10042010", "10042020", "10072090", "10077020", "10077050", "10079990"
        String targetCode = "10072090";

        // 2. Εκτέλεση φιλτραρίσματος
        ArrayList<PublicInvestmentBudgetNationalExpense> result = PublicInvestmentBudgetNationalExpense.getPublicInvestmentBudgetNationalExpensesOfCategoryWithCode(targetCode);

        // 3. Επαλήθευση
        // α) Πρέπει να βρει ακριβώς 1 εγγραφή
        assertEquals(1, result.size());

        // β) Η εγγραφή στη λίστα πρέπει να έχει κωδικό "10072090"
        for (PublicInvestmentBudgetNationalExpense e : result) {
            assertEquals(targetCode, e.getCode());
        }

        // γ) Έλεγχος για κωδικό που δεν υπάρχει
        ArrayList<PublicInvestmentBudgetNationalExpense> emptyResult = PublicInvestmentBudgetNationalExpense.getPublicInvestmentBudgetNationalExpensesOfCategoryWithCode("99999999");
        assertTrue(emptyResult.isEmpty());
    }

    @Test
    void testFindPublicInvestmentBudgetNationalExpenseWithCodes() {
        // Αναζήτηση συγκεκριμένου: Entity 1007, Service 702, Code 10077020
        PublicInvestmentBudgetNationalExpense found = PublicInvestmentBudgetNationalExpense.findPublicInvestmentBudgetNationalExpenseWithCodes(
                "1007", "702", "10077020");

        assertNotNull(found);
        assertEquals(121000000L, found.getAmount());
        assertEquals("Πιστώσεις υπό κατανομή", found.getDescription());

        //Αναζήτηση για έξοδο που δεν υπάρχει
        PublicInvestmentBudgetNationalExpense found2 = PublicInvestmentBudgetNationalExpense.findPublicInvestmentBudgetNationalExpenseWithCodes("999", "999", "99999999");
        assertNull(found2);
    }

    @Test
    void testCalculateSum() {
        // Συνολικό άθροισμα όλων των ποσών:
        // 1004: 1.500.000 + 1.500.000 = 3.000.000
        // 1007: 149.000.000 + 121.000.000 + 30.000.000 + 1.000.000 = 301.000.000
        // Σύνολο: 304.000.000
        assertEquals(304000000L, PublicInvestmentBudgetNationalExpense.calculateSum());
    }

    @Test
    void testGetSumOfEveryPublicInvestmentNationalExpenseCategory() {
        Map<String, Long> categorySums = PublicInvestmentBudgetNationalExpense.getSumOfEveryPublicInvestmentNationalExpenseCategory();

        // Έλεγχος κάθε κατηγορίας
        assertEquals(1500000L, categorySums.get("10042010"));
        assertEquals(1500000L, categorySums.get("10042020"));
        assertEquals(149000000L, categorySums.get("10072090"));
        assertEquals(121000000L, categorySums.get("10077020"));
        assertEquals(30000000L, categorySums.get("10077050"));
        assertEquals(1000000L, categorySums.get("10079990"));

        // Έλεγχος πλήθους κατηγοριών: 6 κατηγορίες
        assertEquals(6, categorySums.size());
    }

    @Test
    void testGetDescriptionWithCode() {
        // Έλεγχος για διάφορες κατηγορίες
        assertEquals("Πιστώσεις υπό κατανομή",
                PublicInvestmentBudgetNationalExpense.getDescriptionWithCode("10042010"));

        assertEquals("Πιστώσεις υπό κατανομή",
                PublicInvestmentBudgetNationalExpense.getDescriptionWithCode("10072090"));

        // Έλεγχος οριακής περίπτωσης: Κωδικός που δεν υπάρχει στα δεδομένα του setup
        assertEquals("Περιγραφή μη διαθέσιμη",
                PublicInvestmentBudgetNationalExpense.getDescriptionWithCode("99999999"));
    }

    @Test
    void implementGlobalIncreaseInCertainPublicInvestmentBudgetNationalCategoryTest1() {
        // Εφαρμογή αύξησης 10% στην κατηγορία 10072090
        double percentage = 0.10;
        PublicInvestmentBudgetNationalExpense.implementGlobalChangesInCertainPublicInvestmentBudgetNationalCategory("10072090", percentage, 0);

        // Έλεγχος όλων των εγγραφών στη βασική λίστα
        for (PublicInvestmentBudgetNationalExpense expense : PublicInvestmentBudgetNationalExpense.getAllPublicInvestmentBudgetNationalExpenses()) {
            if (expense.getCode().equals("10072090")) {
                assertEquals(163900000L, expense.getAmount()); // 149.000.000 * 1.1
            } else {
                // Έλεγχος ότι καμία άλλη κατηγορία δεν επηρεάστηκε
                if (expense.getCode().equals("10042010")) {
                    assertEquals(1500000L, expense.getAmount());
                }
            }
        }
    }

    @Test
    void implementGlobalIncreaseInCertainPublicInvestmentBudgetNationalCategoryTest2() {
        // Σενάριο: Προσθήκη 1.000.000 ευρώ fixed σε κάθε εγγραφή της κατηγορίας 10077020
        long fixedAmount = 1000000L;
        PublicInvestmentBudgetNationalExpense.implementGlobalChangesInCertainPublicInvestmentBudgetNationalCategory("10077020", 0, fixedAmount);

        // Έλεγχος όλων των εγγραφών στη βασική λίστα για την κατηγορία 10077020
        for (PublicInvestmentBudgetNationalExpense expense : PublicInvestmentBudgetNationalExpense.getAllPublicInvestmentBudgetNationalExpenses()) {
            if (expense.getCode().equals("10077020")) {
                // Αρχικό 121.000.000 + 1.000.000 = 122.000.000
                assertEquals(122000000L, expense.getAmount());
            } else {
                // Έλεγχος ότι καμία άλλη κατηγορία δεν επηρεάστηκε
                if (expense.getCode().equals("10042010")) {
                    assertEquals(1500000L, expense.getAmount());
                }
            }
        }
    }

    @Test
    void implementGlobalIncreaseInAllPublicInvestmentBudgetNationalCategoriesTest1() {
        // Εφαρμογή 10% σε ΟΛΑ τα έξοδα του συστήματος
        double percentage = 0.10;
        PublicInvestmentBudgetNationalExpense.implementGlobalChangesInAllPublicInvestmentBudgetNationalCategories(percentage, 0);

        // Σάρωση όλης της λίστας για επαλήθευση
        for (PublicInvestmentBudgetNationalExpense expense : PublicInvestmentBudgetNationalExpense.getAllPublicInvestmentBudgetNationalExpenses()) {
            // Παράδειγμα ελέγχου για την εγγραφή 1004-10042010 (Αρχικό: 1.500.000)
            if (expense.getEntityCode().equals("1004") && expense.getCode().equals("10042010")) {
                assertEquals(1650000L, expense.getAmount());
            }

            // Παράδειγμα ελέγχου για την εγγραφή 1007-10079990 (Αρχικό: 1.000.000)
            if (expense.getEntityCode().equals("1007") && expense.getCode().equals("10079990")) {
                assertEquals(1100000L, expense.getAmount());
            }
        }
    }

    @Test
    void implementGlobalIncreaseInAllPublicInvestmentBudgetNationalCategoriesTest2() {
        // Προσθήκη 100.000 ευρώ σε ΚΑΘΕ εγγραφή στη λίστα
        long fixed = 100000L;
        PublicInvestmentBudgetNationalExpense.implementGlobalChangesInAllPublicInvestmentBudgetNationalCategories(0, fixed);

        ArrayList<PublicInvestmentBudgetNationalExpense> allExpenses = PublicInvestmentBudgetNationalExpense.getAllPublicInvestmentBudgetNationalExpenses();

        // Επαλήθευση ότι και οι 6 εγγραφές αυξήθηκαν
        for (PublicInvestmentBudgetNationalExpense expense : allExpenses) {
            // Ελέγχουμε με βάση τις γνωστές αρχικές τιμές από το setup
            if (expense.getEntityCode().equals("1004") && expense.getCode().equals("10042010")) {
                assertEquals(1600000L, expense.getAmount()); // 1.500.000 + 100.000
            }

            if (expense.getEntityCode().equals("1007") && expense.getCode().equals("10077020")) {
                assertEquals(121100000L, expense.getAmount()); // 121.000.000 + 100.000
            }
        }
    }

    @Test
    void testToStringFormatting() {
        PublicInvestmentBudgetNationalExpense e = new PublicInvestmentBudgetNationalExpense("1004", "ΤΕΣΤ", "201", "ΥΠ", "10042010", "Μισθοί", "ΕΘΝΙΚΟ", "ΕΞΟΔΑ", 1000000L);
        String output = e.toString();

        assertTrue(output.contains("1004"));
        assertTrue(output.contains("ΤΕΣΤ"));
        assertTrue(output.contains("ΥΠ"));
    }
}

