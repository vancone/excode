package com.vancone.excode.executor.enums;

/**
 * @author Tenton Lien
 * @since 8/1/2021
 */
public enum ProgrammingLanguage {
    C,
    CPP,
    JAVA,
    PYTHON,
    SHELL;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
