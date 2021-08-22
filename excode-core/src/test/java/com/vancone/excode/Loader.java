package com.vancone.excode;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.vancone.excode.core.ProjectLoader;
import com.vancone.excode.core.ProjectWriter;
import com.vancone.excode.core.model.Project;
import com.vancone.excode.core.util.FileUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

public class Loader {

    @Test
    public void loadProject() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.getLogger("root").setLevel(Level.toLevel("INFO"));

        Project project = ProjectLoader.parse(FileUtil.read("../examples/mall.json"));
        if (project != null) {
            ProjectWriter writer = new ProjectWriter(project);
            writer.write();
        }
    }
}
