package com.mekcone.studio.codegen.controller.extmgr.springboot;

import com.mekcone.studio.constant.ModuleConstant;
import com.mekcone.studio.codegen.model.module.impl.SpringBootModule;
import com.mekcone.studio.codegen.model.file.springboot.SpringBootDataClass;
import com.mekcone.studio.codegen.model.project.Project;
import lombok.extern.slf4j.Slf4j;

/*
 * Author: Tenton Lien
 */

@Slf4j
public class LombokExtensionManager {

    private Project project;

    public LombokExtensionManager(SpringBootModule springBootModule) {
        springBootModule.getMavenProjectObjectModel().addDependencies(ModuleConstant.SPRING_BOOT_EXTENSION_LOMBOK);

        // Remove getters and setters of the data class
        for (SpringBootDataClass entity: springBootModule.getEntities()) {
            entity.setGetterAndSetterAvailable(false);
            entity.getCompilationUnit().addImport("lombok.Data");
            entity.getEntityClassDeclaration().addMarkerAnnotation("Data");
        }
    }
}
