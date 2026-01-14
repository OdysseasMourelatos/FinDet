package com.financial.pdf;

import com.financial.entries.BudgetRevenue;
import com.financial.entries.PublicInvestmentBudgetCoFundedRevenue;
import com.financial.entries.PublicInvestmentBudgetNationalRevenue;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Class responsible for exporting Greek State Budget revenue data into PDF format.
 * Generates reports for main revenue categories, detailed accounts, and
 * Public Investment Budget (PIB) revenues.
 */
public class BudgetRevenueConvertToPdf {

    public static void createPdf(String filename) {
        try {
            ArrayList<BudgetRevenue> br = BudgetRevenue.getAllBudgetRevenues();
            ArrayList<BudgetRevenue> brm = BudgetRevenue.getMainBudgetRevenues();
            ArrayList<PublicInvestmentBudgetCoFundedRevenue> brcf = PublicInvestmentBudgetCoFundedRevenue.getAllPublicInvestmentBudgetCoFundedRevenues();
            ArrayList<PublicInvestmentBudgetNationalRevenue> brn = PublicInvestmentBudgetNationalRevenue.getAllPublicInvestmentBudgetNationalRevenues();

            Document document = new Document(PageSize.A4);
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


            BaseColor govBlue = new BaseColor(153, 204, 255);
            Font titleFont = new Font(bf, 14, Font.BOLD);
            Font headerFont = new Font(bf, 11, Font.BOLD);
            Font tableFont = new Font(bf, 10, Font.NORMAL);
            Font categoryFont = new Font(bf, 10, Font.BOLD);

            addSectionTitle(document, "ΚΡΑΤΙΚΟΣ ΠΡΟΥΠΟΛΟΓΙΣΜΟΣ: ΚΥΡΙΑ ΕΣΟΔΑ", titleFont, govBlue);
            PdfPTable table0 = createBaseTable();
            addTableHeader(table0, headerFont);
            fillRevenueTable(table0, brm, tableFont, categoryFont, govBlue);
            document.add(table0);
            document.newPage();

            addSectionTitle(document, "ΚΡΑΤΙΚΟΣ ΠΡΟΥΠΟΛΟΓΙΣΜΟΣ: ΕΠΙΜΕΡΟΥΣ ΕΣΟΔΑ", titleFont, govBlue);
            PdfPTable table1 = createBaseTable();
            addTableHeader(table1, headerFont);
            fillRevenueTable(table1, br, tableFont, categoryFont, govBlue);
            document.add(table1);
            document.newPage();

            addSectionTitle(document, "ΕΣΟΔΑ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ: ΕΘΝΙΚΟ ΣΚΕΛΟΣ", titleFont, govBlue);
            PdfPTable table3 = createBaseTable();
            addTableHeader(table3, headerFont);
            fillRevenueTable(table3, brn, tableFont, categoryFont, govBlue);
            document.add(table3);


            addSectionTitle(document, "ΕΣΟΔΑ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ: ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ ΣΚΕΛΟΣ", titleFont, govBlue);
            PdfPTable table2 = createBaseTable();
            addTableHeader(table2, headerFont);
            fillRevenueTable(table2, brcf, tableFont, categoryFont, govBlue);
            document.add(table2);
            document.newPage();


            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addSectionTitle(Document doc, String title, Font font, BaseColor color) throws DocumentException {
        PdfPTable titleTable = new PdfPTable(1);
        titleTable.setWidthPercentage(100);
        PdfPCell cell = new PdfPCell(new Phrase(title, font));
        cell.setBackgroundColor(color);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(10);
        cell.setBorder(Rectangle.NO_BORDER);
        titleTable.addCell(cell);
        doc.add(titleTable);
        doc.add(new Paragraph(" "));
    }

    private static PdfPTable createBaseTable() throws DocumentException {
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{20, 60, 20});
        table.setHeaderRows(1);
        return table;
    }

    private static void addTableHeader(PdfPTable table, Font font) {
        table.addCell(PdfFormat.createCenteredCell("Κωδικός", font, true));
        table.addCell(PdfFormat.createCenteredCell("Ονομασία Λογαριασμού", font, true));
        table.addCell(PdfFormat.createCenteredCell("Ποσό (€)", font, true));
    }

    private static void fillRevenueTable(PdfPTable table, ArrayList<? extends BudgetRevenue> data, Font normal, Font bold, BaseColor zebraColor) {
        boolean isBlueRow = false;
        for (BudgetRevenue r : data) {
            BaseColor rowColor = isBlueRow ? zebraColor : BaseColor.WHITE;
            Font currentFont = (r.getLevelOfHierarchy() <= 2) ? bold : normal;

            table.addCell(PdfFormat.createZebraCell(String.valueOf(r.getCode()), currentFont, rowColor));
            table.addCell(PdfFormat.createZebraCell(r.getDescription(), currentFont, rowColor));
            table.addCell(PdfFormat.createZebraCell(String.format("%,d", r.getAmount()), currentFont, rowColor));

            isBlueRow = !isBlueRow;
        }
    }
}