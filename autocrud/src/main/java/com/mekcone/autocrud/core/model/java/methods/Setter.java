package com.mekcone.autocrud.core.model.java.methods;

import com.mekcone.autocrud.core.model.enums.AccessModifier;
import com.mekcone.autocrud.core.model.enums.BasicDataType;
import com.mekcone.autocrud.core.model.java.Variable;
import com.mekcone.autocrud.core.model.java.expressions.Expression;

public class Setter extends Method {
    public Setter(Variable variable) {
        this.setAccessModifier(AccessModifier.PUBLIC);
        this.setName("set" + variable.getName().capitalizedCamelStyle());
        this.setReturnType(BasicDataType.VOID.toString());
        this.addParam(new Variable(variable.getType(), variable.getName()));
        this.addExpression(new Expression("this." + variable.getName().getData() + " = " + variable.getName().getData() + ";"));
    }
}
