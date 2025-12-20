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
    @Test
    void findSuperCategoryTest() {
        BudgetRevenue parent = revenue13501.findSuperCategory();
        assertNotNull(parent);
        assertEquals("135", parent.getCode());
        assertEquals(4190000000L, parent.getAmount());
    }

    @Test
    void getSuperCategoriesTest() {
        // Έλεγχος γονέων για το βαθύτερο κόμβο (1350101)
        ArrayList<BudgetRevenue> superCategories = revenue1350101.getSuperCategories();

        // Πρέπει να περιέχει 13, 135, 13501
        assertEquals(3, superCategories.size());
        assertTrue(superCategories.contains(revenue13));
        assertTrue(superCategories.contains(revenue135));
        assertTrue(superCategories.contains(revenue13501));
    }

    @Test
    void findNextLevelSubCategoriesTest() {

        ArrayList<BudgetRevenue> subCategories = revenue13.findNextLevelSubCategories();

        //One children in the next level - 135
        assertEquals(1, subCategories.size());
        assertTrue(subCategories.contains(revenue135));
    }

    @Test
    void findAllSubCategoriesTest() {
        ArrayList<BudgetRevenue> allSubCategories = revenue13.findAllSubCategories();
        //3 children total - 135, 13501 & 1350101
        assertEquals(3, allSubCategories.size());
        assertTrue(allSubCategories.contains(revenue135));
        assertTrue(allSubCategories.contains(revenue13501));
        assertTrue(allSubCategories.contains(revenue1350101));
    }
}