package com.mekcone.excrud.codegen.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mekcone.excrud.Application;
import com.mekcone.excrud.codegen.constant.basic.ExportType;
import com.mekcone.excrud.codegen.controller.generator.BaseGenerator;
import com.mekcone.excrud.codegen.controller.generator.EnterpriseOfficialWebsiteGenerator;
import com.mekcone.excrud.codegen.controller.generator.SqlGenerator;
import com.mekcone.excrud.codegen.controller.generator.apidocuments.ApiDocumentGenerator;
import com.mekcone.excrud.codegen.controller.generator.springboot.SpringBootGenerator;
import com.mekcone.excrud.codegen.controller.generator.vue.elementadmin.VueElementAdminGenerator;
import com.mekcone.excrud.codegen.enums.ErrorEnum;
import com.mekcone.excrud.codegen.model.export.GenModel;
import com.mekcone.excrud.codegen.model.export.impl.relationaldatabase.component.Column;
import com.mekcone.excrud.codegen.model.export.impl.relationaldatabase.component.Database;
import com.mekcone.excrud.codegen.model.export.impl.relationaldatabase.component.Table;
import com.mekcone.excrud.codegen.model.project.Project;
import com.mekcone.excrud.codegen.util.FileUtil;
import com.mekcone.excrud.codegen.util.LogUtil;
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
        for (GenModel export: project.getExports().asList()) {
            switch (export.type()) {
                case ExportType.SPRING_BOOT:
                    SpringBootGenerator springBootBackendGenerator = new SpringBootGenerator(project);
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
            SqlGenerator sqlGenerator = new SqlGenerator(project);
            sqlGenerator.generate();
        }

//        ApiDocumentGenerator apiDocumentGenerator = new ApiDocumentGenerator(project);
//        apiDocumentGenerator.generatePdf();
//        System.exit(-1);

        for (GenModel export : project.getExports().asList()) {
            if (!export.isUse()) {
                continue;
            }

            // Print export type
            StringBuilder output = new StringBuilder("[ " + export.type() + " ]");
            int lineSignAmount = 72 - output.length();
            for (int i = 0; i < lineSignAmount/2; i ++) {
                output.insert(0, "-");
            }
            int rightLineSignAmount = lineSignAmount / 2;
            if (lineSignAmount % 2 == 0) {
                rightLineSignAmount ++;
            }
            for (int i = 0; i < rightLineSignAmount; i ++) {
                output.append("-");
            }
            log.info(output.toString());

            // Initialize generators
            BaseGenerator generator;
            switch(export.type()) {
                case ExportType.API_DOCUMENT: generator = new ApiDocumentGenerator(project);
                case ExportType.ENTERPRISE_OFFICIAL_WEBSITE: generator = new EnterpriseOfficialWebsiteGenerator();
                case ExportType.SPRING_BOOT: generator = new SpringBootGenerator(project);
                case ExportType.VUE_ELEMENT_ADMIN: generator = new VueElementAdminGenerator(project);
                default: generator = null;
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
        File projectFile = new File(projectFileName);
        if ((!projectFile.exists())) {
            LogUtil.fatalError(ErrorEnum.PROJECT_FILE_NOT_FOUND, projectFile.getAbsolutePath());
        }

        try {
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            project = xmlMapper.readValue(FileUtil.read(path), Project.class);
            log.info("Load project {}:{} completed", project.getGroupId(), project.getArtifactId());
        } catch (Exception e) {
            // log.error("Parse XML failed ({})", e.getMessage());
            LogUtil.fatalError(ErrorEnum.PARSE_XML_FAILED, e.getMessage());
        }

        List<Database> databases = project.getExports().getRelationalDatabaseExport().getDatabases();
        if (databases == null || databases.isEmpty()) {
            LogUtil.fatalError(ErrorEnum.DATABASE_UNDEFINED);
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
            System.exit(-1);
        } else {
            log.info("{} database(s), {} table(s) detected", databases.size(), tableAmount);
        }

        for (Table table : project.getExports().getRelationalDatabaseExport().getDatabases().get(0).getTables()) {
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
