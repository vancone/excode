package com.mekcone.incrud.util;

public class TemplateUtil {
    private String template;

    public TemplateUtil(String path) {
        this.template = FileUtil.read(path);
    }

    public boolean insert(String label, String content) {
        int index = this.template.indexOf("## " + label + " ##");
        if (index < 0) {
            LogUtil.warn("label \"" + label  + "\" not found.");
            return false;
        }

        this.template = this.template.substring(0, index) +
                content +
                this.template.substring(index + label.length() + 6);

        return true;
    }

    public String toString() {
        return this.template;
    }
}
