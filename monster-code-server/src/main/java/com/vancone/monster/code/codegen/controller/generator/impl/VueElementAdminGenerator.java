package com.vancone.monster.code.codegen.controller.generator.impl;

import com.vancone.monster.code.codegen.controller.generator.Generator;
import com.vancone.monster.code.codegen.controller.parser.template.impl.CommonTemplate;
import com.vancone.monster.code.codegen.model.project.Project;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

/*
 * Author: Tenton Lien
 */

@Slf4j
public class VueElementAdminGenerator extends Generator {

    public VueElementAdminGenerator(Project project) {
        super(project);
    }

    public void addPackageJson() {
        CommonTemplate packageJsonTemplate = new CommonTemplate(
                getTemplatePath() + "default" + File.separator + "package.json");
        packageJsonTemplate.insert("title", project.getArtifactId());
        packageJsonTemplate.insert("description", project.getDescription().getDefaultValue());
        packageJsonTemplate.insert("version", project.getVersion());

        // Add author
        String author = module.asVueElementAdminModule().getAuthor();
        String email = module.asVueElementAdminModule().getEmail();
        if (StringUtils.isNotBlank(author)) {
            if (StringUtils.isNotBlank(email)) {
                author += " <" + email + ">";
            }
            packageJsonTemplate.insert("author", author);
        } else {
            packageJsonTemplate.remove("author");
        }

        // Add license
        String license = module.asVueElementAdminModule().getLicense();
        packageJsonTemplate.insert("license", license);

        addOutputFile("package.json", packageJsonTemplate.toString());
    }

    @Override
    public void generate() {
        addPackageJson();
        write();
    }
}
