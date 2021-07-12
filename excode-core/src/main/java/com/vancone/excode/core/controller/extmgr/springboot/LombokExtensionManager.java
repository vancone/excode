package com.vancone.excode.core.controller.extmgr.springboot;

import com.vancone.excode.core.constant.ModuleConstant;
import com.vancone.excode.core.model.file.springboot.SpringBootDataClass;
import com.vancone.excode.core.model.module.impl.SpringBootModule;
import com.vancone.excode.core.model.project.Project;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Tenton Lien
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
