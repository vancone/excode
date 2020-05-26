package com.mekcone.excrud.codegen.model.project;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.mekcone.excrud.codegen.model.export.impl.relationaldatabase.RelationalDatabaseGenModel;
import com.mekcone.excrud.codegen.model.export.GenModel;
import com.mekcone.excrud.codegen.model.export.impl.springboot.SpringBootGenModel;
import com.mekcone.excrud.codegen.model.export.impl.apidocument.ApiDocumentGenModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Exports {
    @JacksonXmlProperty(localName = "api-document")
    private ApiDocumentGenModel apiDocumentExport;

    @JacksonXmlProperty(localName = "relational-database")
    private RelationalDatabaseGenModel relationalDatabaseExport;

    @JacksonXmlProperty(localName = "spring-boot")
    private SpringBootGenModel springBootGenModel;

    public List<GenModel> asList() {
        List<GenModel> genModels = new ArrayList<>();
        genModels.add(apiDocumentExport);
        genModels.add(relationalDatabaseExport);
        genModels.add(springBootGenModel);
        return genModels;
    }
}
