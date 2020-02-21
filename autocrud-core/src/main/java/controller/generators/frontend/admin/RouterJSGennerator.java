package controller.generators.frontend.admin;

import controller.FileManager;
import controller.Logger;
import model.javascript.RouterJS;
import model.project.Project;
import model.project.Table;

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
