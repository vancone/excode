package com.mekcone.excrud.codegen.model.module.impl;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import com.mekcone.excrud.codegen.constant.ModuleType;
import com.mekcone.excrud.codegen.model.module.Module;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ApiDocumentModule implements Module {

    @JacksonXmlProperty(isAttribute = true)
    private boolean use;

    @JacksonXmlElementWrapper(localName = "keywords")
    @JacksonXmlProperty(localName = "keyword")
    private List<Keyword> keywords = new ArrayList<>();

    public String getKeywordByType(String type) {
        for (Keyword keyword: keywords) {
            if (keyword.getType().equals(type)) {
                return keyword.getValue();
            }
        }
        return null;
    }

    @Override
    public String type() {
        return ModuleType.API_DOCUMENT;
    }

    @Data
    public static class Keyword {
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
}
