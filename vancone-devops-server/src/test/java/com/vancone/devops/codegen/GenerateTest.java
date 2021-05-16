package com.vancone.devops.codegen;

import com.vancone.devops.constant.UrlPath;
import com.vancone.devops.codegen.controller.ProjectLoader;
import com.vancone.devops.codegen.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;

@Slf4j
public class GenerateTest {

    @Test
    void generateMallExample() {
        String mallPath = UrlPath.EXAMPLE_PATH + "mall" + File.separator + "excrud.xml";
        generateProject(mallPath);
    }

    @Test
    void showThirdPartyComponents() {
        log.info("\n{}", new ProjectLoader().showThirdPartyComponentList());
    }

    void generateProject(String path) {
        String projectContent = FileUtil.read(path);
        if (projectContent != null) {
            ProjectLoader projectLoader = new ProjectLoader();
            projectLoader.load(projectContent);
            projectLoader.generate();
        } else {
            log.error("Project file not found: {}", path);
        }
    }
}