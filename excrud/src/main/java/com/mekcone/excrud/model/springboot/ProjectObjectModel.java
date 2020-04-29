package com.mekcone.excrud.model.springboot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mekcone.excrud.Application;
import com.mekcone.excrud.constant.properties.SpringBootBackendProperty;
import com.mekcone.excrud.model.project.Project;
import com.mekcone.excrud.util.FileUtil;
import com.mekcone.excrud.util.LogUtil;
import lombok.Data;
import org.jdom2.Comment;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
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

    public void initialize(Project project) {
        groupId = project.getGroupId();
        artifactId = project.getArtifactId();
        version = project.getVersion();
        name = project.getName();
        description = project.getDescription();
    }

    @Override
    public String toString() {
        // Root
        var rootElement = new Element("project");
        var document = new Document(rootElement);
        if (Application.getDescription() != null) {
            rootElement.addContent(new Comment(Application.getDescription()));
        }
        Namespace xmlns = Namespace.getNamespace("http://maven.apache.org/POM/4.0.0");
        rootElement.setNamespace(xmlns);
        Namespace xsi = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        rootElement.addNamespaceDeclaration(xsi);
        rootElement.setAttribute("schemaLocation", "http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd", xsi);
        rootElement.addContent(new Element("modelVersion", xmlns).setText("4.0.0"));

        // Parent
        var parentElement = new Element("parent", xmlns);
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
        var propertiesElement = new Element("properties", xmlns);
        rootElement.addContent(propertiesElement);
        propertiesElement.addContent(new Element("java.version", xmlns).setText("1.8"));

        // Dependencies
        var dependenciesElement = new Element("dependencies", xmlns);
        rootElement.addContent(dependenciesElement);

        for (var dependency : dependencies) {
            var dependencyElement = new Element("dependency", xmlns);
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
                var exclusionsElement = new Element("exclusions", xmlns);
                dependencyElement.addContent(exclusionsElement);
                for (var exclusionDependency : dependency.getExclusions()) {
                    var exclusionElement = new Element("exclusion", xmlns);
                    exclusionElement.addContent(new Element("groupId", xmlns).setText(exclusionDependency.getGroupId()));
                    exclusionElement.addContent(new Element("artifactId", xmlns).setText(exclusionDependency.getArtifactId()));
                    exclusionsElement.addContent(exclusionElement);
                }
            }
            dependenciesElement.addContent(dependencyElement);
        }

        // <build></build>
        var buildElement = new Element("build", xmlns);
        rootElement.addContent(buildElement);
        var pluginsElement = new Element("plugins", xmlns);
        buildElement.addContent(pluginsElement);
        var pluginElement = new Element("plugin", xmlns);
        pluginsElement.addContent(pluginElement);
        pluginElement.addContent(new Element("groupId", xmlns).setText("org.springframework.boot"));
        pluginElement.addContent(new Element("artifactId", xmlns).setText("spring-boot-maven-plugin"));

        // Generate string
        var format = Format.getPrettyFormat();
        format.setEncoding("UTF-8");
        var xmlOutputter = new XMLOutputter(format);
        var byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            xmlOutputter.output(document, byteArrayOutputStream);
        } catch (IOException e) {
            LogUtil.info(e.getMessage());
        }
        return byteArrayOutputStream.toString();
    }
}
