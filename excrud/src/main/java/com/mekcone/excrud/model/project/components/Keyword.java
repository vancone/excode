package com.mekcone.excrud.model.project.components;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Data;

@Data
public class Keyword {
    @JacksonXmlProperty(isAttribute = true)
    private String type;

    @JacksonXmlText
    private String value;
}
