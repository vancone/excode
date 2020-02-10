package model.java.expressions;

public class Expression {
    private String value;

    public Expression() {}

    public Expression(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
