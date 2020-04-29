package com.mekcone.excrud.model.project.export.impl.apidocument;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.mekcone.excrud.constant.basic.ExportType;
import com.mekcone.excrud.model.project.export.Export;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ApiDocumentExport implements Export {
    private String title;
    private String description;

    @JacksonXmlProperty(isAttribute = true)
    private boolean use;

    @JacksonXmlElementWrapper(localName = "keywords")
    @JacksonXmlProperty(localName = "keyword")
    private List<Keyword> keywords = new ArrayList<>();

    public String getKeywordByType(String type) {
        for (var keyword: keywords) {
            if (keyword.getType().equals(type)) {
                return keyword.getValue();
            }
        }
        return null;
    }

    @Override
    public String type() {
        return ExportType.API_DOCUMENT;
    }
}
