package com.vancone.excode.core.old.controller.generator.impl;

import com.vancone.excode.core.old.constant.ModuleConstant;
import com.vancone.excode.core.old.controller.extmgr.datasource.SqlExtensionManager;
import com.vancone.excode.core.old.model.database.Database;
import com.vancone.excode.core.old.model.database.Table;
import com.vancone.excode.core.old.model.project.ProjectOld;
import com.vancone.excode.core.old.controller.generator.Generator;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Tenton Lien
 */
@Slf4j
public class DatasourceGenerator extends Generator {

    public DatasourceGenerator(ProjectOld projectOld) {
        super(projectOld);
    }

    @Override
    public void generate() {
        String code = "";
        code += "-- " + ModuleConstant.DESCRIPTION + "\n\n";

        for (Database database: projectOld.getModuleSet().getDatasourceModule().getRelationalDatabase().getDatabases()) {
            if (database != null && database.hasTable()) {
                code += SqlExtensionManager.createDatabaseQuery(database) + "\n\n";
                for (Table table: database.getTables()) {
                    if (table != null && table.hasColumn()) {
                        code += SqlExtensionManager.createTableQueryOld(table) + "\n\n";
                    }
                }
            }
        }
        addOutputFile(projectOld.getArtifactId() + ".sql", code);
        write();
    }
}
