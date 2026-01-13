package com.financial.statistical_analysis;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class FrequencyRowTest {

    @Test
    void testFrequencyRowProperties() {

        String interval = "1000 - 5000";
        int frequency = 10;
        double percentage = 25.0;
        int cumulativeFreq = 10;
        double cumulativePct = 25.0;

        FrequencyRow row = new FrequencyRow(interval, frequency, percentage, cumulativeFreq, cumulativePct);

        assertAll("Verify FrequencyRow fields",
                () -> assertEquals(interval, row.getInterval(), "Το διάστημα πρέπει να ταυτίζεται"),
                () -> assertEquals(frequency, row.getFrequency(), "Η συχνότητα πρέπει να ταυτίζεται"),
                () -> assertEquals(percentage, row.getPercentage(), 0.001, "Το ποσοστό πρέπει να ταυτίζεται"),
                () -> assertEquals(cumulativeFreq, row.getCumulativeFrequency(), "Η αθροιστική συχνότητα πρέπει να ταυτίζεται"),
                () -> assertEquals(cumulativePct, row.getCumulativePercentage(), 0.001, "Το αθροιστικό ποσοστό πρέπει να ταυτίζεται")
        );
    }

    @Test
    void testFrequencyRowWithZeroValues() {
        FrequencyRow emptyRow = new FrequencyRow("0 - 0", 0, 0.0, 0, 0.0);

        assertEquals(0, emptyRow.getFrequency());
        assertEquals(0.0, emptyRow.getPercentage());
    }
}
