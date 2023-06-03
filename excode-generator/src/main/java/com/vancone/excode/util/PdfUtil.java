package com.vancone.excode.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

/**
 * @author Tenton Lien
 * @since 2021/03/13
 */
public class PdfUtil {
    private static BaseFont simSunBaseFont;
    private static BaseFont simHeiBaseFont;

    private static Font normalChineseFont;
    private static Font emphasisChineseFont;

    static {
        try {
            simSunBaseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            // Windows only!!
            String fontPath = "C:\\Windows\\Fonts\\msyh.ttc,1";
            simHeiBaseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        normalChineseFont = new Font(simSunBaseFont);
        normalChineseFont.setSize(12);

        emphasisChineseFont = new Font(simSunBaseFont, Font.BOLD);
        emphasisChineseFont.setSize(12);
    }

    public static PdfPTable table(int numColumns) throws DocumentException {
        PdfPTable pdfPTable = new PdfPTable(numColumns);
        pdfPTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPTable.setTotalWidth(new float[]{100, 100, 80, 300});

                    PdfPCell pdfPCell = new PdfPCell();
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//
//                    pdfPTable.addCell(tableHeaderCell(""));
//                    pdfPTable.addCell(tableHeaderCell(LangUtil.get(languageType, "param_name")));
//                    pdfPTable.addCell(tableHeaderCell(LangUtil.get(languageType, "param_type")));
//                    pdfPTable.addCell(tableHeaderCell(LangUtil.get(languageType, "param_description")));
        return pdfPTable;
    }

    public static PdfPCell tableCell(String str) {
        PdfPCell pdfPCell = new PdfPCell();
        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        Font font = new Font(simSunBaseFont);
        font.setSize(12);
        pdfPCell.setPhrase(new Phrase(str, font));
        return pdfPCell;
    }

    public static PdfPCell tableHeaderCell(String str) {
        PdfPCell pdfPCell = tableCell(str);
        pdfPCell.setBackgroundColor(new BaseColor(0xffcccccc));
        pdfPCell.getPhrase().getFont().setStyle(Font.BOLD);
        return pdfPCell;
    }

    public static Paragraph padding(int leading) {
        Paragraph paragraph = new Paragraph(leading);
        Chunk chunk = new Chunk(" ");
        paragraph.add(chunk);
        return paragraph;
    }

    public static Paragraph title(String content) {
        Paragraph paragraph = new Paragraph(30);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        Font font = new Font(simHeiBaseFont, 24, Font.BOLD);
        paragraph.setFont(font);
        String title = "Mellme Product Architecture Specification";
        Chunk chunk = new Chunk(title);
        paragraph.add(chunk);
        return paragraph;
    }

}
