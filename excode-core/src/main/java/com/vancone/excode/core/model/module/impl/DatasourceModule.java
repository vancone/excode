package com.vancone.excode.core.model.module.impl;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.vancone.excode.core.annotation.Validator;
import com.vancone.excode.core.model.module.Module;
import com.vancone.excode.core.model.database.Database;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tenton Lien
 */
@Data
public class DatasourceModule extends Module {

    /**
     * ELEMENT ELASTICSEARCH
     */
    private Elasticsearch elasticsearch;

    @Data
    public static class Elasticsearch {

    }

    /**
     * ELEMENT MONGODB
     */
    private Mongodb mongodb;

    @Data
    public static class Mongodb {

    }

    /**
     * ELEMENT REDIS
     */
    private Redis redis;

    @Data
    public static class Redis {
        @JacksonXmlProperty(isAttribute = true)
        @Validator({"cluster", "master-slave", "standalone"})
        private String mode = "standalone";

        private List<Node> nodes = new ArrayList<>();

        @Data
        public static class Node {
            @JacksonXmlProperty(isAttribute = true)
            private String database;

            @JacksonXmlProperty(isAttribute = true)
            private String host;

            @JacksonXmlProperty(isAttribute = true)
            private int port = 6379;

            @JacksonXmlProperty(isAttribute = true)
            private String password;

            @JacksonXmlProperty(isAttribute = true)
            private long timeout;
        }
    }

    /**
     * ELEMENT RELATIONAL_DATABASE
     */
    @JacksonXmlProperty(localName = "relational-database")
    private RelationalDatabase relationalDatabase;

    @Data
    public static class RelationalDatabase {
        @JacksonXmlProperty(isAttribute = true, localName = "sql-gen")
        private boolean sqlGen = true;

        @JacksonXmlElementWrapper(localName = "databases")
        @JacksonXmlProperty(localName = "database")
        private List<Database> databases;
    }
}