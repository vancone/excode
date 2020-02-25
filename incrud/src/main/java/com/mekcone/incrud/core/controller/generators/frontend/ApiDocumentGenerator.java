package com.mekcone.incrud.core.controller.generators.frontend;

import com.google.gson.GsonBuilder;
import com.mekcone.incrud.core.model.project.Column;
import com.mekcone.incrud.core.model.project.Project;
import com.mekcone.incrud.core.model.project.Table;
import com.mekcone.incrud.core.controller.FileManager;
import com.mekcone.incrud.core.controller.Logger;
import com.mekcone.incrud.core.controller.ProjectBuilder;

import java.util.HashMap;
import java.util.Map;

import static j2html.TagCreator.*;

public class ApiDocumentGenerator {
    public ApiDocumentGenerator(Project project, String path) {

        String html = "<!--" + ProjectBuilder.getDescription() + "-->\n" + document(html(
                head(
                        title(project.getArtifactId().capitalized() + " API"),
                        meta().withCharset("UTF-8"),
                        styleWithInlineFile( System.getProperty("user.dir") + "/templates/api.css")
                ),
                body(
                        div(
                                h1(project.getArtifactId().capitalized() + " API")
                        ).withClass("header"),
                        each(project.getTables(), table ->
                            div(
                                    h2(table.getName().capitalizedCamelStyle()),

                                    span("POST").withClass("PostSpan"),
                                    h3("/api/" + project.getArtifactId().camelStyle() + "/" + table.getName().camelStyle()),
                                    p("Create " + table.getName().camelStyle() + " items").withClass("description"), br(),
                                    h3("Request (application/json)"), br(),
                                    textarea(getRequestJsonSample(table, true)), br(),
                                    h3("Response"), br(),
                                    textarea("// SUCCESS\n" + "{\n" +
                                            "  code: 0,\n" +
                                            "  message: \"success\",\n" +
                                            "}\n\n" +
                                            "// FAILED\n" + "{\n" +
                                            "  code: 1,\n" +
                                            "  message: \"create " + table.getName().camelStyle() +" failed\",\n" +
                                            "}"
                                    ), br(),

                                    span("GET").withClass("GetSpan"),
                                    h3("/api/" + project.getArtifactId().camelStyle() + "/" + table.getName().camelStyle()),
                                    p("Retrieve the list of all " + table.getName().camelStyle() + " items").withClass("description"),
                                    p("Content-type :  application/json"),

                                    span("GET").withClass("GetSpan"),
                                    h3("/api/" + project.getArtifactId().camelStyle()  + "/" + table.getName().camelStyle() + "/{" + table.getPrimaryKey().camelStyle() + "}"),
                                    p("Content-type :  application/json"),
                                    p("Retrieve the list of all " + table.getName().camelStyle() + " items").withClass("description"),

                                    span("PUT").withClass("PutSpan"),
                                    span("DELETE").withClass("DeleteSpan")

                                    /*each (table.getColumns(), column ->
                                            p(column.getName().capitalizedCamelStyle())
                                    )*/
                            ).withClass("module")
                        )
                )
        ));

        FileManager.write(path + "/api.html", html);
        Logger.info("Output API web page file \"" + path + "/api.html" + "\"");
    }

    private String getRequestJsonSample(Table table, boolean removePrimaryKey) {
        Map map = new HashMap();
        for (Column column: table.getColumns()) {
            map.put(column.getName().camelStyle(), "");
        }
        if (removePrimaryKey) {
            map.remove(table.getPrimaryKey().camelStyle());
        }
        return new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(map);
    }
}
