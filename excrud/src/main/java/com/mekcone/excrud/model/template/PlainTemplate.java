package com.mekcone.excrud.model.template;

import com.mekcone.excrud.util.FileUtil;
import com.mekcone.excrud.util.LogUtil;

public class PlainTemplate implements Template {
    private String template;

    public PlainTemplate(String path) {
        this.template = FileUtil.read(path);
    }

    @Override
    public boolean insert(String tag, String content) {
        int index = template.indexOf("## " + tag + " ##");
        if (index < 0) {
            // LogUtil.warn("Tag \"" + tag  + "\" not found.");
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
            // LogUtil.warn("Tag \"" + tag  + "\" not found.");
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
