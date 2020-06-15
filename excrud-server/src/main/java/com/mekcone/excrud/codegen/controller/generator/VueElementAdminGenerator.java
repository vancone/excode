package com.mekcone.excrud.codegen.controller.generator;

import com.mekcone.excrud.codegen.controller.parser.template.impl.UniversalTemplate;
import com.mekcone.excrud.codegen.model.module.impl.VueElementAdminModule;
import com.mekcone.excrud.codegen.model.project.Project;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class VueElementAdminGenerator extends CommonGenerator {

    public VueElementAdminGenerator(Project project) {
        super(project);
    }

    public void addPackageJson() {
        UniversalTemplate packageJsonTemplate = new UniversalTemplate(
                getTemplatePath() + "default" + File.separator + "package.json");
        packageJsonTemplate.insert("title", project.getArtifactId());
        packageJsonTemplate.insert("description", project.getDescription().getDefaultValue());
        packageJsonTemplate.insert("version", project.getVersion());

        // Add author
        String author = ((VueElementAdminModule)module).getAuthor();
        String email = ((VueElementAdminModule)module).getEmail();
        if (author != null && !author.isEmpty()) {
            if (email != null && !email.isEmpty()) {
                author += " <" + email + ">";
            }
            packageJsonTemplate.insert("author", author);
        } else {
            packageJsonTemplate.remove("author");
        }

        // Add license
        String license = ((VueElementAdminModule)module).getLicense();
        packageJsonTemplate.insert("license", license);

        addOutputFile("package.json", packageJsonTemplate.toString());
    }

    @Override
    public void generate() {
        addPackageJson();
        write();
    }
}
