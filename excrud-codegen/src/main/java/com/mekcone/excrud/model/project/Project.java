package com.mekcone.excrud.model.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@JacksonXmlRootElement(localName = "project")
public class Project {
    private String id;
    private String status;
    private String updatedTime;

    private String groupId;
    private String artifactId;
    private String version;
    private String name;
    private String description;
    private String rsaPublicKey;
    private String language;

    @JacksonXmlProperty(isAttribute = true)
    private String noNamespaceSchemaLocation;

//    @JacksonXmlElementWrapper(localName = "databases")
//    @JacksonXmlProperty(localName = "database")
//    private List<Database> databases;

//    private ApiDocument apiDocument;

    private Exports exports;

    /*@JsonIgnore
    public OldExport getExport(String exportType) {
        if (exports != null) {
            for (var export : exports) {
                if (export.getType().equals(exportType)) {
                    return export;
                }
            }
        }
        return null;
    }*/

    public String toXMLString() {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (Exception e) {
            return null;
        }
    }

    public String toJSONString() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (Exception e) {
            return null;
        }
    }
}
