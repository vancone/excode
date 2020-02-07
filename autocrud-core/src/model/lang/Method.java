package model.lang;

import model.enums.AccessModifier;
import model.lang.annotations.Annotation;
import model.project.Value;

import java.util.ArrayList;
import java.util.List;

public class Method {
    private AccessModifier accessModifier;
    private String returnType;
    private Value name = new Value();
    private boolean staticMethod;
    private List<Annotation> annotations = new ArrayList<>();
    private List<Variable> params = new ArrayList<>();
    private List<String> codeLines = new ArrayList<>();
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

    public List<String> getCodeLines() {
        return codeLines;
    }

    public void addCodeLine(String codeLine) {
        this.codeLines.add(codeLine);
    }

    public void clearCodeLines() {
        this.codeLines.clear();
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
            if (!codeLines.isEmpty()) {
                for (String codeline: codeLines) {
                    code += codeline;
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
}
