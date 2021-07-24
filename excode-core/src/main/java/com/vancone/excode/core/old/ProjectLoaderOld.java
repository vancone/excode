package com.vancone.excode.core.old;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.vancone.excode.core.old.constant.ModuleConstant;
import com.vancone.excode.core.old.controller.generator.Generator;
import com.vancone.excode.core.old.enums.ErrorEnum;
import com.vancone.excode.core.old.model.database.Column;
import com.vancone.excode.core.old.model.database.Database;
import com.vancone.excode.core.old.model.database.Table;
import com.vancone.excode.core.old.model.module.ModuleOld;
import com.vancone.excode.core.old.model.project.ProjectOld;
import com.vancone.excode.core.old.controller.generator.impl.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author Tenton Lien
 */
@Slf4j
public class ProjectLoaderOld {
    @Getter
    private ProjectOld projectOld;

    private int tableAmount = 0;

    public void generate() {
        if (projectOld.getModuleSet().asList().isEmpty()) {
            log.info("No module options found");
            System.exit(0);
        }

        for (ModuleOld module : projectOld.getModuleSet().asList()) {
            if (module == null || !module.isUse()) continue;

            // Print module type
            log.info(StringUtils.center("mod::" + module.getType(), 100, "="));
            long startTime = System.currentTimeMillis();

            // Initialize generators
            Generator generator;
            switch (module.getType()) {
                case ModuleConstant.MODULE_TYPE_DOCUMENT:
                    generator = new DocumentGenerator(projectOld);
                    break;
                case ModuleConstant.MODULE_TYPE_DEPLOYMENT:
                    generator = new DeploymentGenerator(projectOld);
                    break;
                case ModuleConstant.MODULE_TYPE_DATASOURCE:
                    generator = tableAmount > 0 ? new DatasourceGenerator(projectOld) : null;
                    break;
                case ModuleConstant.MODULE_TYPE_SPRING_BOOT:
                    generator = new SpringBootGenerator(projectOld);
                    break;
                case ModuleConstant.MODULE_TYPE_VUE_ELEMENT_ADMIN:
                    generator = new VueElementAdminGenerator(projectOld);
                    break;
                case ModuleConstant.MODULE_TYPE_WEBSITE_PAGE:
                    generator = new WebsitePageGenerator(projectOld);
                    break;
                default:
                    generator = null;
            }

            if (generator != null) {
                generator.generate();
                long endTime = System.currentTimeMillis();
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
            projectOld = xmlMapper.readValue(content, ProjectOld.class);
            log.info("Load project {}:{} completed", projectOld.getGroupId(), projectOld.getArtifactId());
        } catch (Exception e) {
            log.error("{}: {}", ErrorEnum.PARSE_XML_FAILED, e.getMessage());
            return false;
        }

        List<Database> databases = projectOld.getModuleSet().getDatasourceModule().getRelationalDatabase().getDatabases();
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

        for (Table table : projectOld.getModuleSet().getDatasourceModule().getRelationalDatabase().getDatabases().get(0).getTables()) {
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
