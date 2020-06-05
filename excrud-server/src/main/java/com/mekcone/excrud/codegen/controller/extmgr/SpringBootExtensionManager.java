package com.mekcone.excrud.codegen.controller.extmgr;

import com.mekcone.excrud.codegen.model.project.Project;

public abstract class SpringBootExtensionManager {

    protected Project project;

    public abstract void addConfig();
}
