package com.mekcone.incrud.core.controller.generators.frontend.admin;

import com.mekcone.incrud.core.controller.FileManager;
import com.mekcone.incrud.core.controller.Logger;
import com.mekcone.incrud.core.model.javascript.RouterJS;
import com.mekcone.incrud.core.model.project.Project;
import com.mekcone.incrud.core.model.project.Table;

public class RouterJSGennerator {
    public RouterJSGennerator(Project project, String path) {
        RouterJS routerJS = new RouterJS(System.getProperty("user.dir") + "/templates/vue/src/router/index.js");

        for (Table table: project.getTables()) {
            routerJS.addImportedItems(table.getName().capitalizedCamelStyle(), table.getName().capitalizedCamelStyle());
            routerJS.routeJSON.addRouter("/" + table.getName().camelStyle(),
                    table.getName().capitalizedCamelStyle(),
                    table.getName().capitalizedCamelStyle());
        }

        FileManager.write(path + "/index.js", routerJS.toString());
        Logger.info("Output Vue router file \"" + path + "/index.js" + "\"");
    }
}
