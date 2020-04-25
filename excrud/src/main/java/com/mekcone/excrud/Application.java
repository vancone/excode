package com.mekcone.excrud;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.mekcone.excrud.controller.ProjectLoader;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Slf4j
public class Application {
    @Getter private static String applicationName;
    @Getter private static String applicationOrganization;
    @Getter private static String applicationVersion;
    @Getter private static String description;

    public static void main(String[] args) {
        // Load application.properties
        var properties = new Properties();
        try {
            properties.load(Application.class.getResourceAsStream("/application.properties"));
        } catch (FileNotFoundException e) {
            log.error("File \"application.properties\" not found");
            System.exit(-1);
        } catch (IOException e) {
            log.error("Parse file \"application.properties\" failed");
            System.exit(-1);
        }
        applicationName = properties.getProperty("application.name");
        applicationOrganization = properties.getProperty("application.organization");
        applicationVersion = properties.getProperty("application.version");
        description = "Generated by " + applicationOrganization + " " + applicationName + " at " + new Date();

        var commandArgument = new CommandArgument();
        var jCommander = JCommander.newBuilder()
                .addObject(commandArgument)
                .build();
        jCommander.setProgramName(applicationName.toLowerCase());
        jCommander.parse(args);

        if (commandArgument.isHelp()) {
            jCommander.usage();
            System.exit(0);
        }

        var projectLoader = new ProjectLoader();

        if (commandArgument.isGen()) {
            projectLoader.load(System.getProperty("user.dir") + "/" + applicationName.toLowerCase() + ".xml");
            projectLoader.generate();
        }

        if (commandArgument.isBuild()) {
            projectLoader.build();
        }
    }

    @Data
    static class CommandArgument {
        @Parameter
        private List<String> parameters = new ArrayList<>();

        @Parameter(names={"build"}, description = "Build application")
        private boolean build;

        @Parameter(names={"clean"}, description = "Clean all outputs last time")
        private boolean clean;

        @Parameter(names={"gen"}, description = "Generate applications")
        private boolean gen;

        @Parameter(names={"help"}, description = "Help options", help = true)
        private boolean help;
    }

//    public static String getHomeDirectory() {
//        return System.getenv(applicationName.toUpperCase() + "_HOME");
//    }
}