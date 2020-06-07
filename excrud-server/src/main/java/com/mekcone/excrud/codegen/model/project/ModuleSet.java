package com.mekcone.excrud.codegen.model.project;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.mekcone.excrud.codegen.model.module.Module;
import com.mekcone.excrud.codegen.model.module.impl.apidocument.ApiDocumentModule;
import com.mekcone.excrud.codegen.model.module.impl.relationaldatabase.RelationalDatabaseModule;
import com.mekcone.excrud.codegen.model.module.impl.springboot.SpringBootModule;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ModuleSet {
    @JacksonXmlProperty(localName = "api-document")
    private ApiDocumentModule apiDocumentModule;

    @JacksonXmlProperty(localName = "relational-database")
    private RelationalDatabaseModule relationalDatabaseModule;

    @JacksonXmlProperty(localName = "spring-boot")
    private SpringBootModule springBootModule;

    public List<Module> asList() {
        List<Module> modules = new ArrayList<>();
        modules.add(apiDocumentModule);
        modules.add(relationalDatabaseModule);
        modules.add(springBootModule);
        return modules;
    }
}
