package com.mekcone.excrud.codegen.controller.extmgr.document;

import com.mekcone.excrud.codegen.model.project.Project;
import com.mekcone.excrud.codegen.util.MdUtil;

public class MarkdownExtensionManager {
    public MarkdownExtensionManager(Project project) {
        MdUtil.heading(project.getName().getDefaultValue(), 1);
    }

}
