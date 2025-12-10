package com.financial.entries;

import com.financial.services.BudgetRevenueHandling;
import com.financial.services.DataOutput;

import java.util.ArrayList;

public class PublicInvestmentBudgetNationalRevenue extends PublicInvestmentBudgetRevenue {

    //Constructor & Fields
    protected static ArrayList<PublicInvestmentBudgetNationalRevenue> publicInvestmentBudgetNationalRevenues = new ArrayList<>();

    public PublicInvestmentBudgetNationalRevenue(String code, String description, String category, String type, long amount) {
        super(code, description, category, type, amount);
        publicInvestmentBudgetNationalRevenues.add(this);
    }

    //Class Methods

    public static ArrayList<PublicInvestmentBudgetNationalRevenue> getPublicInvestmentBudgetNationalRevenues() {
        return publicInvestmentBudgetNationalRevenues;
    }

    public static void printPublicInvestmentBudgetNationalRevenues() {
        DataOutput.printPublicInvestmentBudgetRevenueWithAsciiTable(getPublicInvestmentBudgetNationalRevenues());
    }

    public static ArrayList<PublicInvestmentBudgetNationalRevenue> getMainPublicInvestmentBudgetNationalRevenues() {
        return BudgetRevenueHandling.getMainBudgetRevenues(publicInvestmentBudgetNationalRevenues);
    }

    public static void printMainPublicInvestmentBudgetNationalRevenues() {
        BudgetRevenueHandling.printMainBudgetRevenues(getMainPublicInvestmentBudgetNationalRevenues());
    }

    public static PublicInvestmentBudgetNationalRevenue findPublicInvestmentBudgetNationalRevenueWithCode(String code) {
        return BudgetRevenueHandling.findRevenueWithCode(code, publicInvestmentBudgetNationalRevenues);
    }
}
