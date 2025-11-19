package com.financial;

import java.util.ArrayList;

public class OtherCurrentRevenue extends BudgetRevenue{
    public OtherCurrentRevenue(String code, String description, String category, long amount) {
        super(code, description, category, amount);
    }
    public static ArrayList<OtherCurrentRevenue> getOtherCurrentRevenue() {
        ArrayList<OtherCurrentRevenue> result = new ArrayList<>();
        for (BudgetRevenue revenue : budgetRevenues) {
            if (revenue instanceof OtherCurrentRevenue) {
                result.add((OtherCurrentRevenue) revenue);
            }
        }
        return result;
    }

    public static void printOtherCurrentRevenue(){
        for (BudgetRevenue budgetRevenue : getOtherCurrentRevenue()) {
            System.out.println(budgetRevenue);
        }
    }

    public static OtherCurrentRevenue findOtherCurrentRevenueWithCode (String code) {
        for (OtherCurrentRevenue otherCurrentRevenue : getOtherCurrentRevenue()) {
            if (otherCurrentRevenue.getCode().equals(code)) {
                return otherCurrentRevenue;
            }
        }
        return null;
    }
}
