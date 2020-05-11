package com.mekcone.excrud.model.export.impl.springboot.component;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
public class SpringBootProperties {

    @JacksonXmlProperty(localName = "application-name")
    private String applicationName;

    @JacksonXmlProperty(localName = "page-size")
    private int pageSize;

    @JacksonXmlProperty(localName = "server-port")
    private int serverPort;
}
