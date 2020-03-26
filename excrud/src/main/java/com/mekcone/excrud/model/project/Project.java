package com.mekcone.excrud.model.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mekcone.excrud.model.project.components.ApiDocument;
import com.mekcone.excrud.model.project.components.Database;
import com.mekcone.excrud.model.project.components.Export;
import com.mekcone.excrud.util.LogUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JacksonXmlRootElement(localName = "project")
public class Project {
    private String groupId;
    private String artifactId;
    private String version;
    private String name;
    private String description;
    private String rsaPublicKey;

    @JacksonXmlProperty(isAttribute = true)
    private String noNamespaceSchemaLocation;

    @JacksonXmlElementWrapper(localName = "databases")
    @JacksonXmlProperty(localName = "database")
    private List<Database> databases;

    private ApiDocument apiDocument;

    @JacksonXmlElementWrapper(localName = "exports")
    @JacksonXmlProperty(localName = "export")
    private List<Export> exports = new ArrayList<>();

    @JsonIgnore
    public Export getSpringBootProjectExport() {
        if (exports != null) {
            for (Export export: exports) {
                if (export.getType().equals("spring-boot-project")) {
                    return export;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (Exception ex) {
            LogUtil.warn(ex.getMessage());
            return null;
        }
    }
}
