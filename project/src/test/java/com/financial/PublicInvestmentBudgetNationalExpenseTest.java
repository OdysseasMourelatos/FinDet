package com.financial;

import com.financial.entries.PublicInvestmentBudgetNationalExpense;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PublicInvestmentBudgetNationalExpenseTest {

    @BeforeEach
    void setup() {
        // Καθαρισμός πριν από κάθε test για αποφυγή διπλότυπων
        PublicInvestmentBudgetNationalExpense.getAllPublicInvestmentBudgetNationalExpenses().clear();

        new PublicInvestmentBudgetNationalExpense("1004", "Υπουργείο Επικρατείας", "1004-201-0000000", "Γραμματεία Πρωθυπουργού", "29", "Πιστώσεις υπό κατανομή", "ΕΘΝΙΚΟ", "ΕΞΟΔΑ", 1500000L);
        new PublicInvestmentBudgetNationalExpense("1004", "Υπουργείο Επικρατείας", "1004-202-0000000", "Νομικών & Κοινοβουλευτικών Θεμάτων", "29", "Πιστώσεις υπό κατανομή", "ΕΘΝΙΚΟ", "ΕΞΟΔΑ", 1500000L);
        new PublicInvestmentBudgetNationalExpense("1007", "Υπουργείο Εσωτερικών", "1007-209-0000000", "Αυτοδιοίκησης και Αποκέντρωσης", "29", "Πιστώσεις υπό κατανομή", "ΕΘΝΙΚΟ", "ΕΞΟΔΑ", 149000000L);
        new PublicInvestmentBudgetNationalExpense("1007", "Υπουργείο Εσωτερικών", "1007-702-0000000", "Ταμείο Ανάκαμψης", "29", "Πιστώσεις υπό κατανομή", "ΕΘΝΙΚΟ", "ΕΞΟΔΑ", 121000000L);
        new PublicInvestmentBudgetNationalExpense("1007", "Υπουργείο Εσωτερικών", "1007-705-0000000", "Ταμείο Ανάκαμψης (Ανθρ. Δυναμικό)", "29", "Πιστώσεις υπό κατανομή", "ΕΘΝΙΚΟ", "ΕΞΟΔΑ", 30000000L);
        new PublicInvestmentBudgetNationalExpense("1007", "Υπουργείο Εσωτερικών", "1007-999-0100000", "Γενική Διεύθυνση Μακεδονίας και Θράκης", "29", "Πιστώσεις υπό κατανομή", "ΕΘΝΙΚΟ", "ΕΞΟΔΑ", 1000000L);
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
        // Όλες οι εγγραφές έχουν code "29"
        ArrayList<PublicInvestmentBudgetNationalExpense> result = PublicInvestmentBudgetNationalExpense.getPublicInvestmentBudgetNationalExpensesOfCategoryWithCode("29");
        assertEquals(6, result.size());
    }

    @Test
    void testFindPublicInvestmentBudgetNationalExpenseWithCodes() {
        // Αναζήτηση: Entity 1007, Service πλήρες, Code 29
        PublicInvestmentBudgetNationalExpense found = PublicInvestmentBudgetNationalExpense.findPublicInvestmentBudgetNationalExpenseWithCodes(
                "1007", "1007-702-0000000", "29");

        assertNotNull(found);
        assertEquals(121000000L, found.getAmount());
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
        // Μία κατηγορία "29" που περιλαμβάνει τα πάντα
        assertEquals(304000000L, categorySums.get("29"));
        assertEquals(1, categorySums.size());
    }

    @Test
    void testGetDescriptionWithCode() {
        assertEquals("Πιστώσεις υπό κατανομή", PublicInvestmentBudgetNationalExpense.getDescriptionWithCode("29"));
        assertEquals("Περιγραφή μη διαθέσιμη", PublicInvestmentBudgetNationalExpense.getDescriptionWithCode("9999"));
    }

    @Test
    void implementGlobalIncreaseInCertainCategoryTest1() {
        // 10% αύξηση στο "29"
        double percentage = 0.10;
        PublicInvestmentBudgetNationalExpense.implementGlobalChangesInCertainPublicInvestmentBudgetNationalCategory("29", percentage, 0);

        for (PublicInvestmentBudgetNationalExpense expense : PublicInvestmentBudgetNationalExpense.getAllPublicInvestmentBudgetNationalExpenses()) {
            // Έλεγχος Αυτοδιοίκησης: 149.000.000 * 1.1 = 163.900.000
            if (expense.getServiceCode().equals("1007-209-0000000")) {
                assertEquals(163900000L, expense.getAmount());
            }
            // Έλεγχος Πρωθυπουργού: 1.500.000 * 1.1 = 1.650.000
            if (expense.getServiceCode().equals("1004-201-0000000")) {
                assertEquals(1650000L, expense.getAmount());
            }
        }
    }

    @Test
    void implementGlobalIncreaseInCertainPublicInvestmentBudgetNationalCategoryTest2() {
        // Σενάριο: Προσθήκη 1.000.000 ευρώ fixed σε κάθε εγγραφή της κατηγορίας "29"
        long fixedAmount = 1000000L;
        PublicInvestmentBudgetNationalExpense.implementGlobalChangesInCertainPublicInvestmentBudgetNationalCategory("29", 0, fixedAmount);

        // Έλεγχος όλων των εγγραφών στη βασική λίστα
        for (PublicInvestmentBudgetNationalExpense expense : PublicInvestmentBudgetNationalExpense.getAllPublicInvestmentBudgetNationalExpenses()) {
            // Έλεγχος συγκεκριμένα για την υπηρεσία 1007-702 (Αρχικό 121.000.000 + 1.000.000)
            if (expense.getServiceCode().equals("1007-702-0000000")) {
                assertEquals(122000000L, expense.getAmount());
            } else {
                // Έλεγχος ότι άλλη κατηγορία (π.χ. 1004-201) επηρεάστηκε επίσης αν έχει κωδικό "29"
                if (expense.getServiceCode().equals("1004-201-0000000")) {
                    assertEquals(2500000L, expense.getAmount()); // 1.500.000 + 1.000.000
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
            // Παράδειγμα ελέγχου για την εγγραφή 1004-201-0000000 (Αρχικό: 1.500.000)
            if (expense.getEntityCode().equals("1004") && expense.getServiceCode().equals("1004-201-0000000")) {
                assertEquals(1650000L, expense.getAmount()); // 1.500.000 * 1.1
            }

            // Παράδειγμα ελέγχου για την εγγραφή 1007-999-0100000 (Αρχικό: 1.000.000)
            if (expense.getEntityCode().equals("1007") && expense.getServiceCode().equals("1007-999-0100000")) {
                assertEquals(1100000L, expense.getAmount()); // 1.000.000 * 1.1
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
            if (expense.getEntityCode().equals("1004") && expense.getServiceCode().equals("1004-201-0000000")) {
                assertEquals(1600000L, expense.getAmount()); // 1.500.000 + 100.000
            }

            if (expense.getEntityCode().equals("1007") && expense.getServiceCode().equals("1007-702-0000000")) {
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

