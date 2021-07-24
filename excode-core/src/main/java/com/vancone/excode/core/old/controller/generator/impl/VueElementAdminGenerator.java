package com.vancone.excode.core.old.controller.generator.impl;

import com.vancone.excode.core.old.controller.parser.template.impl.CommonTemplate;
import com.vancone.excode.core.old.model.project.ProjectOld;
import com.vancone.excode.core.old.controller.generator.Generator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * @author Tenton Lien
 */
@Slf4j
public class VueElementAdminGenerator extends Generator {

    public VueElementAdminGenerator(ProjectOld projectOld) {
        super(projectOld);
    }

    public void addPackageJson() {
        CommonTemplate packageJsonTemplate = new CommonTemplate(
                getTemplatePath() + "default" + File.separator + "package.json");
        packageJsonTemplate.insert("title", projectOld.getArtifactId());
        packageJsonTemplate.insert("description", projectOld.getDescription().getDefaultValue());
        packageJsonTemplate.insert("version", projectOld.getVersion());

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
