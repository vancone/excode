package model.project;

import controller.Logger;

public class Value {
    private String type;
    private String data;

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getType() {
        return this.type;
    }

    public static Value Boolean(boolean bool) {
        Value value = new Value();
        value.type = "boolean";
        value.data = String.valueOf(bool);
        return value;
    }

    public static Value Integer(int i) {
        Value value = new Value();
        value.type = "int";
        value.data = String.valueOf(i);
        return value;
    }

    public static Value String(String str) {
        Value value = new Value();
        value.type = "String";
        value.data = str;
        return value;
    }

    @Override
    public String toString() {
        if (this.type == "char") {
            return "'" + this.data + "'";
        } else if (this.type == "String") {
            return "\"" + this.data + "\"";
        } else {
            return this.data;
        }
    }

    public String camelStyle() {
        String data = this.data;
        String[] stringArray = data.split("_");
        if (stringArray.length > 1) {
            data = stringArray[0];
            int i = 1;
            while (i < stringArray.length) {
                data += (stringArray[i].substring(0, 1).toUpperCase() + stringArray[i].substring(1));
                i ++;
            }
        }
        return data;
    }

    public String capitalizedCamelStyle() {
        String data = camelStyle();
        return (data.substring(0, 1).toUpperCase() + data.substring(1));
    }

    public String capitalized() {
        return (this.data.substring(0, 1).toUpperCase() + this.data.substring(1));
    }
}
