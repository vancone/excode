package com.vancone.devops.codegen.controller.extmgr.document;

import com.vancone.devops.codegen.model.project.Project;
import com.vancone.devops.codegen.util.MdUtil;

public class MarkdownExtensionManager {
    public MarkdownExtensionManager(Project project) {
        MdUtil.heading(project.getName().getDefaultValue(), 1);
    }

}
