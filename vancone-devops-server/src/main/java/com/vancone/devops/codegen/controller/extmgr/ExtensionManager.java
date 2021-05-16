package com.vancone.devops.codegen.controller.extmgr;

import com.vancone.devops.codegen.controller.generator.Generator;

/*
 * Author: Tenton Lien
 * Date: 6/25/2020
 */

public class ExtensionManager {
    private Generator generator;

    public ExtensionManager() {
        String callerClassName = Thread.currentThread().getStackTrace()[3].getClassName();
        System.out.println(callerClassName);
    }
}
