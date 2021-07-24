package com.vancone.excode;

import com.vancone.excode.core.old.constant.UrlPath;
import com.vancone.excode.core.old.ProjectLoaderOld;
import com.vancone.excode.core.old.controller.packager.SpringBootPackager;
import com.vancone.excode.core.old.model.project.ProjectOld;
import com.vancone.excode.core.util.FileUtil;
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
            ProjectLoaderOld projectLoaderOld = new ProjectLoaderOld();
            projectLoaderOld.load(projectContent);
            packageSpringBootModule(projectLoaderOld.getProjectOld());
        } else {
            log.error("Failed to package mall project");
        }
    }

    void packageSpringBootModule(ProjectOld projectOld) {
        String groupId = projectOld.getGroupId();
        String artifactId = projectOld.getArtifactId();
        String version = projectOld.getVersion();
        SpringBootPackager springBootPackager = new SpringBootPackager(groupId, artifactId, version);
        springBootPackager.build();
    }
}
