//package org.example;

import com.financial.BudgetRevenue;
import com.financial.PublicInvestmentBudgetRevenue;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class BudgetRevenueConvertToPdf {

    public static void createRPdf(String filename) {
        try {
            ArrayList<BudgetRevenue> br = BudgetRevenue.getAllBudgetRevenues();
            ArrayList<BudgetRevenue> brm = BudgetRevenue.getMainBudgetRevenues();
            ArrayList<PublicInvestmentBudgetRevenue> brcf =PublicInvestmentBudgetRevenue.getPublicInvestmentBudgetCoFundedRevenues();
            ArrayList<PublicInvestmentBudgetRevenue> brn =PublicInvestmentBudgetRevenue.getPublicInvestmentBudgetNationalRevenues();

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();

            BaseFont bf = BaseFont.createFont("C:/Windows/Fonts/arial.ttf",BaseFont.IDENTITY_H,BaseFont.EMBEDDED);

            Font titleFont = new Font(bf, 14, Font.BOLD);
            Font tableFont = new Font(bf, 12, Font.NORMAL);

            BaseColor zebraGray = new BaseColor(245, 245, 245);
            Paragraph title0 = new Paragraph("ΚΡΑΤΙΚΟΣ ΠΡΟΥΠΟΛΟΓΙΣΜΟΣ:ΚΥΡΙΑ ΕΣΟΔΑ", titleFont);
            title0.setAlignment(Element.ALIGN_CENTER);
            title0.setSpacingAfter(20);
            document.add(title0);

            PdfPTable table0 = new PdfPTable(3);
            table0.setWidthPercentage(100);
            table0.setHeaderRows(1);

            table0.addCell(PdfFormat.createCenteredCell("Κωδικός Ταξινόμησης", tableFont, true));
            table0.addCell(PdfFormat.createCenteredCell("Ονομασία", tableFont, true));
            table0.addCell(PdfFormat.createCenteredCell("Ποσό", tableFont, true));

            boolean evenRow = true;

            for (BudgetRevenue r : brm) {
                BaseColor rowColor = evenRow ? BaseColor.WHITE : zebraGray;
                table0.addCell(PdfFormat.createZebraCell(String.valueOf(r.getCode()), tableFont, rowColor));
                table0.addCell(PdfFormat.createZebraCell(r.getDescription(), tableFont, rowColor));
                table0.addCell(PdfFormat.createZebraCell(String.format("%,d", r.getAmount()), tableFont, rowColor));

                evenRow = !evenRow;
            }

            document.add(table0);
            document.newPage();

            Paragraph title1 = new Paragraph("ΚΡΑΤΙΚΟΣ ΠΡΟΥΠΟΛΟΓΙΣΜΟΣ:ΕΠΙΜΕΡΟΥΣ ΕΣΟΔΑ", titleFont);
            title1.setAlignment(Element.ALIGN_CENTER);
            title1.setSpacingAfter(20);
            document.add(title1);

            PdfPTable table1 = new PdfPTable(3);
            table1.setWidthPercentage(100);
            table1.setHeaderRows(1);

            table1.addCell(PdfFormat.createCenteredCell("Κωδικός Ταξινόμησης", tableFont, true));
            table1.addCell(PdfFormat.createCenteredCell("Ονομασία", tableFont, true));
            table1.addCell(PdfFormat.createCenteredCell("Ποσό", tableFont, true));
            evenRow = true;

            for (BudgetRevenue r : br) {
                BaseColor rowColor = evenRow ? BaseColor.WHITE : zebraGray;
                table1.addCell(PdfFormat.createZebraCell(String.valueOf(r.getCode()), tableFont, rowColor));
                table1.addCell(PdfFormat.createZebraCell(r.getDescription(), tableFont, rowColor));
                table1.addCell(PdfFormat.createZebraCell(String.format("%,d", r.getAmount()), tableFont, rowColor));
                evenRow = !evenRow;
            }

            document.add(table1);
            document.newPage();

            Paragraph title2 = new Paragraph("ΚΡΑΤΙΚΟΣ ΠΡΟΥΠΟΛΟΓΙΣΜΟΣ:ΕΣΟΔΑ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟΥ ΣΚΕΛΟΥΣ",titleFont);
            title2.setAlignment(Element.ALIGN_CENTER);
            title2.setSpacingAfter(20);
            document.add(title2);

            PdfPTable table2 = new PdfPTable(3);
            table2.setWidthPercentage(100);
            table2.setHeaderRows(1);

            table2.addCell(PdfFormat.createCenteredCell("Κωδικός Ταξινόμησης", tableFont, true));
            table2.addCell(PdfFormat.createCenteredCell("Ονομασία", tableFont, true));
            table2.addCell(PdfFormat.createCenteredCell("Ποσό", tableFont, true));

            evenRow = true;

            for (PublicInvestmentBudgetRevenue r : brcf) {
                BaseColor rowColor = evenRow ? BaseColor.WHITE : zebraGray;
                table2.addCell(PdfFormat.createZebraCell(String.valueOf(r.getCode()), tableFont, rowColor));
                table2.addCell(PdfFormat.createZebraCell(r.getDescription(), tableFont, rowColor));
                table2.addCell(PdfFormat.createZebraCell(String.format("%,d", r.getAmount()), tableFont, rowColor));
                evenRow = !evenRow;
            }

            document.add(table2);
            document.newPage();

            Paragraph title3 = new Paragraph("ΚΡΑΤΙΚΟΣ ΠΡΟΥΠΟΛΟΓΙΣΜΟΣ:ΕΣΟΔΑ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ ΕΘΝΙΚΟΥ ΣΚΕΛΟΥΣ",titleFont);
            title3.setAlignment(Element.ALIGN_CENTER);
            title3.setSpacingAfter(20);
            document.add(title3);

            PdfPTable table3 = new PdfPTable(3);
            table3.setWidthPercentage(100);
            table3.setHeaderRows(1);

            table3.addCell(PdfFormat.createCenteredCell("Κωδικός Ταξινόμησης", tableFont, true));
            table3.addCell(PdfFormat.createCenteredCell("Ονομασία", tableFont, true));
            table3.addCell(PdfFormat.createCenteredCell("Ποσό", tableFont, true));
            evenRow = true;

            for (PublicInvestmentBudgetRevenue r : brn) {
                BaseColor rowColor = evenRow ? BaseColor.WHITE : zebraGray;
                table3.addCell(PdfFormat.createZebraCell(String.valueOf(r.getCode()), tableFont, rowColor));
                table3.addCell(PdfFormat.createZebraCell(r.getDescription(), tableFont, rowColor));
                table3.addCell(PdfFormat.createZebraCell(String.format("%,d", r.getAmount()), tableFont, rowColor));
                evenRow = !evenRow;
            }

            document.add(table3);
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
