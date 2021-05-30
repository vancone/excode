package com.vancone.devops.codegen.controller.extmgr.deployment;

import com.vancone.devops.codegen.controller.generator.impl.DeploymentGenerator;
import com.vancone.devops.codegen.model.project.Project;

public class RedisExtensionManager {
    private final DeploymentGenerator deploymentGenerator;
    private final Project project;

    public RedisExtensionManager(DeploymentGenerator deploymentGenerator, Project project) {
        this.deploymentGenerator = deploymentGenerator;
        this.project = project;

        addConf();
    }

    public void addConf() {

//        DeploymentModule.Properties.Nginx nginx = project.getModuleSet().getDeploymentModule().getProperties().getNginx();
//        deploymentGenerator.addOutputFile("nginx" + File.separator + "nginx.conf", nginx.toString());
    }

}