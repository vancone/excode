package com.vancone.devops.codegen.model.module.impl;

import com.vancone.devops.codegen.annotation.Validator;
import com.vancone.devops.codegen.model.module.Module;
import com.vancone.devops.codegen.model.project.Internationalization;
import lombok.Data;

import java.util.List;

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
