/*
package com.mekcone.excrud.service.generators.frontend.admin;

import com.mekcone.excrud.util.FileUtil;
import com.mekcone.excrud.util.LogUtil;
import com.mekcone.excrud.model.file.javascript.RouterJS;
import com.mekcone.excrud.model.project.Project;
import com.mekcone.excrud.model.project.components.Table;

public class RouterJSGennerator {
    public RouterJSGennerator(Project project, String path) {
        RouterJS routerJS = new RouterJS(System.getProperty("user.dir") + "/templates/vue/src/router/index.js");

        for (Table table: project.getDatabases().get(0).getTables()) {
            routerJS.addImportedItems(table.getCapitalizedCamelName(), table.getCapitalizedCamelName());
            routerJS.routeJSON.addRouter("/" + table.getCamelName(),
                    table.getCapitalizedCamelName(),
                    table.getCapitalizedCamelName());
        }

        FileUtil.write(path + "/index.js", routerJS.toString());
        LogUtil.info("Output Vue router file \"" + path + "/index.js" + "\"");
    }
}
*/
