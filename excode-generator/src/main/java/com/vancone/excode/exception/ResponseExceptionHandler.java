package com.vancone.excode.exception;

import com.vancone.web.common.model.Response;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Tenton Lien
 * @since 2020/09/20
 */
@RestControllerAdvice
public class ResponseExceptionHandler {
    @ExceptionHandler(value = ResponseException.class)
    public Response handlerLoginException(ResponseException e) {
        return Response.fail(e.getCode(), e.getMessage());
    }
}