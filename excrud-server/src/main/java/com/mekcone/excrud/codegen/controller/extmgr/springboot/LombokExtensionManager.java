package com.mekcone.excrud.codegen.controller.extmgr.springboot;

import com.mekcone.excrud.codegen.constant.ModuleExtensionType;
import com.mekcone.excrud.codegen.model.module.impl.springboot.SpringBootModule;
import com.mekcone.excrud.codegen.model.module.impl.springboot.component.SpringBootDataClass;
import com.mekcone.excrud.codegen.model.project.Project;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LombokExtensionManager {

    private Project project;

    public LombokExtensionManager(SpringBootModule springBootModule) {
        springBootModule.getProjectObjectModel().addDependencies(ModuleExtensionType.LOMBOK);

        // Remove getters and setters of the data class
        for (SpringBootDataClass entity: springBootModule.getEntities()) {
            entity.setGetterAndSetterAvailable(false);
            entity.getCompilationUnit().addImport("lombok.Data");
            entity.getEntityClassDeclaration().addMarkerAnnotation("Data");
        }

        log.info("Execute extension Lombok complete");
    }
}
