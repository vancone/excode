package com.mekcone.excrud.model.project;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.mekcone.excrud.model.project.export.impl.relationaldatabase.RelationalDatabaseExport;
import com.mekcone.excrud.model.project.export.impl.relationaldatabase.database.Database;
import com.mekcone.excrud.model.project.export.Export;
import com.mekcone.excrud.model.project.export.impl.SpringBootExport;
import com.mekcone.excrud.model.project.export.impl.apidocument.ApiDocumentExport;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Exports {
    @JacksonXmlProperty(localName = "api-document")
    private ApiDocumentExport apiDocumentExport;

    @JacksonXmlProperty(localName = "relational-database")
    private RelationalDatabaseExport relationalDatabaseExport;

    @JacksonXmlProperty(localName = "spring-boot")
    private SpringBootExport springBootExport;

    public List<Export> asList() {
        List<Export> exports = new ArrayList<>();
        exports.add(apiDocumentExport);
        exports.add(relationalDatabaseExport);
        exports.add(springBootExport);
        return exports;
    }
}
