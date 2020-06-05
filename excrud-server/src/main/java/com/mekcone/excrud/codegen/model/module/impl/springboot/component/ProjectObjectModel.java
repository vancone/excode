package com.mekcone.excrud.codegen.model.module.impl.springboot.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mekcone.excrud.codegen.constant.ApplicationParameter;
import com.mekcone.excrud.codegen.controller.generator.SpringBootGenerator;
import com.mekcone.excrud.codegen.model.project.Project;
import com.mekcone.excrud.codegen.util.FileUtil;
import com.mekcone.excrud.codegen.util.LogUtil;
import lombok.Data;
import org.jdom2.Comment;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProjectObjectModel {
    private String groupId;
    private String artifactId;
    private String version;
    private String name;
    private String description;
    private List<Dependency> dependencies = new ArrayList<>();

    public ProjectObjectModel(Project project) {
        groupId = project.getGroupId();
        artifactId = project.getArtifactId();
        version = project.getVersion();
        name = project.getName();
        description = project.getDescription();

        addDependencies("default");
        addDependencies("mybatis");
    }

    public void addDependencies(String pomFileName) {
        String pomDependenciesText = FileUtil.read(SpringBootGenerator.getTemplatePath() + "pom/" + pomFileName + ".xml");
        if (pomDependenciesText != null && !pomDependenciesText.isEmpty()) {
            XmlMapper xmlMapper = new XmlMapper();

            try {
                List<Dependency> pomDependencies = xmlMapper.readValue(pomDependenciesText, new TypeReference<List<Dependency>>() {
                });
                if (pomDependencies != null && !pomDependencies.isEmpty()) {
                    dependencies.addAll(pomDependencies);
                }
            } catch (JsonProcessingException e) {
                LogUtil.info("Parse XML error while retrieving dependencies of extension \"" + pomDependenciesText + "\": "+ e.getMessage());
            }
        }
    }

    @Override
    public String toString() {
        // Root
        Element rootElement = new Element("project");
        Document document = new Document(rootElement);
        rootElement.addContent(new Comment(ApplicationParameter.DESCRIPTION));
        Namespace xmlns = Namespace.getNamespace("http://maven.apache.org/POM/4.0.0");
        rootElement.setNamespace(xmlns);
        Namespace xsi = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        rootElement.addNamespaceDeclaration(xsi);
        rootElement.setAttribute("schemaLocation", "http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd", xsi);
        rootElement.addContent(new Element("modelVersion", xmlns).setText("4.0.0"));

        // Parent
        Element parentElement = new Element("parent", xmlns);
        rootElement.addContent(parentElement);
        parentElement.addContent(new Element("groupId", xmlns).setText("org.springframework.boot"));
        parentElement.addContent(new Element("artifactId", xmlns).setText("spring-boot-starter-parent"));
        parentElement.addContent(new Element("version", xmlns).setText("2.2.1.RELEASE"));
        parentElement.addContent(new Element("relativePath", xmlns));

        rootElement.addContent(new Element("groupId", xmlns).setText(groupId));
        rootElement.addContent(new Element("artifactId", xmlns).setText(artifactId));
        rootElement.addContent(new Element("version", xmlns).setText(version));
        rootElement.addContent(new Element("name", xmlns).setText(name));
        rootElement.addContent(new Element("description", xmlns).setText(description));

        // Properties
        Element propertiesElement = new Element("properties", xmlns);
        rootElement.addContent(propertiesElement);
        propertiesElement.addContent(new Element("java.version", xmlns).setText("1.8"));

        // Dependencies
        Element dependenciesElement = new Element("dependencies", xmlns);
        rootElement.addContent(dependenciesElement);

        for (Dependency dependency : dependencies) {
            Element dependencyElement = new Element("dependency", xmlns);
            dependencyElement.addContent(new Element("groupId", xmlns).setText(dependency.getGroupId()));
            dependencyElement.addContent(new Element("artifactId", xmlns).setText(dependency.getArtifactId()));
            if (dependency.getVersion() != null) {
                dependencyElement.addContent(new Element("version", xmlns).setText(dependency.getVersion()));
            }
            if (dependency.getScope() != null) {
                dependencyElement.addContent(new Element("scope", xmlns).setText(dependency.getScope()));
            }

            List<Dependency> exclusionDependencies = dependency.getExclusions();
            if (exclusionDependencies != null && !exclusionDependencies.isEmpty()) {
                Element exclusionsElement = new Element("exclusions", xmlns);
                dependencyElement.addContent(exclusionsElement);
                for (Dependency exclusionDependency : dependency.getExclusions()) {
                    Element exclusionElement = new Element("exclusion", xmlns);
                    exclusionElement.addContent(new Element("groupId", xmlns).setText(exclusionDependency.getGroupId()));
                    exclusionElement.addContent(new Element("artifactId", xmlns).setText(exclusionDependency.getArtifactId()));
                    exclusionsElement.addContent(exclusionElement);
                }
            }
            dependenciesElement.addContent(dependencyElement);
        }

        // <build></build>
        Element buildElement = new Element("build", xmlns);
        rootElement.addContent(buildElement);
        Element pluginsElement = new Element("plugins", xmlns);
        buildElement.addContent(pluginsElement);
        Element pluginElement = new Element("plugin", xmlns);
        pluginsElement.addContent(pluginElement);
        pluginElement.addContent(new Element("groupId", xmlns).setText("org.springframework.boot"));
        pluginElement.addContent(new Element("artifactId", xmlns).setText("spring-boot-maven-plugin"));

        // Generate string
        Format format = Format.getPrettyFormat();
        format.setEncoding("UTF-8");
        XMLOutputter xmlOutputter = new XMLOutputter(format);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            xmlOutputter.output(document, byteArrayOutputStream);
        } catch (IOException e) {
            LogUtil.info(e.getMessage());
        }
        return byteArrayOutputStream.toString();
    }
}
