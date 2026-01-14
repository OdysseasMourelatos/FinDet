package com.financial.statistical_analysis;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FrequencyTableTest {

    private Map<String, Long> sampleSums;
    private DescriptiveStatistics mockStats;

    @BeforeEach
    void setUp() {
        sampleSums = new HashMap<>();
        mockStats = new DescriptiveStatistics();
    }

    @Test
    void testBuildFrequencyTable_EmptyMap() {
        List<FrequencyRow> result = FrequencyTable.buildFrequencyTable(sampleSums, mockStats);
        assertTrue(result.isEmpty());
    }

    @Test
    void testBuildFrequencyTable_CorrectDistribution() {

        // Προσθέτουμε δύο τιμές στο διάστημα 1M - 10M
        sampleSums.put("Ministry A", 2_000_000L);
        sampleSums.put("Ministry B", 5_000_000L);
        // Προσθέτουμε μία τιμή στο διάστημα 10M - 100M
        sampleSums.put("Ministry C", 50_000_000L);

        List<FrequencyRow> result = FrequencyTable.buildFrequencyTable(sampleSums, mockStats);

        // Αναμένουμε 2 γραμμές
        assertEquals(2, result.size());

        // Έλεγχος πρώτης γραμμής (1,000,000 - 10,000,000)
        FrequencyRow row1 = result.get(0);
        assertEquals(2, row1.getFrequency()); // 2 υπουργεία
        assertEquals(66.66, row1.getPercentage(), 0.01); // (2/3)*100

        // Έλεγχος δεύτερης γραμμής (10,000,000 - 100,000,000)
        FrequencyRow row2 = result.get(1);
        assertEquals(1, row2.getFrequency());
        assertEquals(3, row2.getCumulativeFrequency()); // 2 + 1 = 3
        assertEquals(100.0, row2.getCumulativePercentage(), 0.01);
    }

    @Test
    void testBuildFrequencyTable_ValuesOutsideBounds() {

        // Τιμή μικρότερη από 1M (εκτός ορίων του πίνακα)
        sampleSums.put("Small Agency", 500_000L);

        List<FrequencyRow> result = FrequencyTable.buildFrequencyTable(sampleSums, mockStats);

        assertTrue(result.isEmpty());
    }
}