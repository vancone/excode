package com.vancone.excode.generator.codegen.model.module.impl;

import com.vancone.excode.generator.codegen.annotation.Validator;
import com.vancone.excode.generator.codegen.model.module.Module;
import com.vancone.excode.generator.codegen.model.project.Internationalization;
import lombok.Data;

import java.util.List;

/**
 * @author Tenton Lien
 */
@Data
public class WebsitePageModule extends Module {
    private Theme theme;

    private List<Page> pages;

    @Data
    public static class Theme {
        private String type;

        private String id;

        private String color;
    }

    @Data
    public static class Page {
        @Validator({"about", "home"})
        private String type;

        private Internationalization title;

        private String content;

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
