package com.financial.menu;

import java.util.Scanner;
import com.financial.charts.PieChartGenerator;
import com.financial.entries.*;
import com.financial.services.*;

public class ChartsMenu {
    public static void printChartsMenu() {
        Scanner input = new Scanner(System.in);
        System.out.println(Colors.BLUE + Colors.BOLD + "=== ΓΡΑΦΗΜΑΤΑ & ΟΠΤΙΚΟΠΟΙΗΣΕΙΣ ===");
        System.out.println();
        System.out.println(Colors.BLUE + Colors.BOLD + "[1] " + Colors.RESET + "Κατανομή Εσόδων (Pie Chart)");
        System.out.println(Colors.BLUE + Colors.BOLD + "[2] " + Colors.RESET + "Κατανομή Εξόδων (Pie Chart)");
        System.out.println(Colors.BLUE + Colors.BOLD + "[3] " + Colors.RESET + "Σύγκριση Εσόδων–Εξόδων (Bar Chart)");
        System.out.println(Colors.BLUE + Colors.BOLD + "[3] " + Colors.RESET + "Σύγκριση Εσόδων–Εξόδων (Bar Chart)");
        System.out.println(Colors.BLUE + Colors.BOLD + "[4] " + Colors.RESET + "Δαπάνες ανά Υπουργείο (Bar Chart)");
        System.out.println(Colors.BLUE + Colors.BOLD + "[0] " + Colors.RESET + "Επιστροφή στο Κύριο Μενού" + Colors.RESET_2);
        System.out.println();
        System.out.print("Επιλογή: ");
        int choice = input.nextInt();
        input.nextLine();
        if (choice == 1) {
            printRevenueChartsSubMenu();
        }
    }

    public static void printRevenueChartsSubMenu() {
        Scanner input = new Scanner(System.in);
        System.out.println();
        System.out.println(Colors.BLUE + Colors.BOLD + "=== ΚΑΤΑΝΟΜΗ ΕΣΟΔΩΝ (PIE CHART) ===");
        System.out.println();
        System.out.println(Colors.BLUE + Colors.BOLD + "[1] " + Colors.RESET + "Κατανομή Βασικών Λογαριασμών Εσόδων");
        System.out.println(Colors.BLUE + Colors.BOLD + "[2] " + Colors.RESET + "Κατανομή Φόρων");
        System.out.println(Colors.BLUE + Colors.BOLD + "[3] " + Colors.RESET + "Κατανομή Μεταβιβάσεων");
        System.out.println(Colors.BLUE + Colors.BOLD + "[4] " + Colors.RESET + "Κατανομή Πωλήσεων Αγαθών & Υπηρεσιών");
        System.out.println(Colors.BLUE + Colors.BOLD + "[5] " + Colors.RESET + "Κατανομή Λοιπών Τρεχόντων Εσόδων");
        System.out.println(Colors.BLUE + Colors.BOLD + "[0] " + Colors.RESET + "Επιστροφή στο Κύριο Μενού" + Colors.RESET_2);
        System.out.println();
        System.out.print("Επιλογή: ");
        int choice = input.nextInt();
        input.nextLine();
        if (choice == 1) {
            PieChartGenerator.generateChart(BudgetRevenueLogicService.getMainBudgetRevenues(BudgetRevenue.getAllBudgetRevenues()), "Κατανομή Εσόδων");
        } else if (choice == 2) {
            printTaxChartsSubMenu();
        } else if (choice == 3) {
            BudgetRevenue budgetRevenue = BudgetRevenueLogicService.findRevenueWithCode("13", BudgetRevenue.getAllBudgetRevenues());
            PieChartGenerator.generateChart(budgetRevenue.findNextLevelSubCategories(), "Κατανομή Μεταβιβάσεων");
        } else if (choice == 4) {
            BudgetRevenue budgetRevenue = BudgetRevenueLogicService.findRevenueWithCode("14", BudgetRevenue.getAllBudgetRevenues());
            PieChartGenerator.generateChart(budgetRevenue.findNextLevelSubCategories(), "Κατανομή Πωλήσεων Αγαθών & Υπηρεσιών");
        } else if (choice == 5) {
            BudgetRevenue budgetRevenue = BudgetRevenueLogicService.findRevenueWithCode("15", BudgetRevenue.getAllBudgetRevenues());
            PieChartGenerator.generateChart(budgetRevenue.findNextLevelSubCategories(), "Κατανομή Λοιπών Τρεχόντων Εσόδων");
        }
    }

    public static void printTaxChartsSubMenu() {
        Scanner input = new Scanner(System.in);
        System.out.println();
        System.out.println(Colors.BLUE + Colors.BOLD + "=== ΚΑΤΑΝΟΜΗ ΦΟΡΩΝ ===");
        System.out.println();
        System.out.println(Colors.BLUE + Colors.BOLD + "[1] " + Colors.RESET + "Κατανομή Βασικών Λογαριασμών Φόρων");
        System.out.println(Colors.BLUE + Colors.BOLD + "[2] " + Colors.RESET + "Κατανομή Φόρων επί αγαθών και υπηρεσιών");
        System.out.println(Colors.BLUE + Colors.BOLD + "[3] " + Colors.RESET + "Κατανομή Φόρων Εισοδήματος");
        System.out.println(Colors.BLUE + Colors.BOLD + "[0] " + Colors.RESET + "Επιστροφή στο Κύριο Μενού" + Colors.RESET_2);
        System.out.println();
        System.out.print("Επιλογή: ");
        int choice = input.nextInt();
        input.nextLine();
        switch (choice) {
            case 1 -> {
                BudgetRevenue budgetRevenue = BudgetRevenueLogicService.findRevenueWithCode("11", BudgetRevenue.getAllBudgetRevenues());
                PieChartGenerator.generateChart(budgetRevenue.findNextLevelSubCategories(), "Κατανομή Φόρων");
            }
            case 2 -> {
                BudgetRevenue budgetRevenue = BudgetRevenueLogicService.findRevenueWithCode("111", BudgetRevenue.getAllBudgetRevenues());
                PieChartGenerator.generateChart(budgetRevenue.findNextLevelSubCategories(), "Κατανομή Φόρων επί αγαθών και υπηρεσιών");
            }
            case 3 -> {
                BudgetRevenue budgetRevenue = BudgetRevenueLogicService.findRevenueWithCode("115", BudgetRevenue.getAllBudgetRevenues());
                PieChartGenerator.generateChart(budgetRevenue.findNextLevelSubCategories(), "Κατανομή Φόρων Εισοδήματος");
            }
        }
    }
}
