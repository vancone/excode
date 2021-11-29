package com.vancone.excode.generator.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.vancone.excode.generator.util.PdfUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;

/**
 * @author Tenton Lien
 * @date 3/13/2021
 */
@Slf4j
public class DocumentService {

    private BaseFont simSunBaseFont;
    private BaseFont simHeiBaseFont;

    private Font normalChineseFont;
    private Font emphasisChineseFont;

//    public static void main(String[] args) {
//        DocumentServiceImpl documentService = new DocumentServiceImpl();
//    }

    public DocumentService() {
        try {
            simSunBaseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            // Windows only!!
            String fontPath = "C:\\Windows\\Fonts\\msyh.ttc,1";
            simHeiBaseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        normalChineseFont = new Font(simSunBaseFont);
        normalChineseFont.setSize(12);

        emphasisChineseFont = new Font(simSunBaseFont, Font.BOLD);
        emphasisChineseFont.setSize(12);

        log.info("Start generating PDF document...");
        generatePdf();

        log.info("Complete.");
    }

    public void generatePdf() {
        try {
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);

            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream("D:\\api_document.pdf"));
            pdfWriter.setViewerPreferences(PdfWriter.PageLayoutOneColumn);
            pdfWriter.setPageEvent(new PdfPageEvent());
            document.open();

            PdfPTable table = PdfUtil.table(4);
            table.addCell(PdfUtil.tableHeaderCell("VanCone Information Technology Co., Ltd."));
            table.addCell(PdfUtil.tableHeaderCell(" "));
            table.addCell(PdfUtil.tableHeaderCell(" "));
            table.addCell(PdfUtil.tableHeaderCell(" "));
            document.add(table);

            document.add(PdfUtil.padding(180));
            document.add(PdfUtil.title("Mellme Product Architecture Specification"));

            // Version
            Paragraph paragraph = new Paragraph(40);
            paragraph.setAlignment(1);
            Font font = new Font(simHeiBaseFont, Font.BOLD);
            font.setSize(14);
            paragraph.setFont(font);
            Chunk chunk = new Chunk("v");
            paragraph.add(chunk);
            document.add(paragraph);

            // Copyright
            paragraph = new Paragraph();
            paragraph.setSpacingBefore(100);
            paragraph.setAlignment(1);
            Image image = Image.getInstance("http://vancone.com/image/logo.png");
            image.setAlignment(1);
            image.scaleAbsolute(80, 80);
            paragraph.add(image);
            font = new Font(Font.FontFamily.TIMES_ROMAN, Font.BOLD);
            font.setSize(14);
            paragraph.setFont(font);
            chunk = new Chunk("\nVanCone Information Technology Co., Ltd.\n" + "All Rights Reserved.");
            paragraph.add(chunk);
            document.add(paragraph);

            document.newPage();

            paragraph = new Paragraph();
            paragraph.setSpacingBefore(40);
            paragraph.setAlignment(Element.ALIGN_LEFT);
            font = new Font(simHeiBaseFont, Font.BOLD);
            font.setSize(16);
            paragraph.setFont(font);
            chunk = new Chunk("hahahaha");
            paragraph.add(chunk);
            document.add(paragraph);

//            Database database = project.getModuleSet().getDatasourceModule().getRelationalDatabase().getDatabases().get(0);
//            for (int i = 0; i < database.getTables().size(); i++) {
//                Table table = database.getTables().get(i);
//                // Title
//                paragraph = new Paragraph();
//                paragraph.setSpacingBefore(40);
//                paragraph.setAlignment(Element.ALIGN_LEFT);
//                font = new Font(simHeiBaseFont, Font.BOLD);
//                font.setSize(16);
//                paragraph.setFont(font);
//                chunk = new Chunk((i + 1) + ". " + table.getDescription());
//                paragraph.add(chunk);
//                document.add(paragraph);
//
//                List<DocumentModule.Keyword> keywords = project.getModuleSet().getDocumentModule().getKeywords();
//                for (int k = 0; k < keywords.size(); k++) {
//                    // API title
//                    paragraph = new Paragraph();
//                    paragraph.setSpacingBefore(20);
//                    paragraph.setAlignment(Element.ALIGN_LEFT);
//                    font = new Font(simHeiBaseFont, Font.BOLD);
//                    font.setSize(14);
//                    paragraph.setFont(font);
//                    chunk = new Chunk((i + 1) + "." + (k + 1) + " " + keywords.get(k).getValue() + table.getDescription() + "接口");
//                    paragraph.add(chunk);
//                    document.add(paragraph);
//
//                    // Description title
//                    paragraph = new Paragraph();
//                    paragraph.setSpacingBefore(10);
//                    paragraph.setSpacingAfter(10);
//                    paragraph.setAlignment(Element.ALIGN_LEFT);
//                    font = new Font(simHeiBaseFont, Font.BOLD);
//                    font.setSize(12);
//                    paragraph.setFont(font);
//                    chunk = new Chunk((i + 1) + "." + (k + 1) + ".1 " + LangUtil.get(languageType, "description"));
//                    paragraph.add(chunk);
//                    document.add(paragraph);
//
//                    // Description content
//                    paragraph = new Paragraph(18);
//                    paragraph.setAlignment(Element.ALIGN_LEFT);
//                    paragraph.setFont(normalChineseFont);
//                    chunk = new Chunk("    此接口提供" + keywords.get(k).getValue() + table.getDescription() + "的功能。\n" +
//                            "    " + LangUtil.get(languageType, "request_url") + "： http://<server_name>/api/" + table.getCamelCaseName() + "\n" +
//                            "    " + LangUtil.get(languageType, "request_method") + "： " + keywords.get(k).getRequestMethod());
//                    paragraph.add(chunk);
//                    document.add(paragraph);
//
//                    // Parameter title
//                    paragraph = new Paragraph();
//                    paragraph.setSpacingBefore(10);
//                    paragraph.setSpacingAfter(10);
//                    paragraph.setAlignment(Element.ALIGN_LEFT);
//                    font = new Font(simHeiBaseFont, Font.BOLD);
//                    font.setSize(12);
//                    paragraph.setFont(font);
//                    chunk = new Chunk((i + 1) + "." + (k + 1) + ".2 " + LangUtil.get(languageType, "param"));
//                    paragraph.add(chunk);
//                    document.add(paragraph);
//
//                    // Parameter content
//                    PdfPTable pdfPTable = new PdfPTable(4);
//                    pdfPTable.setHorizontalAlignment(Element.ALIGN_CENTER);
//                    pdfPTable.setTotalWidth(new float[]{100, 100, 80, 300});
//
//                    PdfPCell pdfPCell = new PdfPCell();
//                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//
//                    pdfPTable.addCell(tableHeaderCell(""));
//                    pdfPTable.addCell(tableHeaderCell(LangUtil.get(languageType, "param_name")));
//                    pdfPTable.addCell(tableHeaderCell(LangUtil.get(languageType, "param_type")));
//                    pdfPTable.addCell(tableHeaderCell(LangUtil.get(languageType, "param_description")));
//
//                    for (Column column : table.getColumns()) {
//                        if (column.isPrimaryKey()) {
//                            continue;
//                        }
//                        pdfPTable.addCell("");
//                        pdfPTable.addCell(tableCell(column.getCamelCaseName(table.getName())));
//
//                        pdfPTable.addCell(tableCell(DataTypeConverter.convertToJavaDataType(column.getType())));
//
//                        if (StringUtils.isNotBlank(column.getDescription())) {
//                            pdfPTable.addCell(tableCell(column.getDescription()));
//                        } else {
//                            String[] words = column.getName().split("_");
//                            String generatedDescription = "";
//                            for (String word : words) {
//                                String result = LangUtil.get(languageType, word);
//                                if (result != null) {
//                                    generatedDescription += result + LangUtil.separator(languageType);
//                                } else {
//                                    generatedDescription += word + " ";
//                                }
//                            }
//                            pdfPTable.addCell(generatedDescription);
//                        }
//                    }
//
//                    document.add(pdfPTable);
//                }
//            }

            document.close();
            pdfWriter.close();
        } catch (Exception e) {
            log.error("Generate PDF failed: {}", e.getMessage());
        }
    }

    private static class PdfPageEvent extends PdfPageEventHelper {
        public PdfTemplate total;

        public BaseFont bfChinese;

        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            total = writer.getDirectContent().createTemplate(500, 500);
            try {
                bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            } catch (Exception e) {
                throw new ExceptionConverter(e);
            }
        }

        @Override
        public void onStartPage(PdfWriter writer, Document document) {
            if (writer.getPageNumber() == 1) {
                return;
            }

            PdfContentByte pdfContentByte = writer.getDirectContent();
            pdfContentByte.saveState();

            String text = "Security Level: " + "Internal Use";

            float textSize = bfChinese.getWidthPoint(text, 12);
            pdfContentByte.beginText();
            pdfContentByte.setFontAndSize(bfChinese, 15);

            float x = (document.right() - 150f);
            float y = document.top();
            pdfContentByte.setTextMatrix(x, y);
            pdfContentByte.showText(text);
            pdfContentByte.endText();

            pdfContentByte.addTemplate(total, x + textSize, y);

            pdfContentByte.restoreState();
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            if (writer.getPageNumber() == 1) {
                return;
            }

            PdfContentByte pdfContentByte = writer.getDirectContent();

            pdfContentByte.saveState();
            String text = "www.vancone.com      " + (writer.getPageNumber() - 1);

            float textSize = bfChinese.getWidthPoint(text, 12);
            pdfContentByte.beginText();
            pdfContentByte.setFontAndSize(bfChinese, 15);

            float x = (document.right() - 120f);
            float y = 20f;
            pdfContentByte.setTextMatrix(x, y);
            pdfContentByte.showText(text);
            pdfContentByte.endText();

            pdfContentByte.addTemplate(total, x + textSize, y);

            pdfContentByte.restoreState();
        }
    }
}
