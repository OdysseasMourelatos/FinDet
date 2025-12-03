package com.financial;

import java.util.ArrayList;

public class PublicInvestmentBudgetRevenue extends BudgetRevenue {
    private final String type;
    protected static ArrayList<PublicInvestmentBudgetRevenue> publicInvestmentBudgetRevenues = new ArrayList<>();

    public PublicInvestmentBudgetRevenue(String code, String description, String category, String type, long amount) {
        super(code, description, category, amount);
        this.type = type;
        publicInvestmentBudgetRevenues.add(this);
    }

    public static ArrayList<PublicInvestmentBudgetRevenue> getAllPublicInvestmentBudgetRevenues() {
        return publicInvestmentBudgetRevenues;
    }
    
    public static ArrayList<PublicInvestmentBudgetRevenue> getPublicInvestmentBudgetNationalRevenues() {
        return publicInvestmentBudgetNationalRevenues;
    }

    public static ArrayList<PublicInvestmentBudgetRevenue> getPublicInvestmentBudgetCoFundedRevenues() {
        return publicInvestmentBudgetCoFundedRevenues;
    }

    public static void printAllPublicInvestmentBudgetRevenues() {
        DataOutput.printWithAsciiTable(getAllPublicInvestmentBudgetRevenues());
    }

    public static void printPublicInvestmentBudgetNationalRevenues() {
        DataOutput.printWithAsciiTable(getPublicInvestmentBudgetNationalRevenues());
    }

    public static void printPublicInvestmentBudgetCoFundedRevenues() {
        DataOutput.printWithAsciiTable(getPublicInvestmentBudgetCoFundedRevenues());
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Code: " + getCode() + ", Description: " + getDescription() + ", Category: " + getCategory() + ", Type: " + type + ", Amount: " + String.format("%,d", getAmount());
    }
}