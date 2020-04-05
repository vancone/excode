package com.mekcone.excrud.loader;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mekcone.excrud.constant.ExportType;
import com.mekcone.excrud.enums.ErrorEnum;
import com.mekcone.excrud.generator.ApiDocumentGenerator;
import com.mekcone.excrud.generator.EnterpriseOfficialWebsiteGenerator;
import com.mekcone.excrud.generator.SpringBootBackendGenerator;
import com.mekcone.excrud.generator.SqlGenerator;
import com.mekcone.excrud.model.project.Project;
import com.mekcone.excrud.model.project.Export;
import com.mekcone.excrud.model.database.Column;
import com.mekcone.excrud.model.database.Database;
import com.mekcone.excrud.model.database.Table;
import com.mekcone.excrud.util.FileUtil;
import com.mekcone.excrud.util.LogUtil;
import lombok.Getter;

import java.io.File;
import java.util.List;

public class ProjectLoader {
    @Getter
    private Project project;

    private final String projectFileName = "excrud.xml";
    private int tableAmount = 0;

    public void build() {
        for (Export export: project.getExports()) {
            switch (export.getType()) {
                case ExportType.SPRING_BOOT_BACKEND:
                    SpringBootBackendGenerator springBootBackendGenerator = new SpringBootBackendGenerator(project, export);
                    springBootBackendGenerator.build();
                    break;
                default:
                    return;
            }
        }
    }

    public void generate() {

        if (project.getExports() != null && project.getExports().isEmpty()) {
            LogUtil.info("No export options found");
            System.exit(0);
        }

        if (tableAmount > 0) {
            SqlGenerator sqlGenerator = new SqlGenerator(project);
        }

        ApiDocumentGenerator apiDocumentGenerator = new ApiDocumentGenerator(project);
        apiDocumentGenerator.generatePdf();
        System.exit(-1);

        for (Export export : project.getExports()) {
            if (!export.isEnable()) {
                continue;
            }
            LogUtil.title(export.getType());
            switch (export.getType()) {
                case ExportType.API_DOCUMENT:
//                    ApiDocumentGenerator apiDocumentGenerator = new ApiDocumentGenerator(project);
//                    apiDocumentGenerator.generatePdf();
                    break;

                case ExportType.ENTERPRISE_OFFICIAL_WEBSITE:
                    EnterpriseOfficialWebsiteGenerator enterpriseOfficialWebsiteGenerator = new EnterpriseOfficialWebsiteGenerator();
                    enterpriseOfficialWebsiteGenerator.generate();
                    break;

                case ExportType.SPRING_BOOT_BACKEND:
                    SpringBootBackendGenerator springBootBackendGenerator = new SpringBootBackendGenerator(project, export);
                    springBootBackendGenerator.generate();
                    break;

                default:
                    LogUtil.warn("Unsupported export type \"" + export.getType() + "\"");
            }
        }

    }

    public boolean load(String path) {
        File projectFile = new File(projectFileName);
        if ((!projectFile.exists())) {
            LogUtil.error(ErrorEnum.PROJECT_FILE_NOT_FOUND);
        }

        try {
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            project = xmlMapper.readValue(FileUtil.read(path), Project.class);
            LogUtil.info("Load project " + project.getGroupId() + "." + project.getArtifactId() + " completed");
        } catch (Exception e) {
            //LogUtil.error(ErrorEnums.PARSE_XML_FAILED);
            LogUtil.error(-1, "Parse XML failed (" + e.getMessage() + ")");
        }

        List<Database> databases = project.getDatabases();
        if (databases == null || databases.isEmpty()) {
            LogUtil.error(ErrorEnum.DATABASE_UNDEFINED);
        }

        for (Database database : databases) {
            if (database == null) {
                continue;
            }
            for (Table table : database.getTables()) {
                if (table != null) {
                    tableAmount++;
                }
            }
        }
        if (tableAmount == 0) {
            LogUtil.error(ErrorEnum.TABLE_UNDEFINED);
        } else {
            LogUtil.info(databases.size() + " database(s), " + tableAmount + " table(s) detected");
        }

        for (Table table : project.getDatabases().get(0).getTables()) {
            for (Column column : table.getColumns()) {
                if (column.isPrimaryKey()) {
                    table.setPrimaryKey(column.getName());
                    break;
                }
            }
        }
        return true;
    }


    public boolean output() {
        return false;
    }
}
