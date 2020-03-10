package com.mekcone.excrud.model;

import com.mekcone.excrud.util.FileUtil;
import com.mekcone.excrud.util.LogUtil;
import com.mekcone.excrud.util.PathUtil;

public class Template {
    private String path;
    private String template;

    public Template(String exportType, String path) {
        this.path = PathUtil.getProgramPath() + "exports/" + exportType + "/templates/" + path;
        this.template = FileUtil.read(this.path);
    }

    public boolean insert(String label, String content) {
        int index = template.indexOf("## " + label + " ##");
        if (index < 0) {
            LogUtil.warn("label \"" + label  + "\" not found.");
            return false;
        }

        while (index > -1) {
            template = template.substring(0, index) + content +
                    template.substring(index + label.length() + 6);
            index = template.indexOf("## " + label + " ##");
        }

        return true;
    }

    public boolean insertOnce(String label, String content) {
        int index = template.indexOf("## " + label + " ##");
        if (index < 0) {
            LogUtil.warn("label \"" + label  + "\" not found.");
            return false;
        }

        template = template.substring(0, index) + content +
                template.substring(index + label.length() + 6);

        return true;
    }

    public boolean remove(String label) {
        return insert(label, "");
    }

    public boolean output() {
        return FileUtil.write(path, template);
    }

    public String toString() {
        return template;
    }
}
