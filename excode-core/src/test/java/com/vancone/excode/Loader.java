package com.vancone.excode;

import com.vancone.excode.core.ProjectLoader;
import com.vancone.excode.core.ProjectWriter;
import com.vancone.excode.core.model.Project;
import com.vancone.excode.core.util.FileUtil;
import org.junit.jupiter.api.Test;

public class Loader {

    @Test
    public void loadProject() {
        Project project = ProjectLoader.parse(FileUtil.read("../examples/mall/excode.json"));
        if (project != null) {
            ProjectWriter writer = new ProjectWriter(project);
            writer.write();
        }
    }
}
