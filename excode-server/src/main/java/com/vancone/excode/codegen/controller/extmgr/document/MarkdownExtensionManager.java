package com.vancone.excode.codegen.controller.extmgr.document;

import com.vancone.excode.codegen.model.project.Project;
import com.vancone.excode.codegen.util.MdUtil;

public class MarkdownExtensionManager {
    public MarkdownExtensionManager(Project project) {
        MdUtil.heading(project.getName().getDefaultValue(), 1);
    }

}
