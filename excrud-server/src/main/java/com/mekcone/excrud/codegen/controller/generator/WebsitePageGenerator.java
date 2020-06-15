package com.mekcone.excrud.codegen.controller.generator;

import com.mekcone.excrud.codegen.controller.parser.template.impl.UniversalTemplate;
import com.mekcone.excrud.codegen.model.module.impl.WebsitePageModule;
import com.mekcone.excrud.codegen.model.project.Project;
import com.mekcone.excrud.codegen.util.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class WebsitePageGenerator extends CommonGenerator {

    public WebsitePageGenerator(Project project) {
        super(project);
    }

    public void generate() {
        String themeType =((WebsitePageModule)module).getTheme().getType();
        String themeId =((WebsitePageModule)module).getTheme().getId();
        File themeTemplatePath = new File(getTemplatePath() + themeType + File.separator + themeId + File.separator);
        log.info("Theme path: {}", themeTemplatePath);
        FileUtil.copyDirectory(themeTemplatePath.toString(), outputPath);

        String indexHtmlPath = outputPath + "index.html";
        UniversalTemplate indexHtmlTemplate = new UniversalTemplate(indexHtmlPath);
        if (indexHtmlTemplate.getTemplate() != null) {
            indexHtmlTemplate.insert("title", ((WebsitePageModule)module).getPageByType("home").getTitle().getDefaultValue());
            FileUtil.write(indexHtmlPath, indexHtmlTemplate.toString());
        } else {
            log.error("Load template \"{}\" failed", indexHtmlPath);
        }
    }
}
