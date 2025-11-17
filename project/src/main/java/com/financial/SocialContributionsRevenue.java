package com.financial;

import java.util.ArrayList;

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

    public static SocialContributionsRevenue findSocialContributionWithCode (String code) {
        for (SocialContributionsRevenue socialContributionsRevenue : getSocialContributionsRevenue()) {
            if (socialContributionsRevenue.getCode().equals(code)) {
                return socialContributionsRevenue;
            }
        }
        return null;
    }

    @Override
    public String toString () {
        return super.toString();
    }
}