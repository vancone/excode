package com.mekcone.autocrud.core.model.java.methods;

import com.mekcone.autocrud.core.model.enums.AccessModifier;
import com.mekcone.autocrud.core.model.java.Variable;
import com.mekcone.autocrud.core.model.java.expressions.ReturnExpression;

public class Getter extends Method {
    public Getter(Variable variable) {
        this.setAccessModifier(AccessModifier.PUBLIC);
        this.setName("get" + variable.getName().capitalizedCamelStyle());
        this.setReturnType(variable.getType());
        this.addExpression(new ReturnExpression("this." + variable.getName().getData()));
    }
}
