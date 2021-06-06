package com.vancone.excode.exception;

/**
 * @author Tenton Lien
 * @date 9/20/2020
 */
public interface BaseEnum {
    int getCode();
    void setCode(int code);
    String getMessage();
    void setMessage(String message);
}
