package com.financial;

public class PublicInvestmentBudgetRevenue extends BudgetEntry{
    protected static ArrayList<PublicInvestmentBudgetRevenue> publicInvestmentBudgetRevenues = new ArrayList<>();
    private String type;

    public PublicInvestmentBudgetRevenue(String code, String description, String category, String type, long amount) {
        super(code, description, category, amount);
        this.type = type;
        publicInvestmentBudgetRevenues.add(this);
    }

    public String getType(){
        return type;
    }
}