package com.mekcone.incrud.core.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mekcone.incrud.core.model.project.Column;
import com.mekcone.incrud.core.model.project.Project;
import com.mekcone.incrud.core.model.project.Table;
import com.mekcone.incrud.core.model.project.Value;

import java.io.File;
import java.io.IOException;

public class ProjectLoader {
    private String projectUrl;
    private String backendSourceBaseUrl;
    private String frontendSourceBaseUrl;

    public static Project project;

    public void load(String url) {
        this.projectUrl = url;

        File projectFile = new File(url);
        if ((!projectFile.exists())) {
            Logger.error("Project file not found");
        }

        // Read project file
        String data = FileManager.read(url);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Value.class, new TypeAdapter<Value>() {
                    @Override
                    public void write(JsonWriter out, Value value) throws IOException {
                        out.value(value.getData());
                    }
                    @Override
                    public Value read(JsonReader in) throws IOException {
                        try {
                            return Value.String(in.nextString());
                        } catch (NumberFormatException e) {
                            return null;
                        }
                    }
                })
                .create();


        project = gson.fromJson(data, Project.class);
        project.updated();
//        Logger.debug("this.project: "+ this.project.getGroupId());
//        Logger.debug("global context: " + ((Project)CustomApplicationContext.context.getBean("project")).getGroupId());


        for (Table table: project.getTables()) {
            for (Column column: table.getColumns()) {
                if (column.isPrimaryKey()) {
                    table.setPrimaryKey(column.getName().getData());
                    break;
                }
            }
        }

        this.backendSourceBaseUrl = url + "/" + project.getArtifactId().getData() + "-backend" +
                "/src/main/java/" + (this.project.getGroupId() +
                '.' + this.project.getArtifactId().getData()).replace('.', '/');

        this.frontendSourceBaseUrl = url + "/" + project.getArtifactId().getData() + "-frontend";
    }
}
