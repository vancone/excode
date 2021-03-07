package com.vancone.monster.code.codegen.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.vancone.monster.code.codegen.controller.generator.Generator;
import com.vancone.monster.code.codegen.controller.generator.impl.*;
import com.vancone.monster.code.codegen.enums.ErrorEnum;
import com.vancone.monster.code.codegen.model.database.Column;
import com.vancone.monster.code.codegen.model.database.Database;
import com.vancone.monster.code.codegen.model.database.Table;
import com.vancone.monster.code.codegen.model.module.Module;
import com.vancone.monster.code.codegen.model.project.Project;
import com.vancone.monster.code.constant.ModuleConstant;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

/*
 * Author: Tenton Lien
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
            if (!module.isUse()) continue;

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

    public String showThirdPartyComponentList() {
        StringBuilder info = new StringBuilder();
        info.append("==================== EXCRUD STANDS ON THE SHOULDERS OF GIANTS ====================\n");
        info.append("|  Dataway *                  |  hasor.net                                       |\n");
        info.append("|  Cordova *                  |  cordova.apache.org                              |\n");
        info.append("|  ECMAScript                 |  ecma-international.org                          |\n");
        info.append("|  Electron *                 |  electronjs.org                                  |\n");
        info.append("|  Flutter *                  |  flutter.io                                      |\n");
        info.append("|  Java                       |  java.com                                        |\n");
        info.append("|  Maven                      |  maven.apache.org                                |\n");
        info.append("|  MyBatis                    |  mybatis.org                                     |\n");
        info.append("|  MySQL                      |  mysql.com                                       |\n");
        info.append("|  Node.js                    |  nodejs.org                                      |\n");
        info.append("|  Spring                     |  spring.io                                       |\n");
        info.append("|  Swagger                    |  swagger.io                                      |\n");
        info.append("|  TypeScript *               |  typescriptlang.org                              |\n");
        info.append("|  Vue.js                     |  vuejs.org                                       |\n");
        info.append("|  Vue Element Admin          |  panjiachen.github.io/vue-element-admin-site/    |\n");
        info.append("==================================================================================");
        return info.toString();
    }
}
