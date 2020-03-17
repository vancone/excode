package com.mekcone.excrud.model.file.javalang.components;

import com.mekcone.excrud.constant.JavaWords;
import com.mekcone.excrud.model.file.javalang.components.annotations.Annotation;
import com.mekcone.excrud.util.StringUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Variable {
    private String accessModifier;
    private String type;
    private String name;
    private List<Annotation> annotations = new ArrayList<>();

    public Variable() {}

    public Variable(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public void addAnnotation(Annotation annotation) {
        this.annotations.add(annotation);
    }

    public String getCapitalizedCamelName() {
        return StringUtil.capitalizedCamel(name);
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
            code += this.accessModifier + " ";
        }

        code += type + " " + name;

        return code;
    }

    public static Variable privateVariable(String type, String name) {
        Variable variable = new Variable();
        variable.accessModifier = JavaWords.PRIVATE;
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
