package com.vancone.excode.controller;

import com.vancone.cloud.common.model.Response;
import com.vancone.excode.entity.Project;
import com.vancone.excode.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Tenton Lien
 * @since 2022/05/08
 */
@Slf4j
@RestController
@RequestMapping("/api/excode/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping
    public Response create(@RequestBody Project project) {
        log.info("Create project: {}", project.toString());
        return Response.success(projectService.create(project));
    }

    @GetMapping("{id}")
    public Response query(@PathVariable String id) {
        Project project = projectService.query(id);
        if (project != null) {
            log.info("Query project: {}", project);
        }
        return Response.success(project);
    }

    @GetMapping
    public Response queryPage(@RequestParam(defaultValue = "1") int pageNo,
                              @RequestParam(defaultValue = "5") int pageSize,
                              @RequestParam(required = false) String search) {
        return Response.success(projectService.queryPage(pageNo - 1, pageSize, search));
    }

    @PutMapping
    public Response update(@RequestBody Project project) {
        projectService.update(project);
        return Response.success();
    }

    @DeleteMapping("{id}")
    public Response delete(@PathVariable String id) {
        projectService.delete(id);
        return Response.success();
    }

    @GetMapping("/overview/{projectId}")
    public Response queryOverview(@PathVariable String projectId) {
        return Response.success(projectService.overview(projectId));
    }
}
