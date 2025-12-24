package com.financial;

import com.financial.entries.RegularBudgetExpense;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

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
}
