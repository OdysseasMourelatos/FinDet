package com.financial.pdf;    //package org.example;


import com.financial.entries.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.util.Map;

public class BudgetExpenseConvertToPdf {

    public static void createPdf(String filename) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();

            //BaseFont bf = BaseFont.createFont("C:/Windows/Fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            BaseFont bf = BaseFont.createFont("/usr/share/fonts/truetype/freefont/FreeSans.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(bf, 12);

            for (Entity entity : Entity.getEntities()) {
                Paragraph title = new Paragraph(entity.getEntityName(), font);
                title.setAlignment(Element.ALIGN_CENTER);
                title.setSpacingAfter(30);
                document.add(title);
                PdfPTable table = new PdfPTable(3);
                table.addCell(PdfFormat.createCenteredCell("Μείζονα Κατηγορία", font));
                table.addCell(PdfFormat.createCenteredCell("Ονομασία", font));
                table.addCell(PdfFormat.createCenteredCell("Ποσό", font));
                for (Map.Entry<String, Long> entry : entity.getTotalSumOfEveryExpenseCategory().entrySet()) {
                    table.addCell(PdfFormat.createCenteredCell(entry.getKey(), font));
                    table.addCell(PdfFormat.createCenteredCell(RegularBudgetExpense.getDescriptionWithCode(entry.getKey()), font));
                    table.addCell(PdfFormat.createCenteredCell(String.format("%,d", entry.getValue()), font));
                }

                document.add(table);
                System.out.println();
                title = new Paragraph("ΠΙΣΤΩΣΕΙΣ ΚΑΤΑ ΕΙΔΙΚΟ ΦΟΡΕΑ ΚΑΙ ΜΕΙΖΟΝΑ ΚΑΤΗΓΟΡΙΑ ΔΑΠΑΝΗΣ", font);
                title.setAlignment(Element.ALIGN_CENTER);
                title.setSpacingAfter(20);
                document.add(title);

                table = new PdfPTable(3);
                table.addCell(PdfFormat.createCenteredCell("Κωδικός Ταξινόμησης", font));
                table.addCell(PdfFormat.createCenteredCell("Ονομασία", font));
                table.addCell(PdfFormat.createCenteredCell("Ποσό", font));
                for (String serviceCode : entity.getAllRegularServiceCodes()) {
                    table.addCell(PdfFormat.createCenteredCell(serviceCode, font));
                    table.addCell(PdfFormat.createCenteredCell(entity.getRegularServiceNameWithCode(serviceCode), font));
                    table.addCell(PdfFormat.createCenteredCell(String.format("%,d", entity.getRegularSumOfServiceWithCode(serviceCode)), font));
                    for (BudgetExpense expense : entity.getRegularExpensesOfServiceWithCode(serviceCode)) {
                        table.addCell(PdfFormat.createCenteredCell(String.valueOf(expense.getCode()), font));
                        table.addCell(PdfFormat.createCenteredCell(expense.getDescription(), font));
                        table.addCell(PdfFormat.createCenteredCell(String.format("%,d", expense.getAmount()), font));
                    }
                }
                document.add(table);
                System.out.println();
            }
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
