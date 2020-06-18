package com.mekcone.excrud.codegen.controller.generator.impl;

import com.mekcone.excrud.codegen.constant.ModuleConstant;
import com.mekcone.excrud.codegen.controller.generator.Generator;
import com.mekcone.excrud.codegen.model.module.impl.DeploymentModule;
import com.mekcone.excrud.codegen.model.project.Project;
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
