package com.vancone.monster.code.codegen.controller.extmgr.document;

import com.vancone.monster.code.codegen.model.project.Project;
import com.vancone.monster.code.codegen.util.MdUtil;

public class MarkdownExtensionManager {
    public MarkdownExtensionManager(Project project) {
        MdUtil.heading(project.getName().getDefaultValue(), 1);
    }

}
