package com.mekcone.excrud.codegen.model.module.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.mekcone.excrud.codegen.constant.ModuleConstant;
import com.mekcone.excrud.codegen.controller.parser.PropertiesParser;
import com.mekcone.excrud.codegen.model.file.springboot.ProjectObjectModel;
import com.mekcone.excrud.codegen.model.file.springboot.SpringBootComponent;
import com.mekcone.excrud.codegen.model.file.springboot.SpringBootDataClass;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SpringBootModule extends com.mekcone.excrud.codegen.model.module.Module {

    @JsonIgnore
    private String groupId;

    @JsonIgnore
    private String artifactId;

    @JacksonXmlElementWrapper(localName = "extensions")
    @JacksonXmlProperty(localName = "extension")
    private List<Extension> extensions = new ArrayList<>();

    private SpringBootProperties properties;

    @Data
    public class SpringBootProperties {
        // Basic properties
        @JacksonXmlProperty(localName = "application-name")
        private String applicationName;

        @JacksonXmlProperty(localName = "page-size")
        private int pageSize;

        @JacksonXmlProperty(localName = "server-port")
        private int serverPort;

        // Extended properties
        @JacksonXmlProperty(localName = "cross-origin")
        private SpringBootProperties.CrossOrigin crossOrigin;

        @JacksonXmlProperty(localName = "mwp-account")
        private SpringBootProperties.MekConeWebPlatformAccount mekConeWebPlatformAccount;

        @Data
        public class CrossOrigin {

            @JacksonXmlElementWrapper(localName = "allowed-headers")
            @JacksonXmlProperty(localName = "allowed-header")
            private List<String> allowedHeaders = new ArrayList<>();

            @JacksonXmlElementWrapper(localName = "allowed-methods")
            @JacksonXmlProperty(localName = "allowed-method")
            private List<String> allowedMethods = new ArrayList<>();

            @JacksonXmlElementWrapper(localName = "allowed-origins")
            @JacksonXmlProperty(localName = "allowed-origin")
            private List<String> allowedOrigins = new ArrayList<>();
        }

        @Data
        public class MekConeWebPlatformAccount {

            @JacksonXmlProperty(localName = "test-account")
            private SpringBootProperties.MekConeWebPlatformAccount.TestAccount testAccount = new SpringBootProperties.MekConeWebPlatformAccount.TestAccount();

            @Data
            public class TestAccount {
                @JacksonXmlProperty(isAttribute = true)
                private String username;

                @JacksonXmlProperty(isAttribute = true)
                private String password;

                @JacksonXmlProperty(isAttribute = true)
                private String role;

                @JacksonXmlProperty(isAttribute = true)
                private String profile;
            }
        }
    }

    @JsonIgnore
    private ProjectObjectModel projectObjectModel;

    @JsonIgnore
    private PropertiesParser applicationPropertiesParser = new PropertiesParser();

    @JsonIgnore
    private List<SpringBootComponent> configs = new ArrayList<>();

    @JsonIgnore
    private List<SpringBootComponent> controllers = new ArrayList<>();

    @JsonIgnore
    private List<SpringBootDataClass> entities = new ArrayList<>();

    @JsonIgnore
    private List<SpringBootComponent> mybatisMappers = new ArrayList<>();

    @JsonIgnore
    private List<SpringBootComponent> services = new ArrayList<>();

    @JsonIgnore
    private List<SpringBootComponent> serviceImpls = new ArrayList<>();

    public void addController(SpringBootComponent springBootComponent) {
        controllers.add(springBootComponent);
    }

    public void addEntity(SpringBootDataClass springBootDataClass) {
        entities.add(springBootDataClass);
    }

    public void addMybatisMapper(SpringBootComponent springBootComponent) {
        mybatisMappers.add(springBootComponent);
    }

    public void addService(SpringBootComponent springBootComponent) {
        services.add(springBootComponent);
    }

    public void addServiceImpl(SpringBootComponent springBootComponent) {
        serviceImpls.add(springBootComponent);
    }

    @Override
    public String getType() {
        return ModuleConstant.MODULE_TYPE_SPRING_BOOT;
    }

}
