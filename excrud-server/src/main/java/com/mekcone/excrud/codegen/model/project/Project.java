package com.mekcone.excrud.codegen.model.project;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mekcone.excrud.codegen.constant.LanguageType;
import com.mekcone.excrud.codegen.model.module.Module;
import com.mekcone.excrud.codegen.model.module.impl.*;
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
    private String updatedTime;

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
        @JacksonXmlProperty(localName = "document")
        private DocumentModule documentModule;

        @JacksonXmlProperty(localName = "deployment")
        private DeploymentModule deploymentModule;

        @JacksonXmlProperty(localName = "relational-database")
        private RelationalDatabaseModule relationalDatabaseModule;

        @JacksonXmlProperty(localName = "spring-boot")
        private SpringBootModule springBootModule;

        @JacksonXmlProperty(localName = "vue-element-admin")
        private VueElementAdminModule vueElementAdminModule;

        @JacksonXmlProperty(localName = "website-page")
        private WebsitePageModule websitePageModule;

        public List<Module> asList() {
            List<Module> modules = new ArrayList<>();
            modules.add(documentModule);
            modules.add(deploymentModule);
            modules.add(relationalDatabaseModule);
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
