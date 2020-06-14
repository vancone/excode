package com.mekcone.excrud.codegen.controller.generator;

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
        String themeId =((WebsitePageModule)module).getTheme().getId();
        File themeTemplatePath = new File(getTemplatePath() + themeId + File.separator);
        log.info("Theme path: {}", themeTemplatePath);
        FileUtil.copyDirectory(themeTemplatePath.toString(), outputPath);
    }
}
