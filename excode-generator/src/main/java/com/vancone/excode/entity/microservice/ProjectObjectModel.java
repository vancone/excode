package com.vancone.excode.entity.microservice;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.vancone.excode.entity.ProjectOld;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tenton Lien
 */
@Data
@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "project")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class ProjectObjectModel {

    private final static String SPRING_BOOT_VERSION = "2.6.3";

    @JacksonXmlProperty(isAttribute = true, localName = "xmlns")
    private final String NAMESPACE = "http://maven.apache.org/POM/4.0.0";

    @JacksonXmlProperty(isAttribute = true, localName = "xmlns:xsi")
    private final String SCHEMA_INSTANCE = "http://www.w3.org/2001/XMLSchema-instance";

    @JacksonXmlProperty(isAttribute = true, localName = "xsi:schemaLocation")
    private final String SCHEMA_LOCATION = "http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd";

    private String modelVersion = "4.0.0";

    private Parent parent = new Parent();
    private String groupId;
    private String artifactId;
    private String version;

    private String name;

    private String description;

    @JacksonXmlProperty(localName = "dependency")
    @JacksonXmlElementWrapper(localName = "dependencies")
    private List<Dependency> dependencies = new ArrayList<>();

    private Build build = new Build();

    @Data
    public static class Parent {
        private String groupId = "org.springframework.boot";
        private String artifactId = "spring-boot-starter-parent";
        private String version = SPRING_BOOT_VERSION;
        private String relativePath;
    }

    @Data
    @Document("spring_boot_pom")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Dependency {
        private String groupId;
        private String artifactId;
        private String version;
        private String scope;
        private List<Dependency> exclusions;
        private String label;

        public Dependency() {}

        public Dependency(String groupId, String artifactId, String version, String scope) {
            this.groupId = groupId;
            this.artifactId = artifactId;
            this.version = version;
            this.scope = scope;
        }
    }

    @Data
    static class Build {
        private List<Dependency> plugins = new ArrayList<>();

        public Build() {
            plugins.add(new Dependency("org.springframework.boot", "spring-boot-maven-plugin", null, null));
        }
    }

    public ProjectObjectModel(ProjectOld project) {
        ProjectOld.DataAccess.Solution.JavaSpringBoot module = project.getDataAccess().getSolution().getJavaSpringBoot();
        groupId = module.getGroupId();
        artifactId = module.getArtifactId();
        version = module.getVersion();
    }

    public ProjectObjectModel(SpringBootMicroservice microservice) {
        groupId = microservice.getGroupId();
        artifactId = microservice.getArtifactId();
        version = microservice.getVersion();
    }
}
