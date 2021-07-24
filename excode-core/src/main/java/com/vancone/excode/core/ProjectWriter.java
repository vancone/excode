package com.vancone.excode.core;

import com.vancone.excode.core.constant.ModuleType;
import com.vancone.excode.core.generator.SpringBootGenerator;
import com.vancone.excode.core.model.Module;
import com.vancone.excode.core.model.Project;
import com.vancone.excode.core.util.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tenton Lien
 * @date 7/24/2021
 */
@Slf4j
public class ProjectWriter {

    private final String genLocation = "/opt/excode/gen" + File.separator;

    private Project project;

    private String rootDirectory;

    private Map<String, String> outputs = new HashMap<>();

    public ProjectWriter(Project project) {
        this.project = project;
        rootDirectory = genLocation + project.getGroupId() + "." + project.getArtifactId() + "-" + project.getVersion() + File.separator;
//        rootDirectory = genLocation + project.getGroupId() + "." + project.getArtifactId() + "-" + project.getVersion() + "-" + System.currentTimeMillis() + File.separator;
    }

    public Project getProject() {
        return project;
    }

    public void output(String target, String content) {
        outputs.put(target, content);
    }

    public void write() {
        if (project.getModules().isEmpty()) {
            log.error("No valid module found");
            return;
        }

        for (Module module: project.getModules()) {
            if (module.getType().equals(ModuleType.SPRING_BOOT)) {
                log.info("Generate module [{}]", module.getType());
                SpringBootGenerator.generate(module, this);
            }
        }

        /**
         * Output to disk
         */
        for (String target: outputs.keySet()) {
            String content = outputs.get(target);
            FileUtil.write(rootDirectory + target, content);
        }
    }
}
