package com.mekcone.excrud.codegen.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mekcone.excrud.codegen.constant.ModuleType;
import com.mekcone.excrud.codegen.controller.generator.*;
import com.mekcone.excrud.codegen.enums.ErrorEnum;
import com.mekcone.excrud.codegen.model.module.impl.relationaldatabase.component.Column;
import com.mekcone.excrud.codegen.model.module.impl.relationaldatabase.component.Database;
import com.mekcone.excrud.codegen.model.module.impl.relationaldatabase.component.Table;
import com.mekcone.excrud.codegen.model.project.Project;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;

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

        for (com.mekcone.excrud.codegen.model.module.Module module : project.getModuleSet().asList()) {
            if (!module.isUse()) {
                continue;
            }

            // Print module type
            log.info("MODULE :: [ {} ]", module.type());
            long startTime = new Date().getTime();

            // Initialize generators
            CommonGenerator generator;
            switch(module.type()) {
                case ModuleType.API_DOCUMENT:
                    generator = new ApiDocumentGenerator(project);
                    break;
                case ModuleType.ENTERPRISE_OFFICIAL_WEBSITE:
                    generator = new EnterpriseOfficialWebsiteGenerator(project);
                    break;
                case ModuleType.RELATIONAL_DATABASE:
                    generator = tableAmount > 0 ? new RelationalDatabaseGenerator(project) : null;
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
                long endTime = new Date().getTime();
                log.info("Executing Module {} complete in {} ms", module.type(), (endTime - startTime));
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
            log.error("{}: {}", ErrorEnum.PARSE_XML_FAILED, e.getMessage());
        }

        List<Database> databases = project.getModuleSet().getRelationalDatabaseModule().getDatabases();
        if (databases == null || databases.isEmpty()) {
            log.error(ErrorEnum.DATABASE_UNDEFINED.toString());
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
