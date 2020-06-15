package com.mekcone.excrud.codegen.model.module;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Module {
    private String type;

    private boolean use;

    @JacksonXmlElementWrapper(localName = "extensions")
    @JacksonXmlProperty(localName = "extension")
    private List<Extension> extensions = new ArrayList<>();

    @Data
    public static class Extension {
        @JacksonXmlProperty(isAttribute = true)
        private String id;

        @JacksonXmlProperty(isAttribute = true)
        private boolean use;
    }
}
