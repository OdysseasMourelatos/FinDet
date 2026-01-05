import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.financial.statistical_analysis.FrequencyRow;
import static org.junit.jupiter.api.Assertions.*;

public class FrequencyRowTest {
    @BeforeEach
    void setUp() {
        FrequencyRow rowToBeTested = new FrequencyRow(
        "10–20",   // interval
        15,        // frequency
        30.0,      // percentage
        15,        // cumulativeFrequency
        30.0       // cumulativePercentage
        );
    }

    @Test
    void getIntervalTest() {
        assertEquals("10-20",rowToBeTested.getInterval());
    }

    @Test
    void getFrequencyTest() {
        assertEquals(15,rowToBeTested.getFrequency());
    }

    @Test
    void getPercentageTest() {
        assertTrue(rowToBeTested.getPercentage() == 30.0);
    }

    @Test
    void getCumulativeFrequencyTest() {
        assertEquals(15,rowToBeTested.getCumulativeFrequency());
    }

    @Test
    void getCumulativePercentageTest() {
        assertTrue(rowToBeTested.cumulativePercentage() == 30.0);
    }
}
