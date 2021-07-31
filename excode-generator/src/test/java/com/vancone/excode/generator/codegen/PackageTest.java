package com.vancone.excode.generator.codegen;

import com.vancone.excode.generator.constant.UrlPath;
import com.vancone.excode.generator.codegen.controller.ProjectLoader;
import com.vancone.excode.generator.codegen.controller.packager.SpringBootPackager;
import com.vancone.excode.generator.codegen.model.project.Project;
import com.vancone.excode.generator.codegen.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;

@Slf4j
public class PackageTest {

    @Test
    void packageSpringBootModuleOfMall() {

        String mallPath = UrlPath.EXAMPLE_PATH + "mall" + File.separator + "excrud.xml";
        String projectContent = FileUtil.read(mallPath);
        if (projectContent != null) {
            ProjectLoader projectLoader = new ProjectLoader();
            projectLoader.load(projectContent);
            packageSpringBootModule(projectLoader.getProject());
        } else {
            log.error("Failed to package mall project");
        }
    }

    void packageSpringBootModule(Project project) {
        String groupId = project.getGroupId();
        String artifactId = project.getArtifactId();
        String version = project.getVersion();
        SpringBootPackager springBootPackager = new SpringBootPackager(groupId, artifactId, version);
        springBootPackager.build();
    }
}
