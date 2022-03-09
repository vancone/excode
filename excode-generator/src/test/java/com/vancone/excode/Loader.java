package com.vancone.excode;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.vancone.excode.generator.ProjectLoader;
import com.vancone.excode.generator.service.ProjectWriterService;
import com.vancone.excode.generator.entity.Project;
import com.vancone.excode.generator.util.FileUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

public class Loader {

    @Test
    public void loadProject() throws JsonProcessingException {

        DataInitializer ini = new DataInitializer();
        ini.importTemplate();

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.getLogger("root").setLevel(Level.toLevel("INFO"));

        Project project = ProjectLoader.parse(FileUtil.read("../examples/mall.json"));
        if (project != null) {
//            ProjectWriterService writer = new ProjectWriterService(project);
//            writer.write(project);
        }
    }
}
