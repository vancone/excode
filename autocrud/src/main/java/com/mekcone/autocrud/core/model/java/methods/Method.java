package com.mekcone.autocrud.core.model.java.methods;

import com.mekcone.autocrud.core.model.enums.AccessModifier;
import com.mekcone.autocrud.core.model.java.Variable;
import com.mekcone.autocrud.core.model.java.annotations.Annotation;
import com.mekcone.autocrud.core.model.project.Value;
import com.mekcone.autocrud.core.model.java.expressions.Expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Method {
    private AccessModifier accessModifier;
    private String returnType;
    private Value name = new Value();
    private boolean staticMethod;
    private List<Annotation> annotations = new ArrayList<>();
    private List<Variable> params = new ArrayList<>();
    private List<Expression> expressions = new ArrayList<>();
    private boolean hasBody = true;

    public AccessModifier getAccessModifier() {
        return accessModifier;
    }

    public void setAccessModifier(AccessModifier accessModifier) {
        this.accessModifier = accessModifier;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public Value getName() {
        return name;
    }

    public void setName(Value name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name.setData(name);
    }

    public boolean isStaticMethod() {
        return this.staticMethod;
    }

    public void setStaticMethod(boolean staticMethod) {
        this.staticMethod = staticMethod;
    }

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

    public boolean isHasBody() {
        return hasBody;
    }

    public void setHasBody(boolean hasBody) {
        this.hasBody = hasBody;
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
        method.setAccessModifier(AccessModifier.PUBLIC);
        method.setName(name);
        return method;
    }

    public static Method publicMethod(String type, String name, Variable[] params, Expression[] expressions) {
        Method method = new Method();
        method.setAccessModifier(AccessModifier.PUBLIC);
        method.setReturnType(type);
        method.setName(name);
        method.setHasBody(true);
        method.params = new ArrayList<>(Arrays.asList(params));
        method.expressions = new ArrayList<>(Arrays.asList(expressions));
        return method;
    }

    public static Method publicMethod(Annotation annotation, String type, String name, Expression[] expressions) {
        Method method = new Method();
        method.setAccessModifier(AccessModifier.PUBLIC);
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
}
