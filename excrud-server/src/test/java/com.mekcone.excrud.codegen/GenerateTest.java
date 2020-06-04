package com.mekcone.excrud.codegen;

import com.mekcone.excrud.codegen.constant.ApplicationParameter;
import com.mekcone.excrud.codegen.controller.ProjectLoader;
import com.mekcone.excrud.codegen.util.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.File;

public class GenerateTest {

    private final String examplePath = ApplicationParameter.EXCRUD_HOME + "examples" + File.separator;

    @Test
    void generateMallExample() {
        String mallPath = examplePath + "mall" + File.separator + "excrud.xml";
        generateProject(mallPath);
    }

    void generateProject(String path) {
        String projectContent = FileUtil.read(path);
        if (projectContent != null) {
            ProjectLoader projectLoader = new ProjectLoader();
            projectLoader.load(projectContent);
            projectLoader.generate();
        }
    }
}