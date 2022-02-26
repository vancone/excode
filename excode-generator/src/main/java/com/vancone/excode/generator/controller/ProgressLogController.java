package com.vancone.excode.generator.controller;

import com.vancone.cloud.common.model.Response;
import com.vancone.excode.generator.service.ProgressLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tenton Lien
 * @since 2022/02/26
 */
@Slf4j
@RestController
@RequestMapping("/api/excode/log")
public class ProgressLogController {

    @Autowired
    private ProgressLogService progressLogService;

    @GetMapping("{projectId}")
    public Response query(@PathVariable String projectId) {
        return Response.success(progressLogService.query(projectId));
    }
}
