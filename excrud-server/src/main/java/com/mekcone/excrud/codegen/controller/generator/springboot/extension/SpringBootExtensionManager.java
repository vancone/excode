package com.mekcone.excrud.codegen.controller.generator.springboot.extension;

import com.mekcone.excrud.codegen.model.export.impl.springboot.SpringBootGenModel;

public abstract class SpringBootExtensionManager {
    protected SpringBootGenModel springBootGenModel;

    public abstract void addConfig();
}
