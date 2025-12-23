package com.financial.ui.views;

import com.financial.entries.BudgetRevenue;
import com.financial.entries.PublicInvestmentBudgetExpense;
import com.financial.entries.PublicInvestmentBudgetRevenue;
import com.financial.entries.RegularBudgetExpense;
import com.financial.entries.RegularBudgetRevenue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Dashboard view - main overview screen.
 */
public class DashboardView {

    private final VBox view;

    public DashboardView() {
        view = new VBox(20);
        view.setPadding(new Insets(30));
        view.setStyle("-fx-background-color: #11111b;");

        Label title = new Label("Επισκόπηση Προϋπολογισμού");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #cdd6f4;");

        Label subtitle = new Label("Κρατικός Προϋπολογισμός 2025");
        subtitle.setStyle("-fx-font-size: 16px; -fx-text-fill: #6c7086;");

        VBox header = new VBox(5, title, subtitle);
        HBox statsRow = createStatsRow();
        VBox infoSection = createInfoSection();

        view.getChildren().addAll(header, statsRow, infoSection);
    }

    private HBox createStatsRow() {
        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER_LEFT);

        long totalRegularRevenue = calculateTotalRegularRevenue();
        long totalPublicInvestmentRevenue = calculateTotalPublicInvestmentRevenue();
        long totalRevenue = totalRegularRevenue + totalPublicInvestmentRevenue;

        long totalRegularExpense = calculateTotalRegularExpense();
        long totalPublicInvestmentExpense = calculateTotalPublicInvestmentExpense();
        long totalExpense = totalRegularExpense + totalPublicInvestmentExpense;

        long balance = totalRevenue - totalExpense;

        VBox revenueCard = createStatCard("Συνολικά Έσοδα", formatAmount(totalRevenue),
            "#a6e3a1", "Τακτικός: " + formatAmount(totalRegularRevenue));
        VBox expenseCard = createStatCard("Συνολικά Έξοδα", formatAmount(totalExpense),
            "#f38ba8", "Τακτικός: " + formatAmount(totalRegularExpense));
        VBox balanceCard = createStatCard("Ισοζύγιο", formatAmount(balance),
            balance >= 0 ? "#a6e3a1" : "#f38ba8", balance >= 0 ? "Πλεόνασμα" : "Έλλειμμα");
        VBox investmentCard = createStatCard("Δημόσιες Επενδύσεις",
            formatAmount(totalPublicInvestmentRevenue), "#89b4fa", "Έσοδα ΠΔΕ");

        HBox.setHgrow(revenueCard, Priority.ALWAYS);
        HBox.setHgrow(expenseCard, Priority.ALWAYS);
        HBox.setHgrow(balanceCard, Priority.ALWAYS);
        HBox.setHgrow(investmentCard, Priority.ALWAYS);

        row.getChildren().addAll(revenueCard, expenseCard, balanceCard, investmentCard);
        return row;
    }

    private VBox createStatCard(String title, String value, String valueColor, String subtitle) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: #1e1e2e; -fx-background-radius: 12;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #6c7086;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: "
            + valueColor + ";");

        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #6c7086;");

        card.getChildren().addAll(titleLabel, valueLabel, subtitleLabel);
        return card;
    }

    private VBox createInfoSection() {
        VBox section = new VBox(15);
        section.setPadding(new Insets(20));
        section.setStyle("-fx-background-color: #1e1e2e; -fx-background-radius: 12;");

        Label sectionTitle = new Label("Πληροφορίες");
        sectionTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #cdd6f4;");

        GridPane grid = new GridPane();
        grid.setHgap(30);
        grid.setVgap(10);

        int revenueCount = BudgetRevenue.getAllBudgetRevenues().size();
        int regularRevenueCount = RegularBudgetRevenue.getAllRegularBudgetRevenues().size();
        int publicInvestmentRevenueCount =
            PublicInvestmentBudgetRevenue.getAllPublicInvestmentBudgetRevenues().size();

        addInfoRow(grid, 0, "Εγγραφές Εσόδων:", String.valueOf(revenueCount));
        addInfoRow(grid, 1, "Τακτικά Έσοδα:", String.valueOf(regularRevenueCount));
        addInfoRow(grid, 2, "Έσοδα ΠΔΕ:", String.valueOf(publicInvestmentRevenueCount));

        Label hint = new Label("Φορτώστε δεδομένα από CSV για να δείτε τον προϋπολογισμό");
        hint.setStyle("-fx-font-size: 12px; -fx-text-fill: #6c7086; -fx-font-style: italic;");

        section.getChildren().addAll(sectionTitle, grid, hint);
        return section;
    }

    private void addInfoRow(GridPane grid, int row, String label, String value) {
        Label labelNode = new Label(label);
        labelNode.setStyle("-fx-text-fill: #6c7086; -fx-font-size: 14px;");

        Label valueNode = new Label(value);
        valueNode.setStyle("-fx-text-fill: #cdd6f4; -fx-font-size: 14px; -fx-font-weight: bold;");

        grid.add(labelNode, 0, row);
        grid.add(valueNode, 1, row);
    }

    private long calculateTotalRegularRevenue() {
        return RegularBudgetRevenue.getAllRegularBudgetRevenues().stream().
            filter(r -> r.getCode().length() == 2 && r.getCode().charAt(0) <= '3').
            mapToLong(RegularBudgetRevenue::getAmount).sum();
    }

    private long calculateTotalPublicInvestmentRevenue() {
        return PublicInvestmentBudgetRevenue.getAllPublicInvestmentBudgetRevenues().stream().
            filter(r -> r.getCode().length() == 2 && r.getCode().charAt(0) <= '3').
            mapToLong(r -> r.getCoFundedAmount() + r.getNationalAmount()).sum();
    }

    private long calculateTotalRegularExpense() {
        return RegularBudgetExpense.getAllRegularBudgetExpenses().stream().
            filter(e -> e.getCode().charAt(0) <= '3').
            mapToLong(RegularBudgetExpense::getAmount).sum();
    }

    private long calculateTotalPublicInvestmentExpense() {
        return PublicInvestmentBudgetExpense.getAllPublicInvestmentBudgetExpenses().stream().
            filter(e -> e.getCode().charAt(0) <= '3').
            mapToLong(PublicInvestmentBudgetExpense::getAmount).sum();
    }

    private String formatAmount(long amount) {
        if (Math.abs(amount) >= 1_000_000_000) {
            return String.format("€%.2fB", amount / 1_000_000_000.0);
        } else if (Math.abs(amount) >= 1_000_000) {
            return String.format("€%.2fM", amount / 1_000_000.0);
        } else if (Math.abs(amount) >= 1_000) {
            return String.format("€%.2fK", amount / 1_000.0);
        } else if (amount == 0) {
            return "€0";
        } else {
            return String.format("€%,d", amount);
        }
    }

    public Region getView() {
        return view;
    }
}
