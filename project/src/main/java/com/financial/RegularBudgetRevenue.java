package com.financial;

import com.financial.services.DataOutput;

import java.util.ArrayList;

public class RegularBudgetRevenue extends BudgetRevenue {

    protected static ArrayList<RegularBudgetRevenue> regularBudgetRevenues = new ArrayList<>();

    public RegularBudgetRevenue(String code, String description, String category, long amount) {
        super(code, description, category, amount);
        regularBudgetRevenues.add(this);
    }

    public static ArrayList<RegularBudgetRevenue> getAllRegularBudgetRevenues() {
        return regularBudgetRevenues;
    }

    public static void printAllRegularBudgetRevenues() {
        DataOutput.printRevenueWithAsciiTable(regularBudgetRevenues, calculateSum());
    }

    public static ArrayList<RegularBudgetRevenue> getMainRegularBudgetRevenues() {
        ArrayList<RegularBudgetRevenue> mainRegularBudgetRevenues = new ArrayList<>();
        for (RegularBudgetRevenue regularBudgetRevenue : regularBudgetRevenues) {
            if (regularBudgetRevenue.getCode().length() == 2) {
                mainRegularBudgetRevenues.add(regularBudgetRevenue);
            }
        }
        return mainRegularBudgetRevenues;
    }

    public static void printMainRegularBudgetRevenues() {
        DataOutput.printRevenueWithAsciiTable(getMainRegularBudgetRevenues(),  calculateSum());
    }

    public static long calculateSum() {
        long sum = 0;
        for (RegularBudgetRevenue regularBudgetRevenue : regularBudgetRevenues) {
            if (regularBudgetRevenue.getCode().length() == 2) {
                sum += regularBudgetRevenue.getAmount();
            }
        }
        return sum;
    }

    public static RegularBudgetRevenue findRevenueWithCode (String code) {
        for (RegularBudgetRevenue regularBudgetRevenue : regularBudgetRevenues) {
            if (regularBudgetRevenue.getCode().equals(code)) {
                return regularBudgetRevenue;
            }
        }
        return null;
    }

    @Override
    public String toString () {
        return super.toString();
    }
}
