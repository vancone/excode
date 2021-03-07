package com.vancone.monster.code.codegen.controller.generator.impl;

import com.vancone.monster.code.codegen.controller.generator.Generator;
import com.vancone.monster.code.constant.ModuleConstant;
import com.vancone.monster.code.codegen.controller.extmgr.datasource.SqlExtensionManager;
import com.vancone.monster.code.codegen.model.database.Database;
import com.vancone.monster.code.codegen.model.database.Table;
import com.vancone.monster.code.codegen.model.project.Project;
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
