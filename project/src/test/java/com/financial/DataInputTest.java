package com.financial;

import org.junit.jupiter.api.BeforeEach;

public class DataInputTest {

    @BeforeEach
    void resetState() {
        BudgetRevenue.getAllBudgetRevenues().clear();
    }
}
