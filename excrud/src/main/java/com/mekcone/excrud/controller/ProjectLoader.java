package com.mekcone.excrud.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mekcone.excrud.Application;
import com.mekcone.excrud.constant.basic.ExportType;
import com.mekcone.excrud.controller.generator.EnterpriseOfficialWebsiteGenerator;
import com.mekcone.excrud.controller.generator.SqlGenerator;
import com.mekcone.excrud.controller.generator.apidocuments.ApiDocumentGenerator;
import com.mekcone.excrud.controller.generator.springboot.SpringBootGenerator;
import com.mekcone.excrud.controller.generator.vue.elementadmin.VueElementAdminGenerator;
import com.mekcone.excrud.model.project.Project;
import com.mekcone.excrud.enums.ErrorEnum;
import com.mekcone.excrud.controller.generator.BaseGenerator;
import com.mekcone.excrud.model.project.export.impl.relationaldatabase.database.Database;
import com.mekcone.excrud.util.FileUtil;
import com.mekcone.excrud.util.LogUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;

@Slf4j
public class ProjectLoader {
    @Getter
    private Project project;

    private int tableAmount = 0;

    public void build() {
        for (var export: project.getExports().asList()) {
            switch (export.type()) {
                case ExportType.SPRING_BOOT:
                    var springBootBackendGenerator = new SpringBootGenerator(project);
                    springBootBackendGenerator.build();
                    break;
                default:
                    return;
            }
        }
    }

    public void generate() {
        if (project.getExports().asList().isEmpty()) {
            log.info("No export options found");
            System.exit(0);
        }

        if (tableAmount > 0) {
            var sqlGenerator = new SqlGenerator(project);
            sqlGenerator.generate();
        }

//        ApiDocumentGenerator apiDocumentGenerator = new ApiDocumentGenerator(project);
//        apiDocumentGenerator.generatePdf();
//        System.exit(-1);

        for (var export : project.getExports().asList()) {
            if (!export.isUse()) {
                continue;
            }

            // Print export type
            var output = new StringBuilder("[ " + export.type() + " ]");
            int lineSignAmount = 72 - output.length();
            for (var i = 0; i < lineSignAmount/2; i ++) {
                output.insert(0, "-");
            }
            int rightLineSignAmount = lineSignAmount / 2;
            if (lineSignAmount % 2 == 0) {
                rightLineSignAmount ++;
            }
            for (var i = 0; i < rightLineSignAmount; i ++) {
                output.append("-");
            }
            log.info(output.toString());

            // Initialize generators
            BaseGenerator generator = switch(export.type()) {
                case ExportType.API_DOCUMENT -> new ApiDocumentGenerator(project);
                case ExportType.ENTERPRISE_OFFICIAL_WEBSITE -> new EnterpriseOfficialWebsiteGenerator();
                case ExportType.SPRING_BOOT -> new SpringBootGenerator(project);
                case ExportType.VUE_ELEMENT_ADMIN -> new VueElementAdminGenerator(project);
                default -> null;
            };
            if (generator != null) {
                generator.generate();
            } else {
                log.warn("Unsupported export type \"{}\"", export.type());
            }
        }
    }

    public boolean load(String path) {
        String projectFileName = Application.getApplicationName().toLowerCase() + ".xml";
        var projectFile = new File(projectFileName);
        if ((!projectFile.exists())) {
            LogUtil.fatalError(ErrorEnum.PROJECT_FILE_NOT_FOUND, projectFile.getAbsolutePath());
        }

        try {
            var xmlMapper = new XmlMapper();
            xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            project = xmlMapper.readValue(FileUtil.read(path), Project.class);
            log.info("Load project {}:{} completed", project.getGroupId(), project.getArtifactId());
        } catch (Exception e) {
            // log.error("Parse XML failed ({})", e.getMessage());
            LogUtil.fatalError(ErrorEnum.PARSE_XML_FAILED, e.getMessage());
        }

        List<Database> databases = project.getExports().getDatabases();
        if (databases == null || databases.isEmpty()) {
            LogUtil.fatalError(ErrorEnum.DATABASE_UNDEFINED);
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
            LogUtil.error(ErrorEnum.TABLE_UNDEFINED);
            System.exit(-1);
        } else {
            log.info("{} database(s), {} table(s) detected", databases.size(), tableAmount);
        }

        for (var table : project.getExports().getDatabases().get(0).getTables()) {
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
