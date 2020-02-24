package com.mekcone.autocrud.core.model.javascript;

import com.google.gson.GsonBuilder;
import com.mekcone.autocrud.core.controller.TemplateProccesor;
import javafx.util.Pair;
import com.mekcone.autocrud.core.model.json.RouteJSON;

import java.util.ArrayList;
import java.util.List;

public class RouterJS {
    private List<Pair<String, String>> importedItems = new ArrayList<>();
    public RouteJSON routeJSON = new RouteJSON();
    private TemplateProccesor templateProccesor;

    public RouterJS(String templatePath) {
        templateProccesor = new TemplateProccesor(templatePath);
    }

    public void addImportedItems(String componentName, String componentPath) {
        Pair pair = new Pair(componentName, componentPath);
        this.importedItems.add(pair);
    }

    public String toString() {
        String importedItemsString = "";
        for (Pair<String, String> pair: importedItems) {
            importedItemsString += "import " + pair.getKey() + " from '@/components/" + pair.getValue() + "'\n";
        }
        this.templateProccesor.insert("importComponent", importedItemsString);
        String routerJSONString = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(routeJSON);
        this.templateProccesor.insert("routerJSON", routerJSONString);
        String str = this.templateProccesor.toString();
        int index = 0;
        while((index = str.indexOf("\"component\":", index)) > 0) {
            int quotationIndex = str.indexOf("\"", index + 11);
            str = str.substring(0, quotationIndex) + str.substring(quotationIndex + 1);
            quotationIndex = str.indexOf("\"", quotationIndex + 1);
            str = str.substring(0, quotationIndex) + str.substring(quotationIndex + 1);
            index = quotationIndex;
        }
        return str;
    }
}
