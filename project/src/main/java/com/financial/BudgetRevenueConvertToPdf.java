package com.financial;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class BudgetRevenueConvertToPdf {
    
    private static PdfPCell createCenteredCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }


    public static void createPdf(String filename) {
        try {
            ArrayList<BudgetRevenue> br = BudgetRevenue.getAllBudgetRevenues();

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();

           
            BaseFont bf = BaseFont.createFont("C:/Windows/Fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(bf, 12);

            Paragraph title = new Paragraph("ΚΡΑΤΙΚΟΣ ΠΡΟΥΠΟΛΟΓΙΣΜΟΣ:ΕΣΟΔΑ", font); 
            title.setAlignment(Element.ALIGN_CENTER); 
            title.setSpacingAfter(20); 
            document.add(title);

            PdfPTable table = new PdfPTable(3);
            table.addCell(createCenteredCell("Κωδικός Ταξινόμησης", font));
            table.addCell(createCenteredCell("Ονομασία", font));
            table.addCell(createCenteredCell("2025", font)); 

            for (BudgetRevenue budgetrevenue : br) {
                table.addCell(createCenteredCell(String.valueOf(budgetrevenue.getCode()), font));
                table.addCell(createCenteredCell(budgetrevenue.getDescription(), font));
                table.addCell(createCenteredCell(String.format("%,d", budgetrevenue.getAmount()), font));

            }

            document.add(table);
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}