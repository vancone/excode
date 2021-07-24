package com.vancone.excode.core.old.controller.generator.impl;

import com.vancone.excode.core.old.constant.ModuleConstant;
import com.vancone.excode.core.old.controller.extmgr.deployment.NginxExtensionManager;
import com.vancone.excode.core.old.model.module.ModuleOld;
import com.vancone.excode.core.old.model.module.impl.DeploymentModule;
import com.vancone.excode.core.old.model.project.ProjectOld;
import com.vancone.excode.core.old.controller.generator.Generator;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Tenton Lien
 */
@Slf4j
public class DeploymentGenerator extends Generator {
    public DeploymentGenerator(ProjectOld projectOld) {
        super(projectOld);
    }

    @Override
    public void generate() {
        for (DeploymentModule.OperatingSystem operatingSystem: module.asDeploymentModule().getOperatingSystems()) {
            addDockerInstallScript(operatingSystem);
        }

        for (ModuleOld.Extension extension: module.asDeploymentModule().getExtensions()) {
            if (!extension.isUse()) {
                continue;
            }
            switch (extension.getId()) {
                case ModuleConstant.DEPLOYMENT_EXTENSION_MARIADB:
                    break;
                case ModuleConstant.DEPLOYMENT_EXTENSION_NGINX:
                    NginxExtensionManager nginxExtensionManager = new NginxExtensionManager(this, projectOld);
                    break;
            }
        }
        write();
    }

    private void addDockerInstallScript(DeploymentModule.OperatingSystem operatingSystem) {
        switch (operatingSystem.getType()) {
            case ModuleConstant.DEPLOYMENT_OPERATING_SYSTEM_UBUNTU:
                log.info("docker ubuntu");
                break;
            case ModuleConstant.DEPLOYMENT_OPERATING_SYSTEM_CENTOS:
                log.info("docker centos");
                break;
            default:
                log.warn("docker nothing");
        }
    }

    private void addAllInOneScript() {

    }
}
