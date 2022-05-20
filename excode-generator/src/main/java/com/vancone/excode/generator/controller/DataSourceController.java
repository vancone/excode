package com.vancone.excode.generator.controller;

import com.vancone.cloud.common.model.Response;
import com.vancone.excode.generator.entity.DataSource;
import com.vancone.excode.generator.enums.DataCarrier;
import com.vancone.excode.generator.service.DataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Tenton Lien
 * @since 2021/06/13
 */
@RestController
@RequestMapping("/api/excode/data-source")
public class DataSourceController {

    @Autowired
    private DataSourceService dataSourceService;

    @PostMapping
    public Response create(@RequestBody DataSource dataSource) {
        dataSourceService.save(dataSource);
        return Response.success();
    }

    @GetMapping("{dataSourceId}")
    public Response query(@PathVariable String dataSourceId) {
        return Response.success(dataSourceService.query(dataSourceId));
    }

    @GetMapping
    public Response queryPage(@RequestParam(defaultValue = "1") int pageNo,
                              @RequestParam(defaultValue = "5") int pageSize,
                              @RequestParam(required = false) String search,
                              @RequestParam(required = false) String type) {
        return Response.success(dataSourceService.queryPage(pageNo - 1, pageSize, search, type));
    }

    @GetMapping("list")
    public Response queryList(DataCarrier type) {
        return Response.success(dataSourceService.queryList(type));
    }

    @PostMapping("test")
    public Response testConnection(@RequestBody DataSource dataSource) {
        dataSourceService.testConnection(dataSource);
        return Response.success("Test data source connection success", null);
    }

    @DeleteMapping("{dataSourceId}")
    public Response delete(@PathVariable String dataSourceId) {
        dataSourceService.delete(dataSourceId);
        return Response.success();
    }
}
