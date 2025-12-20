package com.financial;

import com.financial.entries.BudgetRevenue;
import com.financial.entries.PublicInvestmentBudgetNationalRevenue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static com.financial.entries.PublicInvestmentBudgetNationalRevenue.calculateSum;
import static org.junit.jupiter.api.Assertions.*;

public class PublicInvestmentBudgetNationalRevenueTest {

    private PublicInvestmentBudgetNationalRevenue revenue13;
    private PublicInvestmentBudgetNationalRevenue revenue134;
    private PublicInvestmentBudgetNationalRevenue revenue13409;
    private PublicInvestmentBudgetNationalRevenue revenue15;
    private PublicInvestmentBudgetNationalRevenue revenue156;
    private PublicInvestmentBudgetNationalRevenue revenue15609;

    @BeforeEach
    void setUp() {
        PublicInvestmentBudgetNationalRevenue.getAllPublicInvestmentBudgetNationalRevenues().clear();
        BudgetRevenue.getAllBudgetRevenues().clear();

        // Δεδομένα που αρχικά ήταν για CoFunded (2 Κλάδοι: 13 και 15)
        revenue13 = new PublicInvestmentBudgetNationalRevenue("13", "Κατηγορία 13", "ΕΣΟΔΑ", 35000000L);
        revenue134 = new PublicInvestmentBudgetNationalRevenue("134", "Υποκατηγορία 134", "ΕΣΟΔΑ", 35000000L);
        revenue13409 = new PublicInvestmentBudgetNationalRevenue("13409", "Υποκατηγορία 13409", "ΕΣΟΔΑ", 35000000L);

        revenue15 = new PublicInvestmentBudgetNationalRevenue("15", "Κατηγορία 15", "ΕΣΟΔΑ", 265000000L);
        revenue156 = new PublicInvestmentBudgetNationalRevenue("156", "Υποκατηγορία 156", "ΕΣΟΔΑ", 1000000L);
        revenue15609 = new PublicInvestmentBudgetNationalRevenue("15609", "Υποκατηγορία 15609", "ΕΣΟΔΑ", 1000000L);
    }


    @Test
    void getAllPublicInvestmentBudgetNationalRevenuesTest() {
        ArrayList<PublicInvestmentBudgetNationalRevenue> allRevenues = PublicInvestmentBudgetNationalRevenue.getAllPublicInvestmentBudgetNationalRevenues();
        assertEquals(6, allRevenues.size());
        assertTrue(allRevenues.get(0) instanceof PublicInvestmentBudgetNationalRevenue);
    }
    @Test
    void getMainPublicInvestmentBudgetNationalRevenuesTest() {
        ArrayList<PublicInvestmentBudgetNationalRevenue> mainRevenues = PublicInvestmentBudgetNationalRevenue.getMainPublicInvestmentBudgetNationalRevenues();
        // Εδώ έχουμε 2 roots (13 και 15)
        assertEquals(2, mainRevenues.size());
        assertTrue(mainRevenues.contains(revenue13));
        assertTrue(mainRevenues.contains(revenue15));
    }

    @Test
    void calculateSumTest() {
        // 35,000,000 (13) + 265,000,000 (15)
        long expectedSum = 300000000L;
        assertEquals(expectedSum, calculateSum());
    }

    @Test
    void findSuperCategoryTest() {
        BudgetRevenue parent = revenue13409.findSuperCategory();
        assertNotNull(parent);
        assertEquals("134", parent.getCode());
        assertEquals(35000000L, parent.getAmount());
    }


    @Test
    void getSuperCategoriesTest() {
        ArrayList<BudgetRevenue> superCategories = revenue13409.getSuperCategories();
        assertEquals(2, superCategories.size());
        //Checking if the list contains all super categories
        assertTrue(superCategories.contains(revenue13));
        assertTrue(superCategories.contains(revenue134));
    }

    @Test
    void findNextLevelSubCategoriesTest() {

        ArrayList<BudgetRevenue> subCategories = revenue13.findNextLevelSubCategories();

        //One children in the next level - 134
        assertEquals(1, subCategories.size());
        assertTrue(subCategories.contains(revenue134));
    }


    @Test
    void findAllSubCategoriesTest() {
        ArrayList<BudgetRevenue> allSubCategories = revenue13.findAllSubCategories();
        //2 children total - 134, 13409
        assertEquals(2, allSubCategories.size());
        assertTrue(allSubCategories.contains(revenue134));
        assertTrue(allSubCategories.contains(revenue13409));
    }
}