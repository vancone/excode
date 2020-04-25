package com.mekcone.excrud.controller.generator;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekcone.excrud.Application;
import com.mekcone.excrud.enums.ErrorEnum;
import com.mekcone.excrud.model.project.Export;
import com.mekcone.excrud.model.project.Project;
import com.mekcone.excrud.util.FileUtil;
import com.mekcone.excrud.util.LogUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

// Every generator should extends this base class
public abstract class BaseGenerator {

    protected final String EXCRUD_HOME = System.getenv(Application.getApplicationName().toUpperCase() + "_HOME");

    protected String componentTemplatePath;
    protected Export export;
    protected String exportType;
    protected String generatedDataPath;
    protected Project project;
    protected String templatePath;

    private Map<String, String> paths = new HashMap<>();

    // Copy templates to the target path
    protected void copyInitialTemplates() {
        // Read initial.txt
        String initialFile = FileUtil.read(templatePath + "initial.json");
        if (initialFile == null) {
            LogUtil.info("No default initializing behavior.");
        }
        var objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
        try {
            var rootJsonNode = objectMapper.readTree(initialFile);
            var pathsJsonNode = rootJsonNode.get("paths");

            if (pathsJsonNode.isObject()) {
                Iterator<Entry<String, JsonNode>> entryIterator = pathsJsonNode.fields();
                while (entryIterator.hasNext()) {
                    Entry<String, JsonNode> entry = entryIterator.next();
                    String value = entry.getValue().asText();
                    if (value.contains("${PACKAGE_PATH}")) {
                        value = value.replace("${PACKAGE_PATH}", (project.getGroupId() + "." +
                                project.getArtifactId()).replace('.', '/'));
                    }
                    paths.put(entry.getKey(), value);
                }
            } else {
                LogUtil.fatalError(ErrorEnum.PATHS_NOT_A_VALID_OBJECT);
            }

            // Create directories
            FileUtil.checkDirectory(generatedDataPath);
            for (String path: paths.values()) {
                FileUtil.checkDirectory(generatedDataPath + path);
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    // This method should be initialized in each child class
    public abstract void generate();

    protected String getPath(String pathName) {
        return paths.get(pathName);
    }

    protected void initialize(Project project, String exportType) {
        this.project = project;
        this.export = project.getExport(exportType);
        this.exportType = exportType;

        // Set template paths
        this.templatePath = EXCRUD_HOME + "/generators/" + exportType + "/templates/";
        this.componentTemplatePath = templatePath + "components/";
        this.generatedDataPath = exportType + "/";
    }
}
