package com.mekcone.studio.codegen.model.project;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mekcone.studio.codegen.constant.LanguageType;
import com.mekcone.studio.codegen.constant.ModuleConstant;
import com.mekcone.studio.codegen.model.module.Module;
import com.mekcone.studio.codegen.model.module.impl.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JacksonXmlRootElement(localName = "project")
public class Project {

    @JacksonXmlElementWrapper(localName = "languages")
    @JacksonXmlProperty(localName = "language")
    private List<String> languages = new ArrayList<>();

    private String id;
    private String status;
    private String modifiedTime;

    private String groupId;
    private String artifactId;
    private String version;
    private Internationalization name;
    private Internationalization description;

    @JacksonXmlProperty(isAttribute = true)
    private String noNamespaceSchemaLocation;

    @JacksonXmlProperty(localName = "modules")
    private ModuleSet moduleSet;

    @Data
    public class ModuleSet {
        @JacksonXmlProperty(localName = ModuleConstant.MODULE_TYPE_DOCUMENT)
        private DocumentModule documentModule;

        @JacksonXmlProperty(localName = ModuleConstant.MODULE_TYPE_DEPLOYMENT)
        private DeploymentModule deploymentModule;

        @JacksonXmlProperty(localName = ModuleConstant.MODULE_TYPE_DATASOURCE)
        private DatasourceModule datasourceModule;

        @JacksonXmlProperty(localName = ModuleConstant.MODULE_TYPE_HYBRID_MOBILE_APP)
        private HybridMobileAppModule hybridMobileAppModule;

        @JacksonXmlProperty(localName = ModuleConstant.MODULE_TYPE_SPRING_BOOT)
        private SpringBootModule springBootModule;

        @JacksonXmlProperty(localName = ModuleConstant.MODULE_TYPE_VUE_ELEMENT_ADMIN)
        private VueElementAdminModule vueElementAdminModule;

        @JacksonXmlProperty(localName = ModuleConstant.MODULE_TYPE_WEBSITE_PAGE)
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

    public String toXMLString() {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (Exception e) {
            return null;
        }
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
