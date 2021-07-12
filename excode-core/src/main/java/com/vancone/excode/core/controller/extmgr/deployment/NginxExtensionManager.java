package com.vancone.excode.core.controller.extmgr.deployment;

import com.vancone.excode.core.controller.extmgr.ExtensionManager;
import com.vancone.excode.core.model.module.impl.DeploymentModule;
import com.vancone.excode.core.model.project.Project;
import com.vancone.excode.core.controller.generator.impl.DeploymentGenerator;

import java.io.File;

/**
 * @author Tenton Lien
 */
public class NginxExtensionManager extends ExtensionManager {

    private final DeploymentGenerator deploymentGenerator;
    private final Project project;

    public NginxExtensionManager(DeploymentGenerator deploymentGenerator, Project project) {
        this.deploymentGenerator = deploymentGenerator;
        this.project = project;

        addConf();
    }

    public void addConf() {
        DeploymentModule.Properties.Nginx nginx = project.getModuleSet().getDeploymentModule().getProperties().getNginx();
        deploymentGenerator.addOutputFile("nginx" + File.separator + "nginx.conf", nginx.toString());
    }

}
