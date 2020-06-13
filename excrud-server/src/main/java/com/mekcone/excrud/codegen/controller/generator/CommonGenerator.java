package com.mekcone.excrud.codegen.controller.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekcone.excrud.codegen.constant.ModuleType;
import com.mekcone.excrud.codegen.constant.UrlPath;
import com.mekcone.excrud.codegen.enums.ErrorEnum;
import com.mekcone.excrud.codegen.model.module.ModuleInfo;
import com.mekcone.excrud.codegen.model.project.Project;
import com.mekcone.excrud.codegen.util.FileUtil;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Every generator should extends this base class
@Slf4j
public abstract class CommonGenerator {

    protected String componentTemplatePath;
    private ModuleInfo moduleInfo;
    protected String moduleType;
    private List<OutputFile> outputFiles = new ArrayList<>();
    protected String outputPath;

    @Getter
    protected Project project;

    @Getter
    private String templatePath;

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

    // Initialize data
    public CommonGenerator(Project project) {
        if (UrlPath.EXCRUD_HOME == null) {
            log.error(ErrorEnum.EXCRUD_HOME_ENV_VARIABLE_NOT_SET.toString());
        }
        this.project = project;

        // Recognize module type
        String callerClassName = Thread.currentThread().getStackTrace()[2].getClassName();
        String comparedString1 = callerClassName.substring(callerClassName.lastIndexOf('.') + 1)
                .replace("Generator", "").toUpperCase();

        for (Field field: ModuleType.class.getDeclaredFields()) {
            try {
                String comparedString2 = field.getName().replace("_", "");
                if (comparedString1.equals(comparedString2)) {
                    moduleType = field.get(null).toString();
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        // Set template paths
        templatePath = UrlPath.MODULE_PATH + moduleType + File.separator + "templates" + File.separator;
        componentTemplatePath = templatePath + "components/";
        outputPath = UrlPath.GEN_PATH + project.getGroupId() + "." +
                project.getArtifactId() + "-" + project.getVersion() + File.separator +
                moduleType + File.separator;
        FileUtil.checkDirectory(outputPath);
    }

    // Copy templates to the target path
    protected void copyInitialTemplates() {
        // Read initial.txt
        String initialFile = FileUtil.read(templatePath + "../gen.json");
        if (initialFile == null) {
            log.error(ErrorEnum.NO_DEFAULT_INITIALIZING_BEHAVIOR.toString() + templatePath + "gen.json");
            return;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            moduleInfo = objectMapper.readValue(initialFile, ModuleInfo.class);

            log.info(moduleInfo.toString());

            // Create directories
            FileUtil.checkDirectory(outputPath);
            Map<String, String> paths = new HashMap<>();
            for (String key: moduleInfo.getInitial().getPaths().keySet()) {
                // Preprocess URL
                String value = moduleInfo.getInitial().getPaths().get(key);
                if (value.contains("${PACKAGE_PATH}")) {
                    value = value.replace("${PACKAGE_PATH}", (project.getGroupId() + "." +
                            project.getArtifactId()).replace('.', '/'));
                }
                paths.put(key, value);
                FileUtil.checkDirectory(outputPath + value);
            }
            moduleInfo.getInitial().setPaths(paths);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    // This method should be initialized in each child class
    public abstract void generate();

    public String getPath(String pathName) {
        return moduleInfo.getInitial().getPaths().get(pathName);
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
