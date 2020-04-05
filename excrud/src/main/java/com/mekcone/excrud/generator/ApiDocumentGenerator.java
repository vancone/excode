package com.mekcone.excrud.generator;

import cn.hutool.core.date.DateUtil;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.mekcone.excrud.Application;
import com.mekcone.excrud.model.database.Database;
import com.mekcone.excrud.model.project.Project;
import com.mekcone.excrud.model.apidoc.Keyword;
import com.mekcone.excrud.model.database.Column;
import com.mekcone.excrud.model.database.Table;
import com.mekcone.excrud.parser.PropertiesParser;
import com.mekcone.excrud.util.DataTypeConverter;
import com.mekcone.excrud.util.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class ApiDocumentGenerator {
    private Project project;

    private BaseFont simSunBaseFont;
    private BaseFont simHeiBaseFont;

    private Font normalChineseFont;
    private Font emphasisChineseFont;

    public ApiDocumentGenerator(Project project) {
        this.project = project;
        smartDescription();

        try {
            simSunBaseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);

            String fontPath = "C:\\Windows\\Fonts\\msyh.ttc,1";
            simHeiBaseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (Exception e) {
            e.printStackTrace();
        }

        normalChineseFont = new Font(simSunBaseFont);
        normalChineseFont.setSize(12);

        emphasisChineseFont = new Font(simSunBaseFont, Font.BOLD);
        emphasisChineseFont.setSize(12);
    }

    public void smartDescription() {
        PropertiesParser propertiesParser = PropertiesParser.readFrom(Application.getHomeDirectory() + "/exports/api-documents/smart-description.properties");
        if (propertiesParser == null) {
            LogUtil.warn("Smart description dataset not found");
            return;
        }

        for (Database database: project.getDatabases()) {
            for (Table table: database.getTables()) {
                for (Column column: table.getColumns()) {
                    if (column.getDescription() == null || column.getDescription().isEmpty()) {
                        String[] words = column.getName().split("_");
                        String generatedDescription = "";
                        for (String word: words) {
                            if (propertiesParser.exist(word)) {
                                generatedDescription += propertiesParser.get(word);
                            } else {
                                generatedDescription += word + " ";
                            }
                        }
                        if (!generatedDescription.isEmpty()) {
                            column.setDescription(generatedDescription);
                        }
                    }
                }
            }
        }
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

    public void generatePdf() {
        try {
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);

            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream("test.pdf"));
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
            String title = project.getApiDocument().getTitle().replace("{br}", "\n");
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
            Image image = Image.getInstance(Application.getHomeDirectory() + "/resources/logo/logo.png");
            image.setAlignment(1);
            image.scaleAbsolute(40,40);
            paragraph.add(image);
            font = new Font(Font.FontFamily.TIMES_ROMAN);
            font.setSize(12);
            paragraph.setFont(font);
            chunk = new Chunk("Generated by MekCone ExCRUD with iText PDF API\n" + DateUtil.today());
            paragraph.add(chunk);
            document.add(paragraph);

            document.newPage();

            for (int i = 0; i < project.getDatabases().get(0).getTables().size(); i ++) {
                Table table = project.getDatabases().get(0).getTables().get(i);
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

                List<Keyword> keywords = project.getApiDocument().getKeywords();
                for (int k = 0; k < keywords.size(); k ++) {
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
                    chunk = new Chunk((i + 1) + "." + (k + 1) + ".1 描述");
                    paragraph.add(chunk);
                    document.add(paragraph);

                    // Description content
                    paragraph = new Paragraph(18);
                    paragraph.setAlignment(Element.ALIGN_LEFT);
                    paragraph.setFont(normalChineseFont);
                    chunk = new Chunk("    此接口提供" + keywords.get(k).getValue() + table.getDescription() + "的功能。\n" +
                            "    调用URL： http://<server_name>/api/" + table.getCamelName() + "\n" +
                            "    调用方法： " + keywords.get(k).getRequestMethod());
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
                    chunk = new Chunk((i + 1) + "." + (k + 1) + ".2 参数");
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
                    pdfPTable.addCell(tableHeaderCell("参数名"));
                    pdfPTable.addCell(tableHeaderCell("类型"));
                    pdfPTable.addCell(tableHeaderCell("参数描述"));

                    for (Column column: table.getColumns()) {
                        if (column.isPrimaryKey()) {
                            continue;
                        }
                        pdfPTable.addCell("");
                        pdfPTable.addCell(tableCell(column.getCamelName(table.getName())));

                        pdfPTable.addCell(tableCell(DataTypeConverter.convertToJavaDataType(column.getType())));

                        if (column.getDescription() != null && !column.getDescription().isEmpty()) {
                            pdfPTable.addCell(tableCell(column.getDescription()));
                        } else {
                            pdfPTable.addCell("-");
                        }
                    }

                    document.add(pdfPTable);
                }
            }


            document.close();
            pdfWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class PdfPageEvent extends PdfPageEventHelper {
        public PdfTemplate total;

        public BaseFont bfChinese;

        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            total = writer.getDirectContent().createTemplate(500, 500);
            try {

                String prefixFont = "";
                String os = System.getProperties().getProperty("os.name");
                if(os.startsWith("win") || os.startsWith("Win")){
                    prefixFont = "C:\\Windows\\Fonts" + File.separator;
                }else {
                    prefixFont = "/usr/share/fonts/chinese" + File.separator;
                }

                // 设置字体对象为Windows系统默认的字体
                //bfChinese = BaseFont.createFont(prefixFont + "simsun.ttc,0", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            } catch (Exception e) {
                throw new ExceptionConverter(e);
            }
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            // 新建获得用户页面文本和图片内容位置的对象
            PdfContentByte pdfContentByte = writer.getDirectContent();
            // 保存图形状态
            pdfContentByte.saveState();
            String text ="www.mekcone.com      "+ writer.getPageNumber();
            // 获取点字符串的宽度
            float textSize = bfChinese.getWidthPoint(text, 12);
            pdfContentByte.beginText();
            // 设置随后的文本内容写作的字体和字号
            pdfContentByte.setFontAndSize(bfChinese, 15);

            // 定位'X/'
            //System.out.println(document.right() +"...."+ document.left());
            // float x = (document.right() + document.left())/2;
            float x = (document.right() - 120f);
            float y = 20f;
            pdfContentByte.setTextMatrix(x, y);
            pdfContentByte.showText(text);
            pdfContentByte.endText();

            // 将模板加入到内容（content）中- // 定位'Y'
            pdfContentByte.addTemplate(total, x + textSize, y);

            pdfContentByte.restoreState();
        }
    }
}
