package com.mekcone.excrud.model.file.javalang.components.annotations;

import lombok.Data;

@Data
public class Annotation {
    protected String name;
    private String value;

    @Override
    public String toString() {
        String str = "";
        str += "@" + name;
        if (value != null) {
            str += "(\"" + value + "\")";
        }
        return str + " ";
    }

    public Annotation() {}

    @Deprecated
    public Annotation(String name) {
        this.name = name;
    }

    public static Annotation simpleAnnotation(String name) {
        Annotation annotation = new Annotation();
        annotation.setName(name);
        return annotation;
    }

    public static Annotation singleValueAnnotation(String name, String value) {
        Annotation annotation = simpleAnnotation(name);
        annotation.setValue(value);
        return annotation;
    }
}
