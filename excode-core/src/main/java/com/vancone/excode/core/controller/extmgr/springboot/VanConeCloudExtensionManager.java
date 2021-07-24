package com.vancone.excode.core.controller.extmgr.springboot;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.vancone.excode.core.constant.ModuleConstant;
import com.vancone.excode.core.controller.parser.PropertiesParser;
import com.vancone.excode.core.controller.parser.template.impl.JavaTemplate;
import com.vancone.excode.core.model.module.impl.SpringBootModule;
import com.vancone.excode.core.model.module.impl.SpringBootModule.SpringBootProperties.VanConeCloud.Router.Node;
import com.vancone.excode.core.model.project.Project;
import com.vancone.excode.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author Tenton Lien
 */
@Slf4j
public class VanConeCloudExtensionManager {

    private Project project;
    private SpringBootModule springBootModule;

    public VanConeCloudExtensionManager(Project project) {
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
        String applicationName = springBootModule.getProperties().getApplicationName();
        if (StringUtils.isNotBlank(applicationName)) {
            propertiesParser.add("spring.application.name", applicationName);
        } else {
            propertiesParser.add("spring.application.name", project.getArtifactId());
        }

        // Set Eureka URL
        String currentMode = springBootModule.getProperties().getVanConeCloud().getMode();
        String urlString = "";
        List<Node> nodes = springBootModule.getProperties().getVanConeCloud().getRouter().getNodes();
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