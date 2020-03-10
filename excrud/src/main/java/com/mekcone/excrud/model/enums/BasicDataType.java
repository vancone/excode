package com.mekcone.excrud.model.enums;

public enum BasicDataType {
    VOID, BYTE, BOOLEAN, SHORT, CHAR, INT, LONG, FLOAT, DOUBLE, STRING, OBJECT;

    @Override
    public String toString() {
        String ret = super.toString().toLowerCase();
        if (ret.equals("string")) {
            return "String";
        } else if (ret.equals("object")) {
            return "Object";
        } else {
            return ret;
        }
    }
}