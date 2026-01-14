package com.financial.statistical_analysis;

import com.financial.entries.Entity;
import com.financial.entries.PublicInvestmentBudgetCoFundedExpense;
import com.financial.entries.PublicInvestmentBudgetNationalExpense;
import com.financial.entries.RegularBudgetExpense;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class StatisticalExpensesTest {

    @BeforeEach
    void setup() {
        // Καθαρισμός όλων των στατικών λιστών
        Entity.getEntities().clear();
        RegularBudgetExpense.getAllRegularBudgetExpenses().clear();
        PublicInvestmentBudgetNationalExpense.getAllPublicInvestmentBudgetNationalExpenses().clear();
        PublicInvestmentBudgetCoFundedExpense.getAllPublicInvestmentBudgetCoFundedExpenses().clear();

        // 1. Προεδρία Δημοκρατίας (Σύνολο: 4.638.000)
        new RegularBudgetExpense("1001", "Προεδρία της Δημοκρατίας", "1001-101-000000", "Προεδρία της Δημοκρατίας", "21", "Παροχές σε εργαζομένους", "ΕΞΟΔΑ", 3532000L);
        new RegularBudgetExpense("1001", "Προεδρία της Δημοκρατίας", "1001-101-000000", "Προεδρία της Δημοκρατίας", "23", "Μεταβιβάσεις", "ΕΞΟΔΑ", 203000L);
        new RegularBudgetExpense("1001", "Προεδρία της Δημοκρατίας", "1001-101-000000", "Προεδρία της Δημοκρατίας", "24", "Αγορές αγαθών και υπηρεσιών", "ΕΞΟΔΑ", 850000L);
        new RegularBudgetExpense("1001", "Προεδρία της Δημοκρατίας", "1001-101-000000", "Προεδρία της Δημοκρατίας", "31", "Πάγια περιουσιακά στοιχεία", "ΕΞΟΔΑ", 53000L);

        // 2. Βουλή των Ελλήνων (Σύνολο: 68.637.000) - Ο OUTLIER
        new RegularBudgetExpense("1003", "Βουλή των Ελλήνων", "1003-101-000000", "Γραφεία Προέδρου", "21", "Παροχές", "ΕΞΟΔΑ", 6423000L);
        new RegularBudgetExpense("1003", "Βουλή των Ελλήνων", "1003-201-000000", "Γενική Γραμματεία", "21", "Παροχές", "ΕΞΟΔΑ", 44609000L);
        new RegularBudgetExpense("1003", "Βουλή των Ελλήνων", "1003-201-000000", "Γενική Γραμματεία", "24", "Αγορές", "ΕΞΟΔΑ", 15605000L);
        new PublicInvestmentBudgetCoFundedExpense("1003", "Βουλή των Ελλήνων", "1003-501-000000", "Λοιπές Υπηρεσίες", "29", "Πιστώσεις", "ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ", "ΕΞΟΔΑ", 2000000L);

        // 3. Προεδρία Κυβέρνησης (Σύνολο: 4.000.000)
        new PublicInvestmentBudgetNationalExpense("1004", "Προεδρία της Κυβέρνησης", "1004-201-000000", "Γενική Γραμματεία", "29", "Πιστώσεις", "ΕΘΝΙΚΟ", "ΕΞΟΔΑ", 3000000L);
        new PublicInvestmentBudgetCoFundedExpense("1004", "Προεδρία της Κυβέρνησης", "1004-201-000000", "Γενική Γραμματεία", "29", "Πιστώσεις", "ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ", "ΕΞΟΔΑ", 1000000L);

        // --- ΕΠΙΠΛΕΟΝ ΦΟΡΕΙΣ ΓΙΑ ΣΤΑΤΙΣΤΙΚΗ ΒΑΣΗ ---
        new RegularBudgetExpense("1005", "Υπ. Εξωτερικών", "1005-101", "Διοίκηση", "21", "Παροχές", "ΕΞΟΔΑ", 5000000L);
        new RegularBudgetExpense("1006", "Υπ. Δικαιοσύνης", "1006-101", "Διοίκηση", "21", "Παροχές", "ΕΞΟΔΑ", 4500000L);
        new RegularBudgetExpense("1007", "Υπ. Εσωτερικών", "1007-101", "Διοίκηση", "21", "Παροχές", "ΕΞΟΔΑ", 5500000L);

        new Entity("1001", "Προεδρία της Δημοκρατίας");
        new Entity("1003", "Βουλή των Ελλήνων");
        new Entity("1004", "Προεδρία της Κυβέρνησης");
        new Entity("1005", "Υπ. Εξωτερικών");
        new Entity("1006", "Υπ. Δικαιοσύνης");
        new Entity("1007", "Υπ. Εσωτερικών");
    }

    @Test
    void testSortMinistrySumsAscendingOrder_CorrectSequence() {
        Map<String, Long> sums = StatisticalExpenses.getMinistrySums();
        Map<String, Long> sortedAsc = StatisticalExpenses.sortMinistrySumsAscendingOrder(sums);

        assertEquals(6, sortedAsc.size());
        var iterator = sortedAsc.entrySet().iterator();

        // Το μικρότερο: Προεδρία Κυβέρνησης (4M)
        Map.Entry<String, Long> firstEntry = iterator.next();
        assertEquals("Προεδρία της Κυβέρνησης", firstEntry.getKey());
        assertEquals(4_000_000L, firstEntry.getValue());

        // Το μεγαλύτερο: Βουλή (68.637.000)
        Map.Entry<String, Long> lastEntry = null;
        while (iterator.hasNext()) {
            lastEntry = iterator.next();
        }
        assertEquals("Βουλή των Ελλήνων", lastEntry.getKey());
        assertEquals(68_637_000L, lastEntry.getValue());
    }

    @Test
    void testSortMinistrySumsDescendingOrder_CorrectSequence() {
        Map<String, Long> sums = StatisticalExpenses.getMinistrySums();
        Map<String, Long> sortedDesc = StatisticalExpenses.sortMinistrySumsDescendingOrder(sums);

        // Πρώτο στη φθίνουσα: Βουλή (68.637.000)
        String firstKey = sortedDesc.keySet().iterator().next();
        assertEquals("Βουλή των Ελλήνων", firstKey);
        assertEquals(68_637_000L, sortedDesc.get(firstKey));

        var iterator = sortedDesc.entrySet().iterator();
        iterator.next(); // προσπέραση Βουλής
        Map.Entry<String, Long> secondEntry = iterator.next();
        assertEquals("Υπ. Εσωτερικών", secondEntry.getKey());
        assertEquals(5_500_000L, secondEntry.getValue());
    }

    @Test
    void testOutliersIQR_IdentifiesParliamentAsOutlier() {
        Map<String, Long> sums = StatisticalExpenses.getMinistrySums();
        DescriptiveStatistics stats = StatisticalExpenses.descriptiveStats(sums);
        Map<String, Long> outliers = StatisticalExpenses.findOutliersIQR(sums, stats);

        // Με 6 τιμές (4M έως 68M), το IQR "πιάνει" τη Βουλή
        assertTrue(outliers.containsKey("Βουλή των Ελλήνων"));
        assertEquals(1, outliers.size());
    }

    @Test
    void testFindOutliersZScore_ChecksStatisticalDistribution() {
        Map<String, Long> sums = StatisticalExpenses.getMinistrySums();
        DescriptiveStatistics stats = StatisticalExpenses.descriptiveStats(sums);
        Map<String, Long> outliersZ = StatisticalExpenses.findOutliersZScore(sums, stats);

        assertNotNull(outliersZ);
        // Με n=6, το Z-score για τα 68M είναι περίπου 2.23 (> 2), άρα αναγνωρίζεται
        assertTrue(outliersZ.containsKey("Βουλή των Ελλήνων"));
    }
}