package com.financial.entries;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Map;

public class RegularBudgetExpenseTest {

    @BeforeEach
    void setup() {
        // Καθαρισμός πριν από κάθε test για αποφυγή διπλότυπων
        RegularBudgetExpense.getAllRegularBudgetExpenses().clear();

        // Εισαγωγή δεδομένων
        new RegularBudgetExpense("1001", "ΠΡΟΕΔΡΙΑ ΤΗΣ ΔΗΜΟΚΡΑΤΙΑΣ", "1001-101-0000000", "Προεδρία της Δημοκρατίας", "21", "Παροχές σε εργαζομένους", "ΕΞΟΔΑ", 3532000L);
        new RegularBudgetExpense("1001", "ΠΡΟΕΔΡΙΑ ΤΗΣ ΔΗΜΟΚΡΑΤΙΑΣ", "1001-101-0000000", "Προεδρία της Δημοκρατίας", "23", "Μεταβιβάσεις", "ΕΞΟΔΑ", 203000L);
        new RegularBudgetExpense("1001", "ΠΡΟΕΔΡΙΑ ΤΗΣ ΔΗΜΟΚΡΑΤΙΑΣ", "1001-101-0000000", "Προεδρία της Δημοκρατίας", "24", "Αγορές αγαθών και υπηρεσιών", "ΕΞΟΔΑ", 850000L);
        new RegularBudgetExpense("1001", "ΠΡΟΕΔΡΙΑ ΤΗΣ ΔΗΜΟΚΡΑΤΙΑΣ", "1001-101-0000000", "Προεδρία της Δημοκρατίας", "31", "Πάγια περιουσιακά στοιχεία", "ΕΞΟΔΑ", 53000L);

        new RegularBudgetExpense("1003", "ΒΟΥΛΗ ΤΩΝ ΕΛΛΗΝΩΝ", "1003-101-0000000", "Γραφεία Προέδρου", "21", "Παροχές σε εργαζομένους", "ΕΞΟΔΑ", 6423000L);
        new RegularBudgetExpense("1003", "ΒΟΥΛΗ ΤΩΝ ΕΛΛΗΝΩΝ", "1003-101-0000000", "Γραφεία Προέδρου", "22", "Κοινωνικές παροχές", "ΕΞΟΔΑ", 12000L);

        new RegularBudgetExpense("1003", "ΒΟΥΛΗ ΤΩΝ ΕΛΛΗΝΩΝ", "1003-201-0000000", "Γενική Γραμματεία", "21", "Παροχές σε εργαζομένους", "ΕΞΟΔΑ", 44609000L);
        new RegularBudgetExpense("1003", "ΒΟΥΛΗ ΤΩΝ ΕΛΛΗΝΩΝ", "1003-201-0000000", "Γενική Γραμματεία", "22", "Κοινωνικές παροχές", "ΕΞΟΔΑ", 716000L);
        new RegularBudgetExpense("1003", "ΒΟΥΛΗ ΤΩΝ ΕΛΛΗΝΩΝ", "1003-201-0000000", "Γενική Γραμματεία", "23", "Μεταβιβάσεις", "ΕΞΟΔΑ", 6318000L);
        new RegularBudgetExpense("1003", "ΒΟΥΛΗ ΤΩΝ ΕΛΛΗΝΩΝ", "1003-201-0000000", "Γενική Γραμματεία", "24", "Αγορές αγαθών και υπηρεσιών", "ΕΞΟΔΑ", 15605000L);
        new RegularBudgetExpense("1003", "ΒΟΥΛΗ ΤΩΝ ΕΛΛΗΝΩΝ", "1003-201-0000000", "Γενική Γραμματεία", "29", "Πιστώσεις υπό κατανομή", "ΕΞΟΔΑ", 100000L);
        new RegularBudgetExpense("1003", "ΒΟΥΛΗ ΤΩΝ ΕΛΛΗΝΩΝ", "1003-201-0000000", "Γενική Γραμματεία", "31", "Πάγια περιουσιακά στοιχεία", "ΕΞΟΔΑ", 3208000L);
        new RegularBudgetExpense("1003", "ΒΟΥΛΗ ΤΩΝ ΕΛΛΗΝΩΝ", "1003-201-0000000", "Γενική Γραμματεία", "33", "Τιμαλφή", "ΕΞΟΔΑ", 80000L);
    }

    @Test
    void testGetAllRegularBudgetExpenses() {
        ArrayList<RegularBudgetExpense> all = RegularBudgetExpense.getAllRegularBudgetExpenses();
        assertEquals(13, all.size());
    }

    @Test
    void testGetRegularBudgetExpensesOfEntityWithCode() {
        // Entity 1001: Έχει 4 εγγραφές
        ArrayList<RegularBudgetExpense> list1001 = RegularBudgetExpense.getRegularBudgetExpensesOfEntityWithCode("1001");
        assertEquals(4, list1001.size());

        // Entity 1003: Έχει 9 εγγραφές (2 από το 1003-101 και 7 από το 1003-201)
        ArrayList<RegularBudgetExpense> list1003 = RegularBudgetExpense.getRegularBudgetExpensesOfEntityWithCode("1003");
        assertEquals(9, list1003.size());
    }

    @Test
    void testGetRegularBudgetExpensesOfCategoryWithCode() {
        // 1. Δεδομένα: Έχουμε ήδη 3 εγγραφές με κωδικό "21" από το setup
        String targetCode = "21";

        // 2. Εκτέλεση φιλτραρίσματος
        ArrayList<RegularBudgetExpense> result = RegularBudgetExpense.getRegularBudgetExpensesOfCategoryWithCode(targetCode);

        // 3. Επαλήθευση
        // α) Πρέπει να βρει ακριβώς 3 εγγραφές
        assertEquals(3, result.size());

        // β) Κάθε εγγραφή στη λίστα πρέπει να έχει κωδικό "21"
        for (RegularBudgetExpense e : result) {
            assertEquals(targetCode, e.getCode());
        }

        // γ) Έλεγχος για κωδικό που δεν υπάρχει (Branch Coverage για την περίπτωση που δε βρίσκει τίποτα)
        ArrayList<RegularBudgetExpense> emptyResult = RegularBudgetExpense.getRegularBudgetExpensesOfCategoryWithCode("999");
        assertTrue(emptyResult.isEmpty());
    }

    @Test
    void testFindRegularBudgetExpenseWithCodes() {
        // Αναζήτηση συγκεκριμένου: Entity 1003, Service 1003-201-0000000, Code 29
        RegularBudgetExpense found = RegularBudgetExpense.findRegularBudgetExpenseWithCodes(
                "1003", "1003-201-0000000", "29");

        assertNotNull(found);
        assertEquals(100000L, found.getAmount());
        assertEquals("Πιστώσεις υπό κατανομή", found.getDescription());

        //Αναζήτηση για έξοδο που δεν υπάρχει
        RegularBudgetExpense found2 = RegularBudgetExpense.findRegularBudgetExpenseWithCodes("999", "9999-999-9999999", "99");
        assertNull(found2);
    }

    @Test
    void testCalculateSum() {
        // Συνολικό άθροισμα όλων των ποσών:
        // 1001: 3.532.000 + 203.000 + 850.000 + 53.000 = 4.638.000
        // 1003: 6.423.000 + 12.000 + 44.609.000 + 716.000 + 6.318.000 + 15.605.000 + 100.000 + 3.208.000 + 80.000 = 77.071.000
        // Σύνολο: 81.709.000
        assertEquals(81709000L, RegularBudgetExpense.calculateSum());
    }

    @Test
    void testGetSumOfEveryExpenseCategory() {
        Map<String, Long> categorySums = RegularBudgetExpense.getSumOfEveryRegularExpenseCategory();

        // Έλεγχος Κατηγορίας 21: 3.532.000 + 6.423.000 + 44.609.000 = 54.564.000
        assertEquals(54564000L, categorySums.get("21"));

        // Έλεγχος Κατηγορίας 23: 203.000 + 6.318.000 = 6.521.000
        assertEquals(6521000L, categorySums.get("23"));

        // Έλεγχος Κατηγορίας 31: 53.000 + 3.208.000 = 3.261.000
        assertEquals(3261000L, categorySums.get("31"));

        // Έλεγχος πλήθους κατηγοριών: {21, 23, 24, 31, 22, 29, 33} = 7 κατηγορίες
        assertEquals(7, categorySums.size());
    }

    @Test
    void testGetDescriptionWithCode() {
        // Έλεγχος για την κατηγορία 21 (υπάρχει σε πολλούς φορείς)
        assertEquals("Παροχές σε εργαζομένους",
                RegularBudgetExpense.getDescriptionWithCode("21"));

        // Έλεγχος για την κατηγορία 24
        assertEquals("Αγορές αγαθών και υπηρεσιών",
                RegularBudgetExpense.getDescriptionWithCode("24"));

        // Έλεγχος για την κατηγορία 33 (υπάρχει μόνο στη Γενική Γραμματεία)
        assertEquals("Τιμαλφή",
                RegularBudgetExpense.getDescriptionWithCode("33"));

        // Έλεγχος για την κατηγορία 29
        assertEquals("Πιστώσεις υπό κατανομή",
                RegularBudgetExpense.getDescriptionWithCode("29"));

        // Έλεγχος οριακής περίπτωσης: Κωδικός που δεν υπάρχει στα δεδομένα του setup
        assertEquals("Περιγραφή μη διαθέσιμη",
                RegularBudgetExpense.getDescriptionWithCode("99"));
    }

    @Test
    void implementGlobalIncreaseInCertainRegularExpenseCategoryWithPercentageAllocationTest1() {
        // Εφαρμογή αύξησης 10% στην κατηγορία 21
        double percentage = 0.10;
        RegularBudgetExpense.implementGlobalChangesInCertainRegularExpenseCategoryWithPercentageAllocation("21", percentage, 0);

        // Έλεγχος όλων των εγγραφών στη βασική λίστα
        for (RegularBudgetExpense expense : RegularBudgetExpense.getAllRegularBudgetExpenses()) {
            if (expense.getCode().equals("21")) {
                // Υπολογίζουμε ποιο θα έπρεπε να είναι το ποσό βάσει των αρχικών δεδομένων
                if (expense.getEntityCode().equals("1001")) {
                    assertEquals(3885200L, expense.getAmount()); // 3.532.000 * 1.1
                } else if (expense.getEntityCode().equals("1003") && expense.getServiceCode().contains("101")) {
                    assertEquals(7065300L, expense.getAmount()); // 6.423.000 * 1.1
                } else if (expense.getEntityCode().equals("1003") && expense.getServiceCode().contains("201")) {
                    assertEquals(49069900L, expense.getAmount()); // 44.609.000 * 1.1
                }
            } else {
                // Έλεγχος ότι καμία άλλη κατηγορία (π.χ. 23) δεν επηρεάστηκε
                if (expense.getCode().equals("23") && expense.getEntityCode().equals("1001")) {
                    assertEquals(203000L, expense.getAmount());
                }
            }
        }
    }

    //Tests ONLY for increases

    @Test
    void implementGlobalIncreaseInCertainRegularExpenseCategoryWithPercentageAllocationTest2() {
        // Σενάριο: Προσθήκη 100.000 ευρώ fixed σε κάθε εγγραφή της κατηγορίας 21
        long fixedAmount = 100000L;
        RegularBudgetExpense.implementGlobalChangesInCertainRegularExpenseCategoryWithPercentageAllocation("21", 0, fixedAmount);

        // Έλεγχος όλων των εγγραφών στη βασική λίστα για την κατηγορία 21
        for (RegularBudgetExpense expense : RegularBudgetExpense.getAllRegularBudgetExpenses()) {
            if (expense.getCode().equals("21")) {
                if (expense.getEntityCode().equals("1001")) {
                    // Αρχικό 3.532.000 + 100.000 = 3.632.000
                    assertEquals(3632000L, expense.getAmount());
                } else if (expense.getEntityCode().equals("1003") && expense.getServiceCode().contains("101")) {
                    // Αρχικό 6.423.000 + 100.000 = 6.523.000
                    assertEquals(6523000L, expense.getAmount());
                } else if (expense.getEntityCode().equals("1003") && expense.getServiceCode().contains("201")) {
                    // Αρχικό 44.609.000 + 100.000 = 44.709.000
                    assertEquals(44709000L, expense.getAmount());
                }
            } else {
                // Έλεγχος ότι καμία άλλη κατηγορία (π.χ. 23) δεν επηρεάστηκε
                if (expense.getCode().equals("23") && expense.getEntityCode().equals("1001")) {
                    assertEquals(203000L, expense.getAmount());
                }
            }
        }
    }

    @Test
    void implementGlobalIncreaseInAllExpenseCategoriesWithPercentageAllocationTest1() {
        // Εφαρμογή 10% σε ΟΛΑ τα έξοδα του συστήματος
        double percentage = 0.10;
        RegularBudgetExpense.implementGlobalChangesInAllExpenseCategoriesWithPercentageAllocation(percentage, 0);

        // Σάρωση όλης της λίστας για επαλήθευση
        for (RegularBudgetExpense expense : RegularBudgetExpense.getAllRegularBudgetExpenses()) {
            // Παράδειγμα ελέγχου για την εγγραφή 1001-21 (Αρχικό: 3.532.000)
            if (expense.getEntityCode().equals("1001") && expense.getCode().equals("21")) {
                assertEquals(3885200L, expense.getAmount());
            }

            // Παράδειγμα ελέγχου για την εγγραφή 1003-33 (Αρχικό: 80.000)
            if (expense.getEntityCode().equals("1003") && expense.getCode().equals("33")) {
                assertEquals(88000L, expense.getAmount());
            }
        }
    }

    @Test
    void implementGlobalIncreaseInAllExpenseCategoriesWithPercentageAllocationTest2() {
        // Προσθήκη 5.000 ευρώ σε ΚΑΘΕ εγγραφή στη λίστα
        long fixed = 5000L;
        RegularBudgetExpense.implementGlobalChangesInAllExpenseCategoriesWithPercentageAllocation(0, fixed);

        ArrayList<RegularBudgetExpense> allExpenses = RegularBudgetExpense.getAllRegularBudgetExpenses();

        // Επαλήθευση ότι και οι 13 εγγραφές αυξήθηκαν
        for (RegularBudgetExpense expense : allExpenses) {
            // Ελέγχουμε με βάση τις γνωστές αρχικές τιμές από το setup
            if (expense.getEntityCode().equals("1001") && expense.getCode().equals("31")) {
                assertEquals(58000L, expense.getAmount()); // 53.000 + 5.000
            }

            if (expense.getEntityCode().equals("1003") && expense.getServiceCode().contains("201") && expense.getCode().equals("29")) {
                assertEquals(105000L, expense.getAmount()); // 100.000 + 5.000
            }
        }
    }

    @Test
    void testToStringFormatting() {
        RegularBudgetExpense e = new RegularBudgetExpense("1001", "ΤΕΣΤ", "1001-101", "ΥΠ", "21", "Μισθοί", "ΕΞΟΔΑ", 1000000L);
        String output = e.toString();

        assertTrue(output.contains("1001"));
        assertTrue(output.contains("ΤΕΣΤ"));
        assertTrue(output.contains("ΥΠ"));
    }
}
