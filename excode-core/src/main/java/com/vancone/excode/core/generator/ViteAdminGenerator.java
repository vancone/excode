package com.vancone.excode.core.generator;

import com.vancone.excode.core.ProjectWriter;
import com.vancone.excode.core.TemplateFactory;
import com.vancone.excode.core.enums.TemplateType;
import com.vancone.excode.core.model.Module;
import com.vancone.excode.core.model.Project;
import com.vancone.excode.core.model.Template;

import java.io.File;
import java.util.List;

/**
 * @author Tenton Lien
 * @since 7/25/2021
 */
public class ViteAdminGenerator {
    private ProjectWriter writer;
    private Project project;
    private Module module;

    public static void generate(Module module, ProjectWriter writer) {
        ViteAdminGenerator generator = new ViteAdminGenerator();
        generator.writer = writer;
        generator.project = writer.getProject();
        generator.module = module;

        List<Template> basicTemplates = TemplateFactory.getTemplatesByModuleName("vite-admin");

        // Process static templates
        for (Template template: basicTemplates) {
            if (template.getType().equals(TemplateType.STATIC_TEMPLATE)) {
                writer.addOutput(template.getType(),
                        module.getName() + File.separator + template.getFileName(),
                        template);
            }
        }

        // Process dynamic templates
        generator.createPackageJson();
    }

    public static void build() {

    }

    public void createPackageJson() {
        Template template = TemplateFactory.getTemplate(TemplateType.VITE_ADMIN_PACKAGE_JSON);
        template.replace("name", project.getArtifactId());
        template.replace("version", project.getVersion());
        writer.addOutput(TemplateType.VITE_ADMIN_PACKAGE_JSON, module.getName() + File.separator + "package.json", template);
    }
}
