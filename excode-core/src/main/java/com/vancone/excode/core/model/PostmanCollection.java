package com.vancone.excode.core.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Tenton Lien
 * @since 9/9/2021
 */
@Slf4j
@Data
public class PostmanCollection {

    private static final String SCHEMA = "https://schema.getpostman.com/json/collection/v2.1.0/collection.json";

    private Info info = new Info();

    @JsonProperty("item")
    private List<Folder> folders = new ArrayList<>();

    @Data
    @JsonPropertyOrder({
            "_postman_id", "name", "schema"
    })
    public static class Info {
        @JsonProperty("_postman_id")
        private String postmanId;

        private String name;

        private String schema = SCHEMA;
    }

    @Data
    public static class Folder {
        private String name;

        @JsonProperty("item")
        private List<Api> apis = new ArrayList<>();

        @Data
        public static class Api {

            private String name;

            private Request request = new Request();

            @JsonProperty("response")
            private List<Response> responses = new ArrayList<>();

            @Data
            public static class Request {
                private String method;
                private List<String> header = new ArrayList<>();
                private Url url = new Url();
            }

            @Data
            public static class Response {

            }

            @Data
            public static class Url {
                private String raw;
                private String protocol;
                private String port;
                private List<String> host = new ArrayList<>();
                private List<String> path = new ArrayList<>();
            }
        }
    }

    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.getInfo().setPostmanId(UUID.randomUUID().toString());
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.error("Failed to encode JSON string of Postman collection object");
            return "{}";
        }
    }
}
