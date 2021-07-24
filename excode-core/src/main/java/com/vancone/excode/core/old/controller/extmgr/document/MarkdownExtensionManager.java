package com.vancone.excode.core.old.controller.extmgr.document;

import com.vancone.excode.core.old.model.project.ProjectOld;
import com.vancone.excode.core.util.MdUtil;

/**
 * @author Tenton Lien
 */
public class MarkdownExtensionManager {
    public MarkdownExtensionManager(ProjectOld projectOld) {
        MdUtil.heading(projectOld.getName().getDefaultValue(), 1);
    }

}
