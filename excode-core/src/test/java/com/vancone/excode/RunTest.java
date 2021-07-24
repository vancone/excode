package com.vancone.excode;

import com.vancone.excode.core.old.constant.UrlPath;
import com.vancone.excode.core.old.ProjectLoaderOld;
import com.vancone.excode.core.old.controller.executor.SpringBootExecutor;
import com.vancone.excode.core.old.model.project.ProjectOld;
import com.vancone.excode.core.util.FileUtil;
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
            ProjectLoaderOld projectLoaderOld = new ProjectLoaderOld();
            projectLoaderOld.load(projectContent);
            runSpringBootModule(projectLoaderOld.getProjectOld());
        } else {
            log.error("Failed to package mall project");
        }
    }

    void runSpringBootModule(ProjectOld projectOld) {
        String groupId = projectOld.getGroupId();
        String artifactId = projectOld.getArtifactId();
        String version = projectOld.getVersion();
        SpringBootExecutor springBootExecutor = new SpringBootExecutor(groupId, artifactId, version);
        springBootExecutor.run();
    }
}
