package com.mekcone.excrud.codegen.controller.extmgr.deployment;

import com.mekcone.excrud.codegen.controller.extmgr.ExtensionManager;
import com.mekcone.excrud.codegen.controller.generator.impl.DeploymentGenerator;
import com.mekcone.excrud.codegen.model.module.impl.DeploymentModule.Properties.Nginx;
import com.mekcone.excrud.codegen.model.project.Project;

import java.io.File;

public class NginxExtensionManager extends ExtensionManager {

    private final DeploymentGenerator deploymentGenerator;
    private final Project project;

    public NginxExtensionManager(DeploymentGenerator deploymentGenerator, Project project) {
        this.deploymentGenerator = deploymentGenerator;
        this.project = project;

        addConf();
    }

    public void addConf() {
        Nginx nginx = project.getModuleSet().getDeploymentModule().getProperties().getNginx();
        deploymentGenerator.addOutputFile("nginx" + File.separator + "nginx.conf", nginx.toString());
    }

}
