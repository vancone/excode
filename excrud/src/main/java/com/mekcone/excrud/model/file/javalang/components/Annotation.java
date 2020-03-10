package com.mekcone.excrud.model.file.javalang.components;

public class Annotation {
    protected String name;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public void setValue(String value) {
        this.value = value;
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
