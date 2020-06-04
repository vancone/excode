package com.mekcone.excrud.codegen.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mekcone.excrud.codegen.constant.ApplicationParameter;
import com.mekcone.excrud.codegen.constant.ModuleType;
import com.mekcone.excrud.codegen.controller.generator.BaseGenerator;
import com.mekcone.excrud.codegen.controller.generator.EnterpriseOfficialWebsiteGenerator;
import com.mekcone.excrud.codegen.controller.generator.SqlGenerator;
import com.mekcone.excrud.codegen.controller.generator.apidocument.ApiDocumentGenerator;
import com.mekcone.excrud.codegen.controller.generator.springboot.SpringBootGenerator;
import com.mekcone.excrud.codegen.controller.generator.vue.elementadmin.VueElementAdminGenerator;
import com.mekcone.excrud.codegen.enums.ErrorEnum;
import com.mekcone.excrud.codegen.model.module.Module;
import com.mekcone.excrud.codegen.model.module.impl.relationaldatabase.component.Column;
import com.mekcone.excrud.codegen.model.module.impl.relationaldatabase.component.Database;
import com.mekcone.excrud.codegen.model.module.impl.relationaldatabase.component.Table;
import com.mekcone.excrud.codegen.model.project.Project;
import com.mekcone.excrud.codegen.util.FileUtil;
import com.mekcone.excrud.codegen.util.LogUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

@Slf4j
public class ProjectLoader {
    @Getter
    private Project project;

    private int tableAmount = 0;

    private Path projectPath;

    public void build() {
        for (Module module: project.getModuleSet().asList()) {
            switch (module.type()) {
                case ModuleType.SPRING_BOOT:
                    SpringBootGenerator springBootBackendGenerator = new SpringBootGenerator(project);
                    springBootBackendGenerator.build();
                    break;
                default:
                    return;
            }
        }
    }

    public void generate() {
        if (project.getModuleSet().asList().isEmpty()) {
            log.info("No module options found");
            System.exit(0);
        }

        /*if (tableAmount > 0) {
            SqlGenerator sqlGenerator = new SqlGenerator(project);
            sqlGenerator.generate();
        }*/

        for (Module module : project.getModuleSet().asList()) {
            if (!module.isUse()) {
                continue;
            }

            // Print module type
            StringBuilder output = new StringBuilder("[ " + module.type() + " ]");
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
            switch(module.type()) {
                case ModuleType.API_DOCUMENT:
                    generator = new ApiDocumentGenerator(project);
                    break;
                case ModuleType.ENTERPRISE_OFFICIAL_WEBSITE:
                    generator = new EnterpriseOfficialWebsiteGenerator();
                    break;
                case ModuleType.SPRING_BOOT:
                    generator = new SpringBootGenerator(project);
                    break;
                case ModuleType.VUE_ELEMENT_ADMIN:
                    generator = new VueElementAdminGenerator(project);
                    break;
                default: generator = null;
            };
            if (generator != null) {
                generator.generate();
            } else {
                log.warn("Unsupported module type \"{}\"", module.type());
            }
        }
    }

    public boolean load(String content) {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            project = xmlMapper.readValue(content, Project.class);
            log.info("Load project {}:{} completed", project.getGroupId(), project.getArtifactId());
        } catch (Exception e) {
            LogUtil.fatalError(ErrorEnum.PARSE_XML_FAILED, e.getMessage());
        }

        List<Database> databases = project.getModuleSet().getRelationalDatabaseModule().getDatabases();
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

        for (Table table : project.getModuleSet().getRelationalDatabaseModule().getDatabases().get(0).getTables()) {
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
