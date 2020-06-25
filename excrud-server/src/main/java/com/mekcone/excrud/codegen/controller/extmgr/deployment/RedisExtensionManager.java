package com.mekcone.excrud.codegen.controller.extmgr.deployment;

import com.mekcone.excrud.codegen.controller.generator.impl.DeploymentGenerator;
import com.mekcone.excrud.codegen.model.project.Project;

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
