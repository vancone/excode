package com.mekcone.excrud.codegen.model.module.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.mekcone.excrud.codegen.annotation.ExtensionClass;
import com.mekcone.excrud.codegen.annotation.Validator;
import com.mekcone.excrud.codegen.controller.parser.PropertiesParser;
import com.mekcone.excrud.codegen.controller.parser.template.impl.JavaTemplate;
import com.mekcone.excrud.codegen.model.file.springboot.MavenProjectObjectModel;
import com.mekcone.excrud.codegen.model.file.springboot.SpringBootComponent;
import com.mekcone.excrud.codegen.model.file.springboot.SpringBootDataClass;
import com.mekcone.excrud.codegen.model.module.Module;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SpringBootModule extends Module {

    @JsonIgnore
    private String groupId;

    @JsonIgnore
    private String artifactId;

    private SpringBootProperties properties;

    @Data
    public static class SpringBootProperties {
        // Basic properties
        @JacksonXmlProperty(localName = "application-name")
        private String applicationName;

        @JacksonXmlProperty(localName = "page-size")
        @Validator(min = 1)
        private int pageSize;

        @JacksonXmlProperty(localName = "server-port")
        @Validator(min = 0, max = 65535)
        private int serverPort;

        // Extended properties
        @JacksonXmlProperty(localName = "cross-origin")
        private SpringBootProperties.CrossOrigin crossOrigin;

        @JacksonXmlProperty(localName = "mekcone-cloud")
        private MekConeCloud mekConeCloud;


        // Properties of extensions

        @Data
        @ExtensionClass
        public static class CrossOrigin {

            @JacksonXmlElementWrapper(localName = "allowed-headers")
            @JacksonXmlProperty(localName = "allowed-header")
            private List<String> allowedHeaders = new ArrayList<>();

            @JacksonXmlElementWrapper(localName = "allowed-methods")
            @JacksonXmlProperty(localName = "allowed-method")
            @Validator(value = {"*", "GET", "POST", "PUT", "DELETE"}, defaultValue = "*")
            private List<String> allowedMethods = new ArrayList<>();

            @JacksonXmlElementWrapper(localName = "allowed-origins")
            @JacksonXmlProperty(localName = "allowed-origin")
            private List<String> allowedOrigins = new ArrayList<>();
        }

        @Data
        @ExtensionClass
        public static class Lombok {

        }

        @Data
        @ExtensionClass
        public static class MekConeCloud {
            @JacksonXmlProperty(isAttribute = true)
            @Validator({"dev", "prod"})
            private String mode;

            private Router router = new Router();
            private Account account = new Account();

            @Data
            @ExtensionClass
            public static class Router {
                @JacksonXmlElementWrapper(localName = "nodes")
                @JacksonXmlProperty(localName = "node")
                private List<Node> nodes;

                @Data
                public static class Node {
                    @JacksonXmlProperty(isAttribute = true)
                    @Validator({"dev", "prod"})
                    private String mode;

                    @JacksonXmlProperty(isAttribute = true)
                    private String address;
                }
            }

            @Data
            @ExtensionClass
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
                    @Validator({"admin"})
                    private String role;

                    @JacksonXmlProperty(isAttribute = true)
                    private String profile;
                }
            }
        }

        @Data
        @ExtensionClass
        public static class Swagger2 {

        }
    }

    @JsonIgnore
    private JavaTemplate applicationEntry;

    @JsonIgnore
    private MavenProjectObjectModel mavenProjectObjectModel;

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
