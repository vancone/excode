package com.mekcone.excrud.codegen.model.module.impl;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.mekcone.excrud.codegen.constant.ModuleConstant;
import com.mekcone.excrud.codegen.model.database.Database;
import lombok.Data;

import java.util.List;

@Data
public class RelationalDatabaseModule extends com.mekcone.excrud.codegen.model.module.Module {

    @JacksonXmlElementWrapper(localName = "databases")
    @JacksonXmlProperty(localName = "database")
    private List<Database> databases;

    @Override
    public String getType() {
        return ModuleConstant.MODULE_TYPE_RELATIONAL_DATABASE;
    }
}
