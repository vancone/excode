package com.vancone.excode.core.old.controller.generator.impl;

import com.vancone.excode.core.old.controller.generator.Generator;
import com.vancone.excode.core.old.controller.parser.template.impl.CommonTemplate;
import com.vancone.excode.core.old.model.project.ProjectOld;
import com.vancone.excode.core.util.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * @author Tenton Lien
 */
@Slf4j
public class WebsitePageGenerator extends Generator {

    public WebsitePageGenerator(ProjectOld projectOld) {
        super(projectOld);
    }

    public void generate() {
        String themeType = module.asWebsitePageModule().getTheme().getType();
        String themeId = module.asWebsitePageModule().getTheme().getId();
        File themeTemplatePath = new File(getTemplatePath() + themeType + File.separator + themeId + File.separator);
        log.info("Theme path: {}", themeTemplatePath);
        FileUtil.copyDirectory(themeTemplatePath.toString(), outputPath);

        String indexHtmlPath = outputPath + "index.html";
        CommonTemplate indexHtmlTemplate = new CommonTemplate(indexHtmlPath);
        if (indexHtmlTemplate.getTemplate() != null) {
            indexHtmlTemplate.insert("title", module.asWebsitePageModule().getPageByType("home").getTitle().getDefaultValue());
            FileUtil.write(indexHtmlPath, indexHtmlTemplate.toString());
        } else {
            log.error("Load template \"{}\" failed", indexHtmlPath);
        }
    }
}
