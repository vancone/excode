package com.mekcone.excrud.model.apidoc;

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
        switch (type) {
            case "create": return "POST";
            case "retrieve":
            case "retrieveList":
                return "GET";
            case "update":
                return "PUT";
            case "delete":
                return "DELETE";
            default:
                return "";
        }
    }
}
