package com.vancone.monster.code.codegen.controller.generator.impl;

import com.vancone.monster.code.constant.ModuleConstant;
import com.vancone.monster.code.codegen.controller.extmgr.deployment.NginxExtensionManager;
import com.vancone.monster.code.codegen.controller.generator.Generator;
import com.vancone.monster.code.codegen.model.module.Module;
import com.vancone.monster.code.codegen.model.module.impl.DeploymentModule;
import com.vancone.monster.code.codegen.model.project.Project;
import lombok.extern.slf4j.Slf4j;

/*
 * Author: Tenton Lien
 * Date: 6/15/2020
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

        /*DeploymentModule.DeploymentExtensions deploymentExtensions = module.asDeploymentModule().getExtensions();
        if (deploymentExtensions.getNginx() != null && deploymentExtensions.getNginx().isUse()) {
            NginxExtensionManager nginxExtensionManager = new NginxExtensionManager(this, project);
        }*/

        for (Module.Extension extension: module.asDeploymentModule().getExtensions().asList()) {
            if (extension == null || !extension.isUse()) {
                continue;
            }

            if (extension instanceof DeploymentModule.DeploymentExtensions.Nginx) {
                NginxExtensionManager nginxExtensionManager = new NginxExtensionManager(this, project);
            }

            // Processing other extensions
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
