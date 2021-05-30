package com.vancone.devops.exception;

import com.vancone.cloud.common.model.Response;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/*
 * Author: Tenton Lien
 * Date: 9/20/2020
 */

@RestControllerAdvice
public class ResponseExceptionHandler {
    @ExceptionHandler(value = ResponseException.class)
    public Response handlerLoginException(ResponseException e) {
        return Response.fail(e.getCode(), e.getMessage());
    }
}