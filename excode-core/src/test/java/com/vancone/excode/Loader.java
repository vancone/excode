package com.vancone.excode;

import com.vancone.excode.core.ProjectLoader;
import com.vancone.excode.core.ProjectWriter;
import com.vancone.excode.core.old.ProjectLoaderOld;
import com.vancone.excode.core.model.Project;
import com.vancone.excode.core.util.FileUtil;
import org.junit.jupiter.api.Test;

public class Loader {

    @Test
    public void loadProjectOld() {
        ProjectLoaderOld projectLoaderOld = new ProjectLoaderOld();
        projectLoaderOld.load(FileUtil.read("../examples/mall/excode.xml"));
        projectLoaderOld.generate();
    }


    @Test
    public void loadProject() {
        Project project = ProjectLoader.parse(FileUtil.read("../examples/mall/excode.json"));
        if (project != null) {
            ProjectWriter writer = new ProjectWriter(project);
            writer.write();
        }
    }
}
