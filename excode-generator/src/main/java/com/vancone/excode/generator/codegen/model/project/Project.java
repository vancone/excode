package com.vancone.excode.generator.codegen.model.project;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vancone.excode.generator.codegen.model.module.Module;
import com.vancone.excode.generator.constant.LanguageType;
import com.vancone.excode.generator.codegen.model.module.impl.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tenton Lien
 */
@Data
public class Project {

    private List<String> languages = new ArrayList<>();

    private String id;
    private String status;
    private String modifiedTime;

    private String groupId;
    private String artifactId;
    private String version;
    private Internationalization name;
    private Internationalization description;

    private String noNamespaceSchemaLocation;

    private ModuleSet moduleSet;

    @Data
    public class ModuleSet {
        private DocumentModule documentModule;

        private DeploymentModule deploymentModule;

        private DatasourceModule datasourceModule;

        private HybridMobileAppModule hybridMobileAppModule;

        private SpringBootModule springBootModule;

        private VueElementAdminModule vueElementAdminModule;

        private WebsitePageModule websitePageModule;

        public List<Module> asList() {
            List<Module> modules = new ArrayList<>();
            modules.add(datasourceModule);
            modules.add(deploymentModule);
            modules.add(documentModule);
            modules.add(hybridMobileAppModule);
            modules.add(springBootModule);
            modules.add(vueElementAdminModule);
            modules.add(websitePageModule);
            return modules;
        }
    }

    public String getDefaultLanguage() {
        return languages.isEmpty() ? LanguageType.ENGLISH_US : languages.get(0);
    }

    public String toJSONString() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (Exception e) {
            return null;
        }
    }
}
