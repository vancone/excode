package com.mekcone.excrud.controller;

import com.mekcone.excrud.annotation.CommandMapping;
import com.mekcone.excrud.service.ProjectService;
import com.mekcone.excrud.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CommandController {

    @Autowired
    private ProjectService projectService;

    @CommandMapping("build")
    public void build() {
        LogUtil.info("build");
    }

    @CommandMapping("gen")
    public void generate() {
        projectService.load(System.getProperty("user.dir") + "/excrud.xml");
        projectService.generate();
    }

    @CommandMapping("*")
    public void showInfo() {
        System.out.println("ExCRUD's CLI tool");
        System.out.println("");
        System.out.println("USAGE:");
        System.out.println("    excrud [OPTIONS] [SUBCOMMAND]");
    }
}
