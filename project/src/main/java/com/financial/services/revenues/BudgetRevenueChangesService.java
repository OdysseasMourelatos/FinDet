package com.financial.services.revenues;

import com.financial.entries.BudgetRevenue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.financial.services.revenues.BudgetRevenueLogicService.getNextLevelSubCategories;

public class BudgetRevenueChangesService {

    private BudgetRevenueChangesService() {
        // utility class – no instances
    }

    public static <T extends BudgetRevenue> void setAmountOfSuperCategories(ArrayList<T> superCategories, long change) {
        for (BudgetRevenue superCategory : superCategories) {
            superCategory.setAmount(superCategory.getAmount() + change);
        }
    }

    public static <T extends BudgetRevenue> void setAmountOfNextLevelSubCategoriesWithEqualDistribution(T parent, ArrayList<T> revenues, long change) {
        ArrayList<BudgetRevenue> nextLevelSubCategories = getNextLevelSubCategories(parent, revenues);
        if (!nextLevelSubCategories.isEmpty()) {
            long changeOfCategory = change / nextLevelSubCategories.size();
            for (BudgetRevenue subCategory : nextLevelSubCategories) {
                subCategory.setAmount(subCategory.getAmount() + changeOfCategory);
            }
        }
    }

    public static <T extends BudgetRevenue> void setAmountOfAllSubCategoriesWithEqualDistribution(T parent, ArrayList<T> revenues, long change) {
        ArrayList<BudgetRevenue> nextLevelSubCategories = getNextLevelSubCategories(parent, revenues);
        long changeOfSubCategory = change;

        try {
            // Βάζω τα αρχικά ποσά κάθε επόμενης κατηγορίας σε Map
            Map<String, Long> map = new HashMap<>();
            for (BudgetRevenue subCategory : nextLevelSubCategories) {
                map.put(subCategory.getCode(), subCategory.getAmount());
            }

            // Ανανεώνω τα ποσά της κάθε επόμενης κατηγορίας (Βασική εφαρμογή αλλαγής)
            setAmountOfNextLevelSubCategoriesWithEqualDistribution(parent, revenues, changeOfSubCategory);

            // Αν δεν υπάρχουν άλλες υποκατηγορίες, τερματίζει.
            // Σημείωση: Υποθέτουμε ότι το μήκος 10 είναι το τελικό επίπεδο
            if (parent.getCode().length() == 10) {
                return;
            }

            // Για κάθε υποκατηγορία σε επόμενο επίπεδο (Αναδρομή)
            for (BudgetRevenue subCategory : nextLevelSubCategories) {
                // Υπολογισμός της πραγματικής αλλαγής που έγινε
                changeOfSubCategory = (subCategory.getAmount() - map.get(subCategory.getCode()));

                // Καλούμε αναδρομικά την ίδια μέθοδο
                setAmountOfAllSubCategoriesWithEqualDistribution((T) subCategory, revenues, changeOfSubCategory);
            }
        } catch (RuntimeException e) {
            return;
        }
    }

    public static <T extends BudgetRevenue> void setAmountOfNextLevelSubCategoriesWithPercentageAdjustment(T parent, ArrayList<T> revenues, double percentage) {
        ArrayList<BudgetRevenue> nextLevelSubCategories = getNextLevelSubCategories(parent, revenues);
        if (!nextLevelSubCategories.isEmpty()) {
            for (BudgetRevenue subCategory : nextLevelSubCategories) {
                subCategory.setAmount((long) (subCategory.getAmount() * (1 + percentage)));
            }
        }
    }

    public static <T extends BudgetRevenue> void setAmountOfAllSubCategoriesWithPercentageAdjustment(T parent, ArrayList<T> revenues, double percentage) {
        try {
            // Ανανεώνω τα ποσά της κάθε επόμενης κατηγορίας (Βασική εφαρμογή αλλαγής)
            setAmountOfNextLevelSubCategoriesWithPercentageAdjustment(parent, revenues, percentage);

            // Αν δεν υπάρχουν άλλες υποκατηγορίες τερματίζει
            // Σημείωση: Υποθέτουμε ότι το μήκος 10 είναι το τελικό επίπεδο
            if (parent.getCode().length() == 10) {
                throw new RuntimeException();
            }

            // Για κάθε υποκατηγορία σε επόμενο επίπεδο (Αναδρομή)
            ArrayList<BudgetRevenue> nextLevelSubCategories = getNextLevelSubCategories(parent, revenues);
            for (BudgetRevenue subCategory : nextLevelSubCategories) {
                setAmountOfAllSubCategoriesWithPercentageAdjustment((T) subCategory, revenues, percentage);
            }
        } catch (RuntimeException e) {
            return;
        }
    }

    public static long roundToNearestHundred(long amount) {
        return Math.round(amount / 100.0) * 100;
    }
}
