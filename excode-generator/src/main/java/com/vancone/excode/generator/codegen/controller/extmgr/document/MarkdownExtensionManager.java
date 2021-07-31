package com.vancone.excode.generator.codegen.controller.extmgr.document;

import com.vancone.excode.generator.codegen.model.project.Project;
import com.vancone.excode.generator.codegen.util.MdUtil;

/**
 * @author Tenton Lien
 */
public class MarkdownExtensionManager {
    public MarkdownExtensionManager(Project project) {
        MdUtil.heading(project.getName().getDefaultValue(), 1);
    }

}
