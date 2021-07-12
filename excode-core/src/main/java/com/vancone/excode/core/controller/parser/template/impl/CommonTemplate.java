package com.vancone.excode.core.controller.parser.template.impl;

import com.vancone.excode.core.util.FileUtil;
import com.vancone.excode.core.controller.parser.template.Template;
import lombok.Getter;

/**
 * @author Tenton Lien
 */
public class CommonTemplate implements Template {
    @Getter
    private String template;

    public CommonTemplate(String path) {
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
