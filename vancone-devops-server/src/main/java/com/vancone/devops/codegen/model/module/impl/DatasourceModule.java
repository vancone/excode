package com.vancone.devops.codegen.model.module.impl;

import com.vancone.devops.codegen.annotation.Validator;
import com.vancone.devops.codegen.model.database.Database;
import com.vancone.devops.codegen.model.module.Module;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DatasourceModule extends Module {

    // ELEMENT ELASTICSEARCH
    private Elasticsearch elasticsearch;

    @Data
    public static class Elasticsearch {

    }

    // ELEMENT MONGODB
    private Mongodb mongodb;

    @Data
    public static class Mongodb {

    }

    // ELEMENT REDIS
    private Redis redis;

    @Data
    public static class Redis {
        @Validator({"cluster", "master-slave", "standalone"})
        private String mode = "standalone";

        private List<Node> nodes = new ArrayList<>();

        @Data
        public static class Node {
            private String database;

            private String host;

            private int port = 6379;

            private String password;

            private long timeout;
        }
    }

    // ELEMENT RELATIONAL_DATABASE
    private RelationalDatabase relationalDatabase;

    @Data
    public static class RelationalDatabase {
        private boolean sqlGen = true;

        private List<Database> databases;
    }
}
