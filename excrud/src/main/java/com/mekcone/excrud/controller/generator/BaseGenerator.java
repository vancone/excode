package com.mekcone.excrud.controller.generator;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekcone.excrud.Application;
import com.mekcone.excrud.enums.ErrorEnum;
import com.mekcone.excrud.model.project.OldExport;
import com.mekcone.excrud.model.project.Project;
import com.mekcone.excrud.util.FileUtil;
import com.mekcone.excrud.util.LogUtil;
import lombok.Data;
import lombok.Getter;

import java.util.*;
import java.util.Map.Entry;

// Every generator should extends this base class
public abstract class BaseGenerator {

    protected final String EXCRUD_HOME = System.getenv(Application.getApplicationName().toUpperCase() + "_HOME");

    protected String componentTemplatePath;
    protected OldExport oldExport;
    protected String exportType;
    protected String generatedDataPath;

    @Getter
    protected Project project;

    @Getter
    protected String templatePath;

    private Map<String, String> paths = new HashMap<>();

    @Data
    public class OutputFile {
        private String path;
        private String type;
        private String content;

        public OutputFile(String path, String type, String content) {
            this.path = path;
            this.type = type;
            this.content = content;
        }

        public String getFileName() {
            System.out.println("path: " + path);
            String[] stringArray = path.split("/");
            if (stringArray.length > 0) {
                String[] stringArray2 = stringArray[stringArray.length - 1].split("\\.");
                if (stringArray2.length > 0) {
                    return stringArray2[0];
                }
                return stringArray[stringArray.length - 1];
            }
            return path;
        }

        public boolean isType(String type) {
            return type.equals(this.type);
        }
    }

    private List<OutputFile> outputFiles = new ArrayList<>();

    // Copy templates to the target path
    protected void copyInitialTemplates() {
        // Read initial.txt
        String initialFile = FileUtil.read(templatePath + "../gen.json");
        if (initialFile == null) {
            LogUtil.error(ErrorEnum.NO_DEFAULT_INITIALIZING_BEHAVIOR, templatePath + "gen.json");
            return;
        }
        var objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
        try {
            var rootJsonNode = objectMapper.readTree(initialFile);
            var pathsJsonNode = rootJsonNode.path("initial").path("paths");

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
        if (EXCRUD_HOME == null) {
            LogUtil.fatalError(ErrorEnum.EXCRUD_HOME_ENV_VARIABLE_NOT_SET);
        }
        this.project = project;
//        this.oldExport = project.getExport(exportType);
        this.exportType = exportType;

        // Set template paths
        this.templatePath = EXCRUD_HOME + "/generators/" + exportType + "/templates/";
        this.componentTemplatePath = templatePath + "components/";
        this.generatedDataPath = exportType + "/";
    }

    public void addOutputFile(String path, String type, String content) {
        outputFiles.add(new OutputFile(path, type, content));
    }

    public List<OutputFile> getOutputFiles() {
        return outputFiles;
    }

    public void write() {
        for (OutputFile outputFile: outputFiles) {
            LogUtil.info("Output " + outputFile.getType() + " file: " + outputFile.getPath());
//            FileUtil.write(outputFile.getPath(), outputFile.getContent());
        }
    }
}
