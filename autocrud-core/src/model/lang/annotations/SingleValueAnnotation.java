package model.lang.annotations;

public class SingleValueAnnotation extends Annotation {
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public SingleValueAnnotation() {}

    public SingleValueAnnotation(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        if (this.value == null) {
            return "@" + this.name;
        } else {
            return "@" + this.name + "(\"" + this.value + "\")\n";
        }

    }
}
