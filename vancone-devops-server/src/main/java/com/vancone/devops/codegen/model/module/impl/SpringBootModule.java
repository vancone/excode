package com.vancone.devops.codegen.model.module.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vancone.devops.codegen.annotation.ExtensionClass;
import com.vancone.devops.codegen.annotation.Validator;
import com.vancone.devops.codegen.controller.parser.PropertiesParser;
import com.vancone.devops.codegen.controller.parser.template.impl.JavaTemplate;
import com.vancone.devops.codegen.model.module.Module;
import com.vancone.devops.constant.ModuleConstant;
import com.vancone.devops.codegen.model.file.springboot.SpringBootComponent;
import com.vancone.devops.codegen.model.file.springboot.MavenProjectObjectModel;
import com.vancone.devops.codegen.model.file.springboot.SpringBootDataClass;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SpringBootModule extends Module {

    @JsonIgnore
    private String groupId;

    @JsonIgnore
    private String artifactId;

    private SpringBootExtensions extensions;

    @Data
    public static class SpringBootExtensions {

        private CrossOrigin crossOrigin;

        private MekConeCloud mekConeCloud;

        private Swagger2 swagger2;


        public List<Extension> asList() {
            List<Extension> extensionList = new ArrayList<>();
            extensionList.add(crossOrigin);
            extensionList.add(mekConeCloud);
            extensionList.add(swagger2);
            return extensionList;
        }

        @Data
        @ExtensionClass
        public static class CrossOrigin extends Extension {

            public CrossOrigin() {
                setId(ModuleConstant.SPRING_BOOT_EXTENSION_CROSS_ORIGIN);
            }

            private List<String> allowedHeaders = new ArrayList<>();

            @Validator(value = {"*", "GET", "POST", "PUT", "DELETE"}, defaultValue = "*")
            private List<String> allowedMethods = new ArrayList<>();

            private List<String> allowedOrigins = new ArrayList<>();
        }

        @Data
        @ExtensionClass
        public static class Lombok extends Extension {

        }

        @Data
        @ExtensionClass
        public static class MekConeCloud extends Extension {

            public MekConeCloud() {
                setId(ModuleConstant.SPRING_BOOT_EXTENSION_MEKCONE_CLOUD);
            }

            @Validator({"dev", "prod"})
            private String mode;

            private Router router = new Router();
            private Account account = new Account();

            @Data
            @ExtensionClass
            public static class Router {
                private List<Node> nodes;

                @Data
                public static class Node {
                    @Validator({"dev", "prod"})
                    private String mode;

                    private String address;
                }
            }

            @Data
            @ExtensionClass
            public static class Account {
                private TestAccount testAccount;

                @Data
                public static class TestAccount {
                    private String username;

                    private String password;

                    @Validator({"admin"})
                    private String role;

                    private String profile;
                }
            }
        }

        @Data
        @ExtensionClass
        public static class Swagger2 extends Extension {

            public Swagger2() {
                setId(ModuleConstant.SPRING_BOOT_EXTENSION_SWAGGER2);
            }
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
