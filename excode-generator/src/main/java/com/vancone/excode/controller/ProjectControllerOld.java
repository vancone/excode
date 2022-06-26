package com.vancone.excode.controller;

import com.vancone.cloud.common.model.Response;
import com.vancone.excode.entity.ProjectOld;
import com.vancone.excode.constant.DataType;
import com.vancone.excode.service.ProjectServiceOld;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tenton Lien
 */
@Slf4j
@RestController
@RequestMapping("/api/excode/project-old")
public class ProjectControllerOld {

    @Autowired
    private ProjectServiceOld projectServiceOld;

    @PostMapping
    public Response create(@RequestBody ProjectOld project) {
        log.info("Create project: {}", project.toString());
        projectServiceOld.save(project);
        return Response.success();
    }

    @GetMapping("{projectId}")
    public Response query(@PathVariable String projectId) {
        ProjectOld project = projectServiceOld.query(projectId);
        if (project != null) {
            log.info("Query project: {}", project.toString());
        }
        return Response.success(project);
    }

    @GetMapping
    public Response queryPage(@RequestParam(defaultValue = "1") int pageNo,
                              @RequestParam(defaultValue = "5") int pageSize,
                              @RequestParam(required = false) String search) {
        return Response.success(projectServiceOld.queryPage(pageNo - 1, pageSize, search));
    }

    @PutMapping
    public Response update(@RequestBody ProjectOld project) {
        projectServiceOld.save(project);
        return Response.success();
    }

    @PatchMapping("/data-access/solution/{projectId}")
    public Response updateDataAccessSolution(
            @PathVariable String projectId,
            @RequestBody ProjectOld.DataAccess.Solution solution) {
        projectServiceOld.saveSolution(projectId, solution);
        return Response.success();
    }

    @DeleteMapping("{projectId}")
    public Response delete(@PathVariable String projectId) {
        projectServiceOld.delete(projectId);
        return Response.success();
    }

    @GetMapping("generate/{projectId}")
    public Response generate(@PathVariable String projectId) {
        projectServiceOld.generate(projectId);
        return Response.success();
    }

    @GetMapping("dbTypeList")
    public Response retrieveDatabaseTypeList() throws IllegalAccessException {
        // Todo: The list should be cached in Redis to optimize performance
        List<String> dbTypes = new ArrayList<>();
        Field[] fields = DataType.class.getFields();
        for (Field field : fields) {
            if (field.getName().matches("SQL_.*")) {
                dbTypes.add(field.get(DataType.class).toString());
            }
        }
        return Response.success(dbTypes);
    }

    @GetMapping("/download/{projectId}")
    public void downloadFile(@PathVariable String projectId, HttpServletResponse response) {
        String path = "/opt/excode/gen/" + projectId + ".zip";
        log.info("Start downloading file: {}", path);
        try {
            File file = new File(path);
            if (!file.exists() || file.isDirectory()) {
                log.error("File not existed: {}", path);
                response.setStatus(404);
                return;
            }
            FileInputStream fis = new FileInputStream(file);
            response.reset();
            response.setStatus(200);
            response.setContentType("application/force-download");
            response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Expires", "0");
            response.addHeader("Content-Disposition", "attachment;filename=" + file.getName());
            response.addIntHeader("Content-Length", (int) file.length());

            byte[] b = new byte[2048];
            int i;
            while ((i = fis.read(b)) != -1) {
                response.getOutputStream().write(b, 0, i);
            }
            fis.close();
        } catch (Exception e) {
            log.info("Failed to download file({}): {}", path, e.toString());
        }
    }

    @GetMapping("/overview/{projectId}")
    public Response queryOverview(@PathVariable String projectId) {
        return Response.success(projectServiceOld.overview(projectId));
    }
}
