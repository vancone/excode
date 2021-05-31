package com.vancone.excode.exception;

/*
 * Author: Tenton Lien
 * Date: 9/20/2020
 */

public interface BaseEnum {
    int getCode();
    void setCode(int code);
    String getMessage();
    void setMessage(String message);
}
