package com.vancone.devops.codegen.controller.extmgr.deployment;

import com.vancone.devops.codegen.controller.generator.impl.DeploymentGenerator;
import com.vancone.devops.codegen.model.project.Project;
import com.vancone.devops.codegen.controller.extmgr.ExtensionManager;
import com.vancone.devops.codegen.model.module.impl.DeploymentModule;

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
        DeploymentModule.DeploymentExtensions.Nginx nginx = project.getModuleSet().getDeploymentModule().getExtensions().getNginx();
        deploymentGenerator.addOutputFile("nginx" + File.separator + "nginx.conf", nginx.toString());
    }

}
