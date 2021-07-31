package com.vancone.excode.generator.codegen.controller.extmgr.deployment;

import com.vancone.excode.generator.codegen.controller.generator.impl.DeploymentGenerator;
import com.vancone.excode.generator.codegen.model.project.Project;

/**
 * @author Tenton Lien
 */
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
