package com.mekcone.excrud.loader.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mekcone.excrud.loader.model.components.ApiDocument;
import com.mekcone.excrud.loader.model.components.Export;
import com.mekcone.excrud.loader.model.data.Database;
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

    @JacksonXmlProperty(isAttribute = true)
    private String noNamespaceSchemaLocation;

    @JacksonXmlElementWrapper(localName = "databases")
    @JacksonXmlProperty(localName = "database")
    private List<Database> databases;

    private ApiDocument apiDocument;

    @JacksonXmlElementWrapper(localName = "exports")
    @JacksonXmlProperty(localName = "export")
    private List<Export> exports;

    /*@JsonIgnore
    public Exports getSpringBootProjectExport() {
        if (exports != null) {
            for (Exports exports : this.exports) {
                if (exports.getType().equals("spring-boot-project")) {
                    return exports;
                }
            }
        }
        return null;
    }*/

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
