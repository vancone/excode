package com.vancone.excode.core.model.module.impl;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.vancone.excode.core.model.module.Module;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tenton Lien
 */
@Data
public class HybridMobileAppModule extends Module {
    @JacksonXmlElementWrapper(localName = "platforms")
    @JacksonXmlProperty(localName = "platform")
    private List<String> platforms = new ArrayList<>();
}
