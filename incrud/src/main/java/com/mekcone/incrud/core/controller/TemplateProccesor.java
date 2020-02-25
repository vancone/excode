package com.mekcone.incrud.core.controller;

public class TemplateProccesor {
    private String template;

    public TemplateProccesor(String path) {
        this.template = FileManager.read(path);
    }

    public boolean insert(String label, String content) {
        int index = this.template.indexOf("## " + label + " ##");
        if (index < 0) {
            Logger.warn("label \"" + label  + "\" not found.");
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
