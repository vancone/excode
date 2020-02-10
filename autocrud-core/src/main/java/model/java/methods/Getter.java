package model.java.methods;

import model.enums.AccessModifier;
import model.java.Variable;
import model.java.expressions.ReturnExpression;

public class Getter extends Method {
    public Getter(Variable variable) {
        this.setAccessModifier(AccessModifier.PUBLIC);
        this.setName("get" + variable.getName().capitalizedCamelStyle());
        this.setReturnType(variable.getType());
        this.addExpression(new ReturnExpression("this." + variable.getName().getData()));
    }
}
