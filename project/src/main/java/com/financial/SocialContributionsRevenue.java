package com.financial;

public class SocialContributionsRevenue extends BudgetRevenue{
    public SocialContributionsRevenue(String code, String description, String category, long amount) {
        super(code, description, category, amount);
    }

    public static ArrayList<SocialContributionsRevenue> getSocialContributionsRevenue() {
        ArrayList<SocialContributionsRevenue> result = new ArrayList<>();
        for (BudgetRevenue revenue : budgetRevenues) {
            if (revenue instanceof SocialContributionsRevenue) {
                result.add((SocialContributionsRevenue) revenue);
            }
        }
        return result;
    }

    public static void printSocialContributionsRevenues(){
        for (BudgetRevenue budgetRevenue : getSocialContributionsRevenue()) {
            System.out.println(budgetRevenue);
        }
    }
}