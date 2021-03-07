package com.mekcone.studio.codegen;

import com.vancone.monster.code.constant.UrlPath;
import com.vancone.monster.code.codegen.controller.ProjectLoader;
import com.vancone.monster.code.codegen.controller.executor.SpringBootExecutor;
import com.vancone.monster.code.codegen.model.project.Project;
import com.vancone.monster.code.codegen.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;

@Slf4j
public class RunTest {

    @Test
    void runSpringBootModuleOfMall() {
        String mallPath = UrlPath.EXAMPLE_PATH + "mall" + File.separator + "excrud.xml";
        String projectContent = FileUtil.read(mallPath);
        if (projectContent != null) {
            ProjectLoader projectLoader = new ProjectLoader();
            projectLoader.load(projectContent);
            runSpringBootModule(projectLoader.getProject());
        } else {
            log.error("Failed to package mall project");
        }
    }

    void runSpringBootModule(Project project) {
        String groupId = project.getGroupId();
        String artifactId = project.getArtifactId();
        String version = project.getVersion();
        SpringBootExecutor springBootExecutor = new SpringBootExecutor(groupId, artifactId, version);
        springBootExecutor.run();
    }
}
