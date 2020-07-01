package com.mekcone.excrud.codegen.model.module.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.mekcone.excrud.codegen.constant.ModuleConstant;
import com.mekcone.excrud.codegen.controller.parser.PropertiesParser;
import com.mekcone.excrud.codegen.controller.parser.template.impl.JavaTemplate;
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

//    @JacksonXmlElementWrapper(localName = "extensions")
//    @JacksonXmlProperty(localName = "extension")
//    private List<Extension> extensions = new ArrayList<>();

    private SpringBootProperties properties;

    @Data
    public static class SpringBootProperties {
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

        @JacksonXmlProperty(localName = "mekcone-cloud")
        private MekConeCloud mekConeCloud;

        @Data
        public static class CrossOrigin {

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
        public static class MekConeCloud {
            @JacksonXmlProperty(isAttribute = true)
            private String mode;
            private Router router = new Router();
            private Account account =  new Account();


            @Data
            public static class Router {
                @JacksonXmlElementWrapper(localName = "nodes")
                @JacksonXmlProperty(localName = "node")
                private List<Node> nodes;

                @Data
                public static class Node {
                    @JacksonXmlProperty(isAttribute = true)
                    private String mode;

                    @JacksonXmlProperty(isAttribute = true)
                    private String address;
                }
            }

            @Data
            public static class Account {
                @JacksonXmlProperty(localName = "test-account")
                private TestAccount testAccount;

                @Data
                public static class TestAccount {
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
    }

    @JsonIgnore
    private JavaTemplate applicationEntry;

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
}
