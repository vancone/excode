package com.mekcone.excrud.model.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mekcone.excrud.model.project.components.ApiDocument;
import com.mekcone.excrud.model.project.components.Database;
import com.mekcone.excrud.model.project.components.Export;
import com.mekcone.excrud.util.LogUtil;
import javafx.beans.property.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
//@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "project")
public class Project {
    private LongProperty modifiedState = new SimpleLongProperty();
    private StringProperty groupId = new SimpleStringProperty();
    private StringProperty artifactId = new SimpleStringProperty();
    private StringProperty version = new SimpleStringProperty();
    private StringProperty name = new SimpleStringProperty();
    private StringProperty description = new SimpleStringProperty();
    private String rsaPublicKey;

    @JacksonXmlElementWrapper(localName = "databases")
    @JacksonXmlProperty(localName = "database")
    private ObjectProperty<List<Database>> databases = new SimpleObjectProperty<>();

    private ObjectProperty<ApiDocument> apiDocument = new SimpleObjectProperty<>();

    @JacksonXmlElementWrapper(useWrapping = true)
    private ObjectProperty<List<Export>> exports = new SimpleObjectProperty<>();

    public ApiDocument getApiDocument() {
        return apiDocument.getValue();
    }

    public ObjectProperty<ApiDocument> apiDocumentProperty() {
        return apiDocument;
    }

    public void setApiDocument(ApiDocument apiDocument) {
        this.apiDocument.set(apiDocument);
    }

    public List<Export> getExports() {
        return exports.getValue();
    }

    public ObjectProperty<List<Export>> exportsProperty() {
        return exports;
    }

    public void setExports(List<Export> exports) {
        this.exports.set(exports);
    }

    public String getGroupId() {
        return groupId.getValue();
    }

    public StringProperty groupIdProperty() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId.setValue(groupId);
    }

    public String getArtifactId() {
        return artifactId.getValue();
    }

    public StringProperty artifactIdProperty() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId.setValue(artifactId);
    }

    public String getVersion() {
        return version.getValue();
    }

    public StringProperty versionProperty() {
        return version;
    }

    public void setVersion(String version) {
        this.version.setValue(version);
    }

    public String getName() {
        return name.getValue();
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    public String getDescription() {
        return description.getValue();
    }

    public void setDescription(String description) {
        this.description.setValue(description);
    }

    public String getRsaPublicKey() {
        return rsaPublicKey;
    }

    public void setRsaPublicKey(String rsaPublicKey) {
        this.rsaPublicKey = rsaPublicKey;
    }

    public List<Database> getDatabases() {
        return databases.getValue();
    }

    public void setDatabases(List<Database> databases) {
        this.databases.setValue(databases);
    }

    public LongProperty modifiedStateProperty() {
        return modifiedState;
    }

    @JsonIgnore
    public Export getSpringBootProjectExport() {
        if (exports != null) {
            for (Export export: exports.getValue()) {
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
            ObjectMapper objectMapper = new ObjectMapper();
//            // return objectMapper.writeValueAsString(this);
//            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (Exception ex) {
            LogUtil.warn(ex.getMessage());
            return null;
        }
    }
}
