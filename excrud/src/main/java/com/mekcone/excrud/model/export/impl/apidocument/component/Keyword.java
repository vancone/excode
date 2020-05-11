package com.mekcone.excrud.model.export.impl.apidocument.component;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Data;

@Data
public class Keyword {
    @JacksonXmlProperty(isAttribute = true)
    private String type;

    @JacksonXmlText
    private String value;

    private String requestMethod;

    public String getRequestMethod() {
        return switch (type) {
            case "create" -> "POST";
            case "retrieve", "retrieveList" -> "GET";
            case "update" -> "PUT";
            case "delete" -> "DELETE";
            default -> "";
        };
    }
}
