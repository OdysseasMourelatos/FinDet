package com.financial.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;

/**
 * Utility class for formatting PDF cells using the iText library.
 * Provides helper methods for cell alignment, zebra-pattern coloring,
 * and the creation of standardized table headers.
 */
public class PdfFormat {

    public static PdfPCell createCenteredCell(String text, Font font) {
        return createCenteredCell(text, font, false);
    }

    public static PdfPCell createCenteredCell(String text, Font font, boolean header) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5);

        if (header) {
            cell.setBackgroundColor(new BaseColor(153, 204, 255));
        }

        return cell;
    }

    
    public static PdfPCell createZebraCell(String text, Font font, BaseColor bgColor) {
        PdfPCell cell = createCenteredCell(text, font);
        cell.setBackgroundColor(bgColor);
        return cell;
    }
}


