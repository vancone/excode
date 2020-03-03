package com.mekcone.incrud.model.file.javalang.components;

import com.mekcone.incrud.model.enums.AccessModifier;

import java.util.ArrayList;
import java.util.List;

// Store class or interface entities
public class Bean {
    private List<Method> methods = new ArrayList<>();
    private List<Variable> variables = new ArrayList<>();

    private String implement;

    private AccessModifier accessModifier;

    public List<Method> getMethods() {
        return methods;
    }

    public void addMethod(Method method) {
        this.methods.add(method);
    }

    public List<Variable> getVariables() {
        return variables;
    }

    public void addVariable(Variable variable) {
        this.variables.add(variable);
    }

    public AccessModifier getAccessModifier() {
        return accessModifier;
    }

    public void setAccessModifier(AccessModifier accessModifier) {
        this.accessModifier = accessModifier;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public void addAnnotation(Annotation annotation) {
        this.annotations.add(annotation);
    }

    private String name;
    private List<Annotation> annotations = new ArrayList<>();

    private boolean isInterface;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImplement() {
        return this.implement;
    }

    public void setImplement(String implement) {
        this.implement = implement;
    }

    public boolean isInterface() {
        return isInterface;
    }

    public void setInterface(boolean anInterface) {
        isInterface = anInterface;
    }

    @Override
    public String toString() {
        String code = "";
        if (!this.annotations.isEmpty()) {
            for (Annotation annotation: this.annotations) {
                code += annotation;
            }
        }

        if (this.isInterface == true) {
            code += accessModifier + " interface " + name;
        } else {
            code += accessModifier + " class " + name;
        }

        if (this.implement != null && !this.implement.isEmpty()) {
            code += " implements " + this.implement;
        }

        code += " {";

        if (!this.variables.isEmpty()) {
            for (int i = 0; i < this.variables.size(); i ++) {
                code += this.variables.get(i).toString() + ";";
                /*if (this.variables.get(i).hasGetter) {
                    Getter getter = new Getter(variables.get(i));
                    code += getter.toString();
                }

                if (this.variables.get(i).hasSetter) {
                    Setter setter = new Setter(variables.get(i));
                    code += setter.toString();
                }*/
            }
        }

        if (!this.methods.isEmpty()) {
            for (Method method: this.methods) {
                code += method.toString();
            }
        }

        code += "}";
        return code;
    }

    public Bean() {
    }

    public Bean(AccessModifier accessModifier, String name) {
        this.accessModifier = accessModifier;
        this.name = name;
    }

    public static Bean publicClass(String name) {
        Bean bean = new Bean();
        bean.setAccessModifier(AccessModifier.PUBLIC);
        bean.setName(name);
        return bean;
    }
}
