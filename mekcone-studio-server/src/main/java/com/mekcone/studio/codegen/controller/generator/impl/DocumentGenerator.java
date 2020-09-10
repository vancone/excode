package com.mekcone.studio.codegen.controller.generator.impl;

import cn.hutool.core.date.DateUtil;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.mekcone.studio.codegen.constant.ModuleConstant;
import com.mekcone.studio.codegen.constant.UrlPath;
import com.mekcone.studio.codegen.controller.extmgr.document.MarkdownExtensionManager;
import com.mekcone.studio.codegen.controller.extmgr.document.PdfExtensionManager;
import com.mekcone.studio.codegen.controller.generator.Generator;
import com.mekcone.studio.codegen.model.database.Column;
import com.mekcone.studio.codegen.model.database.Database;
import com.mekcone.studio.codegen.model.database.Table;
import com.mekcone.studio.codegen.model.module.impl.DocumentModule;
import com.mekcone.studio.codegen.model.project.Project;
import com.mekcone.studio.codegen.util.DataTypeConverter;
import com.mekcone.studio.codegen.util.LangUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.FileOutputStream;
import java.util.List;

@Slf4j
public class DocumentGenerator extends Generator {

    private BaseFont simSunBaseFont;
    private BaseFont simHeiBaseFont;

    private Font normalChineseFont;
    private Font emphasisChineseFont;

    public DocumentGenerator(Project project) {
        super(project);

        module.asDocumentModule().getExports().forEach(export -> {
            switch (export) {
                case ModuleConstant.DOCUMENT_EXPORT_FORMAT_MARKDOWN:
                    new MarkdownExtensionManager(project);
                    break;
                case ModuleConstant.DOCUMENT_EXPORT_FORMAT_PDF:
                    new PdfExtensionManager(project);
                    break;
                default:
                    log.error("Unrecognized extension: {}", export);
            }
        });

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
        for (String languageType : project.getLanguages()) {
            generatePdf(languageType);
        }

        log.info("Complete.");
    }

    public PdfPCell tableCell(String str) {
        PdfPCell pdfPCell = new PdfPCell();
        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        Font font = new Font(simSunBaseFont);
        font.setSize(12);
        pdfPCell.setPhrase(new Phrase(str, font));
        return pdfPCell;
    }

    public PdfPCell tableHeaderCell(String str) {
        PdfPCell pdfPCell = tableCell(str);
        pdfPCell.setBackgroundColor(new BaseColor(0xffcccccc));
        pdfPCell.getPhrase().getFont().setStyle(Font.BOLD);
        return pdfPCell;
    }

    @Override
    public void generate() {

    }

    public void generatePdf(String languageType) {
        try {
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);

            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outputPath + project.getName().getValue(languageType) + LangUtil.separator(languageType) + LangUtil.get(languageType, "api_document") + ".pdf"));
            pdfWriter.setViewerPreferences(PdfWriter.PageLayoutOneColumn);
            pdfWriter.setPageEvent(new PdfPageEvent());
            document.open();

            // Title
            Paragraph paragraph = new Paragraph(180);
            Chunk chunk = new Chunk(" ");
            paragraph.add(chunk);
            document.add(paragraph);
            paragraph = new Paragraph(30);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            Font font = new Font(simHeiBaseFont, 24, Font.BOLD);
            paragraph.setFont(font);
            String title = project.getName().getValue(languageType) + "\n" + LangUtil.get(languageType, "api_document");
            chunk = new Chunk(title);
            paragraph.add(chunk);
            document.add(paragraph);

            // Version
            paragraph = new Paragraph(40);
            paragraph.setAlignment(1);
            font = new Font(simHeiBaseFont, Font.BOLD);
            font.setSize(14);
            paragraph.setFont(font);
            chunk = new Chunk("v" + project.getVersion());
            paragraph.add(chunk);
            document.add(paragraph);

            // Copyright
            paragraph = new Paragraph();
            paragraph.setSpacingBefore(100);
            paragraph.setAlignment(1);
            Image image = Image.getInstance(UrlPath.MEKCONE_STUDIO_HOME + "/resources/logo/logo2.png");
            image.setAlignment(1);
            image.scaleAbsolute(40, 40);
            paragraph.add(image);
            font = new Font(Font.FontFamily.TIMES_ROMAN);
            font.setSize(12);
            paragraph.setFont(font);
            chunk = new Chunk("Generated by MekCone ExCRUD with iText PDF API\n" + DateUtil.today());
            paragraph.add(chunk);
            document.add(paragraph);

            document.newPage();

            Database database = project.getModuleSet().getDatasourceModule().getRelationalDatabase().getDatabases().get(0);
            for (int i = 0; i < database.getTables().size(); i++) {
                Table table = database.getTables().get(i);
                // Title
                paragraph = new Paragraph();
                paragraph.setSpacingBefore(40);
                paragraph.setAlignment(Element.ALIGN_LEFT);
                font = new Font(simHeiBaseFont, Font.BOLD);
                font.setSize(16);
                paragraph.setFont(font);
                chunk = new Chunk((i + 1) + ". " + table.getDescription());
                paragraph.add(chunk);
                document.add(paragraph);

                List<DocumentModule.Keyword> keywords = project.getModuleSet().getDocumentModule().getKeywords();
                for (int k = 0; k < keywords.size(); k++) {
                    // API title
                    paragraph = new Paragraph();
                    paragraph.setSpacingBefore(20);
                    paragraph.setAlignment(Element.ALIGN_LEFT);
                    font = new Font(simHeiBaseFont, Font.BOLD);
                    font.setSize(14);
                    paragraph.setFont(font);
                    chunk = new Chunk((i + 1) + "." + (k + 1) + " " + keywords.get(k).getValue() + table.getDescription() + "接口");
                    paragraph.add(chunk);
                    document.add(paragraph);

                    // Description title
                    paragraph = new Paragraph();
                    paragraph.setSpacingBefore(10);
                    paragraph.setSpacingAfter(10);
                    paragraph.setAlignment(Element.ALIGN_LEFT);
                    font = new Font(simHeiBaseFont, Font.BOLD);
                    font.setSize(12);
                    paragraph.setFont(font);
                    chunk = new Chunk((i + 1) + "." + (k + 1) + ".1 " + LangUtil.get(languageType, "description"));
                    paragraph.add(chunk);
                    document.add(paragraph);

                    // Description content
                    paragraph = new Paragraph(18);
                    paragraph.setAlignment(Element.ALIGN_LEFT);
                    paragraph.setFont(normalChineseFont);
                    chunk = new Chunk("    此接口提供" + keywords.get(k).getValue() + table.getDescription() + "的功能。\n" +
                            "    " + LangUtil.get(languageType, "request_url") + "： http://<server_name>/api/" + table.getCamelCaseName() + "\n" +
                            "    " + LangUtil.get(languageType, "request_method") + "： " + keywords.get(k).getRequestMethod());
                    paragraph.add(chunk);
                    document.add(paragraph);

                    // Parameter title
                    paragraph = new Paragraph();
                    paragraph.setSpacingBefore(10);
                    paragraph.setSpacingAfter(10);
                    paragraph.setAlignment(Element.ALIGN_LEFT);
                    font = new Font(simHeiBaseFont, Font.BOLD);
                    font.setSize(12);
                    paragraph.setFont(font);
                    chunk = new Chunk((i + 1) + "." + (k + 1) + ".2 " + LangUtil.get(languageType, "param"));
                    paragraph.add(chunk);
                    document.add(paragraph);

                    // Parameter content
                    PdfPTable pdfPTable = new PdfPTable(4);
                    pdfPTable.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPTable.setTotalWidth(new float[]{100, 100, 80, 300});

                    PdfPCell pdfPCell = new PdfPCell();
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

                    pdfPTable.addCell(tableHeaderCell(""));
                    pdfPTable.addCell(tableHeaderCell(LangUtil.get(languageType, "param_name")));
                    pdfPTable.addCell(tableHeaderCell(LangUtil.get(languageType, "param_type")));
                    pdfPTable.addCell(tableHeaderCell(LangUtil.get(languageType, "param_description")));

                    for (Column column : table.getColumns()) {
                        if (column.isPrimaryKey()) {
                            continue;
                        }
                        pdfPTable.addCell("");
                        pdfPTable.addCell(tableCell(column.getCamelCaseName(table.getName())));

                        pdfPTable.addCell(tableCell(DataTypeConverter.convertToJavaDataType(column.getType())));

                        if (StringUtils.isNotBlank(column.getDescription())) {
                            pdfPTable.addCell(tableCell(column.getDescription()));
                        } else {
                            String[] words = column.getName().split("_");
                            String generatedDescription = "";
                            for (String word : words) {
                                String result = LangUtil.get(languageType, word);
                                if (result != null) {
                                    generatedDescription += result + LangUtil.separator(languageType);
                                } else {
                                    generatedDescription += word + " ";
                                }
                            }
                            pdfPTable.addCell(generatedDescription);
                        }
                    }

                    document.add(pdfPTable);
                }
            }

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
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte pdfContentByte = writer.getDirectContent();

            pdfContentByte.saveState();
            String text = "www.mekcone.com      " + writer.getPageNumber();

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
