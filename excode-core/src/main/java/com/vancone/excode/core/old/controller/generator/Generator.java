package com.vancone.excode.core.old.controller.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vancone.excode.core.old.constant.UrlPath;
import com.vancone.excode.core.old.enums.ErrorEnum;
import com.vancone.excode.core.old.model.file.ModuleInfo;
import com.vancone.excode.core.old.model.module.ModuleOld;
import com.vancone.excode.core.old.model.project.ProjectOld;
import com.vancone.excode.core.util.FileUtil;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tenton Lien
 * Every generator should extends this base class
 */
@Slf4j
public abstract class Generator {

    protected String componentTemplatePath;
    protected ModuleOld module;
    private ModuleInfo moduleInfo;
    private List<OutputFile> outputFiles = new ArrayList<>();
    protected String outputPath;

    @Getter
    protected ProjectOld projectOld;

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

    /**
     * Initialize data
     * @param projectOld
     */
    public Generator(ProjectOld projectOld) {
        this.projectOld = projectOld;

        // Recognize module type
        String callerClassName = Thread.currentThread().getStackTrace()[2].getClassName();
        String comparedString1 = callerClassName.substring(callerClassName.lastIndexOf('.') + 1)
                .replace("Generator", "").toUpperCase();

        for (ModuleOld module: projectOld.getModuleSet().asList()) {
            if (module == null) {
                continue;
            }
            if (module.getType().replace("-", "").toUpperCase().equals(comparedString1)) {
                this.module = module;
                break;
            }
        }

        // Set template paths
        templatePath = UrlPath.MODULE_PATH + module.getType() + File.separator + "templates" + File.separator;
        componentTemplatePath = templatePath + "components/";
        outputPath = UrlPath.GEN_PATH + projectOld.getGroupId() + "." +
                projectOld.getArtifactId() + "-" + projectOld.getVersion() + File.separator +
                module.getType() + File.separator;
        FileUtil.createDirectoryIfNotExist(outputPath);

        copyInitialTemplates();
    }

    /**
     * Copy templates to the target path
     */
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
                    value = value.replace("${PACKAGE_PATH}", (projectOld.getGroupId() + "." +
                            projectOld.getArtifactId()).replace('.', '/'));
                }
                paths.put(key, value);
                FileUtil.createDirectoryIfNotExist(outputPath + value);
            }
            moduleInfo.getInitial().setPaths(paths);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method should be initialized in each child class
     */
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
