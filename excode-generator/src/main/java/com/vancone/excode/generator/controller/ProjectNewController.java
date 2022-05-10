package com.vancone.excode.generator.controller;

import com.vancone.cloud.common.model.Response;
import com.vancone.excode.generator.entity.MicroserviceSpringBoot;
import com.vancone.excode.generator.entity.ProjectNew;
import com.vancone.excode.generator.service.ProjectNewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Tenton Lien
 * @since 2022/05/08
 */
@Slf4j
@RestController
@RequestMapping("/api/excode/project-new")
public class ProjectNewController {

    @Autowired
    private ProjectNewService projectNewService;

    @PostMapping
    public Response create(@RequestBody ProjectNew project) {
        log.info("Create project: {}", project.toString());
        return Response.success(projectNewService.create(project));
    }

    @GetMapping("{id}")
    public Response query(@PathVariable String id) {
        ProjectNew project = projectNewService.query(id);
        if (project != null) {
            log.info("Query project: {}", project.toString());
        }
        return Response.success(project);
    }

    @GetMapping
    public Response queryPage(@RequestParam(defaultValue = "1") int pageNo,
                              @RequestParam(defaultValue = "5") int pageSize,
                              @RequestParam(required = false) String search) {
        return Response.success(projectNewService.queryPage(pageNo - 1, pageSize, search));
    }

    @PutMapping
    public Response update(@RequestBody ProjectNew project) {
        projectNewService.update(project);
        return Response.success();
    }

    @DeleteMapping("{id}")
    public Response delete(@PathVariable String id) {
        projectNewService.delete(id);
        return Response.success();
    }

    @GetMapping("/overview/{projectId}")
    public Response queryOverview(@PathVariable String projectId) {
        return Response.success(projectNewService.overview(projectId));
    }
}
