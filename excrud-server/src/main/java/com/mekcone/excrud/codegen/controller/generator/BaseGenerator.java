package com.mekcone.excrud.codegen.controller.generator;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekcone.excrud.codegen.constant.UrlPath;
import com.mekcone.excrud.codegen.enums.ErrorEnum;
import com.mekcone.excrud.codegen.model.project.Project;
import com.mekcone.excrud.codegen.util.FileUtil;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.*;
import java.util.Map.Entry;

// Every generator should extends this base class
@Slf4j
public abstract class BaseGenerator {

    protected String componentTemplatePath;
    protected String moduleType;
    protected String outputPath;
    private Map<String, String> paths = new HashMap<>();

    @Getter
    protected Project project;

    @Getter
    protected static String templatePath;

    @Data
    public class OutputFile {
        private String path;
        private String content;

        public OutputFile(String path, String content) {
            this.path = path;
            this.content = content;
        }

        public String getFileName() {
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
    }

    private List<OutputFile> outputFiles = new ArrayList<>();

    // Copy templates to the target path
    protected void copyInitialTemplates() {
        // Read initial.txt
        String initialFile = FileUtil.read(templatePath + "../gen.json");
        if (initialFile == null) {
            log.error(ErrorEnum.NO_DEFAULT_INITIALIZING_BEHAVIOR.toString() + templatePath + "gen.json");
            return;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
        try {
            JsonNode rootJsonNode = objectMapper.readTree(initialFile);
            JsonNode pathsJsonNode = rootJsonNode.path("initial").path("paths");
            JsonNode extensionsJsonNode = rootJsonNode.path("initial").path("extensions");

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
                log.error(ErrorEnum.PATHS_NOT_A_VALID_OBJECT.toString());
            }

            // Create directories
            FileUtil.checkDirectory(outputPath);
            for (String path: paths.values()) {
                FileUtil.checkDirectory(outputPath + path);
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    // This method should be initialized in each child class
    public abstract void generate();

    public String getPath(String pathName) {
        return paths.get(pathName);
    }

    protected void initialize(Project project, String moduleType) {
        if (UrlPath.EXCRUD_HOME == null) {
            log.error(ErrorEnum.EXCRUD_HOME_ENV_VARIABLE_NOT_SET.toString());
        }
        this.project = project;
        this.moduleType = moduleType;

        // Set template paths
        templatePath = UrlPath.SPRING_BOOT_TEMPLATE_PATH;
        componentTemplatePath = templatePath + "components/";
        outputPath = UrlPath.GEN_PATH + project.getGroupId() + "." +
                project.getArtifactId() + "-" + project.getVersion() + File.separator +
                moduleType + File.separator;
        FileUtil.checkDirectory(outputPath);
    }

    public void addOutputFile(String path, String content) {
        outputFiles.add(new OutputFile(path, content));
    }

    public List<OutputFile> getOutputFiles() {
        return outputFiles;
    }

    public void write() {
        for (OutputFile outputFile: outputFiles) {
            FileUtil.write(outputPath + outputFile.getPath(), outputFile.getContent());
        }
    }
}
