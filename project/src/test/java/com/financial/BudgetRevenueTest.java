package com.financial;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BudgetRevenueTest {

    @Before
    public void setUp() {
        BudgetRevenue.revenues.clear();
    }

    @Test
    public void testBudgetRevenueCreation() {
        BudgetRevenue revenue = new BudgetRevenue(11, "Φόροι", "ΕΣΟΔΑ", 62055000000L);
        
        assertEquals(11, revenue.getCode());
        assertEquals("Φόροι", revenue.getDescription());
        assertEquals("ΕΣΟΔΑ", revenue.getCategory());
        assertEquals(62055000000L, revenue.getAmount());
    }

    @Test
    public void testRevenueAddedToStaticList() {
        BudgetRevenue revenue = new BudgetRevenue(12, "Κοινωνικές εισφορές", "ΕΣΟΔΑ", 60000000L);
        
        assertFalse(BudgetRevenue.revenues.isEmpty());
        assertEquals(1, BudgetRevenue.revenues.size());
        assertTrue(BudgetRevenue.revenues.contains(revenue));
    }

    @Test
    public void testCalculateSum() {
        new BudgetRevenue(11, "Φόροι", "ΕΣΟΔΑ", 1000000L);
        new BudgetRevenue(12, "Κοινωνικές εισφορές", "ΕΣΟΔΑ", 500000L);
        new BudgetRevenue(13, "Μεταβιβάσεις", "ΕΣΟΔΑ", 750000L);
        
        long expectedSum = 1000000L + 500000L + 750000L;
        assertEquals(expectedSum, BudgetRevenue.calculateSum());
    }

    @Test
    public void testCalculateSumWithEmptyList() {
        assertEquals(0, BudgetRevenue.calculateSum());
    }

    @Test
    public void testFindRevenueWithCode() {
        BudgetRevenue revenue1 = new BudgetRevenue(11, "Φόροι", "ΕΣΟΔΑ", 62055000000L);
        BudgetRevenue revenue2 = new BudgetRevenue(12, "Κοινωνικές εισφορές", "ΕΣΟΔΑ", 60000000L);
        
        BudgetRevenue found = BudgetRevenue.findRevenueWithCode(12);
        assertNotNull(found);
        assertEquals(revenue2, found);
        
        BudgetRevenue notFound = BudgetRevenue.findRevenueWithCode(999);
        assertNull(notFound);
    }
}
