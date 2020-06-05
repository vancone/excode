package com.mekcone.excrud.codegen.model.module.impl.apidocument;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.mekcone.excrud.codegen.constant.ModuleType;
import com.mekcone.excrud.codegen.model.module.impl.apidocument.component.Keyword;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ApiDocumentModule implements com.mekcone.excrud.codegen.model.module.Module {
    private String title;
    private String description;

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
}
