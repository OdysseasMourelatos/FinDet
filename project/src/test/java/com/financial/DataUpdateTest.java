package com.financial;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class DataUpdateTest {

    @BeforeEach
    void resetState() {
        BudgetRevenue.getAllBudgetRevenues().clear();
        BudgetExpense.expenses.clear();
    }

    @Test
    void testPlaceholder() {
        assertTrue(true);
    }
}
