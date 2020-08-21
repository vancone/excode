package com.mekcone.studio.codegen.model.module.impl;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import com.mekcone.studio.codegen.annotation.Validator;
import com.mekcone.studio.codegen.constant.ModuleConstant;
import com.mekcone.studio.codegen.model.module.Module;
import lombok.Data;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;

@Data
public class DocumentModule extends Module {
    @JacksonXmlElementWrapper(localName = "keywords")
    @JacksonXmlProperty(localName = "keyword")
    private List<Keyword> keywords = new ArrayList<>();

    @JacksonXmlElementWrapper(localName = "exports")
    @JacksonXmlProperty(localName = "export")
    private List<String> exports = new ArrayList<>();

    public String getKeywordByType(String type) {
        for (Keyword keyword: keywords) {
            if (keyword.getType().equals(type)) {
                return keyword.getValue();
            }
        }
        return null;
    }

    @Data
    public static class Keyword {
        @JacksonXmlProperty(isAttribute = true)
        @Validator({})
        private String type;

        @JacksonXmlText
        private String value;

        private String requestMethod;

        public String getRequestMethod() {
            HttpMethod requestMethod;
            switch (type) {
                case ModuleConstant.DOCUMENT_KEYWORD_CREATE:
                    requestMethod = HttpMethod.POST;
                    break;
                case ModuleConstant.DOCUMENT_KEYWORD_RETRIEVE:
                    requestMethod = HttpMethod.GET;
                    break;
                case ModuleConstant.DOCUMENT_KEYWORD_UPDATE:
                    requestMethod = HttpMethod.PUT;
                    break;
                case ModuleConstant.DOCUMENT_KEYWORD_DELETE:
                    requestMethod = HttpMethod.DELETE;
                    break;
                default:
                    return "";
            }
            return requestMethod.toString();
        }
    }
}
