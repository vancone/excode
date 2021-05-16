package com.vancone.devops.controller;

import com.vancone.cloud.common.model.Response;
import com.vancone.devops.entity.DTO.Module;
import com.vancone.devops.service.basic.ModuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author Tenton Lien
 * @date 9/15/2020
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/devops/module", produces = MediaType.APPLICATION_JSON_VALUE)
public class ModuleController {

    @Autowired
    private ModuleService moduleService;

    @PostMapping
    public Response create(@RequestBody Module module) {
        moduleService.save(module);
        return Response.success();
    }

    @GetMapping("/{moduleId}")
    public Response findById(@PathVariable String moduleId) {
        Module module = moduleService.findById(moduleId);
        log.info("Retrieve module: {}", module.toString());
        return Response.success(module);
    }

    @GetMapping
    public Response findAll(@RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                            @RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {
        return Response.success(moduleService.findAll(pageNo, pageSize));
    }

    @PutMapping
    public Response update(@RequestBody Module module) {
        moduleService.save(module);
        return Response.success();
    }

    @DeleteMapping("/{moduleId}")
    public Response delete(@PathVariable String moduleId) {
        moduleService.delete(moduleId);
        return Response.success();
    }

    @PostMapping("/gen/{moduleId}")
    public Response generate(@PathVariable String moduleId) {
        return Response.success(moduleService.findById(moduleId));
    }
}
