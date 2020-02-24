package com.mekcone.autocrud.core.model.java.annotations;

public class Annotation {
    protected String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        switch (this.name) {
            case "RequestBody": return "@" + name + " ";
            default: return "@" + name + "\n";
        }
    }

    public Annotation() {}

    public Annotation(String name) {
        this.name = name;
    }
}
