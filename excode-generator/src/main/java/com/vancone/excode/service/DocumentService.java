package com.vancone.excode.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.vancone.excode.entity.DataStoreOld;
import com.vancone.excode.entity.ProjectOld;
import com.vancone.excode.util.DataTypeConverter;
import com.vancone.excode.util.PdfUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileOutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.vancone.excode.util.PdfUtil.tableCell;
import static com.vancone.excode.util.PdfUtil.tableHeaderCell;

/**
 * @author Tenton Lien
 * @since 2021/03/13
 */
@Slf4j
@Service
public class DocumentService {

    private BaseFont simSunBaseFont;
    private BaseFont simHeiBaseFont;

    private Font normalChineseFont;
    private Font emphasisChineseFont;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ProjectServiceOld projectServiceOld;

    @Autowired
    private TerminologyService terminologyService;

    @PostConstruct
    public void init() {
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
    }

    public void generatePdf(String projectId, String language) {
        log.info("Start generating PDF document...");
        ProjectOld project = projectServiceOld.query(projectId);
        try {
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);

            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream("D:\\api_document.pdf"));
            pdfWriter.setViewerPreferences(PdfWriter.PageLayoutOneColumn);
            pdfWriter.setPageEvent(new PdfPageEvent());
            document.open();

            PdfPTable table = PdfUtil.table(4);
            table.addCell(tableHeaderCell("VanCone Information Technology Co., Ltd."));
            table.addCell(tableHeaderCell(" "));
            table.addCell(tableHeaderCell(" "));
            table.addCell(tableHeaderCell(" "));
            document.add(table);

            document.add(PdfUtil.padding(180));
            document.add(PdfUtil.title("Mellme Product Architecture Specification"));

            // Version
            Paragraph paragraph = new Paragraph(40);
            paragraph.setAlignment(1);
            Font font = new Font(simHeiBaseFont, Font.BOLD);
            font.setSize(14);
            paragraph.setFont(font);
            Chunk chunk = new Chunk("v" + project.getVersion());
            paragraph.add(chunk);
            document.add(paragraph);

            // Copyright
            paragraph = new Paragraph();
            paragraph.setSpacingBefore(100);
            paragraph.setAlignment(1);
            Image image = Image.getInstance("https://vancone.com/logo.png");
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

            Query query = Query.query(Criteria.where("projectId").is(projectId));
            List<DataStoreOld> dataStoreOlds = mongoTemplate.find(query, DataStoreOld.class);
            int number = 1;
            for (DataStoreOld dataStoreOld : dataStoreOlds) {
                // Title
                paragraph = new Paragraph();
                paragraph.setSpacingBefore(40);
                paragraph.setAlignment(Element.ALIGN_LEFT);
                font = new Font(simHeiBaseFont, Font.BOLD);
                font.setSize(16);
                paragraph.setFont(font);
                chunk = new Chunk((number + 1) + ". " + dataStoreOld.getDescription());
                paragraph.add(chunk);
                document.add(paragraph);

                Map<String, String> keywords = new LinkedHashMap<>();
                keywords.put("GET", "获取");
                keywords.put("POST", "创建");
                keywords.put("PUT", "更新");
                keywords.put("DELETE", "删除");

                for (int k = 0; k < keywords.size(); k++) {
                    // API title
                    paragraph = new Paragraph();
                    paragraph.setSpacingBefore(20);
                    paragraph.setAlignment(Element.ALIGN_LEFT);
                    font = new Font(simHeiBaseFont, Font.BOLD);
                    font.setSize(14);
                    paragraph.setFont(font);
                    chunk = new Chunk((number + 1) + "." + (k + 1) + " " + "<>" + dataStoreOld.getDescription() + "接口");
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
                    chunk = new Chunk((number + 1) + "." + (k + 1) + ".1 " + terminologyService.get("description", language));
                    paragraph.add(chunk);
                    document.add(paragraph);

                    // Description content
                    paragraph = new Paragraph(18);
                    paragraph.setAlignment(Element.ALIGN_LEFT);
                    paragraph.setFont(normalChineseFont);
                    chunk = new Chunk("    此接口提供" + "keywords.get(k).getValue()" + dataStoreOld.getDescription() + "的功能。\n" +
                            "    " + terminologyService.get("request_url", language) + "： http://<server_name>/api/" + dataStoreOld.getName() + "\n" +
                            "    " + terminologyService.get("request_method", language) + "： " + "keywords.get(k).getRequestMethod()");
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
                    chunk = new Chunk((number + 1) + "." + (k + 1) + ".2 " + terminologyService.get("param", language));
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
                    pdfPTable.addCell(tableHeaderCell(terminologyService.get("param_name", language)));
                    pdfPTable.addCell(tableHeaderCell(terminologyService.get("param_type", language)));
                    pdfPTable.addCell(tableHeaderCell(terminologyService.get("param_description", language)));

                    for (DataStoreOld.Node node : dataStoreOld.getNodes()) {
                        if (node.isPrimaryKey()) {
                            continue;
                        }
                        pdfPTable.addCell("");
                        pdfPTable.addCell(tableCell(node.getName()));

                        pdfPTable.addCell(tableCell(DataTypeConverter.convertToJavaDataType(node.getType())));

                        if (StringUtils.isNotBlank(node.getComment())) {
                            pdfPTable.addCell(tableCell(node.getComment()));
                        } else {
                            String[] words = node.getName().split("_");
                            String generatedDescription = "";
                            for (String word : words) {
                                String result = "LangUtil.get(languageType, word)";
                                generatedDescription += result + "LangUtil.separator(languageType)";
                            }
                            pdfPTable.addCell(generatedDescription);
                        }
                    }

                    document.add(pdfPTable);
                }
                number ++;
            }

            document.close();
            pdfWriter.close();
            log.info("Generate PDF Complete.");
        } catch (Exception e) {
            log.error("Generate PDF failed: {}", e.toString());
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
