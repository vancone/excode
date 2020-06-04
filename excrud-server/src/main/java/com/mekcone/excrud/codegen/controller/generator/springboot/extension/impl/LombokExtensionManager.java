package com.mekcone.excrud.codegen.controller.generator.springboot.extension.impl;

import com.mekcone.excrud.codegen.constant.ModuleExtensionType;
import com.mekcone.excrud.codegen.controller.generator.springboot.extension.SpringBootExtensionManager;
import com.mekcone.excrud.codegen.model.module.impl.springboot.SpringBootModule;
import com.mekcone.excrud.codegen.model.module.impl.springboot.component.SpringBootDataClass;
import com.mekcone.excrud.codegen.util.LogUtil;

public class LombokExtensionManager extends SpringBootExtensionManager {

    public LombokExtensionManager(SpringBootModule springBootGenModel) {
        this.springBootGenModel = springBootGenModel;
        springBootGenModel.getProjectObjectModel().addDependencies(ModuleExtensionType.LOMBOK);

        // Remove getters and setters of the data class
        for (SpringBootDataClass entity: springBootGenModel.getEntities()) {
            entity.setGetterAndSetterAvailable(false);
            entity.getCompilationUnit().addImport("lombok.Data");
            entity.getEntityClassDeclaration().addMarkerAnnotation("Data");
        }

        LogUtil.info("Execute extension Lombok complete");
    }

    @Override
    public void addConfig() {

    }
}
