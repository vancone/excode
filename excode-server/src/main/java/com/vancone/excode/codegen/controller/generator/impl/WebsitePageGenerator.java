package com.vancone.excode.codegen.controller.generator.impl;

import com.vancone.excode.codegen.model.project.Project;
import com.vancone.excode.codegen.controller.generator.Generator;
import com.vancone.excode.codegen.controller.parser.template.impl.CommonTemplate;
import com.vancone.excode.codegen.util.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * @author Tenton Lien
 */
@Slf4j
public class WebsitePageGenerator extends Generator {

    public WebsitePageGenerator(Project project) {
        super(project);
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
