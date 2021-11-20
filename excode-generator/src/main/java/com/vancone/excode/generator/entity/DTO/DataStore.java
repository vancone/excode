package com.vancone.excode.generator.entity.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vancone.excode.generator.enums.DataCarrier;
import com.vancone.excode.generator.enums.DataStoreType;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Tenton Lien
 * @since 2021/11/18
 */
@Data
@Document("data_store")
public class DataStore {
    private String id;

    private String projectId;

    private String name;

    private DataStoreType type;

    private DataCarrier carrier;

    private String description;

    private List<Node> nodes;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;

    @Data
    public static class Node {
        private String name;
        private String type;
        private Integer length;
        private String comment;
        private List<Node> children;
    }
}
