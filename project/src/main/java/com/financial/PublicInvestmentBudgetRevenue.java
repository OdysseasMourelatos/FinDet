package com.financial;

public class PublicInvestmentBudgetRevenue extends BudgetEntry{
    protected static ArrayList<PublicInvestmentBudgetRevenue> publicInvestmentBudgetRevenues = new ArrayList<>();
    private String type;

    public PublicInvestmentBudgetRevenue(String code, String description, String category, String type, long amount) {
        super(code, description, category, amount);
        this.type = type;
        publicInvestmentBudgetRevenues.add(this);
    }

    public static ArrayList<PublicInvestmentBudgetRevenue> getAllPublicInvestmentBudgetRevenues() {
        return publicInvestmentBudgetRevenues;
    }

    public static void printAllPublicInvestmentBudgetRevenues(){
        for (PublicInvestmentBudgetRevenue publicInvestmentBudgetRevenue : publicInvestmentBudgetRevenues) {
            System.out.println(publicInvestmentBudgetRevenue);
        }
    }

    public String getType(){
        return type;
    }

    @Override
    public String toString(){
        return "Code: " + getCode() + ", Description: " + getDescription() + ", Category: " + getCategory() + ", Type: " + type + ", Amount: " + String.format("%,d", getAmount());
    }
}