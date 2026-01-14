package com.financial.pdf;

import com.financial.entries.BudgetExpense;
import com.financial.entries.Entity;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Class responsible for exporting Greek State Budget expense data into PDF format.
 * Organizes fiscal data hierarchically by Entity (Ministry) and Service,
 * covering both the Regular Budget and the Public Investment Budget (PIB).
 */
public class BudgetExpenseConvertToPdf {

    public static void createPdf(String filename) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();

            String os = System.getProperty("os.name").toLowerCase();
            String fontPath;

            if (os.contains("win")) {
                fontPath = "C:/Windows/Fonts/arial.ttf";
            } else {
                fontPath = "/usr/share/fonts/truetype/freefont/FreeSans.ttf";
            }

            BaseFont bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            Font titleFont = new Font(bf, 14, Font.BOLD);
            Font subTitleFont = new Font(bf, 12, Font.BOLD);
            Font tableFont = new Font(bf, 12, Font.NORMAL);
            BaseColor budgetBlue = new BaseColor(153, 204, 255);
            Font boldFont = new Font(bf, 12, Font.BOLD);

            for (Entity entity : Entity.getEntities()) {
                Paragraph title = new Paragraph(entity.getEntityName(), titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                title.setSpacingAfter(20);
                document.add(title);

                Paragraph sub1 = new Paragraph("ΤΑΚΤΙΚΟΣ ΠΡΟΥΠΟΛΟΓΙΣΜΟΣ", subTitleFont);
                sub1.setAlignment(Element.ALIGN_CENTER);
                document.add(sub1);
                document.add(new Paragraph(" "));

                PdfPTable tableRegular = createBudgetTable(entity, "REGULAR", tableFont, boldFont, budgetBlue);
                document.add(tableRegular);
                document.add(new Paragraph(" "));

                if (!entity.getAllPublicInvestmentNationalServiceCodes().isEmpty()) {
                    Paragraph sub2 = new Paragraph("ΠΡΟΥΠΟΛΟΓΙΜΟΣ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ - ΕΘΝΙΚΟ ΣΚΕΛΟΣ", subTitleFont);
                    sub2.setAlignment(Element.ALIGN_CENTER);
                    document.add(sub2);
                    document.add(new Paragraph(" "));
                    PdfPTable tableNational = createBudgetTable(entity, "NATIONAL", tableFont, boldFont, budgetBlue);
                    document.add(tableNational);
                    document.add(new Paragraph(" "));
                }

                if (!entity.getAllPublicInvestmentCoFundedServiceCodes().isEmpty()) {
                    Paragraph sub3 = new Paragraph("ΠΡΟΥΠΟΛΟΓΙΜΟΣ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ - ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ ΣΚΕΛΟΣ", subTitleFont);
                    sub3.setAlignment(Element.ALIGN_CENTER);
                    document.add(sub3);
                    document.add(new Paragraph(" "));
                    PdfPTable tableCoFunded = createBudgetTable(entity, "COFUNDED", tableFont, boldFont, budgetBlue);
                    document.add(tableCoFunded);
                }

                if (!entity.equals(Entity.getEntities().get(Entity.getEntities().size() - 1))) {
                    document.newPage();
                }
            }

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static PdfPTable createBudgetTable(Entity entity, String type, Font tableFont, Font boldFont, BaseColor budgetBlue) throws DocumentException {
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{20, 60, 20});
        table.setHeaderRows(1);

        table.addCell(PdfFormat.createCenteredCell("Κωδικός Ταξινόμησης", tableFont, true));
        table.addCell(PdfFormat.createCenteredCell("Ονομασία", tableFont, true));
        table.addCell(PdfFormat.createCenteredCell("Ποσό", tableFont, true));

        java.util.List<String> serviceCodes;
        long totalSum;

        // Επιλογή δεδομένων βάσει τύπου
        if (type.equals("NATIONAL")) {
            serviceCodes = entity.getAllPublicInvestmentNationalServiceCodes();
            totalSum = entity.calculatePublicInvestmentNationalSum();
        } else if (type.equals("COFUNDED")) {
            serviceCodes = entity.getAllPublicInvestmentCoFundedServiceCodes();
            totalSum = entity.calculatePublicInvestmentCoFundedSum();
        } else {
            serviceCodes = entity.getAllRegularServiceCodes();
            totalSum = entity.calculateRegularSum();
        }

        boolean evenRow = true;
        for (String serviceCode : serviceCodes) {
            BaseColor rowColor = evenRow ? BaseColor.WHITE : budgetBlue;

            // Στοιχεία Υπηρεσίας
            String serviceName = switch (type) {
                case "NATIONAL" -> entity.getPublicInvestmentNationalServiceNameWithCode(serviceCode);
                case "COFUNDED" -> entity.getPublicInvestmentCoFundedServiceNameWithCode(serviceCode);
                default -> entity.getRegularServiceNameWithCode(serviceCode);
            };

            long serviceSum = switch (type) {
                case "NATIONAL" -> entity.getPublicInvestmentNationalSumOfServiceWithCode(serviceCode);
                case "COFUNDED" -> entity.getPublicInvestmentCoFundedSumOfServiceWithCode(serviceCode);
                default -> entity.getRegularSumOfServiceWithCode(serviceCode);
            };

            PdfPCell sCell1 = PdfFormat.createZebraCell(serviceCode, tableFont, rowColor);
            sCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(sCell1);

            PdfPCell sCell2 = PdfFormat.createZebraCell(serviceName, tableFont, rowColor);
            sCell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(sCell2);

            PdfPCell sCell3 = PdfFormat.createZebraCell(String.format("%,d", serviceSum), tableFont, rowColor);
            sCell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(sCell3);

            evenRow = !evenRow;

            List<? extends BudgetExpense> expenses = switch (type) {
                case "NATIONAL" -> entity.getPublicInvestmentNationalExpensesOfServiceWithCode(serviceCode);
                case "COFUNDED" -> entity.getPublicInvestmentCoFundedExpensesOfServiceWithCode(serviceCode);
                default -> entity.getRegularExpensesOfServiceWithCode(serviceCode);
            };

            for (BudgetExpense expense : expenses) {
                rowColor = evenRow ? BaseColor.WHITE : budgetBlue;

                PdfPCell eCell1 = PdfFormat.createZebraCell(String.valueOf(expense.getCode()), tableFont, rowColor);
                eCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(eCell1);

                PdfPCell eCell2 = PdfFormat.createZebraCell("   " + expense.getDescription(), tableFont, rowColor);
                eCell2.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(eCell2);

                PdfPCell eCell3 = PdfFormat.createZebraCell(String.format("%,d", expense.getAmount()), tableFont, rowColor);
                eCell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(eCell3);

                evenRow = !evenRow;
            }
        }

        PdfPCell totalLabelCell = new PdfPCell(new Phrase("Σύνολο:", boldFont));
        totalLabelCell.setColspan(2);
        totalLabelCell.setBackgroundColor(budgetBlue);
        totalLabelCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        totalLabelCell.setPadding(5);
        table.addCell(totalLabelCell);

        PdfPCell totalAmountCell = new PdfPCell(new Phrase(String.format("%,d", totalSum), boldFont));
        totalAmountCell.setBackgroundColor(budgetBlue);
        totalAmountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalAmountCell.setPadding(5);
        table.addCell(totalAmountCell);

        return table;
    }
}