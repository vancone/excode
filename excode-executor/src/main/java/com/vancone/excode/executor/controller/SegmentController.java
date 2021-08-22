package com.vancone.excode.executor.controller;

import com.vancone.cloud.common.model.Response;
import com.vancone.excode.executor.entity.Segment;
import com.vancone.excode.executor.service.SegmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Tenton Lien
 * @since 8/1/2021
 */
@RestController
@RequestMapping("/api/excode/executor/segment")
public class SegmentController {

    @Autowired
    private SegmentService segmentService;

    @GetMapping
    public Response query() {
        return Response.success();
    }

    @PostMapping
    public Response create(@RequestBody Segment segment) {
        segmentService.create(segment);
        return Response.success();
    }

    @GetMapping("execute")
    public Response execute() {
        segmentService.execute("111");
        return Response.success();
    }
}
