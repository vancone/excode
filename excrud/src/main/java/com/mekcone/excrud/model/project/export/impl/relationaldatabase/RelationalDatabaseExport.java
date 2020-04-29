package com.mekcone.excrud.model.project.export.impl.relationaldatabase;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.mekcone.excrud.constant.basic.ExportType;
import com.mekcone.excrud.model.project.export.Export;
import com.mekcone.excrud.model.project.export.impl.relationaldatabase.database.Database;
import lombok.Data;

import java.util.List;

@Data
public class RelationalDatabaseExport implements Export {

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
