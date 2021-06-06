package com.vancone.excode.codegen.controller.extmgr.deployment;

import com.vancone.excode.codegen.controller.generator.impl.DeploymentGenerator;
import com.vancone.excode.codegen.model.project.Project;
import com.vancone.excode.codegen.controller.extmgr.ExtensionManager;
import com.vancone.excode.codegen.model.module.impl.DeploymentModule;

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
        DeploymentModule.DeploymentExtensions.Nginx nginx = project.getModuleSet().getDeploymentModule().getExtensions().getNginx();
        deploymentGenerator.addOutputFile("nginx" + File.separator + "nginx.conf", nginx.toString());
    }

}
