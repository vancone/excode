package com.vancone.excode.executor.controller;

import com.vancone.cloud.common.model.Response;
import com.vancone.excode.executor.service.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tenton Lien
 * @since 8/1/2021
 */
@RestController
@RequestMapping("/api/excode/program")
public class ProgramController {

    @Autowired
    private ProgramService programService;

    @GetMapping
    public Response query() {
        return Response.success();
    }

    @GetMapping("execute")
    public Response execute() {
        return Response.success();
    }
}
