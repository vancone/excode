package com.mekcone.excrud.codegen.controller.generator.impl;

import com.mekcone.excrud.codegen.controller.generator.Generator;
import com.mekcone.excrud.codegen.controller.parser.template.impl.CommonTemplate;
import com.mekcone.excrud.codegen.model.module.impl.VueElementAdminModule;
import com.mekcone.excrud.codegen.model.project.Project;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

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
        if (author != null && !author.isEmpty()) {
            if (email != null && !email.isEmpty()) {
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