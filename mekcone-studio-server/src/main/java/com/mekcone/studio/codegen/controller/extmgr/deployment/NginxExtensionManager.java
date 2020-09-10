package com.mekcone.studio.codegen.controller.extmgr.deployment;

import com.mekcone.studio.codegen.controller.extmgr.ExtensionManager;
import com.mekcone.studio.codegen.controller.generator.impl.DeploymentGenerator;
import com.mekcone.studio.codegen.model.module.Module;
import com.mekcone.studio.codegen.model.project.Project;
import com.mekcone.studio.codegen.model.module.impl.DeploymentModule;

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
