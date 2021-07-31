package com.vancone.excode.generator.codegen.controller.extmgr.springboot;

import com.vancone.excode.generator.codegen.model.project.Project;
import com.vancone.excode.generator.constant.ModuleConstant;
import com.vancone.excode.generator.codegen.model.module.impl.SpringBootModule;
import com.vancone.excode.generator.codegen.model.file.springboot.SpringBootDataClass;
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
