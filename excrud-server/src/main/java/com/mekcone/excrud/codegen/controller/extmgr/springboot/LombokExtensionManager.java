package com.mekcone.excrud.codegen.controller.extmgr.springboot;

import com.mekcone.excrud.codegen.constant.ModuleExtension;
import com.mekcone.excrud.codegen.controller.extmgr.SpringBootExtensionManager;
import com.mekcone.excrud.codegen.model.module.impl.springboot.SpringBootModule;
import com.mekcone.excrud.codegen.model.module.impl.springboot.component.SpringBootDataClass;
import com.mekcone.excrud.codegen.util.LogUtil;

public class LombokExtensionManager extends SpringBootExtensionManager {

    public LombokExtensionManager(SpringBootModule springBootModule) {
        springBootModule.getProjectObjectModel().addDependencies(ModuleExtension.LOMBOK);

        // Remove getters and setters of the data class
        for (SpringBootDataClass entity: springBootModule.getEntities()) {
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
