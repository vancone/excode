package com.mekcone.excrud.codegen.controller.generator.springboot.extension;

import com.mekcone.excrud.codegen.model.module.impl.springboot.SpringBootModule;

public abstract class SpringBootExtensionManager {
    protected SpringBootModule springBootGenModel;

    public abstract void addConfig();
}
