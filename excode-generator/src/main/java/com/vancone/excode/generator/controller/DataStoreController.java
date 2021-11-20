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

    @GetMapping("{dataStoreId}")
    public Response query(@PathVariable String dataStoreId) {
        return Response.success(dataStoreService.query(dataStoreId));
    }

    @GetMapping
    public Response queryList(@RequestParam String projectId) {
        return Response.success(dataStoreService.queryList(projectId));
    }

    @PostMapping
    public Response create(@RequestBody DataStore store) {
        dataStoreService.save(store);
        return Response.success();
    }

    @PutMapping
    public Response update(@RequestBody DataStore store) {
        dataStoreService.save(store);
        return Response.success();
    }

    @DeleteMapping("{dataStoreId}")
    public Response delete(@PathVariable String dataStoreId) {
        dataStoreService.delete(dataStoreId);
        return Response.success();
    }
}