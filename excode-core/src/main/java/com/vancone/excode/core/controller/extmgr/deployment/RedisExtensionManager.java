package com.vancone.excode.core.controller.extmgr.deployment;

import com.vancone.excode.core.controller.generator.impl.DeploymentGenerator;
import com.vancone.excode.core.model.project.Project;

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

    public void addConf() {}

}
