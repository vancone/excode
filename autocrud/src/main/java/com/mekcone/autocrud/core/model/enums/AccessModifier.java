package com.mekcone.autocrud.core.model.enums;

public enum AccessModifier {
    PUBLIC, PROTECTED, PRIVATE;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
