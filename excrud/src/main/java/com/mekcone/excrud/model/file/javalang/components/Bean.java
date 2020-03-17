package com.mekcone.excrud.model.file.javalang.components;

import com.mekcone.excrud.constant.JavaWords;
import com.mekcone.excrud.model.file.javalang.components.annotations.Annotation;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

// Store class or interface entities
@Data
public class Bean {
    private List<Method> methods = new ArrayList<>();
    private List<Variable> variables = new ArrayList<>();
    private String name;
    private List<Annotation> annotations = new ArrayList<>();
    private String implement;
    private String accessModifier;
    private boolean interfaceBean;

    public void addMethod(Method method) {
        this.methods.add(method);
    }

    public void addVariable(Variable variable) {
        this.variables.add(variable);
    }

    public void addAnnotation(Annotation annotation) {
        this.annotations.add(annotation);
    }

    @Override
    public String toString() {
        String code = "";
        if (!this.annotations.isEmpty()) {
            for (Annotation annotation: this.annotations) {
                code += annotation;
            }
        }

        if (this.isInterfaceBean() == true) {
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

    public Bean(String accessModifier, String name) {
        this.accessModifier = accessModifier;
        this.name = name;
    }

    public static Bean publicClass(String name) {
        Bean bean = new Bean();
        bean.setAccessModifier(JavaWords.PUBLIC);
        bean.setName(name);
        return bean;
    }
}
