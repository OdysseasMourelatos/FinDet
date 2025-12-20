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
    void getAllPublicInvestmentBudgetCoFundedRevenuesTest() {
        ArrayList<PublicInvestmentBudgetCoFundedRevenue> allRevenues = PublicInvestmentBudgetCoFundedRevenue.getAllPublicInvestmentBudgetCoFundedRevenues();
        assertEquals(4, allRevenues.size());
        assertTrue(allRevenues.get(0) instanceof PublicInvestmentBudgetCoFundedRevenue);
    }


    @Test
    void getMainPublicInvestmentBudgetCoFundedRevenueTest() {
        ArrayList<PublicInvestmentBudgetCoFundedRevenue> mainRevenues = PublicInvestmentBudgetCoFundedRevenue.getMainPublicInvestmentBudgetCoFundedRevenues();
        // Μόνο το 13 είναι root (2 ψηφία)
        assertEquals(1, mainRevenues.size());
        assertTrue(mainRevenues.contains(revenue13));
    }


    @Test
    void calculateSumTest() {
        // Υπολογισμός αθροίσματος των Main (μόνο το 13)
        long expectedSum = 4190000000L;
        assertEquals(expectedSum, calculateSum());
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

    @Test
    void implementChangesOfPercentageAdjustmentTest() {
        // Εφαρμογή +10% στο ανωτατο επίπεδο (13)
        double percentage = 0.1;

        long initial13 = revenue13.getAmount();
        long initial135 = revenue135.getAmount();
        long initial13501 = revenue13501.getAmount();
        long initial1350101 = revenue1350101.getAmount();

        long changeOnSelf = (long) (initial13 * percentage);

        revenue13.implementChangesOfPercentageAdjustment(percentage);


        // 1- Checking the same account
        assertEquals(initial13 + changeOnSelf, revenue13.getAmount());

        // 2 - Checking the subcategories
        long expected135 = (long) (initial135 * (1 + percentage));
        long expected13501 = (long) (initial13501 * (1 + percentage));
        long expected1350101 = (long) (initial1350101 * (1 + percentage));

        assertEquals(expected135, revenue135.getAmount());
        assertEquals(expected13501, revenue13501.getAmount());
        assertEquals(expected1350101, revenue1350101.getAmount());
    }

    @Test
    void implementChangesOfPercentageAdjustmentTest2() {
        // Εφαρμογή +10% στο μεσαίο επίπεδο (135)
        double percentage = 0.1;

        long initial13 = revenue13.getAmount();
        long initial135 = revenue135.getAmount();
        long initial13501 = revenue13501.getAmount();
        long initial1350101 = revenue1350101.getAmount();

        long changeOnSelf = (long) (initial135 * percentage);

        revenue135.implementChangesOfPercentageAdjustment(percentage);


        // 1- Checking the same account
        assertEquals(initial135 + changeOnSelf, revenue135.getAmount());

        // 2 - Checking the supercategories
        assertEquals(initial13 + changeOnSelf, revenue13.getAmount());


        // 3 - Checking the subcategories
        long expected13501 = (long) (initial13501 * (1 + percentage));
        assertEquals(expected13501, revenue13501.getAmount());
        long expected1350101 = (long) (initial1350101 * (1 + percentage)); // ΔΙΟΡΘΩΣΗ: Χρήση σωστής βάσης υπολογισμού
        assertEquals(expected1350101, revenue1350101.getAmount());
    }
}