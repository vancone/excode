package com.mekcone.excrud.controller.generator.springboot.extension.impl;

import com.mekcone.excrud.constant.extensions.SpringBootExtensionType;
import com.mekcone.excrud.controller.generator.springboot.extension.SpringBootExtensionManager;
import com.mekcone.excrud.model.export.impl.springboot.SpringBootGenModel;
import com.mekcone.excrud.util.LogUtil;

public class LombokExtensionManager extends SpringBootExtensionManager {

    public LombokExtensionManager(SpringBootGenModel springBootGenModel) {
        this.springBootGenModel = springBootGenModel;
        springBootGenModel.getProjectObjectModel().addDependencies(SpringBootExtensionType.LOMBOK);

        // Remove getters and setters of the data class
        for (var entity: springBootGenModel.getEntities()) {
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
