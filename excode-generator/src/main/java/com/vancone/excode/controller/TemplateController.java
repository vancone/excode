package com.vancone.excode.controller;

import com.vancone.cloud.common.model.Response;
import com.vancone.excode.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Tenton Lien
 * @since 2022/03/08
 */
@RestController
@RequestMapping("/api/excode/template")
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    @GetMapping()
    public Response queryPage(@RequestParam(defaultValue = "1") int pageNo,
                              @RequestParam(defaultValue = "10") int pageSize) {
        return Response.success(templateService.queryPage(pageNo, pageSize - 1));
    }
}
