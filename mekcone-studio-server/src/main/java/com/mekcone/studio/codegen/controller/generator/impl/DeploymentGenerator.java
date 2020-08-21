package com.mekcone.studio.codegen.controller.generator.impl;

import com.mekcone.studio.codegen.constant.ModuleConstant;
import com.mekcone.studio.codegen.controller.extmgr.deployment.NginxExtensionManager;
import com.mekcone.studio.codegen.controller.generator.Generator;
import com.mekcone.studio.codegen.model.module.Module;
import com.mekcone.studio.codegen.model.module.impl.DeploymentModule;
import com.mekcone.studio.codegen.model.project.Project;
import lombok.extern.slf4j.Slf4j;

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
