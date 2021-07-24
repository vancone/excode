package com.vancone.excode.core.model.module.impl;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.vancone.excode.core.annotation.Validator;
import com.vancone.excode.core.model.module.Module;
import com.vancone.excode.core.model.project.Internationalization;
import lombok.Data;

import java.util.List;

/**
 * @author Tenton Lien
 */
@Data
public class WebsitePageModule extends Module {
    private Theme theme;

    @JacksonXmlElementWrapper(localName = "pages")
    @JacksonXmlProperty(localName = "page")
    private List<Page> pages;

    @Data
    public static class Theme {
        @JacksonXmlProperty(isAttribute = true)
        private String type;

        @JacksonXmlProperty(isAttribute = true)
        private String id;

        @JacksonXmlProperty(isAttribute = true)
        private String color;
    }

    @Data
    public static class Page {
        @JacksonXmlProperty(isAttribute = true)
        @Validator({"about", "home"})
        private String type;

        private Internationalization title;

        private String content;

        @JacksonXmlElementWrapper(localName = "images")
        @JacksonXmlProperty(localName = "image")
        private List<String> images;
    }

    public Page getPageByType(String type) {
        for (Page page: pages) {
            if (page.getType().equals(type)) {
                return page;
            }
        }
        return null;
    }
}