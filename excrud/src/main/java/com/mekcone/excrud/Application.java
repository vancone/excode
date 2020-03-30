package com.mekcone.excrud;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.mekcone.excrud.loader.ProjectLoader;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class Application {
    public static void main(String[] args) {
        CommandArgument commandArgument = new CommandArgument();

        JCommander jCommander = JCommander.newBuilder()
                .addObject(commandArgument)
                .build();

        jCommander.setProgramName("excrud");
        jCommander.parse(args);

        if (commandArgument.isGen()) {
            ProjectLoader projectLoader = new ProjectLoader();
            projectLoader.load(System.getProperty("user.dir") + "/excrud.xml");
            projectLoader.generate();
        }

        if (commandArgument.isHelp()) {
            jCommander.usage();
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

    public static String getHomeDirectory() {
        return System.getenv("EXCRUD_HOME");
    }
}