package com.mekcone.excrud.model.file.javalang.components;

import com.mekcone.excrud.constant.JavaWords;
import com.mekcone.excrud.model.file.javalang.components.annotations.Annotation;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class Method {
    private String accessModifier;
    private String returnType;
    private String name;
    private boolean staticMethod;
    private List<Annotation> annotations = new ArrayList<>();
    private List<Variable> params = new ArrayList<>();
    private List<Expression> expressions = new ArrayList<>();
    private boolean hasBody = true;

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public void addAnnotation(Annotation annotation) {
        this.annotations.add(annotation);
    }

    public List<Variable> getParams() {
        return params;
    }

    public void addParam(Variable param) {
        this.params.add(param);
    }

    public List<Expression> getExpressions() {
        return expressions;
    }

    public void addExpression(Expression expression) {
        this.expressions.add(expression);
    }

    public void clearExpressions() {
        this.expressions.clear();
    }


    @Override
    public String toString() {
        String code = "";

        if (!this.annotations.isEmpty()) {
            for (Annotation annotation: this.annotations) {
                code += annotation;
            }
        }

        if (accessModifier != null) {
            code += accessModifier + " ";
        }

        if (this.isStaticMethod()) {
            code += "static ";
        }

        code += returnType + " " + name + "(";
        if (!this.params.isEmpty()) {
            for (int i = 0; i < this.params.size(); i ++) {
                code += this.params.get(i).toString();
                if (i + 1 != this.params.size()) {
                    code += ", ";
                }
            }
        }

        code += ")";
        if (this.hasBody) {
            code += " {";
            if (!expressions.isEmpty()) {
                for (Expression expression: expressions) {
                    code += expression.toString();
                }
            }
            code += "}";
        } else {
            code += ";";
        }

        return code;
    }

    public static Method publicMethod(String name) {
        Method method = new Method();
        method.setAccessModifier(JavaWords.PUBLIC);
        method.setName(name);
        return method;
    }

    public static Method publicMethod(String type, String name, Variable[] params, Expression[] expressions) {
        Method method = new Method();
        method.setAccessModifier(JavaWords.PUBLIC);
        method.setReturnType(type);
        method.setName(name);
        method.setHasBody(true);
        method.params = new ArrayList<>(Arrays.asList(params));
        method.expressions = new ArrayList<>(Arrays.asList(expressions));
        return method;
    }

    public static Method publicMethod(Annotation annotation, String type, String name, Expression[] expressions) {
        Method method = new Method();
        method.setAccessModifier(JavaWords.PUBLIC);
        method.setReturnType(type);
        method.setName(name);
        method.setHasBody(true);
        method.addAnnotation(annotation);
        method.expressions = new ArrayList<>(Arrays.asList(expressions));
        return method;
    }

    public static Method publicMethod(Annotation annotation, String type, String name, Variable[] params, Expression[] expressions) {
        Method method = Method.publicMethod(type, name, params, expressions);
        method.addAnnotation(annotation);
        return method;
    }

    public static Method getter(Variable variable) {
        Method getter = new Method();
        getter.setAccessModifier(JavaWords.PUBLIC);
        getter.setName("get" + variable.getCapitalizedCamelName());
        getter.setReturnType(variable.getType());
        getter.addExpression(Expression.returnExpression(Expression.simpleExpression("this." + variable.getName())));
        return getter;
    }

    public static Method setter(Variable variable) {
        Method setter = new Method();
        setter.setAccessModifier(JavaWords.PUBLIC);
        setter.setName("set" + variable.getCapitalizedCamelName());
        setter.setReturnType(JavaWords.VOID.toString());
        setter.addParam(Variable.simpleVariable(variable.getType(), variable.getName()));
        setter.addExpression(new Expression("this." + variable.getName() + " = " + variable.getName() + ";"));
        return setter;
    }
}
