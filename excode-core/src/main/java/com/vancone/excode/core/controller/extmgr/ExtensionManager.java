package com.vancone.excode.core.controller.extmgr;

import com.vancone.excode.core.controller.generator.Generator;

public class ExtensionManager {
    private Generator generator;

    public ExtensionManager() {
        String callerClassName = Thread.currentThread().getStackTrace()[3].getClassName();
        System.out.println(callerClassName);
    }
}