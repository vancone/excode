package com.mekcone.excrud.model.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mekcone.excrud.model.apidoc.ApiDocument;
import com.mekcone.excrud.model.database.Database;
import com.mekcone.excrud.util.LogUtil;
import lombok.Data;

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
    private String language;

    @JacksonXmlProperty(isAttribute = true)
    private String noNamespaceSchemaLocation;

    @JacksonXmlElementWrapper(localName = "databases")
    @JacksonXmlProperty(localName = "database")
    private List<Database> databases;

    private ApiDocument apiDocument;

    @JacksonXmlElementWrapper(localName = "exports")
    @JacksonXmlProperty(localName = "export")
    private List<Export> exports;

    @JsonIgnore
    public Export getExport(String exportType) {
        if (exports != null) {
            for (Export export : exports) {
                if (export.getType().equals(exportType)) {
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
        } catch (Exception e) {
            LogUtil.warn(e.getMessage());
            return null;
        }
    }
}
