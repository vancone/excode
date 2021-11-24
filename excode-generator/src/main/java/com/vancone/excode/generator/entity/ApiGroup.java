package com.vancone.excode.generator.entity;

import lombok.Data;

import java.util.List;

/**
 * @author Tenton Lien
 * @since 2021/11/23
 */
@Data
public class ApiGroup {
    private String id;
    private String dataStoreId;
    private List<Api> apis;

    @Data
    static class Api {
        private Boolean status;
        private String method;
        private String name;
        private String path;
        private List<String> params;
    }
}
