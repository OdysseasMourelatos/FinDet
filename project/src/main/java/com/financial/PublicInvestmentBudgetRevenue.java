package com.financial;

import java.util.ArrayList;

public class PublicInvestmentBudgetRevenue extends BudgetEntry{
    protected static ArrayList<PublicInvestmentBudgetRevenue> publicInvestmentBudgetNationalRevenues = new ArrayList<>();
    protected static ArrayList<PublicInvestmentBudgetRevenue> publicInvestmentBudgetCoFundedRevenues = new ArrayList<>();
    private String type;

    public PublicInvestmentBudgetRevenue(String code, String description, String category, String type, long amount) {
        super(code, description, category, amount);
        this.type = type;
        if (type.equals("ΕΘΝΙΚΟ") || type.equals("ΕΘΝΙΚΟ ΣΚΕΛΟΣ")) {
            publicInvestmentBudgetNationalRevenues.add(this);
        } else if (type.equals("ΣΥΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ") || type.equals("ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ ΣΚΕΛΟΣ")){
            publicInvestmentBudgetCoFundedRevenues.add(this);
        }
    }

    public static ArrayList<PublicInvestmentBudgetRevenue> getAllPublicInvestmentBudgetRevenues() {
        ArrayList<PublicInvestmentBudgetRevenue> publicInvestmentBudgetRevenues = new ArrayList<>();
        publicInvestmentBudgetRevenues.addAll(publicInvestmentBudgetNationalRevenues);
        publicInvestmentBudgetRevenues.addAll(publicInvestmentBudgetCoFundedRevenues);
        return publicInvestmentBudgetRevenues;
    }

    public static ArrayList<PublicInvestmentBudgetRevenue> getPublicInvestmentBudgetNationalRevenues(){
        return publicInvestmentBudgetNationalRevenues;
    }

    public static ArrayList<PublicInvestmentBudgetRevenue> getPublicInvestmentBudgetCoFundedRevenues(){
        return publicInvestmentBudgetCoFundedRevenues;
    }

    public static void printAllPublicInvestmentBudgetRevenues(){
        ListHandling.printList(getAllPublicInvestmentBudgetRevenues());
    }

    public String getType(){
        return type;
    }

    @Override
    public String toString(){
        return "Code: " + getCode() + ", Description: " + getDescription() + ", Category: " + getCategory() + ", Type: " + type + ", Amount: " + String.format("%,d", getAmount());
    }
}