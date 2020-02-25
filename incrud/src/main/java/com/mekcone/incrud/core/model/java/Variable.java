package com.mekcone.incrud.core.model.java;

import com.mekcone.incrud.core.model.enums.AccessModifier;
import com.mekcone.incrud.core.model.enums.BasicDataType;
import com.mekcone.incrud.core.model.java.annotations.Annotation;
import com.mekcone.incrud.core.model.project.Value;

import java.util.ArrayList;
import java.util.List;

public class Variable {
    private AccessModifier accessModifier;
    private String type;
    private Value name = new Value();
    private List<Annotation> annotations = new ArrayList<>();
    private boolean isCustomizedObject;

    public Variable() {}

    public Variable(String type, Value name) {
        this.type = type;
        this.name = name;
    }

    public Variable(String type, String name) {
        this.type = type;
        this.name.setData(name);
    }

    public void addAnnotation(Annotation annotation) {
        this.annotations.add(annotation);
    }

    public AccessModifier getAccessModifier() {
        return accessModifier;
    }

    public void setAccessModifier(AccessModifier accessModifier) {
        this.accessModifier = accessModifier;
    }

    public String getType() {
        return this.type.toString();
    }

    public void setType(String type) {
        this.type = type;
        this.isCustomizedObject = true;
    }

    public void setType(BasicDataType dataType) {
        this.type = dataType.toString();
        this.isCustomizedObject = false;
    }

    public Value getName() {
        return this.name;
    }

    public void setName(Value name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name.setData(name);
    }

    @Override
    public String toString() {
        String code = "";
        if (!this.annotations.isEmpty()) {
            for (Annotation annotation: this.annotations) {
                code += annotation.toString();
            }
        }
        if (this.accessModifier != null) {
            code += this.accessModifier.toString() + " ";
        }

        code += this.type + " " + this.name.getData();

        return code;
    }

    public static Variable privateVariable(String type, String name) {
        Variable variable = new Variable();
        variable.accessModifier = AccessModifier.PRIVATE;
        variable.type = type;
        variable.name = Value.String(name);
        return variable;
    }

    public Variable(Annotation annotation, String type, String name) {
        this.annotations.add(annotation);
        this.type = type;
        this.name.setData(name);
    }
}
