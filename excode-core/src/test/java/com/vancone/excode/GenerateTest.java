package com.vancone.excode;

import com.vancone.excode.core.old.constant.UrlPath;
import com.vancone.excode.core.old.ProjectLoaderOld;
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
            ProjectLoaderOld projectLoaderOld = new ProjectLoaderOld();
            projectLoaderOld.load(projectContent);
            projectLoaderOld.generate();
        } else {
            log.error("Project file not found: {}", path);
        }
    }
}