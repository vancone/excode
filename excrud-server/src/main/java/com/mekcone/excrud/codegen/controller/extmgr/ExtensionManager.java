package com.mekcone.excrud.codegen.controller.extmgr;

import com.mekcone.excrud.codegen.controller.generator.Generator;

public class ExtensionManager {
    private Generator generator;

    public ExtensionManager() {
        String callerClassName = Thread.currentThread().getStackTrace()[3].getClassName();
        System.out.println(callerClassName);
    }
}
