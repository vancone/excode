package com.vancone.excode.generator.codegen.controller.parser.template;

/**
 * @author Tenton Lien
 */
public interface Template {
    boolean insert(String tag, String replacement);
    boolean insertOnce(String tag, String replacement);
    boolean remove(String tag);
}
