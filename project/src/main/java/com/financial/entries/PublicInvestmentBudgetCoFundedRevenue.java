package com.financial.entries;

import com.financial.services.BudgetRevenueLogicService;
import com.financial.services.DataOutput;

import java.util.ArrayList;

public class PublicInvestmentBudgetCoFundedRevenue extends PublicInvestmentBudgetRevenue {

    //Constructor & Fields

    protected static ArrayList<PublicInvestmentBudgetCoFundedRevenue> publicInvestmentBudgetCoFundedRevenues = new ArrayList<>();

    public PublicInvestmentBudgetCoFundedRevenue(String code, String description, String category, String type, long amount) {
        super(code, description, category, type, amount);
        publicInvestmentBudgetCoFundedRevenues.add(this);
    }

    //Class Methods

    public static ArrayList<PublicInvestmentBudgetCoFundedRevenue> getPublicInvestmentBudgetCoFundedRevenues() {
        return publicInvestmentBudgetCoFundedRevenues;
    }

    public static void printPublicInvestmentBudgetCoFundedRevenues() {
        DataOutput.printPublicInvestmentBudgetRevenueWithAsciiTable(getPublicInvestmentBudgetCoFundedRevenues());
    }

    public static ArrayList<PublicInvestmentBudgetCoFundedRevenue> getMainPublicInvestmentBudgetCoFundedRevenues() {
        return BudgetRevenueLogicService.getMainBudgetRevenues(publicInvestmentBudgetCoFundedRevenues);
    }

    public static void printMainPublicInvestmentBudgetCoFundedRevenues() {
        BudgetRevenueLogicService.printMainBudgetRevenues(getMainPublicInvestmentBudgetCoFundedRevenues());
    }

    public static PublicInvestmentBudgetCoFundedRevenue findPublicInvestmentBudgetCoFundedRevenueWithCode(String code) {
        return BudgetRevenueLogicService.findRevenueWithCode(code, publicInvestmentBudgetCoFundedRevenues);
    }

    //Sum Method

    public static long calculateSum() {
        return BudgetRevenueLogicService.calculateSum(publicInvestmentBudgetCoFundedRevenues);
    }

}
