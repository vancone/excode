package com.vancone.excode.generator.controller;

import com.vancone.cloud.common.model.Response;
import com.vancone.excode.generator.entity.DTO.DataStore;
import com.vancone.excode.generator.entity.DTO.data.source.DataSource;
import com.vancone.excode.generator.service.DataStoreService;
import com.vancone.excode.generator.service.basic.DataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Tenton Lien
 * @date 2021/11/18
 */
@RestController
@RequestMapping("/api/excode/data-store")
public class DataStoreController {

    @Autowired
    private DataStoreService dataStoreService;

    @PostMapping
    public Response create(@RequestBody DataStore store) {
        dataStoreService.save(store);
        return Response.success();
    }

    @GetMapping
    public Response queryList(@RequestParam String projectId) {
        return Response.success(dataStoreService.queryList(projectId));
    }
}
