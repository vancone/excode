package com.vancone.excode.codegen.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Tenton Lien
 * @date 7/7/2020
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Validator {
    String[] value() default {};

    String defaultValue() default "";
    int max() default 0;
    int min() default 0;
    boolean required() default false;

}