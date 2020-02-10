package model.java.expressions;

public class ReturnExpression extends Expression {
    private String returnValue;

    public ReturnExpression(String val) {
        this.returnValue = val;
    }

    @Override
    public String toString() {
        return "return " + this.returnValue + ";";
    }
}
