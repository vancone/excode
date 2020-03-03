package com.mekcone.incrud.service.generators.frontend.admin;

import com.mekcone.incrud.util.FileUtil;
import com.mekcone.incrud.util.LogUtil;
import com.mekcone.incrud.model.file.javascript.RouterJS;
import com.mekcone.incrud.model.project.ProjectModel;
import com.mekcone.incrud.model.project.components.Table;

public class RouterJSGennerator {
    public RouterJSGennerator(ProjectModel projectModel, String path) {
        RouterJS routerJS = new RouterJS(System.getProperty("user.dir") + "/templates/vue/src/router/index.js");

        for (Table table: projectModel.getTables()) {
            routerJS.addImportedItems(table.getCapitalizedCamelName(), table.getCapitalizedCamelName());
            routerJS.routeJSON.addRouter("/" + table.getCamelName(),
                    table.getCapitalizedCamelName(),
                    table.getCapitalizedCamelName());
        }

        FileUtil.write(path + "/index.js", routerJS.toString());
        LogUtil.info("Output Vue router file \"" + path + "/index.js" + "\"");
    }
}
