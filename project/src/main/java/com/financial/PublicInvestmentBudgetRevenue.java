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
        ArrayList<PublicInvestmentBudgetRevenue> publicInvestmentBudgetNationalRevenues = new ArrayList<>();
        for (PublicInvestmentBudgetRevenue revenue : publicInvestmentBudgetRevenues) {
            if (revenue.getType().equals("ΕΘΝΙΚΟ") || revenue.getType().equals("ΕΘΝΙΚΟ ΣΚΕΛΟΣ")) {
                publicInvestmentBudgetNationalRevenues.add(revenue);
            }
        }
        return publicInvestmentBudgetNationalRevenues;
    }

    public static ArrayList<PublicInvestmentBudgetRevenue> getPublicInvestmentBudgetCoFundedRevenues() {
        ArrayList<PublicInvestmentBudgetRevenue> publicInvestmentBudgetCoFundedRevenues = new ArrayList<>();
        for (PublicInvestmentBudgetRevenue revenue : publicInvestmentBudgetRevenues) {
            if (revenue.getType().equals("ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ") || revenue.getType().equals("ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ ΣΚΕΛΟΣ") ) {
                publicInvestmentBudgetCoFundedRevenues.add(revenue);
            }
        }
        return publicInvestmentBudgetCoFundedRevenues;
    }

    public static void printAllPublicInvestmentBudgetRevenues() {
        DataOutput.printPublicInvestmentBudgetRevenueWithAsciiTable(getAllPublicInvestmentBudgetRevenues());
    }

    public static void printPublicInvestmentBudgetNationalRevenues() {
        DataOutput.printPublicInvestmentBudgetRevenueWithAsciiTable(getPublicInvestmentBudgetNationalRevenues());
    }

    public static void printPublicInvestmentBudgetCoFundedRevenues() {
        DataOutput.printPublicInvestmentBudgetRevenueWithAsciiTable(getPublicInvestmentBudgetCoFundedRevenues());
    }

    public static long getSumOfAllPublicInvestmentBudgetRevenues() {
        long sum = 0;
        for (PublicInvestmentBudgetRevenue publicInvestmentBudgetRevenue : publicInvestmentBudgetRevenues) {
            if (publicInvestmentBudgetRevenue.getCode().length() == 2) {
                sum += publicInvestmentBudgetRevenue.getAmount();
            }
        }
        return sum;
    }

    public static long getSumOfPublicInvestmentBudgetNationalRevenues() {
        long sum = 0;
        for (PublicInvestmentBudgetRevenue publicInvestmentBudgetRevenue : getPublicInvestmentBudgetNationalRevenues()) {
            if (publicInvestmentBudgetRevenue.getCode().length() == 2) {
                sum += publicInvestmentBudgetRevenue.getAmount();
            }
        }
        return sum;
    }

    public static long getSumOfPublicInvestmentBudgetCoFundedExpenses() {
        long sum = 0;
        for (PublicInvestmentBudgetRevenue publicInvestmentBudgetRevenue : getPublicInvestmentBudgetCoFundedRevenues()) {
            if (publicInvestmentBudgetRevenue.getCode().length() == 2) {
                sum += publicInvestmentBudgetRevenue.getAmount();
            }
        }
        return sum;
    }
    
    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Code: " + getCode() + ", Description: " + getDescription() + ", Category: " + getCategory() + ", Type: " + type + ", Amount: " + String.format("%,d", getAmount());
    }
}