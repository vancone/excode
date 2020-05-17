package com.mekcone.excrud.controller.generator.springboot.extension;

import com.mekcone.excrud.model.export.impl.springboot.SpringBootGenModel;

public abstract class SpringBootExtensionManager {
    protected SpringBootGenModel springBootGenModel;

    public abstract void addConfig();
}
