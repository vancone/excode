package com.mekcone.excrud.codegen.controller.parser.template.impl;

import com.mekcone.excrud.codegen.util.FileUtil;
import com.mekcone.excrud.codegen.controller.parser.template.Template;

public class UniversalTemplate implements Template {
    private String template;

    public UniversalTemplate(String path) {
        this.template = FileUtil.read(path);
    }

    @Override
    public boolean insert(String tag, String content) {
        int index = template.indexOf("## " + tag + " ##");
        if (index < 0) {
            return false;
        }

        while (index > -1) {
            template = template.substring(0, index) + content +
                    template.substring(index + tag.length() + 6);
            index = template.indexOf("## " + tag + " ##");
        }

        return true;
    }

    @Override
    public boolean insertOnce(String tag, String replacement) {
        int index = template.indexOf("## " + tag + " ##");
        if (index < 0) {
            return false;
        }

        template = template.substring(0, index) + replacement +
                template.substring(index + tag.length() + 6);

        return true;
    }

    @Override
    public boolean remove(String label) {
        return insert(label, "");
    }

    public boolean output() {
        return false;
    }

    public String toString() {
        return template;
    }
}
