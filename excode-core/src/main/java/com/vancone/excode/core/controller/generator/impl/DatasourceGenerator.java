package com.vancone.excode.core.controller.generator.impl;

import com.vancone.excode.core.constant.ModuleConstant;
import com.vancone.excode.core.controller.extmgr.datasource.SqlExtensionManager;
import com.vancone.excode.core.model.database.Database;
import com.vancone.excode.core.model.database.Table;
import com.vancone.excode.core.model.project.Project;
import com.vancone.excode.core.controller.generator.Generator;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Tenton Lien
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
