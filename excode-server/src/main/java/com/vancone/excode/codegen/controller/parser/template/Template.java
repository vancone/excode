package com.vancone.excode.codegen.controller.parser.template;

/**
 * @author Tenton Lien
 */
public interface Template {
    boolean insert(String tag, String replacement);
    boolean insertOnce(String tag, String replacement);
    boolean remove(String tag);
}
