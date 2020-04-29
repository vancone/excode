package com.mekcone.excrud.model.project.export.impl;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.mekcone.excrud.constant.basic.ExportType;
import com.mekcone.excrud.model.project.export.Export;
import lombok.Data;

import java.util.Map;

@Data
public class SpringBootExport implements Export {
    private Map<String, String> extensions;
    private Map<String, String> properties;

    @JacksonXmlProperty(isAttribute = true)
    private boolean use;

    @Override
    public String type() {
        return ExportType.SPRING_BOOT;
    }
}
