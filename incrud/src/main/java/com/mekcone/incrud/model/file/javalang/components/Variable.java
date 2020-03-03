package com.mekcone.incrud.model.file.javalang.components;

import com.mekcone.incrud.model.enums.AccessModifier;
import com.mekcone.incrud.model.enums.BasicDataType;
import com.mekcone.incrud.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class Variable {
    private AccessModifier accessModifier;
    private String type;
    private String name;
    private List<Annotation> annotations = new ArrayList<>();
    private boolean isCustomizedObject;

    public Variable() {}

    public Variable(String type, String name) {
        this.type = type;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public String getCapitalizedCamelName() {
        return StringUtil.capitalizedCamel(name);
    }

    public void setName(String name) {
        this.name = name;
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

        code += type + " " + name;

        return code;
    }

    public static Variable privateVariable(String type, String name) {
        Variable variable = new Variable();
        variable.accessModifier = AccessModifier.PRIVATE;
        variable.type = type;
        variable.name = name;
        return variable;
    }

    @Deprecated
    public Variable(Annotation annotation, String type, String name) {
        this.annotations.add(annotation);
        this.type = type;
        this.name = name;
    }


    public static Variable simpleVariable(String type, String name) {
        Variable variable = new Variable();
        variable.type = type;
        variable.name = name;
        return variable;
    }
}
