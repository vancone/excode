package com.vancone.excode.core.old.controller.extmgr.deployment;

import com.vancone.excode.core.old.controller.generator.impl.DeploymentGenerator;
import com.vancone.excode.core.old.model.project.ProjectOld;

/**
 * @author Tenton Lien
 */
public class RedisExtensionManager {
    private final DeploymentGenerator deploymentGenerator;
    private final ProjectOld projectOld;

    public RedisExtensionManager(DeploymentGenerator deploymentGenerator, ProjectOld projectOld) {
        this.deploymentGenerator = deploymentGenerator;
        this.projectOld = projectOld;

        addConf();
    }

    public void addConf() {}

}
