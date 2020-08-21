package com.mekcone.studio.codegen;

import com.mekcone.studio.codegen.constant.UrlPath;
import com.mekcone.studio.codegen.controller.ProjectLoader;
import com.mekcone.studio.codegen.controller.executor.SpringBootExecutor;
import com.mekcone.studio.codegen.model.project.Project;
import com.mekcone.studio.codegen.util.FileUtil;
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
