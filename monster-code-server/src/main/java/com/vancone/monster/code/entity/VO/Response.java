package com.vancone.monster.code.entity.VO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/*
 * Author: Tenton Lien
 * Date: 9/20/2020
 */

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    private int code;
    private String message;
    private Object data;

    public static Response success() {
        Response response = new Response();
        response.setCode(0);
        response.setMessage("success");
        return response;
    }

    public static Response success(Object data) {
        Response response = new Response();
        response.setCode(0);
        response.setMessage("success");
        response.setData(data);
        return response;
    }

    public static Response success(String message, Object data) {
        Response response = new Response();
        response.setCode(0);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    public static Response fail() {
        Response response = new Response();
        response.setCode(-1);
        response.setMessage("failed");
        return response;
    }

    public static Response fail(String message) {
        Response response = new Response();
        response.setCode(-1);
        response.setMessage(message);
        return response;
    }

    public static Response fail(int code, String message) {
        Response response = new Response();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }
}