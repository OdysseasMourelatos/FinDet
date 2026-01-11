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
            cell.setBackgroundColor(new BaseColor(220, 220, 220));
        }

        return cell;
    }

    
    public static PdfPCell createZebraCell(String text, Font font, BaseColor bgColor) {
        PdfPCell cell = createCenteredCell(text, font);
        cell.setBackgroundColor(bgColor);
        return cell;
    }
}


