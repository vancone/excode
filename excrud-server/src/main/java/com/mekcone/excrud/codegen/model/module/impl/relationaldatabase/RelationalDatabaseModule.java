package com.mekcone.excrud.codegen.model.module.impl.relationaldatabase;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.mekcone.excrud.codegen.constant.ModuleType;
import com.mekcone.excrud.codegen.model.module.Module;
import com.mekcone.excrud.codegen.model.module.impl.relationaldatabase.component.Database;
import lombok.Data;

import java.util.List;

@Data
public class RelationalDatabaseModule implements Module {

    @JacksonXmlProperty(isAttribute = true)
    private boolean use;

    @JacksonXmlElementWrapper(localName = "databases")
    @JacksonXmlProperty(localName = "database")
    private List<Database> databases;

    @Override
    public String type() {
        return ModuleType.RELATIONAL_DATABASE;
    }
}