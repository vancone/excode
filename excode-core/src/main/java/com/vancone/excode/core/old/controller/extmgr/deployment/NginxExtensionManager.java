package com.vancone.excode.core.old.controller.extmgr.deployment;

import com.vancone.excode.core.old.controller.extmgr.ExtensionManager;
import com.vancone.excode.core.old.model.module.impl.DeploymentModule;
import com.vancone.excode.core.old.model.project.ProjectOld;
import com.vancone.excode.core.old.controller.generator.impl.DeploymentGenerator;

import java.io.File;

/**
 * @author Tenton Lien
 */
public class NginxExtensionManager extends ExtensionManager {

    private final DeploymentGenerator deploymentGenerator;
    private final ProjectOld projectOld;

    public NginxExtensionManager(DeploymentGenerator deploymentGenerator, ProjectOld projectOld) {
        this.deploymentGenerator = deploymentGenerator;
        this.projectOld = projectOld;

        addConf();
    }

    public void addConf() {
        DeploymentModule.Properties.Nginx nginx = projectOld.getModuleSet().getDeploymentModule().getProperties().getNginx();
        deploymentGenerator.addOutputFile("nginx" + File.separator + "nginx.conf", nginx.toString());
    }

}
