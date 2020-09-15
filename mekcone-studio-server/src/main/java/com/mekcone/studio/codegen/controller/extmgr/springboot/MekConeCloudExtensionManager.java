package com.mekcone.studio.codegen.controller.extmgr.springboot;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.mekcone.studio.constant.ModuleConstant;
import com.mekcone.studio.codegen.controller.parser.PropertiesParser;
import com.mekcone.studio.codegen.controller.parser.template.impl.JavaTemplate;
import com.mekcone.studio.codegen.model.module.impl.SpringBootModule;
import com.mekcone.studio.codegen.model.project.Project;
import com.mekcone.studio.codegen.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/*
 * Author: Tenton Lien
 * Date: 6/21/2020
 */

@Slf4j
public class MekConeCloudExtensionManager {

    private Project project;
    private SpringBootModule springBootModule;

    public MekConeCloudExtensionManager(Project project) {
        this.project = project;
        this.springBootModule = project.getModuleSet().getSpringBootModule();
        project.getModuleSet().getSpringBootModule().getMavenProjectObjectModel().addDependencies(ModuleConstant.SPRING_BOOT_EXTENSION_MEKCONE_CLOUD);
        modifyApplicationEntry();
        addProperties();
    }

    private void modifyApplicationEntry() {
        JavaTemplate javaTemplate = springBootModule.getApplicationEntry();
        ClassOrInterfaceDeclaration mainClass = javaTemplate.getCompilationUnit().getClassByName(StrUtil.upperCamelCase(project.getArtifactId()) + "Application").get();
        javaTemplate.getCompilationUnit().addImport("org.springframework.cloud.netflix.eureka.EnableEurekaClient");
        mainClass.addAnnotation("EnableEurekaClient");
    }

    private void addProperties() {
        PropertiesParser propertiesParser = springBootModule.getApplicationPropertiesParser();

        // Set application name
        String applicationName = springBootModule.getProperties().get(ModuleConstant.SPRING_BOOT_PROPERTY_APPLICATION_NAME);
        if (StringUtils.isNotBlank(applicationName)) {
            propertiesParser.add("spring.application.name", applicationName);
        } else {
            propertiesParser.add("spring.application.name", project.getArtifactId());
        }

        // Set Eureka URL
        String currentMode = springBootModule.getExtensions().getMekConeCloud().getMode();
        String urlString = "";
        List<SpringBootModule.SpringBootExtensions.MekConeCloud.Router.Node> nodes = springBootModule.getExtensions().getMekConeCloud().getRouter().getNodes();
        for (int i = 0; i < nodes.size(); i ++) {
            if (nodes.get(i).getMode().equals(currentMode)) {
                urlString += "http://" + nodes.get(i).getAddress() + ":6600/eureka";
            } else {
                continue;
            }
            if (i + 1 != nodes.size()) {
                if (nodes.get(i + 1).getMode().equals(currentMode)) {
                    urlString += ", ";
                }
            }
        }
        propertiesParser.add("eureka.client.serviceUrl.defaultZone", urlString);
    }
}
