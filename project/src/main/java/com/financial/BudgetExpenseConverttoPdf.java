//package org.example;

import com.financial.RegularBudgetExpense;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;

public class BudgetExpenseConverttoPdf {

    public static void createEPdf(String filename) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();

            BaseFont bf = BaseFont.createFont("C:/Windows/Fonts/arial.ttf",BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
            Font titleFont = new Font(bf, 14, Font.BOLD);
            Font tableFont = new Font(bf, 12, Font.NORMAL);
            BaseColor zebraGray = new BaseColor(245, 245, 245);

            for (Entity entity : Entity.entities) {
                Paragraph title = new Paragraph(entity.getEntityName(), titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                title.setSpacingAfter(20);
                document.add(title);
                PdfPTable table = new PdfPTable(3);
                table.setWidthPercentage(100);
                table.setHeaderRows(1);
                table.addCell(PdfFormat.createCenteredCell("Κωδικός Ταξινόμησης", tableFont, true));
                table.addCell(PdfFormat.createCenteredCell("Ονομασία", tableFont, true));
                table.addCell(PdfFormat.createCenteredCell("Ποσό", tableFont, true));
                boolean evenRow = true;
                for (String serviceCode : entity.getRegularServiceCodes()) {
                    BaseColor rowColor = evenRow ? BaseColor.WHITE : zebraGray;
                    table.addCell(PdfFormat.createZebraCell(serviceCode, tableFont, rowColor));
                    table.addCell(PdfFormat.createZebraCell(entity.findRegularServiceNameWithCode(serviceCode),tableFont, rowColor));
                    table.addCell(PdfFormat.createZebraCell(String.format("%,d",entity.getRegularSumOfServiceWithCode(serviceCode)),tableFont, rowColor));
                    evenRow = !evenRow;
                    for (RegularBudgetExpense expense :entity.getRegularExpensesOfServiceWithCode(serviceCode)) {
                        rowColor = evenRow ? BaseColor.WHITE : zebraGray;
                        table.addCell(PdfFormat.createZebraCell(String.valueOf(expense.getCode()),tableFont, rowColor));
                        table.addCell(PdfFormat.createZebraCell(expense.getDescription(),tableFont, rowColor));
                        table.addCell(PdfFormat.createZebraCell(String.format("%,d", expense.getAmount()),tableFont, rowColor));
                         evenRow = !evenRow;
                    }
                }

                document.add(table);
                if (!entity.equals(Entity.entities.get(Entity.entities.size() - 1))) {
                    document.newPage();
                }
            }

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

