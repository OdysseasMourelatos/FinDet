package com.financial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        ListHandling.printList(budgetRevenues);
    }

    public static ArrayList<BudgetEntry> getMainBudgetRevenues() {
        ArrayList<BudgetEntry> mainBudgetRevenues = new ArrayList<>();
        for (BudgetEntry budgetRevenue : budgetRevenues){
            if (budgetRevenue.getCode().length()== 2) {
                mainBudgetRevenues.add(budgetRevenue);
            }
        }
        return mainBudgetRevenues;
    }

    public static void printMainBudgetRevenues(){
        BudgetEntry.printWithAsciiTable(getMainBudgetRevenues());
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

    public void printSuperCategoriesTopDown(){
        if (getSuperCategories().isEmpty()) {
            System.out.println("Δεν υπάρχουν κατηγορίες σε υψηλότερη ιεραρχία");
        } else {
            for (int i = getSuperCategories().size() -1; i >= 0; i--){
                System.out.println(getSuperCategories().get(i));
            }
        }
    }

    public void printSuperCategoriesBottomsUp(){
        if (getSuperCategories().isEmpty()) {
            System.out.println("Δεν υπάρχουν κατηγορίες σε υψηλότερη ιεραρχία");
        } else {
            ListHandling.printList(getSuperCategories());
        }
    }

    public void setAmountOfSuperCategories(long change){
        ArrayList<BudgetRevenue> superCategories = getSuperCategories();

        for (BudgetRevenue superCategory : superCategories) {
            superCategory.setAmount(superCategory.getAmount() + change);
        }
    }

    public ArrayList<BudgetRevenue> findAllSubCategories() {
        ArrayList<BudgetRevenue> subCategories = new ArrayList<>();
        for (BudgetRevenue budgetRevenue : budgetRevenues){
            if (budgetRevenue.getCode().startsWith(this.getCode()) && !(budgetRevenue.equals(this))){
                subCategories.add(budgetRevenue);
            }
        }
        return subCategories;
    }

    public void printAllSubCategories(){
        ArrayList<BudgetRevenue> allSubCategories = findAllSubCategories();
        ListHandling.printList(allSubCategories);
    }

    public ArrayList<BudgetRevenue> findNextLevelSubCategories() {
        int level = getLevelOfHierarchy();
        int subCategoryCodeLength;
        switch (level){
            case 1 -> subCategoryCodeLength = 3;
            case 2 -> subCategoryCodeLength = 5;
            case 3 -> subCategoryCodeLength = 7;
            case 4 -> subCategoryCodeLength = 10;
            default -> subCategoryCodeLength = 0;
        }
        ArrayList<BudgetRevenue> subCategories = new ArrayList<>();
        for (BudgetRevenue budgetRevenue : budgetRevenues){
            if (budgetRevenue.getCode().startsWith(this.getCode()) && (budgetRevenue.getCode().length() == subCategoryCodeLength)){
                subCategories.add(budgetRevenue);
            }
        }
        return subCategories;
    }

    public void printNextLevelSubCategories(){
        ArrayList<BudgetRevenue> nextLevelSubCategories = findNextLevelSubCategories();
        ListHandling.printList(findNextLevelSubCategories());
    }

    public void setAmountOfNextLevelSubCategoriesWithEqualDistribution(long change){
        ArrayList<BudgetRevenue> nextLevelSubCategories = findNextLevelSubCategories();
        if (!nextLevelSubCategories.isEmpty()) {
            long changeOfCategory = change / nextLevelSubCategories.size();
            for (BudgetRevenue subCategory : nextLevelSubCategories){
                subCategory.setAmount(subCategory.getAmount() + changeOfCategory);
            }
        }
    }

    public void setAmountOfAllSubCategoriesWithEqualDistribution(long change){
        ArrayList<BudgetRevenue> nextLevelSubCategories = findNextLevelSubCategories();
        long changeOfSubCategory = change;
        try {
            //Βάζω τα αρχικά ποσά κάθε επόμενης κατηγορίας σε Map
            Map<String, Long> map = new HashMap<>();
            for (BudgetRevenue subCategory : nextLevelSubCategories){
                map.put(subCategory.getCode(), subCategory.getAmount());
            }
            //Ανανεώνω τα ποσά της κάθε επόμενης κατηγορίας
            this.setAmountOfNextLevelSubCategoriesWithEqualDistribution(changeOfSubCategory);
            //Αν δεν υπάρχουν άλλες υποκατηγορίες τερματίζει
            if (this.getCode().length() == 10) {
                throw new RuntimeException();
            }
            //Για κάθε υποκατηγορία σε επόμενο επίπεδο
            for (BudgetRevenue subCategory : nextLevelSubCategories){
                changeOfSubCategory = (subCategory.getAmount() - map.get(subCategory.getCode()));
                subCategory.setAmountOfAllSubCategoriesWithEqualDistribution(changeOfSubCategory);
            }
        } catch (RuntimeException e) {
            return;
        }
    }

    public void setAmountOfNextLevelSubCategoriesWithPercentageAdjustment(double percentage) {
        ArrayList<BudgetRevenue> nextLevelSubCategories = findNextLevelSubCategories();
        if (!nextLevelSubCategories.isEmpty()) {
            for (BudgetRevenue subCategory : nextLevelSubCategories) {
                subCategory.setAmount((long) (subCategory.getAmount() * (1 + percentage)));
            }
        }
    }

    public void setAmountOfAllSubCategoriesWithPercentageAdjustment(double percentage) {
        ArrayList<BudgetRevenue> nextLevelSubCategories = findNextLevelSubCategories();
        try {
            // Ανανεώνω τα ποσά της κάθε επόμενης κατηγορίας
            this.setAmountOfNextLevelSubCategoriesWithPercentageAdjustment(percentage);
            // Αν δεν υπάρχουν άλλες υποκατηγορίες τερματίζει
            if (this.getCode().length() == 10) {
                throw new RuntimeException();
            }
            // Για κάθε υποκατηγορία σε επόμενο επίπεδο
            for (BudgetRevenue subCategory : nextLevelSubCategories) {
                subCategory.setAmountOfAllSubCategoriesWithPercentageAdjustment(percentage);
            }
        } catch (RuntimeException e) {
            return;
        }
    }

    public static ArrayList<BudgetRevenue> getBudgetRevenuesOfMainCategoryWithCode(String code) {
        ArrayList<BudgetRevenue> mainRevenues = new ArrayList<>();
        for (BudgetRevenue revenue : budgetRevenues) {
            if (revenue.getCode().startsWith(code)) {
                mainRevenues.add(revenue);
            }
        }
        return mainRevenues;
    }

    public static void printBudgetRevenuesOfMainCategoryWithCode(String code){
        ListHandling.printList(getBudgetRevenuesOfMainCategoryWithCode(code));
    }

    public static long calculateSum() {
        long sum = 0;
        for (BudgetRevenue budgetRevenue : budgetRevenues) {
            if (budgetRevenue.getCode().length() == 2) {
                sum += budgetRevenue.getAmount();
            }
        }
        return sum;
    }

    public void implementChangesOfEqualDistribution(long change){
        setAmountOfSuperCategories(change);
        setAmountOfAllSubCategoriesWithEqualDistribution(change);
        setAmount(getAmount() + change);
    }

    public void implementChangesOfPercentageAdjustment(double percentage){
        setAmountOfSuperCategories((long) (getAmount()*(percentage)));
        setAmountOfAllSubCategoriesWithPercentageAdjustment(percentage);
        setAmount((long) (getAmount()*(1+percentage)));
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
