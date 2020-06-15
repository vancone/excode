package com.mekcone.excrud.codegen.controller.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekcone.excrud.codegen.constant.UrlPath;
import com.mekcone.excrud.codegen.enums.ErrorEnum;
import com.mekcone.excrud.codegen.model.module.Module;
import com.mekcone.excrud.codegen.model.module.ModuleInfo;
import com.mekcone.excrud.codegen.model.project.Project;
import com.mekcone.excrud.codegen.util.FileUtil;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Every generator should extends this base class
@Slf4j
public abstract class CommonGenerator {

    protected String componentTemplatePath;
    protected Module module;
    private ModuleInfo moduleInfo;
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

        for (Module module: project.getModuleSet().asList()) {
            if (module.getType().replace("-", "").toUpperCase().equals(comparedString1)) {
                this.module = module;
                break;
            }
        }

        // Set template paths
        templatePath = UrlPath.MODULE_PATH + module.getType() + File.separator + "templates" + File.separator;
        componentTemplatePath = templatePath + "components/";
        outputPath = UrlPath.GEN_PATH + project.getGroupId() + "." +
                project.getArtifactId() + "-" + project.getVersion() + File.separator +
                module.getType() + File.separator;
        FileUtil.createDirectoryIfNotExist(outputPath);

        copyInitialTemplates();
    }

    // Copy templates to the target path
    protected void copyInitialTemplates() {
        // Read initial.txt
        String genFilePath = new File(templatePath).toPath().getParent().toString() + File.separator + "gen.json";
        String initialFile = FileUtil.read(genFilePath);
        if (initialFile == null) {
            log.error(ErrorEnum.NO_DEFAULT_INITIALIZING_BEHAVIOR.getMessage(), genFilePath);
            return;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            moduleInfo = objectMapper.readValue(initialFile, ModuleInfo.class);

            log.info(moduleInfo.toString());

            // Copy directories
            for (String dir: moduleInfo.getInitial().getDirectories()) {
                String destDir = dir;
                if (dir.contains("${VUE_ELEMENT_ADMIN_VERSION}")) {
                    dir = dir.replace("${VUE_ELEMENT_ADMIN_VERSION}", moduleInfo.getVersion().get(module.getType()));
                    destDir = dir.replace(moduleInfo.getVersion().get(module.getType()) + "/", "");
                }
                FileUtil.copyDirectory(templatePath + dir, outputPath + destDir);
            }

            // Copy files
            for (String file: moduleInfo.getInitial().getFiles()) {
                String destFile = file;
                if (file.contains("${VUE_ELEMENT_ADMIN_VERSION}")) {
                    file = file.replace("${VUE_ELEMENT_ADMIN_VERSION}", moduleInfo.getVersion().get(module.getType()));
                    destFile = file.replace(moduleInfo.getVersion().get(module.getType()) + "/", "");
                }
                FileUtil.copyFile(templatePath + file, outputPath + destFile);
            }

            // Register paths and create directories
            FileUtil.createDirectoryIfNotExist(outputPath);
            Map<String, String> paths = new HashMap<>();
            for (String key: moduleInfo.getInitial().getPaths().keySet()) {
                // Preprocess URL
                String value = moduleInfo.getInitial().getPaths().get(key);
                if (value.contains("${PACKAGE_PATH}")) {
                    value = value.replace("${PACKAGE_PATH}", (project.getGroupId() + "." +
                            project.getArtifactId()).replace('.', '/'));
                }
                paths.put(key, value);
                FileUtil.createDirectoryIfNotExist(outputPath + value);
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
