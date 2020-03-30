package com.mekcone.excrud.loader.model.data;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.List;

@Data
public class Database {
    @JacksonXmlProperty(isAttribute = true)
    private String type;

    @JacksonXmlProperty(isAttribute = true)
    private String host;

    @JacksonXmlProperty(isAttribute = true)
    private String username;

    @JacksonXmlProperty(isAttribute = true)
    private String password;

    @JacksonXmlElementWrapper(localName = "tables")
    @JacksonXmlProperty(localName = "table")
    private List<Table> tables;
}
