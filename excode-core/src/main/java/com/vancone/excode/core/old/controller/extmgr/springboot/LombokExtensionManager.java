package com.vancone.excode.core.old.controller.extmgr.springboot;

import com.vancone.excode.core.old.constant.ModuleConstant;
import com.vancone.excode.core.model.SpringBootDataClass;
import com.vancone.excode.core.old.model.module.impl.SpringBootModule;
import com.vancone.excode.core.old.model.project.ProjectOld;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Tenton Lien
 */
@Slf4j
public class LombokExtensionManager {

    private ProjectOld projectOld;

    public LombokExtensionManager(SpringBootModule springBootModule) {
        springBootModule.getMavenPom().addDependenciesOld(ModuleConstant.SPRING_BOOT_EXTENSION_LOMBOK);

        // Remove getters and setters of the data class
        for (SpringBootDataClass entity: springBootModule.getEntities()) {
            entity.setGetterAndSetterAvailable(false);
            entity.getCompilationUnit().addImport("lombok.Data");
            entity.getEntityClassDeclaration().addMarkerAnnotation("Data");
        }
    }
}
