package com.financial;

import com.financial.entries.BudgetRevenue;
import com.financial.entries.PublicInvestmentBudgetCoFundedRevenue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static com.financial.entries.PublicInvestmentBudgetCoFundedRevenue.calculateSum;
import static org.junit.jupiter.api.Assertions.*;

public class PublicInvestmentBudgetCoFundedRevenueTest {

    private PublicInvestmentBudgetCoFundedRevenue revenue13;
    private PublicInvestmentBudgetCoFundedRevenue revenue135;
    private PublicInvestmentBudgetCoFundedRevenue revenue13501;
    private PublicInvestmentBudgetCoFundedRevenue revenue1350101;

    @BeforeEach
    void setUp() {
        PublicInvestmentBudgetCoFundedRevenue.getAllPublicInvestmentBudgetCoFundedRevenues().clear();
        BudgetRevenue.getAllBudgetRevenues().clear();
        revenue13 = new PublicInvestmentBudgetCoFundedRevenue("13", "Κατηγορία 13", "ΕΣΟΔΑ", 4190000000L);
        revenue135 = new PublicInvestmentBudgetCoFundedRevenue("135", "Υποκατηγορία 135", "ΕΣΟΔΑ", 4190000000L);
        revenue13501 = new PublicInvestmentBudgetCoFundedRevenue("13501", "Υποκατηγορία 13501", "ΕΣΟΔΑ", 4190000000L);
        revenue1350101 = new PublicInvestmentBudgetCoFundedRevenue("1350101", "Υποκατηγορία 1350101", "ΕΣΟΔΑ", 1308000000L);
    }
}