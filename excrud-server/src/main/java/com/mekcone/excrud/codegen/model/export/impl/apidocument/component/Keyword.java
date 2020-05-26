package com.mekcone.excrud.codegen.model.export.impl.apidocument.component;

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
        String requestMethod;
        switch (type) {
            case "create": requestMethod = "POST"; break;
            case "retrieve": requestMethod = "GET"; break;
            case "update": requestMethod = "PUT"; break;
            case "delete": requestMethod = "DELETE"; break;
            default: requestMethod = "";
        };
        return requestMethod;
    }
}
