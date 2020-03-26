//package com.mekcone.excrud.model.file.javascript;
//
//import com.google.gson.GsonBuilder;
//import com.mekcone.excrud.model.template.Template;
//import javafx.util.Pair;
//import com.mekcone.excrud.model.file.json.RouteJSON;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class RouterJS {
//    private List<Pair<String, String>> importedItems = new ArrayList<>();
//    public RouteJSON routeJSON = new RouteJSON();
//    private Template template;
//
//    public RouterJS(String templatePath) {
//        template = new Template(templatePath);
//    }
//
//    public void addImportedItems(String componentName, String componentPath) {
//        Pair pair = new Pair(componentName, componentPath);
//        this.importedItems.add(pair);
//    }
//
//    public String toString() {
//        String importedItemsString = "";
//        for (Pair<String, String> pair: importedItems) {
//            importedItemsString += "import " + pair.getKey() + " from '@/components/" + pair.getValue() + "'\n";
//        }
//        this.template.insert("importComponent", importedItemsString);
//        String routerJSONString = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(routeJSON);
//        this.template.insert("routerJSON", routerJSONString);
//        String str = this.template.toString();
//        int index = 0;
//        while((index = str.indexOf("\"component\":", index)) > 0) {
//            int quotationIndex = str.indexOf("\"", index + 11);
//            str = str.substring(0, quotationIndex) + str.substring(quotationIndex + 1);
//            quotationIndex = str.indexOf("\"", quotationIndex + 1);
//            str = str.substring(0, quotationIndex) + str.substring(quotationIndex + 1);
//            index = quotationIndex;
//        }
//        return str;
//    }
//}
