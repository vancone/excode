package com.mekcone.excrud.loader;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mekcone.excrud.constant.ExportType;
import com.mekcone.excrud.enums.ErrorEnum;
import com.mekcone.excrud.generator.*;
import com.mekcone.excrud.generator.impl.ApiDocumentGenerator;
import com.mekcone.excrud.generator.impl.EnterpriseOfficialWebsiteGenerator;
import com.mekcone.excrud.generator.impl.SpringBootBackendGenerator;
import com.mekcone.excrud.generator.impl.SqlGenerator;
import com.mekcone.excrud.model.database.Database;
import com.mekcone.excrud.model.project.Export;
import com.mekcone.excrud.model.project.Project;
import com.mekcone.excrud.util.FileUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;

@Slf4j
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
            log.info("No export options found");
            System.exit(0);
        }

        if (tableAmount > 0) {
            SqlGenerator sqlGenerator = new SqlGenerator(project);
        }

//        ApiDocumentGenerator apiDocumentGenerator = new ApiDocumentGenerator(project);
//        apiDocumentGenerator.generatePdf();
//        System.exit(-1);

        for (var export : project.getExports()) {
            if (!export.isEnable()) {
                continue;
            }

            // Print export type
            String output = "[ " + export.getType() + " ]";
            int lineSignAmount = 72 - output.length();
            for (var i = 0; i < lineSignAmount/2; i ++) {
                output = "-" + output;
            }
            int rightLineSignAmount = lineSignAmount / 2;
            if (lineSignAmount % 2 == 0) {
                rightLineSignAmount ++;
            }
            for (int i = 0; i < rightLineSignAmount; i ++) {
                output += "-";
            }
            log.info(output);

            // Initialize generators
            Generator generator = switch(export.getType()) {
                case ExportType.API_DOCUMENT -> new ApiDocumentGenerator(project);
                case ExportType.ENTERPRISE_OFFICIAL_WEBSITE -> new EnterpriseOfficialWebsiteGenerator();
                case ExportType.SPRING_BOOT_BACKEND -> new SqlGenerator(project);
                default -> null;
            };
            if (generator != null) {
                generator.generate();
            } else {
                log.warn("Unsupported export type \"{}\"", export.getType());
            }
        }
    }

    public boolean load(String path) {
        var projectFile = new File(projectFileName);
        if ((!projectFile.exists())) {
            log.error(ErrorEnum.PROJECT_FILE_NOT_FOUND.toString());
        }

        try {
            var xmlMapper = new XmlMapper();
            xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            project = xmlMapper.readValue(FileUtil.read(path), Project.class);
            log.info("Load project {}:{} completed", project.getGroupId(), project.getArtifactId());
        } catch (Exception e) {
            //LogUtil.error(ErrorEnums.PARSE_XML_FAILED);
            log.error("Parse XML failed ({})", e.getMessage());
        }

        List<Database> databases = project.getDatabases();
        if (databases == null || databases.isEmpty()) {
            log.error(ErrorEnum.DATABASE_UNDEFINED.toString());
        }

        for (var database : databases) {
            if (database == null) {
                continue;
            }
            for (var table : database.getTables()) {
                if (table != null) {
                    tableAmount++;
                }
            }
        }
        if (tableAmount == 0) {
            log.error(ErrorEnum.TABLE_UNDEFINED.toString());
        } else {
            log.info("{} database(s), {} table(s) detected", databases.size(), tableAmount);
        }

        for (var table : project.getDatabases().get(0).getTables()) {
            for (var column : table.getColumns()) {
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
