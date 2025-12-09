    //package org.example;

import com.financial.BudgetRevenue;
import com.financial.PublicInvestmentBudgetRevenue;
import com.financial.RegularBudgetExpense;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class BudgetExpenseConverttoPdf{

public static void createEPdf(String filename){
    try {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();

        BaseFont bf = BaseFont.createFont("C:/Windows/Fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(bf, 12);

        for (Entity entity : Entity.entities) {
            Paragraph title = new Paragraph(entity.getEntityName(), font); 
            title.setAlignment(Element.ALIGN_CENTER); 
            title.setSpacingAfter(20); 
            document.add(title);
            PdfPTable table = new PdfPTable(3);
            table.addCell(PdfFormat.createCenteredCell("Κωδικός Ταξινόμησης", font));
            table.addCell(PdfFormat.createCenteredCell("Ονομασία", font));
            table.addCell(PdfFormat.createCenteredCell("Ποσό", font)); 
            for (String serviceCode : entity.getRegularServiceCodes()) {
                table.addCell(PdfFormat.createCenteredCell(serviceCode, font));
                table.addCell(PdfFormat.createCenteredCell(entity.findRegularServiceNameWithCode(serviceCode), font));
                table.addCell(PdfFormat.createCenteredCell(String.format("%,d", entity.getRegularSumOfServiceWithCode(serviceCode)), font));
                for (RegularBudgetExpense expense : entity.getRegularExpensesOfServiceWithCode(serviceCode)) {
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