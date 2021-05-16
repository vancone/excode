package com.vancone.devops.codegen.controller.generator.impl;

import com.vancone.devops.codegen.controller.generator.Generator;
import com.vancone.devops.codegen.model.database.Database;
import com.vancone.devops.codegen.model.database.Table;
import com.vancone.devops.codegen.model.project.Project;
import com.vancone.devops.constant.ModuleConstant;
import com.vancone.devops.codegen.controller.extmgr.datasource.SqlExtensionManager;
import lombok.extern.slf4j.Slf4j;

/*
 * Author: Tenton Lien
 */

@Slf4j
public class DatasourceGenerator extends Generator {

    public DatasourceGenerator(Project project) {
        super(project);
    }

    @Override
    public void generate() {
        String code = "";
        code += "-- " + ModuleConstant.DESCRIPTION + "\n\n";

        for (Database database: project.getModuleSet().getDatasourceModule().getRelationalDatabase().getDatabases()) {
            if (database != null && database.hasTable()) {
                code += SqlExtensionManager.createDatabaseQuery(database) + "\n\n";
                for (Table table: database.getTables()) {
                    if (table != null && table.hasColumn()) {
                        code += SqlExtensionManager.createTableQuery(table) + "\n\n";
                    }
                }
            }
        }
        addOutputFile(project.getArtifactId() + ".sql", code);
        write();
    }
}
