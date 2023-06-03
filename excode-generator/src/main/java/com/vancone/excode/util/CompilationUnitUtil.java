package com.vancone.excode.util;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

import java.util.List;

/**
 * @author Tenton Lien
 * @since 2021/07/25
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

    public static MethodDeclaration getMethodByName(CompilationUnit unit, String name) {
        ClassOrInterfaceDeclaration clazz = getMainClassOrInterface(unit);
        if (clazz != null) {
            List<MethodDeclaration> methods = clazz.getMethods();
            if (!methods.isEmpty()) {
                for (MethodDeclaration method: methods) {
                    if (method.getName().toString().equals(name)) {
                        return method;
                    }
                }
            }
        }
        return null;
    }
}
