package com.vancone.excode.core.controller.extmgr.document;

import com.vancone.excode.core.model.project.Project;
import com.vancone.excode.core.util.MdUtil;

/**
 * @author Tenton Lien
 */
public class MarkdownExtensionManager {
    public MarkdownExtensionManager(Project project) {
        MdUtil.heading(project.getName().getDefaultValue(), 1);
    }

}
