package com.financial.pdf;

import com.financial.entries.BudgetRevenue;
import com.financial.entries.PublicInvestmentBudgetCoFundedRevenue;
import com.financial.entries.PublicInvestmentBudgetNationalRevenue;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class BudgetRevenueConvertToPdf {
    
 
    public static void createPdf(String filename) {
        try {
            ArrayList<BudgetRevenue> br = BudgetRevenue.getAllBudgetRevenues();
            ArrayList<BudgetRevenue> brm = BudgetRevenue.getMainBudgetRevenues();
            ArrayList<PublicInvestmentBudgetCoFundedRevenue> brcf = PublicInvestmentBudgetCoFundedRevenue.getAllPublicInvestmentBudgetCoFundedRevenues();
            ArrayList<PublicInvestmentBudgetNationalRevenue> brn = PublicInvestmentBudgetNationalRevenue.getAllPublicInvestmentBudgetNationalRevenues();


            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();

           
            //BaseFont bf = BaseFont.createFont("C:/Windows/Fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            BaseFont bf = BaseFont.createFont("/usr/share/fonts/truetype/freefont/FreeSans.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(bf, 12);

            Paragraph title0 = new Paragraph("ΚΡΑΤΙΚΟΣ ΠΡΟΥΠΟΛΟΓΙΣΜΟΣ:ΚΥΡΙΑ ΕΣΟΔΑ", font); 
            title0.setAlignment(Element.ALIGN_CENTER); 
            title0.setSpacingAfter(20); 
            document.add(title0);

            PdfPTable table0 = new PdfPTable(3);
            table0.addCell(PdfFormat.createCenteredCell("Κωδικός Ταξινόμησης", font));
            table0.addCell(PdfFormat.createCenteredCell("Ονομασία", font));
            table0.addCell(PdfFormat.createCenteredCell("Ποσό", font)); 

            for (BudgetRevenue budgetrevenuemain : brm) {
                table0.addCell(PdfFormat.createCenteredCell(String.valueOf(budgetrevenuemain.getCode()), font));
                table0.addCell(PdfFormat.createCenteredCell(budgetrevenuemain.getDescription(), font));
                table0.addCell(PdfFormat.createCenteredCell(String.format("%,d", budgetrevenuemain.getAmount()), font));

            }

            document.add(table0);

            Paragraph title1 = new Paragraph("ΚΡΑΤΙΚΟΣ ΠΡΟΥΠΟΛΟΓΙΣΜΟΣ:ΕΠΙΜΕΡΟΥΣ ΕΣΟΔΑ", font); 
            title1.setAlignment(Element.ALIGN_CENTER); 
            title1.setSpacingAfter(20); 
            document.add(title1);

            PdfPTable table1 = new PdfPTable(3);
            table1.addCell(PdfFormat.createCenteredCell("Κωδικός Ταξινόμησης", font));
            table1.addCell(PdfFormat.createCenteredCell("Ονομασία", font));
            table1.addCell(PdfFormat.createCenteredCell("Ποσό", font)); 

            for (BudgetRevenue budgetrevenue : br) {
                table1.addCell(PdfFormat.createCenteredCell(String.valueOf(budgetrevenue.getCode()), font)); 
                table1.addCell(PdfFormat.createCenteredCell(budgetrevenue.getDescription(), font)); 
                table1.addCell(PdfFormat.createCenteredCell(String.format("%,d", budgetrevenue.getAmount()), font));
                
            }

            document.add(table1);

            Paragraph title2 = new Paragraph("ΚΡΑΤΙΚΟΣ ΠΡΟΥΠΟΛΟΓΙΣΜΟΣ:ΕΣΟΔΑ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟΥ ΣΚΕΛΟΥΣ", font); 
            title2.setAlignment(Element.ALIGN_CENTER); 
            title2.setSpacingAfter(20); 
            document.add(title2);

            PdfPTable table2 = new PdfPTable(3);
            table2.addCell(PdfFormat.createCenteredCell("Κωδικός Ταξινόμησης", font));
            table2.addCell(PdfFormat.createCenteredCell("Ονομασία", font));
            table2.addCell(PdfFormat.createCenteredCell("Ποσό", font)); 

            for (PublicInvestmentBudgetCoFundedRevenue budgetrevenuecof : brcf) {
                table2.addCell(PdfFormat.createCenteredCell(String.valueOf(budgetrevenuecof.getCode()), font));
                table2.addCell(PdfFormat.createCenteredCell(budgetrevenuecof.getDescription(), font));
                table2.addCell(PdfFormat.createCenteredCell(String.format("%,d", budgetrevenuecof.getAmount()), font));

            }
            document.add(table2);

            Paragraph title3 = new Paragraph("ΚΡΑΤΙΚΟΣ ΠΡΟΥΠΟΛΟΓΙΣΜΟΣ:ΕΣΟΔΑ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ ΕΘΝΙΚΟΥ ΣΚΕΛΟΥΣ", font); 
            title3.setAlignment(Element.ALIGN_CENTER); 
            title3.setSpacingAfter(20); 
            document.add(title3);

            PdfPTable table3 = new PdfPTable(3);
            table3.addCell(PdfFormat.createCenteredCell("Κωδικός Ταξινόμησης", font));
            table3.addCell(PdfFormat.createCenteredCell("Ονομασία", font));
            table3.addCell(PdfFormat.createCenteredCell("Ποσό", font)); 

            for (PublicInvestmentBudgetNationalRevenue budgetrevenuenat : brn) {
                table3.addCell(PdfFormat.createCenteredCell(String.valueOf(budgetrevenuenat.getCode()), font));
                table3.addCell(PdfFormat.createCenteredCell(budgetrevenuenat.getDescription(), font));
                table3.addCell(PdfFormat.createCenteredCell(String.format("%,d", budgetrevenuenat.getAmount()), font));

            }
            document.add(table3);


            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}