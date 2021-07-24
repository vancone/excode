package com.vancone.excode.core.util;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

/**
 * @author Tenton Lien
 * @date 7/25/2021
 */
public class CompilationUnitUtil {
    public static ClassOrInterfaceDeclaration getMainClassOrInterface(CompilationUnit unit) {
        NodeList<TypeDeclaration<?>> list = unit.getTypes();
        for (TypeDeclaration<?> node: list) {
            if (node instanceof ClassOrInterfaceDeclaration) {
                return (ClassOrInterfaceDeclaration) node;
            }
        }
        return null;
    }
}
