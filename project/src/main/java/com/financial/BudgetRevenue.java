package com.financial;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BudgetRevenue extends BudgetEntry{

    protected static ArrayList <BudgetRevenue> budgetRevenues = new ArrayList<>();

    public BudgetRevenue(String code, String description, String category, long amount) {
        super(code, description, category, amount);
        budgetRevenues.add(this);
    }

    public static ArrayList<BudgetRevenue> getAllBudgetRevenues() {
        return budgetRevenues;
    }

    public static void printAllBudgetRevenues(){
        for (BudgetRevenue budgetRevenue : budgetRevenues) {
            System.out.println(budgetRevenue);
        }
    }

    public static List<BudgetRevenue> getMainBudgetRevenues() {
        return budgetRevenues.stream().filter(r -> r.getCode().length() == 2).collect(Collectors.toList());
    }

    public static void printMainBudgetRevenues(){
        for (BudgetRevenue budgetRevenue : getMainBudgetRevenues()) {
            System.out.println(budgetRevenue);
        }
    }

    public int getLevelOfHierarchy() {
        return switch (getCode().length()) {
            case 2 -> 1;   // "11" - top level
            case 3 -> 2;   // "111" - second level
            case 5 -> 3;   // "11101" - third level
            case 7 -> 4;   // "1110103" - fourth level
            case 10 -> 5;  // "1110103001" - fifth level
            default -> 0;  // unknown
        };
    }

    public BudgetRevenue findSuperCategory() {
        int level = getLevelOfHierarchy();
        String tempCode;
        switch (level) {
            case 2 -> tempCode = getCode().substring(0,2);
            case 3 -> tempCode = getCode().substring(0,3);
            case 4 -> tempCode = getCode().substring(0,5);
            case 5 -> tempCode = getCode().substring(0,7);
            default -> tempCode = "0";
        }
        return findRevenueWithCode(tempCode);
    }

    public ArrayList<BudgetRevenue> getSuperCategories(){
        ArrayList<BudgetRevenue> superCategories= new ArrayList<>();
        BudgetRevenue superCategory = findSuperCategory();
        while (superCategory != null) {
            superCategories.add(superCategory);
            superCategory = superCategory.findSuperCategory();
        }
        return superCategories;
    }

    public void printSuperCategories(){
        if (getSuperCategories().isEmpty()) {
            System.out.println("Δεν υπάρχουν κατηγορίες σε υψηλότερη ιεραρχία");
        } else {
            for (int i = getSuperCategories().size() -1; i >= 0; i--){
                System.out.println(getSuperCategories().get(i));
            }
        }
    }

    public void setAmountOfSuperCategories(long change){
        BudgetRevenue revenueSuperCategory = findSuperCategory();

        while (revenueSuperCategory != null){
            revenueSuperCategory.setAmount(revenueSuperCategory.getAmount() + change);
            revenueSuperCategory = revenueSuperCategory.findSuperCategory();
        }
    }

    public static BudgetRevenue findRevenueWithCode (String code) {
        for (BudgetRevenue budgetRevenue : budgetRevenues) {
            if (budgetRevenue.getCode().equals(code)) {
                return budgetRevenue;
            }
        }
        return null;
    }

    @Override
    public String toString () {
        return super.toString();
    }
}

