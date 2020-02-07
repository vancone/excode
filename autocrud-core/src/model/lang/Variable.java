package model.lang;

import model.enums.AccessModifier;
import model.enums.BasicDataType;
import model.lang.annotations.Annotation;
import model.project.Value;

import java.util.ArrayList;
import java.util.List;

public class Variable {
    private AccessModifier accessModifier;
    private String type;
    private Value name = new Value();
    private List<Annotation> annotations = new ArrayList<>();
    private boolean isCustomizedObject;
    public boolean hasGetter;
    public boolean hasSetter;

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

    public void addGetterAndSetter() {
        this.hasGetter = true;
        this.hasSetter = true;
    }

    public static Variable BasicVariable(BasicDataType dataType, String name) {
        Variable variable = new Variable();
        variable.accessModifier = AccessModifier.PRIVATE;
        variable.type = dataType.toString();
        variable.name = Value.String(name);
        variable.hasGetter = true;
        variable.hasSetter = true;
        return variable;
    }
}
