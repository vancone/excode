package com.mekcone.studio.controller;

import com.mekcone.studio.codegen.constant.DataType;
import com.mekcone.studio.entity.Project;
import com.mekcone.studio.service.ProjectService;
import com.mekcone.webplatform.common.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/*
 * Author: Tenton Lien
 * Date: 9/14/2020
 */

@Slf4j
@RestController
@RequestMapping(value = "/api/studio/extension", produces = MediaType.APPLICATION_JSON_VALUE)
public class ExtensionController {

    @Autowired
    private ProjectService projectService;

    @PostMapping
    public Response create(@RequestBody Project project) {
        projectService.create(project);
        return Response.success();
    }

    @GetMapping("/{projectId}")
    public Response findById(@PathVariable String projectId) {
        Project project = projectService.findById(projectId);
        log.info("Retrieve project: {}", project.toString());
        return Response.success(project);
    }

    @GetMapping
    public Response findAll(@RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                            @RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {
        return Response.success(projectService.findAll(pageNo, pageSize));
    }

    @PutMapping
    public Response update(@RequestBody Project project) {
        projectService.save(project);
        return Response.success();
    }

    @DeleteMapping("/{projectId}")
    public Response delete(@PathVariable String projectId) {
        projectService.delete(projectId);
        return Response.success();
    }

    @PostMapping("/import")
    public Response importProject(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Response.fail(1, "File is empty");
        }

        try {
            /*XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            Project project = xmlMapper.readValue(new String(file.getBytes()), Project.class);
            log.info("Save imported project: {}", project.toString());
            projectService.saveProject(project);*/
            return Response.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.fail(2, "Import failed");
        }
    }

    @GetMapping("/download/{fileType}/{projectId}")
    public void downloadFile(HttpServletResponse response, @PathVariable String fileType, @PathVariable String projectId) {
        projectService.export(response, fileType, projectId);
    }

    @GetMapping("/dbTypeList")
    public Response retrieveDatabaseTypeList() throws IllegalAccessException {
        // Todo: The list should be cached in Redis to optimize performance
        List<String> dbTypes = new ArrayList<>();
        Field[] fields = DataType.class.getFields();
        for (Field field: fields) {
            if (field.getName().matches("SQL_.*")) {
                dbTypes.add(field.get(DataType.class).toString());
            }
        }
        return Response.success(dbTypes);
    }
}
