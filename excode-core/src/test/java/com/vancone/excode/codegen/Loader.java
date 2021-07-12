package com.vancone.excode.codegen;

import com.vancone.excode.core.controller.ProjectLoader;
import com.vancone.excode.core.util.FileUtil;
import org.junit.jupiter.api.Test;

public class Loader {

    @Test
    public void loadProject() {
        ProjectLoader projectLoader = new ProjectLoader();
        projectLoader.load(FileUtil.read("../examples/mall/excode.xml"));
        projectLoader.generate();
    }

}
