package com.financial;

import org.junit.jupiter.api.BeforeEach;

public class DataInputTest {

    @BeforeEach
    void resetState() {
        BudgetRevenue.getAllBudgetRevenues().clear();
    }

    package com.financial;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

    public class DataInputTest {

        @BeforeEach
        void resetState() {
            BudgetRevenue.getAllBudgetRevenues().clear();
        }

        @Test
        void testCreateBudgetRevenueFromCSV() throws Exception {
            String[] csvLine = {"11", "Main Revenue", "1000"};

            Method m = DataInput.class.getDeclaredMethod("createBudgetRevenueFromCSV", String[].class);
            m.setAccessible(true);
            m.invoke(null, (Object) csvLine);

            assertEquals(1, BudgetRevenue.getAllBudgetRevenues().size());
            BudgetRevenue rev = BudgetRevenue.getAllBudgetRevenues().get(0);

            assertEquals("11", rev.getCode());
            assertEquals("Main Revenue", rev.getDescription());
            assertEquals("ΕΣΟΔΑ", rev.getCategory());
            assertEquals(1000, rev.getAmount());
        }
    }

}
