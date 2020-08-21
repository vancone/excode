package com.mekcone.studio.codegen.controller.extmgr.document;

import com.mekcone.studio.codegen.model.project.Project;
import com.mekcone.studio.codegen.util.MdUtil;

public class MarkdownExtensionManager {
    public MarkdownExtensionManager(Project project) {
        MdUtil.heading(project.getName().getDefaultValue(), 1);
    }

}
