package com.financial;

import com.financial.entries.RegularBudgetExpense;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Map;

class RegularBudgetExpenseTest {

    @BeforeEach
    void setup() {
        // Καθαρισμός των static λιστών πριν από κάθε test για αποφυγή διπλότυπων
        RegularBudgetExpense.getAllRegularBudgetExpenses().clear();
        RegularBudgetExpense.getRegularBudgetExpensesPerCategory().clear();

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
        Map<String, Long> categorySums = RegularBudgetExpense.getSumOfEveryExpenseCategory();

        // Έλεγχος Κατηγορίας 21: 3.532.000 + 6.423.000 + 44.609.000 = 54.564.000
        assertEquals(54564000L, categorySums.get("21"));

        // Έλεγχος Κατηγορίας 23: 203.000 + 6.318.000 = 6.521.000
        assertEquals(6521000L, categorySums.get("23"));

        // Έλεγχος Κατηγορίας 31: 53.000 + 3.208.000 = 3.261.000
        assertEquals(3261000L, categorySums.get("31"));

        // Έλεγχος πλήθους κατηγοριών: {21, 23, 24, 31, 22, 29, 33} = 7 κατηγορίες
        assertEquals(7, categorySums.size());
    }
}
