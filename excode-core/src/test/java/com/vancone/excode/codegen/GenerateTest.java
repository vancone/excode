package com.vancone.excode.codegen;

import com.vancone.excode.core.constant.UrlPath;
import com.vancone.excode.core.controller.ProjectLoader;
import com.vancone.excode.core.util.FileUtil;
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