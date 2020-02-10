package model.java.methods;

import model.enums.AccessModifier;
import model.enums.BasicDataType;
import model.java.Variable;
import model.java.expressions.Expression;

public class Setter extends Method {
    public Setter(Variable variable) {
        this.setAccessModifier(AccessModifier.PUBLIC);
        this.setName("set" + variable.getName().capitalizedCamelStyle());
        this.setReturnType(BasicDataType.VOID.toString());
        this.addParam(new Variable(variable.getType(), variable.getName()));
        this.addExpression(new Expression("this." + variable.getName().getData() + " = " + variable.getName().getData() + ";"));
    }
}
