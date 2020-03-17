package com.mekcone.excrud.model.file.javalang.components;

import lombok.Data;

@Data
public class Expression {
    private String data;

    public Expression() {}

    @Deprecated
    public Expression(String value) {
        this.data = value;
    }

    @Override
    public String toString() {
        return this.data;
    }


    public static Expression simpleExpression(String value) {
        Expression simpleException = new Expression();
        simpleException.setData(value);
        return simpleException;
    }

    public static Expression returnExpression(Expression expression) {
        Expression returnExpression = new Expression("return " + expression.toString() + ";");
        return returnExpression;
    }
}
