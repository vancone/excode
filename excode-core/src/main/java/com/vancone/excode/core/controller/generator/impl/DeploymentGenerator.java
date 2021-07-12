package com.vancone.excode.core.controller.generator.impl;

import com.vancone.excode.core.constant.ModuleConstant;
import com.vancone.excode.core.controller.extmgr.deployment.NginxExtensionManager;
import com.vancone.excode.core.model.module.Module;
import com.vancone.excode.core.model.module.impl.DeploymentModule;
import com.vancone.excode.core.model.project.Project;
import com.vancone.excode.core.controller.generator.Generator;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Tenton Lien
 */
@Slf4j
public class DeploymentGenerator extends Generator {
    public DeploymentGenerator(Project project) {
        super(project);
    }

    @Override
    public void generate() {
        for (DeploymentModule.OperatingSystem operatingSystem: module.asDeploymentModule().getOperatingSystems()) {
            addDockerInstallScript(operatingSystem);
        }

        for (Module.Extension extension: module.asDeploymentModule().getExtensions()) {
            if (!extension.isUse()) {
                continue;
            }
            switch (extension.getId()) {
                case ModuleConstant.DEPLOYMENT_EXTENSION_MARIADB:
                    break;
                case ModuleConstant.DEPLOYMENT_EXTENSION_NGINX:
                    NginxExtensionManager nginxExtensionManager = new NginxExtensionManager(this, project);
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
