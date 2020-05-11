package com.mekcone.excrud.model.export.impl.relationaldatabase;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.mekcone.excrud.constant.basic.ExportType;
import com.mekcone.excrud.model.export.GenModel;
import com.mekcone.excrud.model.export.impl.relationaldatabase.component.Database;
import lombok.Data;

import java.util.List;

@Data
public class RelationalDatabaseGenModel implements GenModel {

    @JacksonXmlProperty(isAttribute = true)
    private boolean use;

    @JacksonXmlElementWrapper(localName = "databases")
    @JacksonXmlProperty(localName = "database")
    private List<Database> databases;

    @Override
    public String type() {
        return ExportType.RELATIONAL_DATABASE;
    }
}
