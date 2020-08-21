package com.mekcone.studio.codegen.controller.extmgr;

import com.mekcone.studio.codegen.controller.generator.Generator;

public class ExtensionManager {
    private Generator generator;

    public ExtensionManager() {
        String callerClassName = Thread.currentThread().getStackTrace()[3].getClassName();
        System.out.println(callerClassName);
    }
}
