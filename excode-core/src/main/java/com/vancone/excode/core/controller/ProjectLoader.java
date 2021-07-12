package com.vancone.excode.core.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.vancone.excode.core.constant.ModuleConstant;
import com.vancone.excode.core.controller.generator.Generator;
import com.vancone.excode.core.controller.generator.impl.*;
import com.vancone.excode.core.enums.ErrorEnum;
import com.vancone.excode.core.model.database.Column;
import com.vancone.excode.core.model.database.Database;
import com.vancone.excode.core.model.database.Table;
import com.vancone.excode.core.model.module.Module;
import com.vancone.excode.core.model.project.Project;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @author Tenton Lien
 */
@Slf4j
public class ProjectLoader {
    @Getter
    private Project project;

    private int tableAmount = 0;

    public void generate() {
        if (project.getModuleSet().asList().isEmpty()) {
            log.info("No module options found");
            System.exit(0);
        }

        for (Module module : project.getModuleSet().asList()) {
            if (module == null || !module.isUse()) continue;

            // Print module type
            log.info(StringUtils.center("mod::" + module.getType(), 100, "="));
            long startTime = new Date().getTime();

            // Initialize generators
            Generator generator;
            switch (module.getType()) {
                case ModuleConstant.MODULE_TYPE_DOCUMENT:
                    generator = new DocumentGenerator(project);
                    break;
                case ModuleConstant.MODULE_TYPE_DEPLOYMENT:
                    generator = new DeploymentGenerator(project);
                    break;
                case ModuleConstant.MODULE_TYPE_DATASOURCE:
                    generator = tableAmount > 0 ? new DatasourceGenerator(project) : null;
                    break;
                case ModuleConstant.MODULE_TYPE_SPRING_BOOT:
                    generator = new SpringBootGenerator(project);
                    break;
                case ModuleConstant.MODULE_TYPE_VUE_ELEMENT_ADMIN:
                    generator = new VueElementAdminGenerator(project);
                    break;
                case ModuleConstant.MODULE_TYPE_WEBSITE_PAGE:
                    generator = new WebsitePageGenerator(project);
                    break;
                default:
                    generator = null;
            }

            if (generator != null) {
                generator.generate();
                long endTime = new Date().getTime();
                log.info("Generating module({}) completed in {} ms", module.getType(), (endTime - startTime));
            } else {
                log.warn("Unsupported module type \"{}\"", module.getType());
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
            log.error("{}: {}", ErrorEnum.PARSE_XML_FAILED, e.getMessage());
        }

        List<Database> databases = project.getModuleSet().getDatasourceModule().getRelationalDatabase().getDatabases();
        if (databases == null || databases.isEmpty()) {
            log.error(ErrorEnum.DATABASE_UNDEFINED.toString());
            return false;
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
            log.error(ErrorEnum.TABLE_UNDEFINED.toString());
            return false;
        } else {
            log.info("{} database(s), {} table(s) detected", databases.size(), tableAmount);
        }

        for (Table table : project.getModuleSet().getDatasourceModule().getRelationalDatabase().getDatabases().get(0).getTables()) {
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
